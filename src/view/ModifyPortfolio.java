package view;

import java.io.IOException;
import java.util.List;

/**
 * This interface represents modify portfolio operation frame and its related operations.
 */
public interface ModifyPortfolio extends ViewComponentGUI {
  /**
   * Method for showing operations list on modify portfolio page.
   * @param modifyPortOptions modify portfolio operations.
   * @param selectedPortfolio  selected portfolio.
   * @throws IOException for error in input and output source.
   */
  void selectModifyPortfolioOperation(String[] modifyPortOptions, String selectedPortfolio)
          throws IOException;

  void modifyPortfolio(List<String> portfolioList) throws IOException;
}
