package view;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * This interface represents the import portfolio operation frame and its related operations.
 */
public interface ImportPortfolio extends ViewComponentGUI {
  /**
   * Method for printing the list of available portfolios.
   *
   * @throws IOException for error in input and output source.
   */
  void showFilePath() throws IOException;

  /**
   * Method for showing existing file type options available.
   * @param options available file type options.
   * @throws IOException exception.
   */
  void showFileTypeOption(List<String> options) throws IOException;

  /**
   * Method for printing file type error.
   * @throws IOException exception
   */
  void showFileTypeError() throws IOException;

  /**
   * Method for showing error message.
   * @param errors HashMap containing error messages.
   * @throws IOException exception.
   */
  void showMessage(HashMap<String, String> errors) throws IOException;
}
