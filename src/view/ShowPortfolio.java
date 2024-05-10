package view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import controller.Features;
import model.CompanyStock;
import model.dto.PortfolioPerformanceDTO;

/**
 * This interface represents the show portfolio operation frame and its related operations.
 */
public interface ShowPortfolio {

  /**
   * Method for printing the list of available portfolios.
   *
   * @param portfolioList list of portfolios to show.
   * @throws IOException for error in input and output source.
   */
  void showPortfolios(List<String> portfolioList) throws IOException;

  /**
   * Method to print message when there are no portfolios to show.
   *
   * @throws IOException for error in input and output source.
   */
  void showNoPortfoliosMessage() throws IOException;

  /**
   * Method for showing operations available for a given portfolio.
   *
   * @param showPortOptions   show portfolio options.
   * @param selectedPortfolio selected portfolio.
   * @throws IOException for error in input and output source.
   */
  void selectShowPortfolioOperation(String[] showPortOptions, String selectedPortfolio)
      throws IOException;

  /**
   * Method for showing the portfolio composition.
   *
   * @param portfolioComposition Hash map of the portfolio contents.
   * @return composition of the portfolio.
   * @throws IOException for error in input and output source.
   */
  double showPortfolioComposition(HashMap<String,
      CompanyStock> portfolioComposition) throws IOException;

  /**
   * Method for showing cost basis of a portfolio for a date.
   *
   * @param date      Date at which cost basis is calculated.
   * @param costBasis Cost basis.
   * @throws IOException for error in input and output source.
   */
  void showPortfolioCostBasis(String date, double costBasis) throws IOException;

  /**
   * Method for showing portfolio performance.
   *
   * @param dateRange seleced date range.
   * @param dto       pair.
   * @throws IOException for error in input and output source.
   */
  void showPortfolioPerformance(String dateRange,
      PortfolioPerformanceDTO dto) throws IOException;

  /**
   * Method for showing value of the portfolio for the given date.
   *
   * @param date      Date for which portfolio value is required.
   * @param portfolio Portfolio.
   * @throws IOException for error in input and output source.
   */
  void showPortfolioValue(String date, HashMap<String, CompanyStock> portfolio) throws IOException;

  /**
   * Method for showing expected date format.
   *
   * @throws IOException for error in input and output source.
   */
  void showInputDateFormat() throws IOException;

  void addFeatures(Features features);
}
