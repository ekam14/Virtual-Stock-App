package view;

import java.awt.HeadlessException;
import java.awt.Container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;

import controller.Features;

import static java.lang.Integer.parseInt;

/**
 * This class represents the investment strategy operation frame and its related operations.
 */
public class InvestmentStrategyImpl extends AbstractPortfolioHelper implements InvestmentStrategy {
  private String portfolioName;
  private List<String> companySymbols;
  private final JTextField selectStrategy;
  private final JLabel invalidSelectStrategy;
  private final JLabel invalidSelectedPortfolioMessage;
  private final JTextField selectedPortfolio;
  private final JTextField amount;
  private final JLabel invalidAmountMessage;
  private final JTextField companySymbol;
  private final JLabel invalidCompanySymbolMessage;
  private final JTextField commissionFee;
  private final JLabel invalidFeesMessage;
  private final JTextField weights;
  private final JLabel invalidWeightMessage;
  private final JTextField dateRange;
  private final JLabel invalidDateRangeMessage;
  private final JTextField freq;
  private final JLabel invalidFreqMessage;
  private final JTextField fileType;
  private final JLabel invalidFileTypeMessage;
  private final JButton submit;
  private final HashMap<JLabel, String> invalidComponents;
  private final JButton goMainPageBtn;

  /**
   * Constructs an investment strategy operation object in view component
   * and initializes frame values.
   *
   * @param caption Title of the frame.
   */
  public InvestmentStrategyImpl(String caption) throws HeadlessException {
    super(caption);
    setSize(1000, 1000);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    portfolioName = "";

    companySymbols = new ArrayList<>();

    selectStrategy = new JTextField(20);
    invalidSelectStrategy = new JLabel();

    invalidSelectedPortfolioMessage = new JLabel();
    selectedPortfolio = new JTextField(20);

    amount = new JTextField();
    invalidAmountMessage = new JLabel();

    companySymbol = new JTextField();
    invalidCompanySymbolMessage = new JLabel();

    commissionFee = new JTextField();
    invalidFeesMessage = new JLabel();

    weights = new JTextField();
    invalidWeightMessage = new JLabel();

    dateRange = new JTextField();
    invalidDateRangeMessage = new JLabel();

    freq = new JTextField();
    invalidFreqMessage = new JLabel();

    fileType = new JTextField();
    invalidFileTypeMessage = new JLabel();

    submit = new JButton("Apply strategy");

    invalidComponents = new HashMap<>();
    invalidComponents.put(invalidAmountMessage, "invalidAmountMessage");
    invalidComponents.put(invalidCompanySymbolMessage, "invalidCompanySymbolMessage");
    invalidComponents.put(invalidFeesMessage, "invalidFeesMessage");
    invalidComponents.put(invalidWeightMessage, "invalidWeightMessage");
    invalidComponents.put(invalidDateRangeMessage, "invalidDateRangeMessage");
    invalidComponents.put(invalidFreqMessage, "invalidFreqMessage");
    invalidComponents.put(invalidFileTypeMessage, "invalidFileTypeMessage");

    goMainPageBtn = new JButton("Go to main page");
  }

  /**
   * Helper method to reset class component values.
   */
  private void initComponents() {
    for (JLabel component : invalidComponents.keySet()) {
      component.setText("");
    }

    portfolioName = "";
    amount.setText("");
    companySymbol.setText("");
    commissionFee.setText("");
    weights.setText("");
    dateRange.setText("");
    freq.setText("");
    fileType.setText("");
    selectStrategy.setText("");
    invalidSelectStrategy.setText("");
    selectedPortfolio.setText("");
    invalidSelectedPortfolioMessage.setText("");
  }

  @Override
  public void showStrategies(String[] strategies) {
    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    for (int i = 0; i < strategies.length; i++) {
      String strategy = strategies[i];
      pane.add(new JLabel(String.format("%d.%s", i + 1, strategy)));
    }

    JPanel row = new JPanel();

    row.add(new JLabel("Select a strategy: "));
    row.add(selectStrategy);
    pane.add(row);

    pane.add(getHeading(invalidSelectStrategy.getText()));

    pane.add(goMainPageBtn);

    pack();
    setVisible(true);
  }

  @Override
  public void strategiesPortfolio(List<String> portfolioList) {
    showPortfolioList(portfolioList, selectedPortfolio, invalidSelectedPortfolioMessage,
            goMainPageBtn);
  }

  @Override
  public void loadInvestmentStrategy(String portfolioName, List<String> companySymbols) {
    this.portfolioName = portfolioName;
    this.companySymbols = companySymbols;

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(new JLabel("Enter Amount to invest:"));
    pane.add(amount);
    pane.add(getHeading(invalidAmountMessage.getText()));

    if (companySymbols.size() > 0) {
      pane.add(new JLabel("Company symbols separated by colon(:) in portfolio:"));
      StringBuilder symbols = new StringBuilder();

      for (int i = 0; i < companySymbols.size(); i++) {
        symbols.append(companySymbols.get(i));
        if (i < companySymbols.size() - 1) {
          symbols.append(":");
        }
      }

      companySymbol.setText(symbols.toString());
      pane.add(new JLabel(symbols.toString()));
    } else {
      pane.add(new JLabel("Enter company symbols separated by colon(:) to invest:"));
      pane.add(companySymbol);
      pane.add(getHeading(invalidCompanySymbolMessage.getText()));
    }

    pane.add(new JLabel(
            "Enter weights(%) separated by colon(:) in same order of above company symbol:"));
    pane.add(weights);
    pane.add(getHeading(invalidWeightMessage.getText()));

    pane.add(new JLabel("Enter commissionFee(%):"));
    pane.add(commissionFee);
    pane.add(getHeading(invalidFeesMessage.getText()));

    if (!Objects.equals(portfolioName, "")) {
      pane.add(new JLabel("Format of date range: YYYY-MM-DD#"));
    } else {
      pane.add(new JLabel("Enter data range separated by # => "
              + "(YYYY-MM-DD)#(YYYY-MM-DD) format:"));
    }

    pane.add(dateRange);
    pane.add(getHeading(invalidDateRangeMessage.getText()));

    if (Objects.equals(portfolioName, "")) {
      pane.add(new JLabel("Enter frequency for this strategy(Yearly/Monthly/Daily):"));
      pane.add(freq);
      pane.add(getHeading(invalidFreqMessage.getText()));

      pane.add(new JLabel("Enter file Type(Flexible/Inflexible):"));
      pane.add(fileType);
      pane.add(getHeading(invalidFileTypeMessage.getText()));
    }

    pane.add(submit);
    pane.add(goMainPageBtn);

    pack();
    setVisible(true);
  }

  @Override
  public void showMessage(HashMap<String, String> errors) {
    resetFrame();

    boolean ok = true;

    for (JLabel component : invalidComponents.keySet()) {
      String componentMessage = invalidComponents.get(component);

      component.setText(errors.getOrDefault(componentMessage, ""));

      if (!Objects.equals(errors.get(componentMessage), "")
              && errors.containsKey(componentMessage)) {
        ok = false;
      }
    }

    if (ok) {
      initComponents();
      goMainPageBtn.doClick();
      return;
    }

    loadInvestmentStrategy(this.portfolioName, this.companySymbols);
  }

  @Override
  public void addFeatures(Features features) {
    submit.addActionListener(event -> {
      try {
        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount.getText());
        data.put("companySymbol", companySymbol.getText());
        data.put("weights", weights.getText());
        data.put("commissionFee", commissionFee.getText());
        data.put("dateRange", dateRange.getText());
        data.put("fileType", fileType.getText());
        data.put("StrategyFrequency", freq.getText());
        data.put("portfolioName", this.portfolioName);
        features.applyInvestmentStrategy(data);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    selectStrategy.addActionListener(event -> {
      String temp = selectStrategy.getText();

      if (!features.checkQnty(temp) || parseInt(temp) < 1 || parseInt(temp) > 2) {
        invalidSelectStrategy.setText("Please enter a valid operation");
        selectStrategy.setText("");
        try {
          resetFrame();
          features.showStrategies();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        return;
      }

      resetFrame();
      initComponents();

      if (Objects.equals(temp, "1")) {
        try {
          features.strategiesPortfolio();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        loadInvestmentStrategy("", new ArrayList<>());
      }
    });

    selectedPortfolio.addActionListener(event -> {
      try {
        resetFrame();
        if (!features.strategySelectedPortfolio(selectedPortfolio.getText())) {
          selectedPortfolio.setText("");
          invalidSelectedPortfolioMessage.setText("Select a valid portfolio.");
          features.strategiesPortfolio();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    goMainPageBtn.addActionListener(event -> {
      resetFrame();
      initComponents();
      this.setVisible(false);
      features.goMainPage();
    });
  }
}
