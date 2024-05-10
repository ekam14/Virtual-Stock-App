package view;

import java.awt.Container;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import controller.Features;
import model.CompanyStock;
import model.dto.Pair;
import model.dto.PortfolioPerformanceDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import static java.lang.Integer.parseInt;

/**
 * This class represents the show portfolio operation frame and its related operations.
 */
public class ShowPortfolioImpl extends AbstractPortfolioHelper implements ShowPortfolio {

  JFreeChart chart;
  private final JButton goBackFromSelectPortfolioOp;
  private final JButton goBackFromPortfolioOp;
  private final JLabel invalidSelectedPortfolioMessage;
  private final JTextField selectedPortfolio;
  private final JLabel invalidSelectedPortfolioOperationMessage;
  private final JTextField selectedPortfolioOperation;
  private final JLabel invalidDateMessage;
  private final JTextField date;
  private final JTextField dateRange;
  private final JButton goMainPageBtn;

  /**
   * Constructs show portfolio operation object in view component and initializes frame values.
   *
   * @param caption Title of the frame.
   */
  public ShowPortfolioImpl(String caption) {
    super(caption);

    goBackFromSelectPortfolioOp = new JButton("Go to last page");

    goBackFromPortfolioOp = new JButton("Go to last page");

    invalidSelectedPortfolioMessage = new JLabel();
    selectedPortfolio = new JTextField(20);

    invalidSelectedPortfolioOperationMessage = new JLabel();
    selectedPortfolioOperation = new JTextField(10);

    date = new JTextField(10);
    dateRange = new JTextField(10);

    invalidDateMessage = new JLabel("");

    goMainPageBtn = new JButton("Go to main page");

    setSize(1000, 1000);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Helper method to validate entered date.
   * @throws IOException exception.
   */
  private void enterDate() throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    JPanel row = new JPanel();

    row.add(new JLabel("Enter a date in (YYYY-MM-DD) format: "));
    row.add(date);

    pane.add(row);

    pane.add(getHeading(invalidDateMessage.getText()));

    pane.add(getButton(goBackFromPortfolioOp));

    pack();
    setVisible(true);
  }

  /**
   * Helper method to validate entered date range.
   * @throws IOException exception.
   */
  private void enterDateRange() throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    JPanel row = new JPanel();

    row.add(new JLabel(
            "Enter startDate and endDate date separated by # => "
                    + "(YYYY-MM-DD)#(YYYY-MM-DD) format: "));
    row.add(dateRange);

    pane.add(row);

    pane.add(getHeading(invalidDateMessage.getText()));

    pane.add(getButton(goBackFromPortfolioOp));

    pack();
    setVisible(true);
  }

  /**
   * Helper method to reset class component values.
   */
  private void initComponents() {
    invalidSelectedPortfolioMessage.setText("");
    selectedPortfolio.setText("");
    invalidSelectedPortfolioOperationMessage.setText("");
    selectedPortfolioOperation.setText("");
    date.setText("");
    dateRange.setText("");
    invalidDateMessage.setText("");
  }

  @Override
  public void showPortfolios(List<String> portfolioList) throws IOException {
    showPortfolioList(portfolioList, selectedPortfolio, invalidSelectedPortfolioMessage,
        goMainPageBtn);
  }

  @Override
  public void showNoPortfoliosMessage() throws IOException {
    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(new JLabel("There no portfolios to show!", SwingConstants.CENTER));

    pane.add(getButton(goMainPageBtn));

    pack();
    setVisible(true);
  }

  @Override
  public void selectShowPortfolioOperation(String[] showPortOptions, String selectedPortfolio)
      throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(getHeading(String.format("Selected portfolio: %s", selectedPortfolio)));

    for (String option : showPortOptions) {
      JPanel textPanel = new JPanel();
      textPanel.add(new JLabel(option, SwingConstants.CENTER));
      pane.add(textPanel);
    }

    JPanel row = new JPanel();

    row.add(new JLabel("Select a portfolio operation: "));
    row.add(selectedPortfolioOperation);

    pane.add(row);

    pane.add(getHeading(invalidSelectedPortfolioOperationMessage.getText()));

    pane.add(getButton(goBackFromSelectPortfolioOp));

    pack();
    setVisible(true);
  }

  @Override
  public double showPortfolioComposition(HashMap<String, CompanyStock> portfolioComposition)
      throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    double totalValue = 0.0;

    if (portfolioComposition == null) {
      pane.add(new JLabel("No portfolio maintained."));
    } else {
      //table;
      pane.add(new JLabel(String.format("%s%n\n",
          "--------------------------------------------"
              + "---------------------------------------------------")));

      pane.add(new JLabel(String.format("%15s%3s%15s%3s%15s%3s%15s%3s%15s%3s%15s%n\n",
          "CompanyName", "|", "CompanySymbol", "|", "Quantity", "|", "Unit Price", "|",
          "Total Value", "|", "Net Commission Fees")));
      pane.add(new JLabel(String.format("%s%n",
          "--------------------------------------------------"
              + "---------------------------------------------")));
      String format = "%15s%3s%15s%3s%15.2f%3s%15.2f%3s%15.2f%3s%15.2f\n";
      for (CompanyStock companyStock : portfolioComposition.values()) {
        pane.add(new JLabel(String.format((format) + "%n",
            companyStock.getCompanyName(), "|", companyStock.getCompanySymbol(),
            "|", companyStock.getQuantity(),
            "|", companyStock.getBoughtPrice(),
            "|", companyStock.getBoughtValue(),
            "|", companyStock.getCommissionFees())));
        totalValue += companyStock.getBoughtValue();
      }
    }

    pane.add(getButton(goBackFromPortfolioOp));

    pack();
    setVisible(true);

    return totalValue;
  }

  @Override
  public void showPortfolioCostBasis(String date, double costBasis) throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(
        getHeading(String.format("Cost basis of the portfolio till %s is $%.2f", date, costBasis)));

    pane.add(getButton(goBackFromPortfolioOp));

    pack();
    setVisible(true);
  }

  private JPanel showBarChart(String dateRange, PortfolioPerformanceDTO dto) {
    resetFrame();

    DefaultCategoryDataset portfolioPerformanceData = new DefaultCategoryDataset();

    String val = "Scale = $" + String.format("%.2f", dto.getScaleFactor());

    for (Pair p : dto.getData()) {
      portfolioPerformanceData.addValue(p.getValue(), val, p.getKey());
    }

    String startDate = dateRange.substring(0, 10);
    String endDate = dateRange.substring(11);

    String title = String.format("Portfolio Performance (%s to %s)", startDate, endDate);

    //Create chart
    JFreeChart chart = ChartFactory.createBarChart(
        title, "Time", "Value of portfolio",
            portfolioPerformanceData, PlotOrientation.VERTICAL,
        true, true, false
    );

    return new ChartPanel(chart);
  }

  @Override
  public void showPortfolioPerformance(String dateRange, PortfolioPerformanceDTO dto)
      throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(showBarChart(dateRange, dto));

    pane.add(getButton(goBackFromPortfolioOp));

    pack();
    setVisible(true);
  }


  @Override
  public void showPortfolioValue(String date, HashMap<String, CompanyStock> portfolio)
      throws IOException {
    resetFrame();

    Container pane = this.getContentPane();
    this.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    double portfolioValue = showPortfolioComposition(portfolio);

    pane.add(getHeading(
        String.format("Value of the portfolio on %s was $%.2f\n", date, portfolioValue)));

    pane.add(getButton(goBackFromPortfolioOp));

    pack();
    setVisible(true);
  }

  @Override
  public void showInputDateFormat() throws IOException {
    // pass;
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

    goBackFromSelectPortfolioOp.addActionListener(event -> {
      initComponents();
      resetFrame();
      this.setVisible(false);
      try {
        features.showPortfolio();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    goBackFromPortfolioOp.addActionListener(event -> {
      selectedPortfolioOperation.setText("");
      date.setText("");
      dateRange.setText("");
      invalidDateMessage.setText("");
      try {
        features.showSelectedPortfolio(selectedPortfolio.getText());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    selectedPortfolio.addActionListener(event -> {
      try {
        if (!features.showSelectedPortfolio(selectedPortfolio.getText())) {
          selectedPortfolio.setText("");
          invalidSelectedPortfolioMessage.setText("Please enter a valid portfolio number.");
          resetFrame();
          features.showPortfolio();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    date.addActionListener(event -> {
      if (!features.checkDate(date.getText())) {
        date.setText("");
        invalidDateMessage.setText("Entered date is invalid.");
        try {
          enterDate();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        try {
          features.performPortfolioOperation(selectedPortfolio.getText(),
              selectedPortfolioOperation.getText(), date.getText());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    dateRange.addActionListener(event -> {
      if (!features.checkDateRange(dateRange.getText())) {
        dateRange.setText("");
        invalidDateMessage.setText("Entered date range is invalid.");
        try {
          enterDateRange();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      } else {
        try {
          features.performPortfolioOperation(selectedPortfolio.getText(), "4",
                  dateRange.getText());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });

    selectedPortfolioOperation.addActionListener(event -> {
      try {
        String portfolioOperation = selectedPortfolioOperation.getText();

        if (!features.checkQnty(portfolioOperation)
            || parseInt(portfolioOperation) < 1
            || parseInt(portfolioOperation) > 4) {
          invalidSelectedPortfolioOperationMessage.setText("Please enter a valid operation");
          selectedPortfolioOperation.setText("");
          features.showSelectedPortfolio(selectedPortfolio.getText());
          return;
        }

        invalidSelectedPortfolioOperationMessage.setText("");

        if (Objects.equals(portfolioOperation, "1")) {
          features.performPortfolioOperation(selectedPortfolio.getText(), "1",
                  "");
        } else if (Objects.equals(portfolioOperation, "4")) {
          enterDateRange();
        } else {
          enterDate();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
