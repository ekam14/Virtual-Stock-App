package controller;

import java.io.IOException;
import java.util.Map;

import model.enums.TransactionType;

/**
 * This interface represents all the helper operations for supporting GUI
 * based view in controller side.
 */
public interface Features {
  /**
   * Method for creating a new portfolio.
   * @param portfolioName Name of the portfolio.
   * @param type Transaction type.
   * @throws IOException for error in input and output source.
   */
  void createPortfolio(String portfolioName, TransactionType type) throws IOException;

  /**
   * Method for showing list of available portfolios.
   * @throws IOException for error in input and output source.
   */
  void showPortfolio() throws IOException;

  /**
   * Method for calling modify portfolio component in view.
   * @throws IOException for error in input and output source.
   */
  void modifyPortfolio() throws IOException;

  /**
   * Method for calling investment strategies component in view.
   * @throws IOException for error in input and output source.
   */
  void strategiesPortfolio() throws IOException;

  /**
   * Method for calling import portfolio component in view.
   * @throws IOException for error in input and output source.
   */
  void loadImportPortfolioPage() throws IOException;

  /**
   * Method for selecting portfolio for modification.
   * @param selectedPortfolio Index of the selected portfolio.
   * @return validity of the given portfolio index.
   * @throws IOException for error in input and output source.
   */
  boolean modifySelectedPortfolio(String selectedPortfolio) throws IOException;

  /**
   * Method for selecting portfolio for investment strategy.
   * @param selectedPortfolio Index of the selected portfolio.
   * @return validity of the given portfolio index.
   */
  boolean strategySelectedPortfolio(String selectedPortfolio);

  /**
   * Helper method for showing the main page frame.
   */
  void goMainPage();

  /**
   * Method for selecting portfolio for performing operations such as composition,
   * value, cost basis and portfolio performance.
   * @param selectedPortfolio Index of the selected portfolio.
   * @return validity of the given portfolio index.
   * @throws IOException for error in input and output source.
   */
  boolean showSelectedPortfolio(String selectedPortfolio) throws IOException;

  /**
   * Method for performing operations: show composition, value, cost basis
   * portfolio performance on selected portfolio.
   * @param selectedPortfolio Index of selected portfolio.
   * @param selectedPortfolioOperation user entered portfolio operation.
   * @param date date.
   * @throws IOException for error in input and output source.
   */
  void performPortfolioOperation(String selectedPortfolio, String selectedPortfolioOperation,
                                 String date) throws IOException;

  /**
   * Method for getting company name based on company symbol and
   * populates model company stock variable based on symbol and date.
   * @param companySymbol symbol of the company.
   * @param date date.
   * @return company name of the stock.
   */
  String getCompanyName(String companySymbol, String date);

  /**
   * Method for getting stock unit price.
   * @return stock unit price.
   */
  double getPrice();

  /**
   * Method for saving current stock transactions in model class data.
   * @param quantity stock quantity.
   * @param fees commission fees.
   * @param txnType transaction type.
   * @throws IOException for error in input and output source.
   */
  void saveStock(String quantity, String fees, TransactionType txnType) throws IOException;

  /**
   * Method for saving current transactions in the selected portfolio.
   * @param portfolioName portfolio name.
   * @throws IOException for error in input and output source.
   */
  void savePortfolio(String portfolioName) throws IOException;

  /**
   * Method for performing import portfolio operation.
   * @param path path of the portfolio.
   * @param fileType portfolio file type.
   * @throws IOException for error in input and output source.
   */
  void importPortfolio(String path, String fileType) throws IOException;

  /**
   * Helper method for exiting the program/frame.
   */
  void exitProgram();

  /**
   * Shows list of available strategies.
   * @throws IOException for error in input and output source.
   */
  void showStrategies() throws IOException;

  /**
   * Performs investment strategy.
   * @param data user entered data.
   * @throws IOException for error in input and output source.
   */
  void applyInvestmentStrategy(Map<String, Object> data) throws IOException;

  /**
   * Helper method for validating user entered date.
   * @param date user entered date.
   * @return validity of user entered date.
   */
  boolean checkDate(String date);

  /**
   * Helper method for validating user entered date range.
   * @param dateRange user entered date range.
   * @return validity of user entered date range.
   */
  boolean checkDateRange(String dateRange);

  /**
   * Helper method for validating user entered company symbol.
   * @param companySymbol user entered company symbol.
   * @return validity of user entered company symbol.
   */
  boolean checkCompanySymbol(String companySymbol);

  /**
   * Helper method for validating user entered quantity.
   * @param quantity user entered quantity.
   * @return validity of user entered quantity.
   */
  boolean checkQnty(String quantity);

  /**
   * Helper method for validating user entered commission fees.
   * @param fees user entered commission fees.
   * @return validity of user entered commission fees.
   */
  boolean checkFees(String fees);

  /**
   * Helper method for validating if user can sell quantity amount os shares in a
   * portfolio.
   * @param quantity stock quantity.
   * @param date date at which user wants to sell shares.
   * @param portfolioName name of the portfolio.
   * @return validity of sell operation.
   */
  String checkSellQnty(String quantity, String date, String portfolioName);
}
