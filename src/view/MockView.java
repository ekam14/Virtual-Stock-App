package view;

import controller.Features;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import model.CompanyStock;
import model.dto.PortfolioPerformanceDTO;
import model.enums.TransactionType;

/**
 * This class represents a Mock View which mimics the real view. This class is utilized to test the
 * Controller in isolation.
 */
public class MockView implements ViewComponent {

  private final StringBuilder log;

  /**
   * Constructs a Mock View object and initializes the log and uniqueCode class attributes.
   *
   * @param log        StringBuilder log used for testing.
   * @param uniqueCode code used for testing.
   */
  public MockView(StringBuilder log, int uniqueCode) {
    this.log = log;
  }

  @Override
  public void setUp(String[] setupOptions) throws IOException {
    // pass;
  }

  @Override
  public void showPortfolios(List<String> portfolioList) throws IOException {
    for (String s : portfolioList) {
      this.log.append(s).append("\n");
    }
  }

  @Override
  public void showNoPortfoliosMessage() throws IOException {
    // pass
  }

  @Override
  public void selectShowPortfolioOperation(String[] showPortOptions, String selectedPortfolio)
      throws IOException {
    // pass
  }

  @Override
  public double showPortfolioComposition(HashMap<String, CompanyStock> portfolioComposition)
      throws IOException {
    return 0;
  }

  @Override
  public void printWrongInputMessage() throws IOException {
    // pass;
  }

  @Override
  public void printStopExecutionMessage() throws IOException {
    // pass;
  }

  @Override
  public void showMainPage() {
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
  public void showFileTypeError() throws IOException {
    // pass;
  }

  @Override
  public void showComment(HashMap<String, String> errors, String val) throws IOException {
    // pass;
  }

  @Override
  public void enterNumberOfStocksToEnter() throws IOException {
    // pass;
  }

  @Override
  public void showPortfolioValue(String date, HashMap<String, CompanyStock> portfolio)
      throws IOException {
    // pass;
  }

  @Override
  public void createPortfolio(String portfolioName, TransactionType type) throws IOException {
    // pass;
  }

  @Override
  public void getEnteredStockDetailsMessage(int stockNumber) throws IOException {
    this.log.append("Stock number is: ").append(stockNumber);
  }

  @Override
  public void selectModifyPortfolioOperation(String[] modifyPortOptions, String
      selectedPortfolio)
      throws IOException {
    // pass
  }

  @Override
  public void showPortfolioCostBasis(String date, double costBasis) throws IOException {
    // pass
  }

  @Override
  public void showPortfolioPerformance(String dateRange,
      PortfolioPerformanceDTO dto) throws IOException {
    // pass
  }

  @Override
  public void showInputDateFormat() throws IOException {
    // pass
  }

  @Override
  public void showFilePath() throws IOException {
    // pass;
  }

  @Override
  public void showFileTypeOption(List<String> options) throws IOException {
    // pass;
  }
}