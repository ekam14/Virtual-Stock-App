package model;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dto.PortfolioPerformanceDTO;
import model.enums.TransactionType;

import static java.lang.Integer.parseInt;

/**
 * This class represents a Mock Model which mimics the real model. This class is utilized to test
 * the Controller in isolation.
 */
public class MockModel implements ModelComponent {

  private final StringBuilder log;
  private final int uniqueCode;

  /**
   * Constructs a Mock Model object and initializes the log and uniqueCode class attributes.
   *
   * @param log        StringBuilder log used for testing.
   * @param uniqueCode code used for testing.
   */
  public MockModel(StringBuilder log, int uniqueCode) {
    this.log = log;
    this.uniqueCode = uniqueCode;
  }

  /**
   * Helper method for creating a dummy company stock data.
   *
   * @return Dummy company stock data.
   */
  private HashMap<String, CompanyStock> getDummyCompanyData() {
    HashMap<String, CompanyStock> dummyCompany = new HashMap<>();

    dummyCompany.put("Dummy Company", new CompanyStockImpl("2022-10-09", "Oracle",
        "ORCL", 109.0, 100.0, TransactionType.BUY, 100.0));

    return dummyCompany;
  }

  @Override
  public Boolean checkStock(String companySymbol) {
    return true;
  }

  @Override
  public String getCompanySymbol() {
    return "Dummy";
  }

  @Override
  public String getCompanyName() {
    return null;
  }

  @Override
  public double getPrice() {
    return uniqueCode;
  }

  @Override
  public void addStock(double quantity, double commissionFees, TransactionType type)
      throws IOException {
    log.append("Quantity is: ").append(quantity);
  }

  @Override
  public void savePortfolio(String fileType) {
    // pass
  }

  @Override
  public List<String> getPortfolioList() {
    List<String> dummyList = new ArrayList<>();

    dummyList.add("Portfolio_2022-10-31_23:12:46");
    dummyList.add("Portfolio_2022-10-30_23:12:46");
    dummyList.add("Portfolio_2022-10-29_23:12:46");

    for (String s : dummyList) {
      log.append(s).append("\n");
    }

    return dummyList;
  }

  @Override
  public HashMap<String, CompanyStock> getPortfolioComposition(String portfolioName) {
    log.append("Portfolio name: ").append(portfolioName).append("\n");

    return getDummyCompanyData();
  }

  @Override
  public HashMap<String, CompanyStock> getPortfolioValue(String date, String portfolioName) {
    log.append("Date value: ").append(date).append("\n");
    log.append("Portfolio name: ").append(portfolioName).append("\n");

    return getDummyCompanyData();
  }

  @Override
  public boolean checkNumber(String value) {
    log.append("Number value is: ").append(value).append("\n");

    try {
      parseInt(value);
    } catch (NumberFormatException e) {
      return false;
    }

    return parseInt(value) > 0;
  }

  @Override
  public boolean checkDate(String date) {
    log.append("Date is: ").append(date).append("\n");

    try {
      LocalDate now = LocalDate.now();
      LocalDate given = LocalDate.parse(date,
          DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT));
      if (now.compareTo(given) >= 0) {
        return true;
      }
    } catch (DateTimeParseException e) {
      // pass;
      return false;
    }

    return true;
  }

  @Override
  public boolean validDateRange(String dateRange, boolean isShowPortfolio) {
    return false;
  }

  @Override
  public boolean validatePath(String path) {
    return false;
  }

  @Override
  public String importPortfolio(String fileType, String path) {
    return "";
  }

  @Override
  public void updateFile(String fileName) {
    // pass;
  }

  @Override
  public boolean populateModelStock(String companySymbol, String date) {
    return true;
  }

  @Override
  public HashMap<String, Double> getPortfolioQuantity(String date, String selectedPortfolio) {
    return new HashMap<>();
  }

  @Override
  public double getCostBasis(String date, String selectedPortfolio) {
    return 0;
  }

  @Override
  public PortfolioPerformanceDTO getPerformanceOfPortfolio(String dateRange,
      String selectedPortfolio) {
    return null;
  }

  @Override
  public boolean validateInput(String input, String format) {
    return false;
  }

  public HashMap<String, String> applyStrategy(Map<String, Object> data) {
    return null;
  }

  @Override
  public boolean validFileType(String fileType) {
    return false;
  }

  @Override
  public String validateCommissionFees(String fees) {
    return null;
  }
}
