package view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BoxLayout;

import controller.Features;
import model.enums.TransactionType;

/**
 * This class represents modify portfolio operation frame and its related operations.
 */
public class ModifyPortfolioImpl extends AbstractPortfolioHelper implements ModifyPortfolio,
        CreatePortfolio, ViewComponentGUI {
  private final JLabel portfolioName;
  private final JLabel invalidSelectedPortfolioMessage;
  private final JTextField selectedPortfolio;
  private final JTextField selectedPortfolioOperation;
  private final JButton goBackFromPortfolioOp;
  private final JButton buy;
  private final JButton sell;
  private final JPanel buttonPanel;
  private final JButton goMainPageBtn;

  /**
   * Constructs modify portfolio operation object in view component
   * and initializes frame values.
   *
   * @param caption Title of the frame.
   */
  public ModifyPortfolioImpl(String caption) throws HeadlessException {
    super(caption);

    portfolioName = new JLabel();

    invalidSelectedPortfolioMessage = new JLabel();
    selectedPortfolio = new JTextField(20);
    selectedPortfolioOperation = new JTextField(20);

    goBackFromPortfolioOp = new JButton("Go to last page");
    buy = new JButton("Buy a stock");
    sell = new JButton("Sell a stock");

    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    buttonPanel.add(buy);
    buttonPanel.add(sell);
    buttonPanel.add(goBackFromPortfolioOp);

    goMainPageBtn = new JButton("Go to main page");
  }

  /**
   * Helper method to reset class component values.
   */
  private void initComponents() {
    portfolioName.setText("");

    invalidSelectedPortfolioMessage.setText("");
    selectedPortfolio.setText("");
    selectedPortfolioOperation.setText("");
  }

  @Override
  public void modifyPortfolio(List<String> portfolioList) throws IOException {
    showPortfolioList(portfolioList, selectedPortfolio, invalidSelectedPortfolioMessage,
            goMainPageBtn);
  }

  @Override
  public void selectModifyPortfolioOperation(String[] modifyPortOptions, String selectedPortfolio)
          throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    portfolioName.setText(selectedPortfolio);

    pane.add(getHeading(portfolioName.getText()));

    pane.add(buttonPanel);

    pack();
    setVisible(true);
  }

  @Override
  public void createPortfolio(String portfolioName, TransactionType type) throws IOException {
    // pass;
  }

  @Override
  public void getEnteredStockDetailsMessage(int stockNumber) throws IOException {
    enterStocksDetails(stockNumber);
  }

  @Override
  public void enterNumberOfStocksToEnter() throws IOException {
    enterStocksNum();
  }

  @Override
  public void addFeatures(Features features) {
    linkFeatures(features);

    goMainPageBtn.addActionListener(event -> {
      resetFrame();
      initComponents();
      this.setVisible(false);
      features.goMainPage();
    });

    selectedPortfolio.addActionListener(event -> {
      try {
        resetFrame();
        String temp = selectedPortfolio.getText();
        initComponents();
        if (!features.modifySelectedPortfolio(temp)) {
          selectedPortfolio.setText("");
          invalidSelectedPortfolioMessage.setText("Select a valid portfolio.");
          features.modifyPortfolio();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    goBackFromPortfolioOp.addActionListener(event -> {
      resetFrame();
      portfolioName.setText("");
      selectedPortfolio.setText("");
      selectedPortfolioOperation.setText("");
      try {
        features.modifyPortfolio();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    buy.addActionListener(event -> {
      resetFrame();
      resetComponents();
      this.setVisible(false);
      String temp = portfolioName.getText();
      try {
        features.createPortfolio(temp, TransactionType.BUY);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    sell.addActionListener(event -> {
      resetFrame();
      resetComponents();
      this.setVisible(false);
      String temp = portfolioName.getText();
      try {
        features.createPortfolio(temp, TransactionType.SELL);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
