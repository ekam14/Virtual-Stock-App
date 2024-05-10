package view;

import java.io.IOException;

import model.enums.TransactionType;

/**
 * This interface represents the create portfolio operation frame and its related operations.
 */
public interface CreatePortfolio extends ViewComponentGUI {
  /**
   * Method to print contents of the createPortfolio page.
   * @param portfolioName name of the portfolio
   * @param type transaction type
   * @throws IOException for error in input and output source.
   */
  void createPortfolio(String portfolioName, TransactionType type) throws IOException;

  /**
   * Method for showing helper message.
   * @param stockNumber index of current stock.
   * @throws IOException for error in input and output source.
   */
  void getEnteredStockDetailsMessage(int stockNumber) throws IOException;

  /**
   * Method for showing contents when we are asking the user to enter number of stocks.
   * @throws IOException for error in input and output source.
   */
  void enterNumberOfStocksToEnter() throws IOException;
}
