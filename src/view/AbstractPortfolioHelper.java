package view;

import java.awt.HeadlessException;
import java.awt.Container;
import java.awt.Font;
import java.awt.FlowLayout;

import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;

import controller.Features;

/**
 * Abstract class for common methods in view component class for GUI.
 */
abstract class AbstractPortfolioHelper extends JFrame {
  protected JTextField numStocks;
  protected JLabel invalidNumStocksMessage;
  protected int stockNumber;
  private JLabel invalidCompanySymbolMessage;
  private JLabel enterCompanySymbol;
  private JTextField companySymbol;
  private JLabel companyName;
  private JLabel invalidDateMessage;
  private JLabel enterDate;
  protected JTextField date;
  private JLabel price;
  protected JLabel invalidQntyMessage;
  protected JLabel enterQnty;
  protected JTextField qnty;
  private JLabel invalidFeesMessage;
  protected JLabel enterFees;
  protected JTextField fees;
  protected JButton goMainPage;
  protected JButton buy;

  /**
   * Constructs and initializes class variables.
   *
   * @param title frame caption.
   * @throws HeadlessException error.
   */
  public AbstractPortfolioHelper(String title) throws HeadlessException {
    super(title);
    numStocks = new JTextField(10);
    stockNumber = 1;
    initComponents();
    resetComponents();
    setSize(1000, 1000);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Method for checking if any invalid message JTextFields is empty or not.
   *
   * @param field   JTextField of the invalid message.
   * @param message JLabel of the invalid message.
   * @return true if invalid message JTextFields is empty else false.
   */
  private boolean checkValidity(JTextField field, JLabel message) {
    return field.getText().length() > 0 && Objects.equals(message.getText(), "");
  }

  /**
   * Helper method for initializing frame components.
   */
  private void initComponents() {
    numStocks = new JTextField(20);
    invalidNumStocksMessage = new JLabel();
    invalidCompanySymbolMessage = new JLabel();
    enterCompanySymbol = new JLabel("Enter Company Symbol: ");
    companySymbol = new JTextField(20);
    companyName = new JLabel("Company name: ");
    invalidDateMessage = new JLabel();
    enterDate = new JLabel("Enter a date in (YYYY-MM-DD) format: ");
    date = new JTextField(20);
    price = new JLabel("Price: ");
    invalidQntyMessage = new JLabel();
    enterQnty = new JLabel("Transaction quantity: ");
    qnty = new JTextField(20);
    invalidFeesMessage = new JLabel();
    enterFees = new JLabel("Commission Fees % (Must be in range 1-50): ");
    fees = new JTextField(20);
    goMainPage = new JButton("Go to main page");
    buy = new JButton("Submit");
  }

  /**
   * Helper method for checking if current data like symbol,
   * quantity, date etc. is valid.
   *
   * @param pane           container in which we need to show UI components.
   * @param textField      Field containing specific stock data.
   * @param invalidMessage JLabel for showing messages if data is invalid.
   * @param label          label for showing entered data.
   * @return validity of current data.
   */
  private boolean stockDetails(Container pane, JTextField textField, JLabel invalidMessage,
                               JLabel label) {
    boolean isValid = checkValidity(textField, invalidMessage);

    JPanel row1 = new JPanel();

    row1.add(label);
    if (!isValid) {
      row1.add(textField);
    }

    pane.add(row1);
    pane.add(getHeading(invalidMessage.getText()));

    return isValid;
  }

  /**
   * Helper method for removing all from this frame.
   */
  protected void resetFrame() {
    this.getContentPane().removeAll();
  }

  /**
   * Helper method for resetting all frame components.
   */
  protected void resetComponents() {
    invalidNumStocksMessage.setText("");

    invalidCompanySymbolMessage.setText("");
    enterCompanySymbol.setText("Enter Company Symbol: ");
    companySymbol.setText("");

    companyName.setText("Company name: ");

    invalidDateMessage.setText("");
    enterDate.setText("Enter a date in (YYYY-MM-DD) format: ");
    date.setText("");

    price.setText("Price: ");

    invalidQntyMessage.setText("");
    enterQnty.setText("Transaction quantity: ");
    qnty.setText("");

    invalidFeesMessage.setText("");
    enterFees.setText("Commission Fees % (Must be in range 1-50): ");
    fees.setText("");
  }

  /**
   * Method for creating JLabel with bold content.
   *
   * @param content JLabel content.
   * @return JLabel with bold content.
   */
  protected JPanel getHeading(String content) {
    JLabel heading = new JLabel(content, SwingConstants.CENTER);
    Font f = heading.getFont();
    heading.setFont(f.deriveFont(f.getStyle() | Font.BOLD));

    JPanel textPanel = new JPanel();
    textPanel.add(heading);

    return textPanel;
  }

  /**
   * Method for creating JButton with proper alignment.
   * @param button JButton which needs to be added into panel.
   * @return JPanel containing specified button.
   */
  protected JPanel getButton(JButton button) {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    buttonPanel.add(button);

    return buttonPanel;
  }

  /**
   * Gets user entered number of stocks.
   */
  protected void enterStocksNum() {
    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    JPanel row = new JPanel();
    row.setLayout(new FlowLayout());

    row.add(new JLabel("Enter number of stocks you want to add: "));
    row.add(numStocks);

    pane.add(row);

    pane.add(getHeading(invalidNumStocksMessage.getText()));

    pane.add(getButton(goMainPage));

    pack();
    setVisible(true);
  }

  /**
   * Method for showing frame consisting of available portfolios.
   *
   * @param portfolioList                   list of available portfolios.
   * @param selectedPortfolio               field seeking user entered selected portfolio index.
   * @param invalidSelectedPortfolioMessage JLabel for showing message if wrong index is typed.
   * @param goMainPageBtn                   main page JButton.
   */
  protected void showPortfolioList(List<String> portfolioList, JTextField selectedPortfolio,
                                   JLabel invalidSelectedPortfolioMessage, JButton goMainPageBtn) {
    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(getHeading("LIST OF PORTFOLIOS"));

    for (int i = 0; i < portfolioList.size(); i++) {
      pane.add(new JLabel(String.format("%d. %s", i + 1, portfolioList.get(i))));
    }

    JPanel row = new JPanel();
    row.add(new JLabel("Select a portfolio: "));
    row.add(selectedPortfolio);

    pane.add(row);

    pane.add(getHeading(invalidSelectedPortfolioMessage.getText()));

    pane.add(getButton(goMainPageBtn));

    pack();
    setVisible(true);
  }

  /**
   * Method for getting user entered stock details.
   *
   * @param stockNumber current stock number for user is entering data.
   */
  protected void enterStocksDetails(int stockNumber) {
    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    this.stockNumber = stockNumber;

    pane.add(getHeading(String.format("Enter details for stock-%d", stockNumber)));

    boolean isCompanySymbolValid = stockDetails(pane, companySymbol, invalidCompanySymbolMessage,
            enterCompanySymbol);

    if (isCompanySymbolValid) {
      boolean isDateValid = stockDetails(pane, date, invalidDateMessage, enterDate);

      if (isDateValid) {
        pane.add((new JPanel()).add(companyName));
        pane.add((new JPanel()).add(price));

        boolean isQntyValid = stockDetails(pane, qnty, invalidQntyMessage, enterQnty);

        if (isQntyValid) {
          boolean isFeesValid = stockDetails(pane, fees, invalidFeesMessage, enterFees);
          if (isFeesValid) {
            pane.add(buy);
          }
        }
      }
    }

    pack();
    setVisible(true);
  }

  /**
   * Method for linking feature methods defined in controller
   * to view component.
   *
   * @param features list of feature methods.
   */
  protected void linkFeatures(Features features) {
    goMainPage.addActionListener(event -> {
      resetFrame();
      resetComponents();
      numStocks.setText("");
      this.setVisible(false);
      features.goMainPage();
    });

    numStocks.addActionListener(event -> {
      resetFrame();

      String temp = numStocks.getText();

      if (!features.checkQnty(temp)) {
        resetComponents();
        numStocks.setText("");
        invalidNumStocksMessage.setText("Enter a valid number");
        enterStocksNum();
      } else {
        enterStocksDetails(this.stockNumber);
      }
    });

    companySymbol.addActionListener(event -> {
      if (!features.checkCompanySymbol(companySymbol.getText())) {
        companySymbol.setText("");
        invalidCompanySymbolMessage.setText("Entered company symbol is invalid");
      } else {
        enterCompanySymbol.setText(String.format("Company symbol: %s", companySymbol.getText()));
        invalidCompanySymbolMessage.setText("");
      }

      resetFrame();
      enterStocksDetails(this.stockNumber);
    });

    date.addActionListener(event -> {
      if (!features.checkDate(date.getText())) {
        date.setText("");
        invalidDateMessage.setText("Entered date is invalid.");
      } else {
        enterDate.setText(String.format("Date: %s", date.getText()));
        invalidDateMessage.setText("");
        companyName.setText(
                companyName.getText() + features.getCompanyName(companySymbol.getText(),
                        date.getText()));
        price.setText(String.format("%s%.2f", price.getText(), features.getPrice()));
      }

      resetFrame();
      enterStocksDetails(this.stockNumber);
    });

    fees.addActionListener(event -> {
      if (!features.checkFees(fees.getText())) {
        fees.setText("");
        invalidFeesMessage.setText("Commission fees must be between 1 and 50.");
      } else {
        enterFees.setText(String.format("Commission fees: %s%%", fees.getText()));
        invalidFeesMessage.setText("");
      }

      resetFrame();
      enterStocksDetails(this.stockNumber);
    });
  }
}
