package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import model.CompanyStock;
import model.dto.PortfolioPerformanceDTO;
import model.ModelComponent;
import view.ViewComponent;

/**
 * This class represents all the operations possible when show portfolio command is invoked from the
 * Controller.
 */
public class ShowPortfolio implements ControllerCommand {

  private final ModelComponent modelComponent;
  private final ViewComponent viewComponent;
  private final Scanner scan;
  private final Appendable out;
  private final IOMethods IOObj;

  /**
   * Constructs a ShowPortfolio object and initializes all the class attributes.
   *
   * @param attributesObj Class Attribute object.
   */
  ShowPortfolio(ClassAttributes attributesObj) {
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
    List<String> portfolioList = modelComponent.getPortfolioList();

    // if there are no portfolios to show.
    if (!IOObj.checkPortfolioExists(portfolioList)) {
      return;
    }

    String selectedPortfolio = IOObj.enterPortfolioNumber(portfolioList);

    if (Objects.equals(selectedPortfolio, "")) {
      return;
    }

    String[] showPortOptions = {"1. Composition of the portfolio.", "2. Value of the portfolio.",
        "3. Cost basis of the portfolio.", "4. Show portfolio performance."};

    viewComponent.selectShowPortfolioOperation(showPortOptions, selectedPortfolio);
    String portfolioOperation = IOObj.enterPortfolioOperation(4);

    switch (portfolioOperation) {
      case "1":
        HashMap<String, CompanyStock> portfolioComposition = modelComponent.getPortfolioComposition(
            selectedPortfolio);
        viewComponent.showPortfolioComposition(portfolioComposition);
        break;
      case "2": {
        String date = IOObj.enterDate();
        viewComponent.showPortfolioValue(date,
            modelComponent.getPortfolioValue(date, selectedPortfolio));
        break;
      }
      case "3": {
        String date = IOObj.enterDate();
        viewComponent.showPortfolioCostBasis(date,
            modelComponent.getCostBasis(date, selectedPortfolio));
        break;
      }
      default:
        String dateRange = "";

        viewComponent.showInputDateFormat();
        dateRange = scan.nextLine();

        if (modelComponent.validDateRange(dateRange, true)) {
          PortfolioPerformanceDTO dto
              = modelComponent.getPerformanceOfPortfolio(dateRange, selectedPortfolio);
          if (dto != null && dto.getData() != null && dto.getData().size() >= 5) {
            viewComponent.showPortfolioPerformance(dateRange, dto);
            break;
          }
        } else if (dateRange != null && dateRange.equals("M")) {
          return;
        }

        this.out.append("Entered dateRange is invalid. Try Again!\n");
        break;
    }

    followCommand();
  }
}