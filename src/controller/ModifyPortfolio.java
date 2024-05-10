package controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import model.ModelComponent;
import model.enums.PortfolioType;
import model.enums.TransactionType;
import view.ViewComponent;

/**
 * This class represents all the operations possible when modify portfolio command is invoked from
 * the Controller.
 */
public class ModifyPortfolio implements ControllerCommand {

  private final ModelComponent modelComponent;
  private final ViewComponent viewComponent;
  private final Appendable out;
  private final IOMethods IOObj;

  /**
   * Constructs a ModifyPortfolio object and initializes all the class attributes.
   *
   * @param attributesObj Class Attribute object.
   */
  ModifyPortfolio(ClassAttributes attributesObj) {
    Objects.requireNonNull(attributesObj.getModelComponent());
    Objects.requireNonNull(attributesObj.getViewComponent());
    this.out = attributesObj.getAppendable();
    this.modelComponent = attributesObj.getModelComponent();
    this.viewComponent = attributesObj.getViewComponent();
    this.IOObj = new IOMethods(attributesObj);
  }

  @Override
  public void followCommand() throws IOException {
    List<String> portfolioList = modelComponent.getPortfolioList();

    // if there are no portfolios to show.
    if (!IOObj.checkPortfolioExists(portfolioList)) {
      return;
    }

    String selectedPortfolio = "";

    boolean isValidFileType = false;

    while (!isValidFileType) {
      selectedPortfolio = IOObj.enterPortfolioNumber(portfolioList);

      if (Objects.equals(selectedPortfolio, "")) {
        return;
      }

      if (selectedPortfolio.contains(PortfolioType.INFLEXIBLE.getValue())) {
        this.out.append("This file contains inflexible portfolio, you cannot edit it. "
            + "Please select another portfolio\n");
      } else {
        isValidFileType = true;
      }
    }

    final String[] modifyPortOptions = {"1. Add a new stock.", "2. Sell a stock."};

    viewComponent.selectModifyPortfolioOperation(modifyPortOptions, selectedPortfolio);
    String portfolioOperation = IOObj.enterPortfolioOperation(2);

    if (Objects.equals(portfolioOperation, "1")) {
      IOObj.getEnteredStockDetails(-1, TransactionType.BUY, selectedPortfolio);
    } else if (Objects.equals(portfolioOperation, "2")) {
      IOObj.getEnteredStockDetails(-1, TransactionType.SELL, selectedPortfolio);
    }

    modelComponent.updateFile(selectedPortfolio);

    followCommand();
  }
}