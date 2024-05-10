package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.CompanyStock;
import model.ModelComponent;
import model.dto.PortfolioPerformanceDTO;
import model.enums.PortfolioType;
import model.enums.TransactionType;
import view.ViewComponent;

import static java.lang.Integer.parseInt;

/**
 * This class represents the controller of the application for GUI. It contains modelComponent and
 * viewComponent objects as class attributes as it directly communicates with them for different
 * application features.
 */
public class ControllerGUI implements Features {
  private final ModelComponent modelComponent;
  private final ViewComponent viewComponent;

  /**
   * Constructs a ControllerGUI object and initializes all the class attributes.
   *
   * @param modelComponent ModelComponent object.
   * @param viewComponent  ModelComponent object.
   */
  public ControllerGUI(ModelComponent modelComponent, ViewComponent viewComponent) {
    this.modelComponent = modelComponent;
    this.viewComponent = viewComponent;
    viewComponent.addFeatures(this);
  }

  /**
   * Helper method for checking validity of the selected portfolio index.
   *
   * @param selectedPortfolio portfolio index.
   * @return portfolio name if selected portfolio index is valid, else empty string.
   */
  private String checkValidPortfolioIndex(String selectedPortfolio) {
    List<String> portfolioList = modelComponent.getPortfolioList();

    if (!modelComponent.checkNumber(selectedPortfolio)
            || parseInt(selectedPortfolio) > portfolioList.size()) {
      return "";
    }

    return portfolioList.get(parseInt(selectedPortfolio) - 1);
  }

  @Override
  public void createPortfolio(String portfolioName, TransactionType type) throws IOException {
    viewComponent.createPortfolio(portfolioName, type);
  }

  @Override
  public void showPortfolio() throws IOException {
    List<String> portfolioList = modelComponent.getPortfolioList();
    viewComponent.showPortfolios(portfolioList);
  }

  @Override
  public void loadImportPortfolioPage() throws IOException {
    viewComponent.showFilePath();
    viewComponent.showFileTypeOption(PortfolioType.list());
  }

  @Override
  public void showStrategies() throws IOException {
    String[] strategies = {"Fixed Amount", "Dollar cost averaging"};

    viewComponent.showStrategies(strategies);
  }

  @Override
  public void applyInvestmentStrategy(Map<String, Object> data) throws IOException {
    viewComponent.showComment(modelComponent.applyStrategy(data), "investment");
  }

  @Override
  public void goMainPage() {
    viewComponent.showMainPage();
  }

  @Override
  public void importPortfolio(String path, String fileType) throws IOException {
    HashMap<String, String> errors = new HashMap<>();

    errors.put("error", "");

    if (path == null || !modelComponent.validatePath(path)) {
      errors.put("error", "File path is invalid data. Please verify file path.");
    } else if (fileType == null || !modelComponent.validFileType(fileType)) {
      errors.put("error", "File type is invalid data. Please verify file type.");
    } else {
      String importFileName = modelComponent.importPortfolio(fileType, path);
      if (importFileName == null) {
        errors.put("error", "File contains invalid data. Please verify file content.");
      } else {
        errors.put("success", "File is successfully imported by name:" + importFileName);
      }
    }

    viewComponent.showComment(errors, "");
  }

  @Override
  public void modifyPortfolio() throws IOException {
    viewComponent.modifyPortfolio(modelComponent.getPortfolioList());
  }

  @Override
  public void strategiesPortfolio() throws IOException {
    viewComponent.strategiesPortfolio(modelComponent.getPortfolioList());
  }

  @Override
  public boolean modifySelectedPortfolio(String selectedPortfolio) throws IOException {
    String portfolioName = checkValidPortfolioIndex(selectedPortfolio);

    if (portfolioName.contains(PortfolioType.INFLEXIBLE.getValue())
            || Objects.equals(portfolioName, "")) {
      return false;
    }

    String[] modifyPortOptions = {"1. Buy a stock", "2. Sell a stock"};

    viewComponent.selectModifyPortfolioOperation(modifyPortOptions, portfolioName);

    return true;
  }

  @Override
  public boolean showSelectedPortfolio(String selectedPortfolio) throws IOException {
    String portfolioName = checkValidPortfolioIndex(selectedPortfolio);

    if (Objects.equals(portfolioName, "")) {
      return false;
    }

    String[] showPortOptions = {"1. Composition of the portfolio.", "2. Value of the portfolio.",
      "3. Cost basis of the portfolio.", "4. Show portfolio performance."};

    viewComponent.selectShowPortfolioOperation(showPortOptions, portfolioName);

    return true;
  }

  @Override
  public boolean strategySelectedPortfolio(String selectedPortfolio) {
    String portfolioName = checkValidPortfolioIndex(selectedPortfolio);

    if (portfolioName.contains(PortfolioType.INFLEXIBLE.getValue())
            || Objects.equals(portfolioName, "")) {
      return false;
    }

    HashMap<String, CompanyStock> composition
            = modelComponent.getPortfolioComposition(portfolioName);

    List<String> companySymbols = new ArrayList<>(composition.keySet());

    viewComponent.loadInvestmentStrategy(portfolioName, companySymbols);

    return true;
  }

  @Override
  public void performPortfolioOperation(String selectedPortfolio, String selectedPortfolioOperation,
                                        String date) throws IOException {
    String portfolioName = checkValidPortfolioIndex(selectedPortfolio);

    switch (selectedPortfolioOperation) {
      case "1":
        viewComponent.showPortfolioComposition(
                modelComponent.getPortfolioComposition(portfolioName));
        break;
      case "2":
        viewComponent.showPortfolioValue(date,
                modelComponent.getPortfolioValue(date, portfolioName));
        break;
      case "3":
        viewComponent.showPortfolioCostBasis(date,
                modelComponent.getCostBasis(date, portfolioName));
        break;
      case "4":
        PortfolioPerformanceDTO dto = modelComponent.getPerformanceOfPortfolio(date, portfolioName);
        viewComponent.showPortfolioPerformance(date, dto);
        break;
      default:
        //pass
    }
  }

  @Override
  public boolean checkDate(String date) {
    return modelComponent.checkDate(date);
  }

  @Override
  public boolean checkDateRange(String dateRange) {
    return modelComponent.validDateRange(dateRange, true);
  }

  @Override
  public boolean checkCompanySymbol(String companySymbol) {
    return modelComponent.checkStock(companySymbol);
  }

  @Override
  public boolean checkQnty(String quantity) {
    return modelComponent.checkNumber(quantity);
  }

  @Override
  public String checkSellQnty(String quantity, String date, String portfolioName) {
    HashMap<String, Double> qnty = new HashMap<>();

    qnty.put("minima", 0.0);
    qnty.put("currentQnty", 0.0);
    qnty.put("beforeQnty", 0.0);

    qnty = modelComponent.getPortfolioQuantity(date, portfolioName);

    if (qnty.get("currentQnty") == 0
            || Double.min(qnty.get("beforeQnty"), qnty.get("minima")) == 0) {
      return "There are no shares of this company in the portfolio for this date.";
    }

    if (qnty.get("minima") < Long.parseLong(quantity)
            || qnty.get("beforeQnty") < Long.parseLong(quantity)) {
      double portfolioQuantity = Double.min(qnty.get("beforeQnty"), qnty.get("minima"));
      String singular = portfolioQuantity == 1 ? "" : "s";
      return String.format("You can sell %.0f unit%s as you have "
              + "this amount of shares only!\n", portfolioQuantity, singular);
    }

    return "ok";
  }

  @Override
  public boolean checkFees(String fees) {
    try {
      Double.parseDouble(fees);
    } catch (NumberFormatException e) {
      return false;
    }
    return Double.parseDouble(fees) >= 1 && Double.parseDouble(fees) <= 50;
  }

  @Override
  public String getCompanyName(String companySymbol, String date) {
    modelComponent.populateModelStock(companySymbol, date);
    return modelComponent.getCompanyName();
  }

  @Override
  public double getPrice() {
    return modelComponent.getPrice();
  }

  @Override
  public void saveStock(String quantity, String fees, TransactionType txnType) throws IOException {
    modelComponent.addStock(Long.parseLong(quantity), Double.parseDouble(fees), txnType);
  }

  @Override
  public void savePortfolio(String portfolioName) throws IOException {
    if (Objects.equals(portfolioName, "")) {
      modelComponent.savePortfolio(PortfolioType.FLEXIBLE.getValue());
    } else {
      modelComponent.updateFile(portfolioName);
    }
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }
}
