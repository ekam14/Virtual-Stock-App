package model;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.dto.Pair;
import model.dto.PortfolioPerformanceDTO;
import model.enums.InvestmentStrategy;
import model.enums.PortfolioType;
import model.enums.TimeUnit;
import model.enums.TransactionType;
import model.strategy.Strategy;

import static java.lang.Integer.parseInt;

/**
 * This class represents the model component which contains the main logic for all the stock
 * operations handled by the application.
 */
public class ModelComponentImpl extends AbstractModel implements ModelComponent {

  private CompanyStock currentStock;
  private final List<CompanyStock> stocks;
  private final PortfolioRepository portfolioRepo;
  private final Appendable out;

  /**
   * Constructs a ModelComponentImpl class and initializes the output source.
   *
   * @param portfolioRepo PortfolioRepository object.
   * @param api           APIRequests object.
   * @param out           output source.
   */
  public ModelComponentImpl(Appendable out, PortfolioRepository portfolioRepo, APIRequests api) {
    super(api);
    this.out = out;
    stocks = new ArrayList<>();
    Cache.loadConfig();
    this.portfolioRepo = portfolioRepo;
    Cache.loadAppData(portfolioRepo);
  }

  private String validateCompanySymbol(String[] companySymbol) {
    String error = "Entered company symbol is invalid.";
    if (companySymbol.length == 0) {
      return error;
    }
    for (String symbol : companySymbol) {
      if (!checkStock(symbol)) {
        return error;
      }
    }
    return "";
  }

  @Override
  public Boolean checkStock(String companySymbol) {
    if (companySymbol == null || Objects.equals(companySymbol.trim(), "")) {
      return false;
    }
    // if true - populate Model.CompanyStock object;
    String companyName = Cache.companyList.get(companySymbol);

    return companyName != null && !Objects.equals(companyName, "");
  }

  @Override
  public boolean populateModelStock(String companySymbol, String date) {
    if (date.isEmpty() || Objects.equals(date, "")) {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      date = format.format(new Date());
    }
    double price = getStockPrice(companySymbol, date);
    if (price <= 0) {
      return false;
    }

    currentStock = new CompanyStockImpl(date, Cache.companyList.get(companySymbol),
            companySymbol, 0.0, price, TransactionType.BUY,
            0.0);
    return true;
  }

  @Override
  public HashMap<String, Double> getPortfolioQuantity(String date, String selectedPortfolio) {
    HashMap<String, List<CompanyStock>> stocks = Cache.objectStore.get(selectedPortfolio);

    HashMap<String, Double> qnty = new HashMap<>();
    qnty.put("minima", Double.MAX_VALUE);
    qnty.put("currentQnty", 0.0);
    qnty.put("beforeQnty", 0.0);

    if (!stocks.containsKey(getCompanySymbol())) {
      return qnty;
    }

    HashMap<String, Double> qntyDate = new HashMap<>();

    for (CompanyStock s : stocks.get(getCompanySymbol())) {
      int sign = s.getType() == TransactionType.SELL ? -1 : 1;
      Double currentQnty = s.getQuantity() * sign;
      String currentDate = s.getDate();

      if (!qntyDate.containsKey(currentDate)) {
        qntyDate.put(currentDate, currentQnty);
      } else {
        qntyDate.put(currentDate, qntyDate.get(currentDate) + currentQnty);
      }
    }

    ArrayList<String> sortedKeys = new ArrayList<>(qntyDate.keySet());
    Collections.sort(sortedKeys);

    Double currQnty = 0.0;
    Double beforeQnty = 0.0;
    Double minima = Double.MAX_VALUE;

    LocalDate givenDate = LocalDate.parse(date,
            DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));

    for (String el : sortedKeys) {
      LocalDate elDate = LocalDate.parse(el);
      currQnty += qntyDate.get(el);

      if (elDate.isAfter(givenDate)) {
        minima = Double.min(minima, currQnty);
      } else {
        beforeQnty = currQnty;
      }
    }

    qnty.put("minima", minima);
    qnty.put("currentQnty", currQnty);
    qnty.put("beforeQnty", beforeQnty);

    return qnty;
  }

  @Override
  public double getCostBasis(String date, String selectedPortfolio) {
    HashMap<String, List<CompanyStock>> stocks = updateStocksByDate(date,
            Cache.objectStore.get(selectedPortfolio));

    double costBasis = 0.0;

    for (String companySymbol : stocks.keySet()) {
      for (CompanyStock s : stocks.get(companySymbol)) {
        if (s.getType() == TransactionType.BUY) {
          costBasis += s.getBoughtValue();
        }

        costBasis += s.getCommissionFees();
      }
    }

    return costBasis;
  }

  @Override
  public PortfolioPerformanceDTO getPerformanceOfPortfolio(String dateRange,
                                                           String selectedPortfolio) {
    String stDate = dateRange.substring(0, 10);
    String edDate = dateRange.substring(11);
    DateTime startDate = new DateTime(stDate);
    DateTime endDate = new DateTime(edDate);
    TimeUnit scale = getTimeUnit(startDate, endDate);
    ArrayList<Pair> defaultPortfolioData = new ArrayList<>();
    HashMap<String, List<CompanyStock>> data = portfolioRepo.getPortfolio(selectedPortfolio);
    //data = filterDate();
    for (String companySymbol : data.keySet()) {
      ArrayList<Pair> priceValues = getStockPriceRange(companySymbol, stDate, edDate, scale);
      for (int i = 0; i < priceValues.size(); i++) {
        Pair p = priceValues.get(i);
        HashMap<String, List<CompanyStock>> companyStocks = new HashMap<>();
        companyStocks.put(companySymbol, data.get(companySymbol));
        HashMap<String, CompanyStock> cumulativeData = getCumulativeData(companyStocks, p.getKey(),
                "");
        Double quantity = 0.0;
        if (!cumulativeData.isEmpty() && cumulativeData.get(companySymbol) != null) {
          quantity = cumulativeData.get(companySymbol).getQuantity();
        }
        priceValues.get(i).setValue(p.getValue() * quantity);
      }
      if (defaultPortfolioData.size() != 0 && defaultPortfolioData.size() != priceValues.size()) {
        return null;
      }
      //code to add different company stock
      Integer len = defaultPortfolioData.size();
      if (len == 0) {
        defaultPortfolioData = priceValues;
      } else {
        for (int i = 0; i < len; i++) {
          if (defaultPortfolioData != null && priceValues != null && priceValues.size() > i) {
            defaultPortfolioData.get(i).add(priceValues.get(i).getValue());
          }
        }
      }
    }
    if (defaultPortfolioData.size() < 5) {
      return null;
    }
    return scalingValues(defaultPortfolioData, selectedPortfolio);
  }

  @Override
  public String getCompanySymbol() {
    return currentStock.getCompanySymbol();
  }

  @Override
  public String getCompanyName() {
    return currentStock.getCompanyName();
  }

  @Override
  public double getPrice() {
    return currentStock.getBoughtPrice();
  }

  @Override
  public void addStock(double quantity, double commissionFees, TransactionType type)
          throws IOException {
    String operation = type == TransactionType.BUY ? "Purchasing" : "Selling";
    String perc = "%";

    this.out.append(
            String.format("%s %.2f stocks of %s for a total of $%.2f at %.2f%s commission.\n",
                    operation, quantity, currentStock.getCompanyName(),
                    quantity * currentStock.getBoughtPrice(), commissionFees, perc));

    String date = currentStock.getDate();
    String companyName = currentStock.getCompanyName();
    String companySymbol = currentStock.getCompanySymbol();
    double stockPrice = currentStock.getBoughtPrice();

    this.stocks.add(new CompanyStockImpl(date, companyName, companySymbol, quantity,
            stockPrice, type, (commissionFees * quantity * stockPrice) / 100.0));
  }

  @Override
  public void savePortfolio(String fileType) throws IOException {
    this.out.append("\n********************************\nTRANSACTIONS: \n");
    portfolioRepo.saveCompanyStock(fileType, stocks);
    for (CompanyStock c : stocks) {
      this.out.append(String.format("%s, quantity: %.2f, %s\n", c.getCompanyName(), c.getQuantity(),
              c.getType()));
    }
    this.out.append("********************************\n\n");
    stocks.clear();
  }

  @Override
  public List<String> getPortfolioList() {
    List<String> list = new ArrayList<>(Cache.objectStore.keySet());
    Collections.sort(list);
    return list;
  }

  @Override
  public HashMap<String, CompanyStock> getPortfolioComposition(String portfolioName) {
    HashMap<String, List<CompanyStock>> companyStocks = Cache.objectStore.get(portfolioName);

    return getCumulativeData(companyStocks, null, "");
  }

  @Override
  public HashMap<String, CompanyStock> getPortfolioValue(String date, String portfolioName) {
    //date is required
    HashMap<String, List<CompanyStock>> companyStocks = updateStocksByDate(date,
            Cache.objectStore.get(portfolioName));

    return getCumulativeData(companyStocks, null, date);
  }

  @Override
  public boolean checkNumber(String value) throws NumberFormatException {
    try {
      parseInt(value);
    } catch (NumberFormatException e) {
      return false;
    }
    return parseInt(value) > 0;
  }

  @Override
  public boolean validDateRange(String dateRange, boolean isShowPortfolio) {
    if (dateRange.length() == 21
            && (dateRange.charAt(10) == ('#'))) {
      String startDate = dateRange.substring(0, 10);
      String endDate = dateRange.substring(11);
      // 5 business days
      return checkDate(startDate)
              && checkDate(endDate)
              && endDate.compareTo(startDate) >= 0
              && endDate.substring(0, 3).compareTo("1999") > 0
              && startDate.substring(0, 3).compareTo("1999") > 0
              && (!isShowPortfolio || additionalDateCheckForShowPortfolio(startDate, endDate));
    }
    return false;
  }

  /**
   * Helper method for checking if current date range is in correct format.
   *
   * @param startDate starting date.
   * @param endDate   ending date.
   * @return validity of date range format.
   */
  private boolean additionalDateCheckForShowPortfolio(String startDate, String endDate) {
    return !(startDate.substring(0, 7).equals(endDate.substring(0, 7))
            && Integer.parseInt(endDate.substring(8, 10))
            - Integer.parseInt(startDate.substring(8, 10)) < 5);
  }

  @Override
  public boolean checkDate(String date) {
    if ((date.length() >= 3 && date.substring(0, 3).compareTo("1999") <= 0)
            || date.length() != 10) {
      return false;
    }

    boolean valid = false;
    try {
      LocalDate now = LocalDate.now();
      LocalDate given = LocalDate.parse(date,
              DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
      if (now.compareTo(given) >= 0) {
        valid = true;
      }
    } catch (DateTimeParseException e) {
      // pass;
    }

    return valid;
  }

  @Override
  public boolean validatePath(String path) throws IOException {
    File file = new File(path);
    String contentType = Files.probeContentType(Paths.get(path));
    return (file.exists()
            && !file.isDirectory()
            && path.endsWith(".csv")
            && contentType.equals("text/csv"));
  }

  @Override
  public String importPortfolio(String fileType, String path) {
    HashMap<String, List<CompanyStock>> fileStocks = portfolioRepo.readFile(path);
    if (fileStocks == null || fileStocks.isEmpty()) {
      return null;
    }

    List<CompanyStock> newStocks = new ArrayList<>();

    for (String key : fileStocks.keySet()) {
      List<CompanyStock> stockTrans = fileStocks.get(key);

      newStocks.addAll(stockTrans);
    }

    return portfolioRepo.saveCompanyStock(fileType, newStocks);
  }

  @Override
  public void updateFile(String fileName) {
    if (this.stocks.size() == 0) {
      return;
    }

    portfolioRepo.updateFile(fileName, this.stocks);
    this.stocks.clear();
  }

  @Override
  public boolean validateInput(String input, String format) {
    switch (format) {
      case "Amount":
        try {
          double isNum = Double.parseDouble(input);
          return isNum > 0;
        } catch (Exception e) {
          return false;
        }
      case "TimeUnit":
        return input.equals("Yearly") || input.equals("Monthly") || input.equals("Daily");
      default:
    }
    return true;
  }

  @Override
  public HashMap<String, String> applyStrategy(Map<String, Object> data) throws IOException {
    stocks.clear();
    String dateRange = (String) data.get("dateRange");

    if (dateRange.length() == 11) {
      if (data.containsKey("portfolioName") &&  Objects.equals(data.get("portfolioName"), "")) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateRange += format.format(new Date());
      } else {
        dateRange += dateRange.substring(0, 10);
      }

      data.put("dateRange", dateRange);
    }

    HashMap<String, String> message = validateInvestmentStrategyData(
            (String) data.get("portfolioName"), data);

    if (Objects.equals(message.get("isError"), "true")) {
      return message;
    }

    Strategy strategy = getStrategy(InvestmentStrategy.DOLLAR_COST_AVERAGING);
    String companySet = (String) data.get("companySymbol");
    String fileType = (String) data.get("fileType");
    String[] weights = ((String) data.get("weights")).split(":");
    String[] companySymArray = companySet.split(":");
    TimeUnit timeUnit = Objects.equals(data.get("StrategyFrequency"), "")
            ? TimeUnit.Daily
            : TimeUnit.getTimeUnit((String) data.get("StrategyFrequency"));
    Map<String, Pair> map = new HashMap<>();
    int i = 0;
    for (String companySymbol : companySymArray) {
      if (map.containsKey(companySymbol)) {
        Double wt = map.get(companySymbol).getValue();
        wt += Double.parseDouble(weights[i++]);
        map.get(companySymbol).setValue(wt);
        continue;
      } else {
        map.put(companySymbol,
                new Pair(Cache.companyList.get(companySymbol), Double.parseDouble(weights[i++])));
      }
      ArrayList<Pair> priceRange = null;
      if (dateRange.length() == 21 && dateRange.substring(0, 10)
              .equals(dateRange.substring(11))) {
        priceRange = new ArrayList<>();
        priceRange.add(new Pair(dateRange, getStockPrice(companySymbol, dateRange)));
      } else {
        priceRange = getStockPriceRange(companySymbol,
                dateRange.substring(0, 10),
                dateRange.substring(11),
                timeUnit);
      }
      data.put(companySymbol + "_priceList", priceRange);
    }
    data.put("companyDetails", map);
    if (strategy != null) {
      stocks.addAll(strategy.apply(data));
    }
    String existingPortfolio = (String) data.get("portfolioName");
    if (existingPortfolio.isEmpty()) {
      savePortfolio(fileType);
    } else {
      updateFile(existingPortfolio);
    }

    return new HashMap<>();
  }

  private HashMap<String, String> validateInvestmentStrategyData(String portfolioName,
                                                                 Map<String, Object> data) {
    HashMap<String, String> errors = new HashMap<>();

    if (!validateInput((String) data.get("amount"), "Amount")) {
      errors.put("invalidAmountMessage", "Entered amount is invalid");
    }

    String[] companySymbols = ((String) data.get("companySymbol")).split(":");
    errors.put("invalidCompanySymbolMessage", validateCompanySymbol(companySymbols));

    String[] weights = ((String) data.get("weights")).split(":");
    if (companySymbols.length != weights.length) {
      errors.put("invalidWeightsMessage",
              "Entered number of company symbol is not equal to number weights.");
    }

    errors.put("invalidWeightMessage", validateWeights(weights));

    if (Objects.equals(portfolioName, "") && !validFileType((String) data.get("fileType"))) {
      errors.put("invalidFileTypeMessage", "Entered file type is invalid.");
    }

    errors.put("invalidFeesMessage", validateCommissionFees((String) data.get("commissionFee")));

    if (!(validDateRange((String) data.get("dateRange"), false))) {
      errors.put("invalidDateRangeMessage", "Entered date Range is invalid.");
    }

    if ((portfolioName == null || Objects.equals(portfolioName, "")) && !validateInput(
            (String) data.get("StrategyFrequency"), "TimeUnit")) {
      errors.put("invalidFreqMessage", "Entered frequency is invalid.");
    }

    errors.put("isError", "");

    for (String key : errors.keySet()) {
      if (errors.get(key).length() > 0) {
        errors.put("isError", "true");
      }
    }

    return errors;
  }

  @Override
  public String validateCommissionFees(String fees) {
    return (checkDouble(fees) && Double.parseDouble(fees) >= 1 && Double.parseDouble(fees) <= 50)
            ? "" :
            "Given commission fees % is invalid (Must be between 1-50). Try Again.";
  }

  @Override
  public boolean validFileType(String fileType) {
    return Objects.equals(fileType, PortfolioType.FLEXIBLE.getValue())
            || Objects.equals(fileType, PortfolioType.INFLEXIBLE.getValue());
  }
}
