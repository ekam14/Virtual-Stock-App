package view;

import java.util.HashMap;
import java.util.List;

import controller.Features;

/**
 * This interface represents the import portfolio operation frame and its related operations.
 */
public interface InvestmentStrategy {
  /**
   * Method to show available strategies.
   * @param strategies available strategies.
   */
  void showStrategies(String[] strategies);

  /**
   * Show portfolios on investment strategies page.
   * @param portfolioList list of available portfolios.
   */
  void strategiesPortfolio(List<String> portfolioList);

  /**
   * Shows apply investment strategy page.
   * @param portfolioName name of portfolio.
   * @param companySymbols list of company symbols.
   */
  void loadInvestmentStrategy(String portfolioName, List<String> companySymbols);

  /**
   * Links controller feature methods to view component.
   * @param features controller feature methods.
   */
  void addFeatures(Features features);

  /**
   * Method for showing error message.
   * @param errors HashMap containing error messages.
   */
  void showMessage(HashMap<String, String> errors);
}
