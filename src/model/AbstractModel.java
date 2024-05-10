package model;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import model.dto.Pair;
import model.dto.PortfolioPerformanceDTO;
import model.enums.InvestmentStrategy;
import model.enums.TimeUnit;
import model.enums.TransactionType;
import model.strategy.DollarCostAveraging;
import model.strategy.Strategy;

import static java.lang.Double.max;
import static java.lang.Integer.min;

/**
 * This class contains all the helper methods required by the model component.
 */
abstract class AbstractModel {

  private final APIRequests api;

  /**
   * Constructs a AbstractModel object.
   *
   * @param api APIRequests object.
   */
  public AbstractModel(APIRequests api) {
    this.api = api;
  }

  /**
   * Helper method to get specific date range given start and end date.
   *
   * @param startDate start date.
   * @param endDate   end date.
   * @return timeunit of date range.
   */
  protected TimeUnit getTimeUnit(DateTime startDate, DateTime endDate) {
    //Check for time unit
    int yearDifference = endDate.getYear() - startDate.getYear() + 1;
    if (yearDifference >= 5) {
      return TimeUnit.Yearly;
    }

    yearDifference = (yearDifference == 0) ? 1 : yearDifference;

    int monthDifference =
            endDate.getMonthOfYear() - startDate.getMonthOfYear() + 1 + (yearDifference - 1) * 12;
    if (monthDifference >= 5) {
      return TimeUnit.Monthly;
    }
    return TimeUnit.Daily;
  }

  /**
   * Helper method to get values required for showing the portfolio performance.
   *
   * @param portfolioData     portfolio data used to for showing performance.
   * @param selectedPortfolio selected portfolio.
   * @return PortfolioPerformanceDTO containing performance values.
   */
  protected PortfolioPerformanceDTO scalingValues(ArrayList<Pair> portfolioData,
                                                  String selectedPortfolio) {
    int len = portfolioData.size();
    Collections.sort(portfolioData, (x, y) -> x.getKey().compareTo(y.getKey()));
    ArrayList<Pair> finalPair = new ArrayList<>();
    if (len > 30) {
      for (int i = 0; i < len; i += 5) {
        Pair x = portfolioData.get(i);
        int finalIdx = min(len - 1, i + 4);
        Pair y = portfolioData.get(finalIdx);
        if (!x.getKey().equals(y.getKey())) {
          finalPair.add(new Pair(x.getKey() + " to " + y.getKey(), y.getValue()));
        } else {
          finalPair.add(new Pair(x.getKey() + " to " + y.getKey(), y.getValue()));
        }
        if (finalIdx == (len - 1)) {
          break;
        }
      }
    } else {
      finalPair = portfolioData;
    }
    Double maxValueOfPortfolio = 0.0;
    for (Pair pair : finalPair) {
      maxValueOfPortfolio = max(maxValueOfPortfolio, pair.getValue());
    }
    maxValueOfPortfolio = maxValueOfPortfolio / 20.0;
    maxValueOfPortfolio = max(1, maxValueOfPortfolio);
    for (int i = 0; i < finalPair.size(); i++) {
      Pair p = finalPair.get(i);
      if (p.getValue() != 0) {
        finalPair.get(i).setValue(Math.ceil(p.getValue() / maxValueOfPortfolio));
      }
    }
    return new PortfolioPerformanceDTO(finalPair, maxValueOfPortfolio,
            selectedPortfolio);
  }


  /**
   * Helper method to get stock price for a company symbol for a given date range.
   *
   * @param companySymbol Company/Stock symbol.
   * @param startDate     Starting date.
   * @param endDate       End date.
   * @param scale         Date range scale.
   * @return stock price of the company.
   */
  protected ArrayList<Pair> getStockPriceRange(String companySymbol, String startDate,
                                               String endDate,
                                               TimeUnit scale) {

    String data = api.getTimeSeriesData(scale, companySymbol);

    ArrayList<Pair> listOfPrices = new ArrayList<>();
    if (data != null) {
      JSONObject jsonObject = new JSONObject(data);

      // monthly
      if ((scale.equals(TimeUnit.Monthly) || (scale.equals(TimeUnit.Yearly))) && jsonObject != null
              && jsonObject.getJSONObject("Monthly Time Series") != null) {
        endDate = endDate.substring(0, 8) + "31";
        startDate = startDate.substring(0, 8) + "01";
        JSONObject myResponse = jsonObject.getJSONObject("Monthly Time Series");
        if (myResponse != null && myResponse.keySet() != null && myResponse.keySet().size() > 0) {
          ArrayList<String> stockPriceDates
                  = new ArrayList<>(myResponse.keySet());
          stockPriceDates.sort(Collections.reverseOrder());
          JSONObject closeValue = null;
          if (scale.equals(TimeUnit.Monthly)) {
            for (String validDate : stockPriceDates) {
              if (validDate.compareTo(startDate) < 0) {
                break;
              }
              if (endDate.compareTo(validDate) >= 0) {
                if (myResponse.get(validDate) != null) {
                  closeValue = (JSONObject) myResponse.get(validDate);
                  String stockPrice = (String) closeValue.get("4. close");
                  listOfPrices.add(new Pair(validDate, Double.parseDouble(stockPrice)));
                }
              }
            }
          } else {
            String currentYear = endDate.substring(0, 7);
            for (String validDate : stockPriceDates) {
              if (validDate.compareTo(startDate) < 0) {
                break;
              }
              if ((validDate.substring(0, 7).equals(currentYear)
                      || Integer.parseInt(validDate.substring(5, 7)) == 12)
                      && endDate.compareTo(validDate) >= 0) {
                if (myResponse.get(validDate) != null) {
                  closeValue = (JSONObject) myResponse.get(validDate);
                  String stockPrice = (String) closeValue.get("4. close");
                  listOfPrices.add(new Pair(validDate, Double.parseDouble(stockPrice)));
                }
              }
            }
          }
        }
      }

      // daily
      if (scale.equals(TimeUnit.Daily) && jsonObject != null
              && jsonObject.getJSONObject("Time Series (Daily)") != null) {
        JSONObject myResponse = jsonObject.getJSONObject("Time Series (Daily)");
        if (myResponse != null && myResponse.keySet() != null && myResponse.keySet().size() > 0) {
          ArrayList<String> stockPriceDates
                  = new ArrayList<>(myResponse.keySet());
          stockPriceDates.sort(Collections.reverseOrder());
          JSONObject closeValue = null;
          for (String validDate : stockPriceDates) {
            if (endDate.compareTo(validDate) >= 0 && validDate.compareTo(startDate) >= 0) {
              closeValue = (JSONObject) myResponse.get(validDate);
              String stockPrice = (String) closeValue.get("4. close");
              listOfPrices.add(new Pair(validDate, Double.parseDouble(stockPrice)));
            }
          }
        }
      }
    }

    return listOfPrices;
  }

  /**
   * Helper method to get stock's unit price, given the company's stock symbol and a date value.
   *
   * @param companySymbol Stock symbol.
   * @param date          Date for which price is required.
   * @return stock's unit price.
   */
  protected double getStockPrice(String companySymbol, String date) {
    if (date.isEmpty()) {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      date = format.format(new Date());
    }
    String data = api.getTimeSeriesData(TimeUnit.Daily, companySymbol);
    if (data != null) {
      JSONObject jsonObject = new JSONObject(data);

      if (jsonObject.getJSONObject("Time Series (Daily)") != null) {
        JSONObject myResponse = jsonObject.getJSONObject("Time Series (Daily)");
        if (myResponse != null && myResponse.keySet() != null && myResponse.keySet().size() > 0) {
          ArrayList<String> stockPriceDates
                  = new ArrayList<>(myResponse.keySet());
          stockPriceDates.sort(Collections.reverseOrder());
          JSONObject closeValue = null;
          for (String validDate : stockPriceDates) {
            if (date.compareTo(validDate) >= 0) {
              if (myResponse.get(validDate) != null) {
                closeValue = (JSONObject) myResponse.get(validDate);
                String stockPrice = (String) closeValue.get("4. close");
                return Double.parseDouble(stockPrice);
              }
            }
          }
        }
      }
    }
    return 0;
  }

  /**
   * Helper method to filter portfolio based on a specific date.
   *
   * @param date          date for which portfolio must be filtered.
   * @param companyStocks list of portfolio stocks.
   * @return update stock map.
   */
  protected HashMap<String, List<CompanyStock>> updateStocksByDate(String date, HashMap<String,
          List<CompanyStock>> companyStocks) {
    HashMap<String, List<CompanyStock>> updatedStocks = new HashMap<>();

    LocalDate givenDate = LocalDate.parse(date,
            DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));

    // adding only stocks with dates less than date parameter.
    for (String key : companyStocks.keySet()) {
      List<CompanyStock> newList = new ArrayList<>();

      for (CompanyStock c : companyStocks.get(key)) {
        LocalDate cDate = LocalDate.parse(c.getDate());

        if (!cDate.isAfter(givenDate)) {
          newList.add(c);
        }
      }

      updatedStocks.put(key, newList);
    }

    return updatedStocks;
  }

  /**
   * Helper method to get the cumulative portfolio data from the transaction stored in a portfolio.
   *
   * @param companyStocks list of stocks saved in the portfolio.
   * @param edDate        endDate.
   * @param date          date at which data is needed.
   * @return HashMap containing symbol - portfolio contents.
   */
  protected HashMap<String, CompanyStock> getCumulativeData(
          HashMap<String, List<CompanyStock>> companyStocks, String edDate, String date) {
    HashMap<String, CompanyStock> stocks = new HashMap<>();

    for (String companySymbol : companyStocks.keySet()) {
      for (CompanyStock currentStock : companyStocks.get(companySymbol)) {
        String csDate = currentStock.getDate();
        if (edDate != null && edDate.compareTo(csDate) < 0) {
          continue;
        }

        if (stocks.containsKey(companySymbol)) {
          CompanyStock stockInMap = stocks.get(companySymbol);

          // update bought price to average bought price.
          TransactionType type = currentStock.getType();
          int sign = type == TransactionType.BUY ? 1 : -1;
          double netQuantity = sign * currentStock.getQuantity() + stockInMap.getQuantity();
          double netCommissionFees =
                  currentStock.getCommissionFees() + stockInMap.getCommissionFees();
          double netBoughtValue = currentStock.getBoughtValue() + stockInMap.getBoughtValue();
          double boughtPrice = stockInMap.getBoughtPrice();

          // calculating average bought price
          if (type == TransactionType.BUY && date.isEmpty()) {
            boughtPrice = netBoughtValue / netQuantity;
          }

          currentStock = new CompanyStockImpl("", currentStock.getCompanyName(), companySymbol,
                  netQuantity, boughtPrice, type, netCommissionFees);
        } else {
          int sign = currentStock.getType() == TransactionType.SELL ? -1 : 1;
          double quantity = sign * currentStock.getQuantity();
          double boughtPrice = currentStock.getBoughtPrice();

          if (!date.isEmpty()) {
            boughtPrice = getStockPrice(companySymbol, date);
          }

          currentStock = new CompanyStockImpl("", currentStock.getCompanyName(), companySymbol,
                  quantity, boughtPrice, currentStock.getType(), currentStock.getCommissionFees());
        }

        if (currentStock.getQuantity() == 0) {
          stocks.remove(companySymbol);
        } else {
          stocks.put(companySymbol, currentStock);
        }
      }
    }
    return stocks;
  }

  /**
   * Gets name of strategy based on enum type.
   *
   * @param investmentStrategy name of strategy.
   * @return name of strategy.
   */
  protected Strategy getStrategy(InvestmentStrategy investmentStrategy) {
    if (investmentStrategy.equals(InvestmentStrategy.DOLLAR_COST_AVERAGING)) {
      return new DollarCostAveraging();
    }
    return null;
  }

  /**
   * Helper method to validate if a value is of double type.
   *
   * @param value value that needs to be validated.
   * @return true if value is double, else false.
   * @throws NumberFormatException if value is not of double type.
   */
  protected boolean checkDouble(String value) throws NumberFormatException {
    try {
      Double.parseDouble(value);
    } catch (NumberFormatException e) {
      return false;
    }
    return Double.parseDouble(value) > 0;
  }

  /**
   * Helper method for validating weights given.
   *
   * @param weights list of company weights
   * @return total of weight as string if correct, else an error.
   */
  protected String validateWeights(String[] weights) {
    String error = "Sum of all the weights is not 100 or invalid value is provided.";
    if (weights.length == 0) {
      return error;
    }
    Double total = 0.0;
    for (String wt : weights) {
      if (!checkDouble(wt) || Double.parseDouble(wt) < 0) {
        return error;
      }
      total = total + Double.parseDouble(wt);
    }
    return (total == 100.0) ? "" : error;
  }
}
