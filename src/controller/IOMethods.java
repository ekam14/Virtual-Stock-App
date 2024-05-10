package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import model.ModelComponent;
import model.enums.PortfolioType;
import model.enums.TransactionType;
import view.ViewComponent;

import static java.lang.Integer.parseInt;

/**
 * This class contains all the Helper methods required to take and validate user input.
 */
public class IOMethods {

  private final ModelComponent modelComponent;
  private final ViewComponent viewComponent;
  private final Scanner scan;
  private final Appendable out;

  /**
   * Constructs a IOMethods object and initializes all the class attributes.
   *
   * @param attributesObj Class Attribute object.
   */
  IOMethods(ClassAttributes attributesObj) {
    Objects.requireNonNull(attributesObj.getModelComponent());
    Objects.requireNonNull(attributesObj.getViewComponent());
    this.out = attributesObj.getAppendable();
    this.scan = attributesObj.getScanner();
    this.modelComponent = attributesObj.getModelComponent();
    this.viewComponent = attributesObj.getViewComponent();
  }

  /**
   * Helper method to get stock details from the user.
   *
   * @param stockNumber       Current stock number.
   * @param type              Transaction type.
   * @param selectedPortfolio Name of the portfolio.
   * @throws IOException for error in input and output source.
   */
  protected void getEnteredStockDetails(int stockNumber, TransactionType type,
                                        String selectedPortfolio) throws IOException {
    viewComponent.getEnteredStockDetailsMessage(stockNumber);
    HashMap<String, Double> qnty = new HashMap<>();

    qnty.put("minima", 0.0);
    qnty.put("currentQnty", 0.0);
    qnty.put("beforeQnty", 0.0);

    String date;

    date = enterCompanySymbol();

    if (type == TransactionType.SELL) {
      qnty = modelComponent.getPortfolioQuantity(date, selectedPortfolio);

      if (qnty.get("currentQnty") == 0
              || Double.min(qnty.get("beforeQnty"), qnty.get("minima")) == 0) {
        this.out.append("There are no shares of this company in the portfolio for this date.\n");
        return;
      }
    }

    this.out.append(String.format("Company Name: %s\n", modelComponent.getCompanyName()));
    this.out.append(String.format("Price: %.2f\n", modelComponent.getPrice()));
    String quantity = enterQuantity();
    String commissionFees = enterCommissionFees();

    while (type == TransactionType.SELL && (qnty.get("minima") < Long.parseLong(quantity)
            || qnty.get("beforeQnty") < Long.parseLong(quantity))) {
      double portfolioQuantity = Double.min(qnty.get("beforeQnty"), qnty.get("minima"));
      String singular = portfolioQuantity == 1 ? "" : "s";
      this.out.append(String.format("You can sell %.0f unit%s as you have "
              + "this amount of shares only!\n", portfolioQuantity, singular));
      quantity = enterQuantity();
    }

    modelComponent.addStock(parseInt(quantity), Double.parseDouble(commissionFees), type);
  }


  /**
   * Helper method to take commission fees from the user.
   *
   * @return User entered commission fees.
   * @throws IOException for error in input and output source.
   */
  protected String enterCommissionFees() throws IOException {
    String fees = "";
    boolean isInValidQuantity = true;
    do {
      this.out.append("Commission Fees % (Must be in range 1-50): ");
      fees = scan.nextLine();
      String error = modelComponent.validateCommissionFees(fees);
      if (error.equals("")) {
        isInValidQuantity = false;
      } else {
        this.out.append(error + "\n");
      }
    }
    while (isInValidQuantity);

    return fees;
  }


  /**
   * Helper method to take file/portfolio type from the user.
   *
   * @return User entered file/portfolio type.
   * @throws IOException for error in input and output source.
   */
  protected String enterFileType() throws IOException {
    String fileType = "";

    this.out.append("Enter portfolio type (Flexible/Inflexible): ");
    fileType = scan.nextLine();

    while (!Objects.equals(fileType, PortfolioType.FLEXIBLE.getValue())
            && !Objects.equals(fileType, PortfolioType.INFLEXIBLE.getValue())) {
      this.out.append("Please enter Flexible or Inflexible only.\n");
      this.out.append("Enter portfolio type (Flexible/Inflexible): ");
      fileType = scan.nextLine();
    }

    return fileType;
  }

  /**
   * Helper method to take portfolio number from the user.
   *
   * @param portfolioList list of portfolios.
   * @return portfolio from the portfolio list at portfolio number.
   * @throws IOException for error in input and output source.
   */
  protected String enterPortfolioNumber(List<String> portfolioList) throws IOException {
    String portfolioNumber;

    boolean isInValidOption = true;

    do {
      this.out.append("Enter portfolio number: ");
      portfolioNumber = scan.nextLine();
      if (Objects.equals(portfolioNumber, "M")) {
        return "";
      }
      if (modelComponent.checkNumber(portfolioNumber)
              && parseInt(portfolioNumber) <= portfolioList.size()) {
        isInValidOption = false;
      } else {
        this.out.append("Entered portfolio number is invalid. Try Again.\n");
      }
    }
    while (isInValidOption);

    return portfolioList.get(parseInt(portfolioNumber) - 1);
  }

  /**
   * Helper method to check if the list of portfolio exists.
   *
   * @param portfolioList list of portfolios.
   * @return true if there is a list of portfolio, else false.
   * @throws IOException for error in input and output source.
   */
  protected Boolean checkPortfolioExists(List<String> portfolioList) throws IOException {
    if (portfolioList.size() == 0) {
      viewComponent.showNoPortfoliosMessage();
      return false;
    }
    viewComponent.showPortfolios(portfolioList);

    return true;
  }

  /**
   * Helper method to take portfolio operation from the user.
   *
   * @param maxOp maximum number of operations possible.
   * @return user entered portfolio operation.
   * @throws IOException for error in input and output source.
   */
  protected String enterPortfolioOperation(int maxOp) throws IOException {
    String portfolioOperation;

    boolean isInValidOption = true;
    do {
      this.out.append("What do you want? ");
      portfolioOperation = scan.nextLine();
      if (modelComponent.checkNumber(portfolioOperation) && parseInt(portfolioOperation) <= maxOp) {
        isInValidOption = false;
      } else {
        this.out.append("Entered portfolio operation is invalid. Try Again.\n");
      }
    }
    while (isInValidOption);

    return portfolioOperation;
  }

  /**
   * Helper method to take company symbol from the user.
   *
   * @return User entered company symbol.
   * @throws IOException for error in input and output source.
   */
  protected String enterCompanySymbol() throws IOException {
    String companySymbol;
    String date = "";

    boolean isValidSymbolAndDate = false;
    do {
      this.out.append("Company Symbol: ");
      companySymbol = scan.nextLine();
      if (modelComponent.checkStock(companySymbol)) {
        date = enterDate();
        isValidSymbolAndDate = modelComponent.populateModelStock(companySymbol, date);
        if (!isValidSymbolAndDate) {
          this.out.append("Given Company Symbol is invalid for given date. "
                  + "Try with different date Again.\n");
        }
      } else {
        this.out.append("Given Company Symbol is invalid. Try Again.\n");
      }
    }
    while (!isValidSymbolAndDate);

    return date;
  }

  /**
   * Helper method to take stock quantity from the user.
   *
   * @return User entered stock quantity.
   * @throws IOException for error in input and output source.
   */
  protected String enterQuantity() throws IOException {
    String quantity = "";
    boolean isInValidQuantity = true;
    do {
      this.out.append("Transaction quantity: ");
      quantity = scan.nextLine();
      if (modelComponent.checkNumber(quantity)) {
        isInValidQuantity = false;
      } else {
        this.out.append("Given quantity is invalid. Try Again.\n");
      }
    }
    while (isInValidQuantity);

    return quantity;
  }

  /**
   * Helper method to take date from the user.
   *
   * @return User entered date.
   * @throws IOException for error in input and output source.
   */
  protected String enterDate() throws IOException {
    String date = "";
    boolean isInValid = true;
    do {
      this.out.append("Please enter a date in (YYYY-MM-DD) format: ");
      date = scan.nextLine();
      if (modelComponent.checkDate(date)) {
        isInValid = false;
      } else {
        this.out.append("Entered date is invalid. Try Again.\n");
      }
    }
    while (isInValid);
    return date;
  }
}
