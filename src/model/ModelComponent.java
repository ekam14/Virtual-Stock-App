package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dto.PortfolioPerformanceDTO;
import model.enums.TransactionType;

/**
 * This interface contains method signatures for the ModelComponent.
 */
public interface ModelComponent {

  /**
   * Helper method to check if the given companyName has valid company Symbol in exchange.
   *
   * @param companyName Name of the company.
   * @return validity of the company name.
   */
  Boolean checkStock(String companyName);

  /**
   * Helper method to get the stock symbol.
   *
   * @return Stock symbol.
   */
  String getCompanySymbol();

  /**
   * Helper method to get the company name.
   *
   * @return Company Name
   */
  String getCompanyName();

  /**
   * Helper method to get the stock price of 1 stock.
   *
   * @return Stock price per unit.
   */
  double getPrice();

  /**
   * Helper method to add stock details into a hash.
   *
   * @param quantity       stock quantity.
   * @param commissionFees commission fees.
   * @param type           transaction type.
   * @throws IOException for error in input and output source.
   */
  void addStock(double quantity, double commissionFees, TransactionType type) throws IOException;

  /**
   * Helper method to save portfolio onto an external file.
   *
   * @param fileType portfolio type.
   * @throws IOException for error in input and output source.
   */
  void savePortfolio(String fileType) throws IOException;

  /**
   * Helper method to get the saved portfolio list.
   *
   * @return list of portfolios created.
   */
  List<String> getPortfolioList();

  /**
   * Helper method for getting the composition of the portfolio stored in file.
   *
   * @param portfolioName name of the portfolio to be retrieved.
   * @return composition of the portfolio.
   */
  HashMap<String, CompanyStock> getPortfolioComposition(String portfolioName);

  /**
   * Helper method to get the total value of the portfolio for a given date.
   *
   * @param date          date as string
   * @param portfolioName name of the portfolio.
   * @return Portfolio value for some date.
   */
  HashMap<String, CompanyStock> getPortfolioValue(String date, String portfolioName);

  /**
   * Helper method to check if the given user input represents a valid number or not.
   *
   * @param value User input.
   * @return validity of the user input.
   */
  boolean checkNumber(String value);

  /**
   * Helper method to check if the given user input date represents a valid date or not.
   *
   * @param date User input date.
   * @return validity of the user input date.
   */
  boolean checkDate(String date);

  /**
   * Method for checking the validity of a given date range.
   *
   * @param dateRange date range.
   * @param isShowPortfolio part of show portfolio operation or not.
   * @return validity of a date range.
   */
  boolean validDateRange(String dateRange, boolean isShowPortfolio);

  /**
   * Helper method to validate if the imported portfolio path.
   *
   * @param path path of imported file.
   * @return validity of the given path.
   * @throws IOException for error in input and output source.
   */
  boolean validatePath(String path) throws IOException;

  /**
   * Helper method to save imported portfolio data and returns the new generated file name.
   *
   * @param fileType portfolio type.
   * @param path     path of imported file.
   * @return filename of portfolio.
   */
  String importPortfolio(String fileType, String path);

  /**
   * Method for updating an existing file.
   *
   * @param fileName portfolio file name.
   */
  void updateFile(String fileName);

  /**
   * Method for populating the class stock attribute to given arguments.
   *
   * @param companySymbol Company/Stock symbol.
   * @param date          Date of transaction.
   * @return true if able to populate, else false.
   */
  boolean populateModelStock(String companySymbol, String date);

  /**
   * Method for getting stock quantities in the portfolio for a given date.
   *
   * @param date              Date for which quantity is required.
   * @param selectedPortfolio selected portfolio.
   * @return stock quantities in a portfolio.
   */
  HashMap<String, Double> getPortfolioQuantity(String date, String selectedPortfolio);

  /**
   * Method for getting the cost basis for a given portfolio and date.
   *
   * @param date              Date.
   * @param selectedPortfolio selected portfolio.
   * @return cost basis of the portfolio for a given date.
   */
  double getCostBasis(String date, String selectedPortfolio);

  /**
   * Method for getting values required to plot portfolio performance.
   *
   * @param dateRange         Required Date range of performance.
   * @param selectedPortfolio selected portfolio.
   * @return PortfolioPerformanceDTO.
   */
  PortfolioPerformanceDTO getPerformanceOfPortfolio(String dateRange,
      String selectedPortfolio);

  /**
   * Method for applying investment strategy on stock data.
   * @param data stock data - symbol, weights, frequency, amount.
   * @return input errors message if any.
   * @throws IOException for invalid data type errors.
   */
  HashMap<String, String> applyStrategy(Map<String, Object> data) throws IOException;

  /**
   * Method for validating investment strategy data and correct format.
   * @param input data to be validated.
   * @param format data format.
   * @return validity of investment strategy data and correct format.
   */
  boolean validateInput(String input, String format);

  /**
   * Method for checking if the passed file type is of valid type.
   * @param fileType user entered file type.
   * @return validity of file type.
   */
  boolean validFileType(String fileType);

  /**
   * Validate user entered commission fees.
   * @param fees user entered commission fees.
   * @return Validity user entered commission fees.
   */
  String validateCommissionFees(String fees);
}
