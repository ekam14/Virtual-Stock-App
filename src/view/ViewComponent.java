package view;

import java.io.IOException;
import java.util.HashMap;

/**
 * This interface contains method signatures for the ViewComponent.
 */
public interface ViewComponent extends CreatePortfolio, ShowPortfolio, ImportPortfolio,
    ModifyPortfolio, InvestmentStrategy {

  /**
   * Method to print contents of the main page.
   *
   * @param setupOptions string array.
   * @throws IOException for error in input and output source.
   */
  void setUp(String[] setupOptions) throws IOException;

  /**
   * Method to print Invalid input message.
   *
   * @throws IOException for error in input and output source.
   */
  void printWrongInputMessage() throws IOException;

  /**
   * Method for printing exiting statement.
   *
   * @throws IOException for error in input and output source.
   */
  void printStopExecutionMessage() throws IOException;

  void showMainPage();

  void showComment(HashMap<String, String> errors, String val) throws IOException;
}
