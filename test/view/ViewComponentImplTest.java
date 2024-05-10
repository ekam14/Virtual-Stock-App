package view;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.APIRequestsImpl;
import model.CompanyStock;
import model.CompanyStockImpl;
import model.dto.Pair;
import model.dto.PortfolioPerformanceDTO;
import model.ModelComponentImpl;
import model.PortfolioRepositoryImpl;
import model.enums.TransactionType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * A JUnit test class for the ViewComponentImpl class.
 */
public class ViewComponentImplTest {

  private ViewComponent viewComponent;
  private StringBuffer out;

  @Before
  public void setup() {
    out = new StringBuffer();
    viewComponent = new ViewComponentImpl(out);
  }

  @Test
  public void testObjectSuccess() throws IOException {
    assertNotNull(viewComponent);
  }

  @Test
  public void testSetUpPage() throws IOException {
    String[] setupOptions = {"1. Create portfolio.", "2. Show portfolios.", "3. Import portfolio.",
        "4. Modify portfolios.", "Enter q or quit to quit the application."};

    viewComponent.setUp(setupOptions);
    assertEquals("*********************** MAIN PAGE *********************\n"
            + "1. Create portfolio.\n2. Show portfolios.\n3. Import portfolio.\n"
            + "4. Modify portfolios.\nEnter q or quit to quit the application.\n",
        out.toString());
  }

  @Test
  public void testCreatePortfolio() throws IOException {
    viewComponent.createPortfolio("", TransactionType.BUY);
    assertEquals("*********************** NEW PORTFOLIO ***********************\n", out.toString());
  }

  @Test
  public void testShowPortfolio() throws IOException {
    List<String> dummyPortfolios = new ArrayList<String>();
    dummyPortfolios.add("Portfolio_2022-10-31_22/37/34");
    dummyPortfolios.add("Portfolio_2022-10-31_22/17/14");
    dummyPortfolios.add("Portfolio_2022-10-29_22/37/54");
    dummyPortfolios.add("Portfolio_2022-10-29_22/37/34");
    dummyPortfolios.add("Portfolio_2022-10-29_02/09/00");

    viewComponent.showPortfolios(dummyPortfolios);

    String expectedOutput = "*********************** PORTFOLIOS ***********************\n"
        + "1. Portfolio_2022-10-31_22/37/34\n2. Portfolio_2022-10-31_22/17/14\n"
        + "3. Portfolio_2022-10-29_22/37/54\n4. Portfolio_2022-10-29_22/37/34\n"
        + "5. Portfolio_2022-10-29_02/09/00\nType M to go to main page.\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowNoPortfoliosMessage() throws IOException {
    viewComponent.showNoPortfoliosMessage();
    assertEquals("There no portfolios to show!\n", out.toString());
  }

  @Test
  public void testSelectShowPortfolioOperation() throws IOException {
    String[] setupOptions = {"1. Create portfolio.", "2. Show portfolios.", "3. Import portfolio.",
        "4. Modify portfolios.", "Enter q or quit to quit the application."};

    viewComponent.selectShowPortfolioOperation(setupOptions, "Ekam");
    String expectedOutput = "Selected portfolio: Ekam.\n\n1. Create portfolio.\n"
        + "2. Show portfolios.\n3. Import portfolio.\n4. Modify portfolios.\n"
        + "Enter q or quit to quit the application.\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowPortfolioComposition() throws IOException {
    HashMap<String, CompanyStock> dummy = new HashMap<>();

    CompanyStock dummyStock1 = new CompanyStockImpl("", "Dummy Company",
        "DC", 1997.0, 14.0, TransactionType.BUY,
        27958.0);
    dummy.put("DC", dummyStock1);

    CompanyStock dummyStock2 = new CompanyStockImpl("", "Dummy Company",
        "DC", 1997.0, 14.0, TransactionType.BUY,
        27958.0);
    dummy.put("DC", dummyStock2);
    viewComponent.showPortfolioComposition(dummy);

    String expectedOutput = "----------------------------------------------------------------------"
        + "-------------------------\n\n    CompanyName  |  CompanySymbol  |       Quantity  |"
        + "     "
        + "Unit Price  |    Total Value  |Net Commission Fees\n\n-----------------------------"
        + "------------------------------------------------------------------\n  Dummy Company"
        + "  |   "
        + "          DC  |           1997  |          14.00  |       27958.00  |    "
        + "   27958.00\n\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowPortfolioIndividualValues() throws IOException {
    HashMap<String, CompanyStock> dummy = new HashMap<>();

    CompanyStock dummyStock1 = new CompanyStockImpl("", "Dummy Company",
        "DC1", 97.0, 14.9, TransactionType.BUY,
        1445.3);
    CompanyStock dummyStock2 = new CompanyStockImpl("", "Dummy Company",
        "DC2", 107.0, 10.0, TransactionType.BUY,
        1070.0);

    dummy.put("DC1", dummyStock1);
    dummy.put("DC2", dummyStock2);

    double totalValue = viewComponent.showPortfolioComposition(dummy);

    String expectedOutput = "----------------------------------------------------------------------"
        + "-------------------------\n\n    CompanyName  |  CompanySymbol  |       Quantity  | "
        + "    Unit Price  |    Total Value  |Net Commission Fees\n\n--------------------------"
        + "---------------------------------------------------------------------\n  Dummy "
        + "Company  |            DC2  |            107  |          10.00  |        1070.00  |  "
        + "      1070.00\n\n  Dummy Company  |            DC1  |             97  |          "
        + "14.90  |        1445.30  |        1445.30\n\n";

    assertEquals(expectedOutput, out.toString());
    assertEquals(totalValue, 1445.3 + 1070, 0.00001);
  }

  @Test
  public void testPrintWrongInputMessage() throws IOException {
    viewComponent.printWrongInputMessage();
    assertEquals("Enter a valid option.\n", out.toString());
  }

  @Test
  public void testPrintStopExecutionMessage() throws IOException {
    viewComponent.printStopExecutionMessage();
    assertEquals("***********************************\n"
        + "Stopping program execution.\n"
        + "***********************************\n", out.toString());
  }

  @Test
  public void testEnterNumberOfStocksToEnter() throws IOException {
    viewComponent.enterNumberOfStocksToEnter();
    assertEquals("Enter number of stocks you want to add: ", out.toString());
  }

  @Test
  public void testShowPortfolioValue() throws IOException {
    HashMap<String, CompanyStock> dummy = new HashMap<>();

    CompanyStock dummyStock = new CompanyStockImpl("", "Dummy Company",
        "DC", 1997.0, 14.0, TransactionType.BUY,
        27958.0);
    dummy.put("DC", dummyStock);

    viewComponent.showPortfolioValue("2022-09-02", dummy);

    String expectedOutput = "----------------------------------------------------------------------"
        + "-------------------------\n\n    CompanyName  |  CompanySymbol  |       Quantity  |"
        + "     Unit Price  |    Total Value  |Net Commission Fees\n\n-------------------------"
        + "----------------------------------------------------------------------\n  Dummy "
        + "Company  |             DC  |           1997  |          14.00  |       27958.00  |  "
        + "     27958.00\n\nValue of the portfolio on 2022-09-02 was $27958.00.\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowAvailableOptionsModifyPage() throws IOException {
    List<String> portfolioList = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();

    String[] modifyPortOptions = {"1. Add a new stock.", "2. Sell a stock."};

    String selectedPortfolio = portfolioList.size() > 0 ? portfolioList.get(0) : "";
    viewComponent.selectModifyPortfolioOperation(modifyPortOptions, selectedPortfolio);

    String expectedOutput = "Selected portfolio: " + selectedPortfolio + ".\n"
        + "\n1. Add a new stock.\n2. Sell a stock.\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testGetEnteredStockDetailsMessage() throws IOException {
    viewComponent.getEnteredStockDetailsMessage(1);

    String expectedOutput = "\n**********************************\n"
        + "Enter details for the stock-1\n**********************************\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowPortfolioPerformance() throws IOException {
    ArrayList<Pair> data = new ArrayList<>();
    data.add(new Pair("2011-11-01", 10.0));
    data.add(new Pair("2011-11-02", 20.0));
    data.add(new Pair("2011-11-03", 30.0));
    data.add(new Pair("2011-11-04", 40.0));
    data.add(new Pair("2011-11-07", 60.0));
    data.add(new Pair("2011-11-08", 60.0));
    data.add(new Pair("2011-11-09", 60.0));
    data.add(new Pair("2011-11-10", 60.0));
    data.add(new Pair("2011-11-11", 60.0));
    Double scaleFactor = 3.0;
    String portfolioName = "Portfolio_2022-11-16_18:14:28_Flexible";
    PortfolioPerformanceDTO dto = new PortfolioPerformanceDTO(data, scaleFactor, portfolioName);
    String dateRange = "2012-10-10#2022-10-10";
    viewComponent.showPortfolioPerformance(dateRange, dto);
    String expectedOutput = "Performance of portfolio Portfolio_2022-11-16_18:"
        + "14:28_Flexible from 2012-10-10 to 2022-10-10\n"
        + "\n"
        + "2011-11-01: **********\n"
        + "2011-11-02: ********************\n"
        + "2011-11-03: ******************************\n"
        + "2011-11-04: ****************************************\n"
        + "2011-11-07: ************************************************************\n"
        + "2011-11-08: ************************************************************\n"
        + "2011-11-09: ************************************************************\n"
        + "2011-11-10: ************************************************************\n"
        + "2011-11-11: ************************************************************\n"
        + "\n"
        + "Scale: * = $3.0\n";

    assertEquals(expectedOutput, out.toString());

  }

  @Test
  public void testSelectShowPortfolioOperationForViewPortfolio() throws IOException {
    String[] showPortOptions = {"1. Composition of the portfolio.", "2. Value of the portfolio.",
        "3. Cost basis of the portfolio.", "4. Show portfolio performance."};

    viewComponent.selectShowPortfolioOperation(showPortOptions, "Portfolio1");
    String expectedOutput = "Selected portfolio: Portfolio1.\n\n"
        + "1. Composition of the portfolio.\n"
        + "2. Value of the portfolio.\n"
        + "3. Cost basis of the portfolio.\n"
        + "4. Show portfolio performance.\n";

    assertEquals(expectedOutput, out.toString());
  }
}