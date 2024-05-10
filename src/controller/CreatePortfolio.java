package controller;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import model.ModelComponent;
import model.enums.TransactionType;
import view.ViewComponent;

import static java.lang.Integer.parseInt;

/**
 * This class represents all the operations possible when create portfolio command is invoked from
 * the Controller.
 */
public class CreatePortfolio implements ControllerCommand {
  private final ModelComponent modelComponent;
  private final ViewComponent viewComponent;
  private final Scanner scan;
  private final Appendable out;
  private final IOMethods IOObj;

  /**
   * Constructs a CreatePortfolio object and initializes all the class attributes.
   *
   * @param attributesObj Class Attribute object.
   */
  CreatePortfolio(ClassAttributes attributesObj) {
    Objects.requireNonNull(attributesObj.getModelComponent());
    Objects.requireNonNull(attributesObj.getViewComponent());
    this.out = attributesObj.getAppendable();
    this.scan = attributesObj.getScanner();
    this.modelComponent = attributesObj.getModelComponent();
    this.viewComponent = attributesObj.getViewComponent();
    this.IOObj = new IOMethods(attributesObj);
  }

  @Override
  public void followCommand() throws IOException {
    viewComponent.createPortfolio("", TransactionType.BUY);

    String fileType = IOObj.enterFileType();

    viewComponent.enterNumberOfStocksToEnter();
    String numStocks = scan.nextLine();

    while (!modelComponent.checkNumber(numStocks)) {
      this.out.append("Please enter a natural number of new stocks to be added.\n");
      viewComponent.enterNumberOfStocksToEnter();
      numStocks = scan.nextLine();
    }

    for (int i = 0; i < parseInt(numStocks); i++) {
      IOObj.getEnteredStockDetails(i + 1, TransactionType.BUY, "");
    }

    modelComponent.savePortfolio(fileType);
  }
}
