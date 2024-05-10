package view;

import java.awt.FlowLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import controller.Features;
import model.CompanyStock;
import model.dto.PortfolioPerformanceDTO;
import model.enums.TransactionType;

/**
 * This class represents the main page frame and also acts as a parent
 * class for its subsequent child classes which are different portfolio operations.
 */
public class JFrameView extends JFrame implements ViewComponent, ViewComponentGUI {

  private final CreatePortfolio createPortfolioFrame;
  private final ShowPortfolio showPortfolioFrame;

  private final ImportPortfolio importPortfolioFrame;

  private final ModifyPortfolio modifyPortfolioFrame;

  private final InvestmentStrategy investmentStrategyFrame;

  private final JButton createPortfolioButton;
  private final JButton showPortfolioButton;
  private final JButton importPortfolioButton;
  private final JButton modifyPortfolioButton;

  private final JButton investmentStrategyButton;
  private final JButton exitButton;

  /**
   * Constructs JFrame and initializes all class attributes.
   * @param caption frame caption.
   */
  public JFrameView(String caption) {
    super(caption);

    setSize(1000, 1000);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.setLayout(new FlowLayout());

    createPortfolioButton = new JButton("Create portfolio");
    createPortfolioButton.setActionCommand("Create portfolio");
    this.add(createPortfolioButton);

    showPortfolioButton = new JButton("Show portfolio");
    showPortfolioButton.setActionCommand("Show portfolio");
    this.add(showPortfolioButton);

    importPortfolioButton = new JButton("Import portfolio");
    importPortfolioButton.setActionCommand("Import portfolio");
    this.add(importPortfolioButton);

    modifyPortfolioButton = new JButton("Modify portfolio");
    modifyPortfolioButton.setActionCommand("Modify portfolio");
    this.add(modifyPortfolioButton);

    investmentStrategyButton = new JButton("Strategies Investment");
    investmentStrategyButton.setActionCommand("Investment Strategy");
    this.add(investmentStrategyButton);

    exitButton = new JButton("Exit");
    exitButton.setActionCommand("Exit");
    this.add(exitButton);

    pack();
    setVisible(true);

    createPortfolioFrame = new CreatePortfolioImpl("Create portfolio");
    showPortfolioFrame = new ShowPortfolioImpl("Portfolio operations");
    importPortfolioFrame = new ImportPortfolioImpl("Import Portfolio");
    modifyPortfolioFrame = new ModifyPortfolioImpl("Modify Portfolio");
    investmentStrategyFrame = new InvestmentStrategyImpl("Strategies Investment");
  }


  @Override
  public void addFeatures(Features features) {
    createPortfolioButton.addActionListener(event -> {
      try {
        features.createPortfolio("", TransactionType.BUY);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    showPortfolioButton.addActionListener(event -> {
      try {
        features.showPortfolio();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    modifyPortfolioButton.addActionListener(event -> {
      try {
        features.modifyPortfolio();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
    importPortfolioButton.addActionListener(event -> {
      try {
        features.loadImportPortfolioPage();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    investmentStrategyButton.addActionListener(event -> {
      try {
        features.showStrategies();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    exitButton.addActionListener(event -> features.exitProgram());

    createPortfolioFrame.addFeatures(features);
    showPortfolioFrame.addFeatures(features);
    modifyPortfolioFrame.addFeatures(features);
    investmentStrategyFrame.addFeatures(features);
    importPortfolioFrame.addFeatures(features);
  }

  @Override
  public void showMessage(HashMap<String, String> errors) {
    // pass;
  }

  @Override
  public void modifyPortfolio(List<String> portfolioList) throws IOException {
    this.setVisible(false);
    modifyPortfolioFrame.modifyPortfolio(portfolioList);
  }

  @Override
  public void showMainPage() {
    this.setVisible(true);
  }


  @Override
  public void showFilePath() throws IOException {
    // pass;
  }

  @Override
  public void showFileTypeOption(List<String> options) throws IOException {
    this.setVisible(false);
    importPortfolioFrame.showFileTypeOption(options);
  }

  @Override
  public void showFileTypeError() throws IOException {
    // pass;
  }

  @Override
  public void showComment(HashMap<String, String> errors, String val) throws IOException {
    if (val.equals("investment")) {
      investmentStrategyFrame.showMessage(errors);
    } else {
      importPortfolioFrame.showMessage(errors);
    }
  }

  @Override
  public void createPortfolio(String portfolioName, TransactionType type) throws IOException {
    this.setVisible(false);

    createPortfolioFrame.createPortfolio(portfolioName, type);
  }

  @Override
  public void getEnteredStockDetailsMessage(int stockNumber) throws IOException {
    createPortfolioFrame.getEnteredStockDetailsMessage(stockNumber);
  }

  @Override
  public void enterNumberOfStocksToEnter() throws IOException {
    // pass;
  }

  @Override
  public void selectModifyPortfolioOperation(String[] modifyPortOptions, String
          selectedPortfolio)
          throws IOException {
    modifyPortfolioFrame.selectModifyPortfolioOperation(modifyPortOptions,
            selectedPortfolio);
  }

  @Override
  public void showPortfolios(List<String> portfolioList) throws IOException {
    this.setVisible(false);

    if (portfolioList.size() == 0) {
      showPortfolioFrame.showNoPortfoliosMessage();
    } else {
      showPortfolioFrame.showPortfolios(portfolioList);
    }
  }

  @Override
  public void showNoPortfoliosMessage() throws IOException {
    // pass;
  }

  @Override
  public void selectShowPortfolioOperation(String[] showPortOptions, String
          selectedPortfolio)
          throws IOException {
    showPortfolioFrame.selectShowPortfolioOperation(showPortOptions, selectedPortfolio);

  }

  @Override
  public double showPortfolioComposition(HashMap<String, CompanyStock> portfolioComposition)
          throws IOException {
    showPortfolioFrame.showPortfolioComposition(portfolioComposition);
    return 0.0;

  }

  @Override
  public void showPortfolioCostBasis(String date, double costBasis) throws IOException {
    showPortfolioFrame.showPortfolioCostBasis(date, costBasis);
  }

  @Override
  public void showPortfolioPerformance(String dateRange, PortfolioPerformanceDTO dto)
          throws IOException {
    showPortfolioFrame.showPortfolioPerformance(dateRange, dto);

  }

  @Override
  public void showPortfolioValue(String date, HashMap<String, CompanyStock> portfolio)
          throws IOException {
    showPortfolioFrame.showPortfolioValue(date, portfolio);

  }

  @Override
  public void showInputDateFormat() throws IOException {
    // pass;
  }

  @Override
  public void setUp(String[] setupOptions) throws IOException {
    // pass;
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
  public void showStrategies(String[] strategies) {
    this.setVisible(false);
    investmentStrategyFrame.showStrategies(strategies);
  }

  @Override
  public void strategiesPortfolio(List<String> portfolioList) {
    investmentStrategyFrame.strategiesPortfolio(portfolioList);
  }

  @Override
  public void loadInvestmentStrategy(String portfolioName, List<String> companySymbols) {
    investmentStrategyFrame.loadInvestmentStrategy(portfolioName, companySymbols);
  }
}

