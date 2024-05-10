package view;

import controller.Features;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import model.CompanyStock;
import model.dto.Pair;
import model.dto.PortfolioPerformanceDTO;
import model.enums.TransactionType;

/**
 * Class for representing the view component of the stock application. It implements all the methods
 * as per the ViewComponent interface.
 */
public class ViewComponentImpl implements ViewComponent {

  private final Appendable out;

  /**
   * Constructs a viewComponent object and initializes the class attribute out which represents an
   * output source.
   *
   * @param out Output source.
   */
  public ViewComponentImpl(Appendable out) {
    this.out = out;
  }

  /**
   * Helper method to create a string out of given options array.
   *
   * @param strArr options array.
   * @throws IOException for error in input and output source.
   */
  private void printArrayContents(String[] strArr) throws IOException {
    for (String option : strArr) {
      this.out.append(option).append("\n");
    }
  }

  @Override
  public void setUp(String[] setupOptions) throws IOException {
    this.out.append("*********************** MAIN PAGE *********************\n");
    printArrayContents(setupOptions);
  }

  @Override
  public void createPortfolio(String portfolioName, TransactionType type) throws IOException {
    this.out.append("*********************** NEW PORTFOLIO ***********************\n");
  }

  @Override
  public void showPortfolios(List<String> portfolioList) throws IOException {
    this.out.append("*********************** PORTFOLIOS ***********************\n");

    for (int i = 0; i < portfolioList.size(); i++) {
      this.out.append(String.format("%d. %s\n", i + 1, portfolioList.get(i)));
    }

    this.out.append("Type M to go to main page.\n");
  }

  @Override
  public void showNoPortfoliosMessage() throws IOException {
    this.out.append("There no portfolios to show!\n");
  }

  @Override
  public void selectShowPortfolioOperation(String[] showPortOptions, String selectedPortfolio)
      throws IOException {
    this.out.append(String.format("Selected portfolio: %s.%n\n", selectedPortfolio));
    printArrayContents(showPortOptions);
  }

  @Override
  public void selectModifyPortfolioOperation(String[] modifyPortOptions, String selectedPortfolio)
      throws IOException {
    this.out.append(String.format("Selected portfolio: %s.%n\n", selectedPortfolio));
    printArrayContents(modifyPortOptions);
  }

  @Override
  public double showPortfolioComposition(HashMap<String,
      CompanyStock> portfolioComposition) throws IOException {
    double totalValue = 0.0;

    if (portfolioComposition == null) {
      this.out.append("No portfolio maintained.\n");
    } else {
      //table;
      this.out.append(String.format("%s%n\n",
          "--------------------------------------------"
              + "---------------------------------------------------"));

      this.out.append(String.format("%15s%3s%15s%3s%15s%3s%15s%3s%15s%3s%15s%n\n",
          "CompanyName", "|", "CompanySymbol", "|", "Quantity", "|", "Unit Price", "|",
          "Total Value", "|", "Net Commission Fees"));
      this.out.append(String.format("%s%n",
          "--------------------------------------------------"
              + "---------------------------------------------"));
      String format = "%15s%3s%15s%3s%15.2f%3s%15.2f%3s%15.2f%3s%15.2f\n";
      for (CompanyStock companyStock : portfolioComposition.values()) {
        this.out.append(String.format((format) + "%n",
            companyStock.getCompanyName(), "|", companyStock.getCompanySymbol(),
            "|", companyStock.getQuantity(),
            "|", companyStock.getBoughtPrice(),
            "|", companyStock.getBoughtValue(),
            "|", companyStock.getCommissionFees()));
        totalValue += companyStock.getBoughtValue();
      }
    }

    return totalValue;
  }

  @Override
  public void printWrongInputMessage() throws IOException {
    this.out.append("Enter a valid option.\n");
  }

  @Override
  public void printStopExecutionMessage() throws IOException {
    this.out.append("***********************************\n");
    this.out.append("Stopping program execution.\n");
    this.out.append("***********************************\n");
  }

  @Override
  public void addFeatures(Features features) {
    // pass;
  }

  @Override
  public void showMessage(HashMap<String, String> errors) {
    // pass;
  }

  @Override
  public void modifyPortfolio(List<String> portfolioList) throws IOException {
    // pass;
  }

  @Override
  public void showMainPage() {
    // pass;
  }

  @Override
  public void enterNumberOfStocksToEnter() throws IOException {
    this.out.append("Enter number of stocks you want to add: ");
  }

  @Override
  public void showPortfolioValue(String date,
      HashMap<String, CompanyStock> portfolio) throws IOException {
    double portfolioValue = showPortfolioComposition(portfolio);
    this.out.append(String.format("Value of the portfolio on %s was $%.2f.\n",
        date, portfolioValue));
  }

  @Override
  public void showPortfolioCostBasis(String date, double costBasis) throws IOException {
    this.out.append(String.format("Cost basis of the portfolio till %s is $%.2f.\n",
        date, costBasis));
  }

  @Override
  public void showPortfolioPerformance(String dateRange, PortfolioPerformanceDTO dto)
      throws IOException {
    this.out.append("Performance of portfolio ").append(dto.getPortfolioName()).append(" from ")
        .append(dateRange.substring(0,
            10)).append(" to ").append(dateRange.substring(11)).append("\n\n");

    for (Pair p : dto.getData()) {
      this.out.append(p.getKey()).append(": ");
      StringBuilder performanceLevel = new StringBuilder();
      for (int i = 0; i < p.getValue(); i++) {
        performanceLevel.append("*");
      }
      this.out.append(performanceLevel.toString()).append("\n");
    }
    this.out.append("\nScale: * = $").append(String.format("%.2f", dto.getScaleFactor()))
        .append("\n");
  }

  @Override
  public void getEnteredStockDetailsMessage(int stockNumber) throws IOException {
    this.out.append("\n**********************************\n");
    if (stockNumber == -1) {
      this.out.append("Enter details for the stock.\n");
    } else {
      this.out.append(String.format("Enter details for the stock-%d\n", stockNumber));
    }
    this.out.append("**********************************\n");
  }

  @Override
  public void showInputDateFormat() throws IOException {
    this.out.append(
        "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:");
  }

  @Override
  public void showFilePath() throws IOException {
    this.out.append("Enter file path:");
  }

  @Override
  public void showFileTypeOption(List<String> options) throws IOException {
    this.out.append("Type M to go to main page.\n");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(").append(options.get(0)).append("/").append(options.get(1)).append(")");
    this.out.append("Enter portfolio type " + stringBuilder.toString() + ": ");
  }

  @Override
  public void showFileTypeError() throws IOException {
    this.out.append("Please enter Flexible or Inflexible only.\n");
  }

  @Override
  public void showComment(HashMap<String, String> errors, String val) throws IOException {
    // pass;
  }

  @Override
  public void showStrategies(String[] strategies) {
    // pass;
  }

  @Override
  public void strategiesPortfolio(List<String> portfolioList) {
    // pass;
  }

  @Override
  public void loadInvestmentStrategy(String portfolioName, List<String> companySymbols) {
    // pass;
  }
}
