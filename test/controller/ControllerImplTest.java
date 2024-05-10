package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import model.APIRequestsImpl;
import model.CompanyStock;
import model.MockModel;
import model.ModelComponent;
import model.ModelComponentImpl;
import model.PortfolioRepository;
import model.PortfolioRepositoryImpl;
import model.enums.PortfolioType;
import view.MockView;
import view.ViewComponentImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * A JUnit test class for the ControllerImpl class.
 */
public class ControllerImplTest {

  private Controller controller;
  private StringBuffer out;

  @Before
  public void setup() {
    out = new StringBuffer();
  }

  /**
   * Helper method to create and return a Controller object.
   *
   * @param in Readable object.
   * @return new controller object.
   * @throws IOException for error thrown by Readable object.
   */
  private Controller setupController(Readable in) throws IOException {
    return new ControllerImpl(in, out, new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()), new ViewComponentImpl(out));
  }

  /**
   * Helper method for setting up controller component.
   *
   * @param in             Readable object.
   * @param modelComponent model component object.
   * @return new controller object.
   * @throws IOException for error thrown by Readable object.
   */
  private Controller setupController(Readable in, ModelComponent modelComponent)
      throws IOException {
    return new ControllerImpl(in, out, modelComponent, new ViewComponentImpl(out));
  }

  /**
   * Helper method for getting the file name of the portfolio.
   *
   * @param pType Portfolio Type
   * @return filename.
   */
  private String getFilename(PortfolioType pType) {
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String date = dataFormat.format(new Date());
    return "Portfolio_" + date + "_" + pType.getValue();
  }

  /**
   * Helper method for getting string format of the portfolios list.
   *
   * @param list portfolio list.
   * @return string format of the portfolios list.
   */
  private StringBuilder getPortfolioListString(List<String> list) {
    StringBuilder portfolioList =
        new StringBuilder("*********************** PORTFOLIOS ***********************\n");

    for (int i = 0; i < list.size(); i++) {
      portfolioList.append(String.format("%d. %s\n", i + 1, list.get(i)));
    }

    return portfolioList;
  }

  /**
   * Main page contents.
   *
   * @return Main page contents.
   */
  private String mainPageContents() {
    return "*********************** MAIN PAGE *********************\n"
        + "1. Create portfolio.\n2. Show portfolios.\n3. Import portfolio.\n"
        + "4. Modify portfolios.\nEnter q or quit to quit the application.\nEnter option: ";
  }

  /**
   * Quit message contents.
   *
   * @return Quit message contents.
   */
  private String quitMessage() {
    return "***********************************\nStopping program execution.\n"
        + "***********************************\n";
  }

  /**
   * Show portfolio contents.
   *
   * @return Show portfolio contents.
   */
  private String showPortfolioContents() {
    return "1. Composition of the portfolio.\n2. Value of the portfolio.\n3. Cost basis of "
        + "the portfolio.\n4. Show portfolio performance.\nWhat do you want? ";
  }

  /**
   * Modify portfolio contents.
   *
   * @return Modify portfolio contents.
   */
  private String modifyPortfolioContents() {
    return "1. Add a new stock.\n2. Sell a stock.\nWhat do you want? \n";
  }

  @Test
  public void testQuit1() throws IOException {
    Reader in = new StringReader("quit");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testQuit2() throws IOException {
    Reader in = new StringReader("q");
    controller = setupController(in);
    controller.start();
    String expectedOutput = mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testObjectSuccess() throws IOException {
    controller = setupController(new StringReader(""));
    assertNotNull(controller);
  }

  @Test
  public void testSellStock() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test1.csv";

    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("3\n%s\nFlexible\n4\n%d\n2\nAMZN\n2024-11-11\n"
            + "2022-11-11\n90\nM\n2\n%d\n1\nM\nq",
        path, latestIndex, latestIndex));
    controller = setupController(in);
    controller.start();

    list = (new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl()))
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + list.get(latestIndex - 1) + "\n" + mainPageContents()
        + portfolioList + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n"
        + modifyPortfolioContents() + "**********************************\nEnter details for "
        + "the stock.\n**********************************\nCompany Symbol: Please enter a date"
        + " in (YYYY-MM-DD) format: Entered date is invalid. Try Again.\n"
        + "Please enter a date in (YYYY-MM-DD) format: Company Name: Amazon.com Inc\nPrice:"
        + " 100.79\nTransaction quantity: Selling 90 stocks of Amazon.com Inc for a total of"
        + " $9071.10.\n" + portfolioList + mainPageContents() + portfolioList
        + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n" + showPortfolioContents()
        + "---------------------------------------------------------------------------------"
        + "--------------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n"
        + "----------------------------------------------------------------------------------"
        + "-------------\nAlphabet Inc - Class C  |           GOOG  |             10  |"
        + "          90.50  |         905.00  |          18.10\n\n"
        + " Amazon.com Inc  |           AMZN  |             10  |          96.79  |"
        + "         967.90  |         375.00\n\n" + portfolioList + mainPageContents()
        + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testBuyNowSeeFutureValue() throws IOException {
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("1\nFlexible\n1\nAMZN\n2020101\n2022-09-02\n13.42"
        + "\n1342.0\n-1\n1342\n"
        + "2\n%d\n2\n2022-11-32\n2022-11-14\nM\nquit", latestIndex));
    controller = setupController(in);
    controller.start();

    list = (new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl()))
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "*********************** NEW PORTFOLIO ***********"
        + "************\nEnter portfolio type (Flexible/Inflexible): Enter number of stocks you"
        + " want to add: \n**********************************\nEnter details for the stock-1\n"
        + "**********************************\nCompany Symbol: Please enter a date in "
        + "(YYYY-MM-DD) format: Entered date is invalid. Try Again.\nPlease enter a date in "
        + "(YYYY-MM-DD) format: Company Name: Amazon.com Inc\nPrice: 127.51\n"
        + "Transaction quantity: Given quantity is invalid. Try Again.\nTransaction quantity: "
        + "Given quantity is invalid. Try Again.\nTransaction quantity: Given quantity is "
        + "invalid. Try Again.\nTransaction quantity: Purchasing 1342 stocks of Amazon.com Inc"
        + " for a total of $171118.42.\n\n********************************\nTRANSACTIONS: "
        + "\nAmazon.com Inc, quantity: 1342, BUY\n********************************\n\n"
        + mainPageContents() + portfolioList + "Selected portfolio: "
        + list.get(latestIndex - 1) + ".\n\n" + showPortfolioContents()
        + "Please enter a date in (YYYY-MM-DD) format: Entered date is invalid. Try Again.\n"
        + "Please enter a date in (YYYY-MM-DD) format: ---------------------------------------"
        + "--------------------------------------------------------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n---------------------------------------------"
        + "--------------------------------------------------\n"
        + " Amazon.com Inc  |           AMZN  |           1342  |          98.49  |      "
        + "132173.58  |        3422.37\n\nValue of the portfolio on 2022-11-14 was $132173.58"
        + ".\n" + portfolioList + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testSellMoreThanStock() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test1.csv";

    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("3\n%s\nFlexible\n4\n%d\n2\nAMZN\n2022-11-11\n1.9"
            + "\n900\n90\nM\n2\n%d\n1\nM\nq",
        path, latestIndex, latestIndex));
    controller = setupController(in);
    controller.start();

    list = (new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl()))
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + list.get(latestIndex - 1) + "\n" + mainPageContents()
        + portfolioList + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n"
        + modifyPortfolioContents() + "**********************************\nEnter details for "
        + "the stock.\n**********************************\nCompany Symbol: Please enter a date"
        + " in (YYYY-MM-DD) format: Company Name: Amazon.com Inc\nPrice: 100.79\n"
        + "Transaction quantity: Given quantity is invalid. Try Again.\n"
        + "Transaction quantity: You can sell 100 units as you have this amount of shares "
        + "only!\nTransaction quantity: Selling 90 stocks of Amazon.com Inc for a total of "
        + "$9071.10.\n" + portfolioList + mainPageContents() + portfolioList
        + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n" + showPortfolioContents()
        + "--------------------------------------------------------------------------"
        + "---------------------\n\n    CompanyName  |  CompanySymbol  |       Quantity  |"
        + "     Unit Price  |    Total Value  |Net Commission Fees\n\n----------------------"
        + "-------------------------------------------------------------------------\n"
        + "Alphabet Inc - Class C  |           GOOG  |             10  |          90.50  |"
        + "         905.00  |          18.10\n\n Amazon.com Inc  |           AMZN  |     "
        + "        10  |          96.79  |         967.90  |         375.00\n\n"
        + portfolioList + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCreateAndModifyPortfolio() throws IOException {
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("1\nFlex ible\nFlexible\n2\nAMZN\n2022-11-14\n"
            + "10\nGOOG\n2022-11-14\n10\n4\n%d\n1\nAMZN\n2022-11-14\n10\n%d\n2\nGOOG"
            + "\n2022-14-11\n2022-11-14\n0.9\nj gasgugs\n13\n9\nM\n2\n%d\n1\nM\nq",
        latestIndex, latestIndex, latestIndex));
    controller = setupController(in);
    controller.start();

    out = new StringBuffer();
    in = new StringReader(String.format("2\n%d\n1\nM\nq\n", latestIndex));
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl())
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + portfolioList
        + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n" + showPortfolioContents()
        + "------------------------------------------------------------------------------------"
        + "-----------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n"
        + "-----------------------------------------------------------------------------------"
        + "------------\n"
        + "Alphabet Inc - Class C  |           GOOG  |              1  |          96.03  |"
        + "          96.03  |          36.49\n\n"
        + " Amazon.com Inc  |           AMZN  |             20  |          98.49  |        "
        + "1969.80  |          39.40\n\n" + portfolioList + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testBuyStockFeatureInvalidCompany() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test1.csv";
    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("3\n%s\n%s\n4\n%d\n1\nAMZNa\nAMZN\n2022-10-19\n "
            + "q w\n101\nM\nq",
        path, pType.getValue(), latestIndex));
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl())
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n"
        + modifyPortfolioContents() + "**********************************\nEnter details "
        + "for the stock.\n**********************************\nCompany Symbol: Given "
        + "Company Symbol is invalid. Try Again.\nCompany Symbol: Please enter a date in "
        + "(YYYY-MM-DD) format: Company Name:"
        + " Amazon.com Inc\nPrice: 115.07\nTransaction quantity: Given quantity is invalid."
        + " Try Again.\nTransaction quantity: Purchasing 101 stocks of Amazon.com"
        + " Inc for a total of $11622.07.\n" + portfolioList + mainPageContents()
        + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testBuyStockInvalidDate() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test1.csv";

    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("3\n%s\nFlexible\n4\n%d\n1\nAMZN\n2024-10-19"
            + "\n2022-10-19\n q w\n0.9\n101\nM\nq",
        path, latestIndex));
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl())
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + list.get(latestIndex - 1) + "\n" + mainPageContents()
        + portfolioList + "Selected portfolio: " + list.get(latestIndex - 1) + ".\n\n"
        + modifyPortfolioContents()
        + "**********************************\nEnter details for the stock.\n"
        + "**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Entered date is invalid."
        + " Try Again.\n"
        + "Please enter a date in (YYYY-MM-DD) format: Company Name: Amazon.com Inc\nPrice: "
        + "115.07\nTransaction quantity: Given quantity is invalid. Try Again.\n"
        + "Transaction quantity: Given quantity is invalid. Try Again.\n"
        + "Transaction quantity: Purchasing 101 stocks of Amazon.com Inc for a total of "
        + "$11622.07.\n" + portfolioList + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testPortfolioCumulativeComposition() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test4.csv";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("3\n%s\n%s\n2\n%d\n1\nM\nq",
        path, PortfolioType.INFLEXIBLE.getValue(), latestIndex));
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl())
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + list.get(latestIndex - 1) + "\n"
        + mainPageContents() + portfolioList + "Selected portfolio: "
        + list.get(latestIndex - 1) + ".\n\n" + showPortfolioContents()
        + "--------------------------------------------------------------------------------"
        + "---------------\n\n    CompanyName  |  CompanySymbol  |       Quantity  |     "
        + "Unit Price  |    Total Value  |Net Commission Fees\n\n"
        + "---------------------------------------------------------------------------------"
        + "--------------\n Amazon.com Inc  |           AMZN  |            490  |         "
        + "102.35  |       50149.10  |        1002.98\n\n" + portfolioList + mainPageContents()
        + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testPortfolioCumulativeCorrectValues() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test4.csv";

    String filename = getFilename(PortfolioType.FLEXIBLE);

    Reader in = new StringReader(String.format("3\n%s\nFlexible\nM\nq", path));
    controller = setupController(in);
    controller.start();

    HashMap<String, CompanyStock> comp = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioComposition(filename);

    for (String key : comp.keySet()) {
      CompanyStock obj = comp.get(key);

      Double quantity = obj.getQuantity();
      String unitPrice = String.format("%.2f", obj.getBoughtPrice());
      String boughtValue = String.format("%.2f", obj.getBoughtValue());

      Double val = 490.0;
      assertEquals(quantity, val);
      assertEquals(unitPrice, "102.35");
      assertEquals(boughtValue, "50149.10");
    }
  }

  @Test
  public void testShowModifyPortfolio() throws IOException {
    Reader in = new StringReader("q");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCostBasisBuy() throws IOException {
    ModelComponent modelComponent = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl());

    int latestIndex = modelComponent.getPortfolioList().size() + 1;
    Reader in = new StringReader(String.format("1\nFlexible\n1\nAMZN\n2022-11-13\n100\n4\n%d\n1"
            + "\nAMZN\n2022-11-10\n100\n%d\n1\nAMZN\n2022-11-08\n50\nM\nq", latestIndex,
        latestIndex));
    controller = setupController(in, modelComponent);
    controller.start();

    String selectedPortfolio = (new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl())).getPortfolioList().get(latestIndex - 1);

    double c1 = modelComponent.getCostBasis("2022-11-13", selectedPortfolio);
    double c2 = modelComponent.getCostBasis("2022-11-10", selectedPortfolio);
    double c3 = modelComponent.getCostBasis("2022-11-08", selectedPortfolio);
    double c4 = modelComponent.getCostBasis("2020-11-06", selectedPortfolio);

    assertEquals(String.format("%.2f", c1), "24725.82");
    assertEquals(String.format("%.2f", c2), "14445.24");
    assertEquals(String.format("%.2f", c3), "4588.98");
    assertEquals(String.format("%.2f", c4), "0.00");
  }

  @Test
  public void testCostBasisBuyAndSell() throws IOException {
    ModelComponent modelComponent = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl());

    int latestIndex = modelComponent.getPortfolioList().size() + 1;
    Reader in = new StringReader(String.format("1\nFlexible\n1\nAMZN\n2022-19-19\n2022-11-13\n\n"
        + "100\n4\n%d\n2\nAMZN\n"
        + "2022-11-13\n100\n%d\n1\nAMZN\n2022-11-08\n50\nM\nq", latestIndex, latestIndex));
    controller = setupController(in, modelComponent);
    controller.start();

    String selectedPortfolio = (new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl())).getPortfolioList().get(latestIndex - 1);

    double c1 = modelComponent.getCostBasis("2022-11-13", selectedPortfolio);
    double c2 = modelComponent.getCostBasis("2022-11-10", selectedPortfolio);
    double c3 = modelComponent.getCostBasis("2022-11-08", selectedPortfolio);
    double c4 = modelComponent.getCostBasis("2020-11-06", selectedPortfolio);

    assertEquals(String.format("%.2f", c1), "15071.14");
    assertEquals(String.format("%.2f", c2), "4588.98");
    assertEquals(String.format("%.2f", c3), "4588.98");
    assertEquals(String.format("%.2f", c4), "0.00");
  }

  @Test
  public void testPortfolioCompositionPortfolioQuantity0() throws IOException {
    ModelComponent modelComponent = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl());

    int latestIndex = modelComponent.getPortfolioList().size() + 1;

    Reader in = new StringReader(String.format("1\nFlexible\n1\nAMZN\n2022-19-18\n2022-11-13\n10"
        + "\n4\n%d\n2\nAMZN\n2029-10-01\n2022-11-14\n10\nM\nq", latestIndex));
    controller = setupController(in, modelComponent);
    controller.start();

    String selectedPortfolio = (new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl())).getPortfolioList().get(latestIndex - 1);

    HashMap<String, CompanyStock> c = modelComponent.getPortfolioComposition(selectedPortfolio);

    assertEquals(0, c.size());
    assertEquals(String.format("%.2f", modelComponent.getCostBasis("2022-11-14",
        selectedPortfolio)), "1047.76");
  }

  @Test
  public void testPortfolioTypes() throws IOException {
    Reader in = new StringReader("1\nFlexiv\nInflexi\nFLEXIBLE\nINFLEXIBLE\nInflexible"
        + "\n1\nAMZN\n1\nq");
    controller = setupController(in);

    String filename = getFilename(PortfolioType.INFLEXIBLE);

    PortfolioRepository repo = new PortfolioRepositoryImpl();
    List<String> portfolioList = repo.getListOfPortfolio();

    boolean filePresent = false;

    for (String portfolio : portfolioList) {
      filePresent |= Objects.equals(portfolio, filename);
    }

    assertTrue(filePresent);
  }

  @Test
  public void testCreatePortfolio1Stock() throws IOException {
    Reader in = new StringReader("1\nInflexible\n1\nAMZN\n2022-09-09\n33\nq\n");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "*********************** NEW PORTFOLIO ********"
        + "***************\nEnter portfolio type (Flexible/Inflexible): Enter number of "
        + "stocks you want to add: \n**********************************\n"
        + "Enter details for the stock-1\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 133.27\nTransaction quantity: Purchasing 33 stocks "
        + "of Amazon.com Inc for a total of $4397.91.\n\n********************************\n"
        + "TRANSACTIONS: \nAmazon.com Inc, quantity: 33, BUY\n***************************"
        + "*****\n\n" + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCreatePortfolio2Stock() throws IOException {
    Reader in = new StringReader("1\nInflexible\n2\nAMZN\n2022-09-09\n33\nORCL\n2022-09-09"
        + "\n30\nq\n");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "*********************** NEW PORTFOLIO **********"
        + "*************\n"
        + "Enter portfolio type (Flexible/Inflexible): Enter number of stocks you want to add:"
        + " \n**********************************\nEnter details for the stock-1\n"
        + "**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: Amazon.com"
        + " Inc\nPrice: 133.27\n"
        + "Transaction quantity: Purchasing 33 stocks of Amazon.com Inc for a total of $4397.91"
        + ".\n\n**********************************\nEnter details for the stock-2\n"
        + "**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: Oracle "
        + "Corp\nPrice: 75.91\n"
        + "Transaction quantity: Purchasing 30 stocks of Oracle Corp for a total of $2277.30"
        + ".\n\n********************************\nTRANSACTIONS: \nAmazon.com Inc, quantity:"
        + " 33, BUY\nOracle Corp, quantity: 30, BUY\n********************************\n\n"
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCreatePortfolio2SameStock() throws IOException {
    Reader in = new StringReader("1\nInflexible\n2\nAMZN\n2022-11-14\n33\n-1\n51\n12\nAMZN"
        + "\n2022-09-02\n20\n16.7\nq");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "*********************** NEW PORTFOLIO *******"
        + "****************\n"
        + "Enter portfolio type (Flexible/Inflexible): Enter number of stocks you want to "
        + "add: \n"
        + "**********************************\nEnter details for the stock-1\n**************"
        + "********************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 98.49\n"
        + "Transaction quantity: Commission Fees % (Must be in range 1-50): Given "
        + "commission fees % is invalid (Must be between 1-50). Try Again.\n"
        + "Commission Fees % (Must be in range 1-50): Given commission fees % is invalid "
        + "(Must be between 1-50). Try Again.\n"
        + "Commission Fees % (Must be in range 1-50): Purchasing 33 stocks of Amazon.com "
        + "Inc for a total of $3250.17 at 12.00% commission.\n\n***************************"
        + "*******\nEnter details for the stock-2\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 127.51\nTransaction quantity: Commission Fees % "
        + "(Must be in range 1-50): Purchasing 20 stocks of Amazon.com Inc for a total of "
        + "$2550.20 at 16.70% commission.\n\n********************************\nTRANSACTIONS:"
        + " \nAmazon.com Inc, quantity: 33, BUY\n"
        + "Amazon.com Inc, quantity: 20, BUY\n********************************\n\n"
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCreatePortfolio3Stock() throws IOException {
    Reader in = new StringReader("1\nInflexible\n3\nAMZN\n2022-09-09\n33\n16\nORCL\n2022-09-09"
        + "\n167\n33\nGOOG\n2022-03-09\n99\n19\nq");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "*********************** NEW PORTFOLIO ********"
        + "***************\nEnter portfolio type (Flexible/Inflexible): Enter number of "
        + "stocks you want to add: \n**********************************\n"
        + "Enter details for the stock-1\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 133.27\nTransaction quantity: Commission Fees % "
        + "(Must be in range 1-50): Purchasing 33 stocks of Amazon.com Inc for a total of "
        + "$4397.91 at 16.00% commission.\n\n**********************************\n"
        + "Enter details for the stock-2\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Oracle Corp\nPrice: 75.91\n"
        + "Transaction quantity: Commission Fees % (Must be in range 1-50): Purchasing 167 "
        + "stocks of Oracle Corp for a total of $12676.97 at 33.00% commission.\n\n*******"
        + "***************************\nEnter details for the stock-3\n"
        + "**********************************\nCompany Symbol: Please enter a date in "
        + "(YYYY-MM-DD) format: Company Name:"
        + " Alphabet Inc - Class C\nPrice: 2677.32\nTransaction quantity: Commission Fees % "
        + "(Must be in range 1-50): Purchasing 99 stocks of Alphabet Inc - Class C for a "
        + "total of $265054.68 at 19.00% commission.\n\n"
        + "********************************\nTRANSACTIONS: \nAmazon.com Inc, quantity: 33, "
        + "BUY\nOracle Corp, quantity: 167, BUY\n"
        + "Alphabet Inc - Class C, quantity: 99, BUY\n********************************\n\n"
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCreate2Portfolios() throws IOException {
    Reader in = new StringReader("1\nInflexible\n1\nAMZN\n2022-11-11\n100\n1\n1\nInflexible"
        + "\n2\nORCL\n2022-11-11\n-108\n100\n19\nGOOGS\nGOOG\n2022-09-02\n1000\n10\nq");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "*********************** NEW PORTFOLIO *******"
        + "****************\nEnter portfolio type (Flexible/Inflexible): Enter number of "
        + "stocks you want to add: \n**********************************\nEnter details for"
        + " the stock-1\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 100.79\nTransaction quantity: Commission Fees % (Must "
        + "be in range 1-50): Purchasing 100 stocks of Amazon.com Inc for a "
        + "total of $10079.00 at 1.00% commission.\n\n********************************\n"
        + "TRANSACTIONS: \nAmazon.com Inc, quantity: 100, BUY\n**************************"
        + "******\n\n" + mainPageContents() + "*********************** NEW PORTFOLIO ********"
        + "***************\nEnter portfolio type (Flexible/Inflexible): "
        + "Enter number of stocks you want to add: \n**********************************\n"
        + "Enter details for the stock-1\n**********************************\nCompany Symbol"
        + ": Please enter a date in (YYYY-MM-DD) format: Company Name: Oracle Corp\n"
        + "Price: 77.74\nTransaction quantity: Given quantity is invalid. Try Again.\nTrans"
        + "action quantity: Commission Fees % (Must "
        + "be in range 1-50): Purchasing 100 stocks of Oracle Corp for a total of $7774.00"
        + " at 19.00% commission.\n**********************************\nEnter details for"
        + " the stock-2\n**********************************\nCompany Symbol: Given Company"
        + " Symbol is invalid. Try Again.\nCompany Symbol: Please enter a date in (YYYY-MM-DD)"
        + " format: Company Name: Alphabet Inc - Class C\nPrice: 108.68\nTransaction "
        + "quantity: Commission Fees % (Must be in range 1-50): "
        + "Purchasing 1000 stocks of Alphabet Inc - Class C for a total of $108680.00"
        + " at 10.00% commission.\n\n*********************"
        + "***********\nTRANSACTIONS: \nOracle Corp, quantity: 100, BUY\nAlphabet Inc - "
        + "Class C, quantity: 1000, BUY\n********************************\n\n"
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testInvalidOptionsOnMainPage() throws IOException {
    Reader in = new StringReader("seqq\n12\n23\nsqdqw\n12sd\nd1d21e\ndw1\nq\n");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "Enter a valid option.\n"
        + "Enter option: Enter a valid option.\nEnter option: Enter a valid option.\n"
        + "Enter option: Enter a valid option.\nEnter option: Enter a valid option.\n"
        + "Enter option: Enter a valid option.\nEnter option: Enter a valid option.\n"
        + "Enter option: " + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testMockCheckDate() throws IOException {
    Reader in = new StringReader("1\nFlexible\n1\nAMZN\n2022-09-02\n1\n1\nq");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 997),
        new ViewComponentImpl(out));
    controller.start();

    String expectedOutput = "Number value is: 1\nNumber value is: 1\nDate is: 2022-09-02\n"
        + "Number value is: 1\nQuantity is: 1";

    assertEquals(expectedOutput, log.toString());
  }

  @Test
  public void testMockGetPortfolioValue() throws IOException {
    Reader in = new StringReader("2\n1\n2\n2022-09-02\nM\nq\n");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 997),
        new ViewComponentImpl(out));
    controller.start();

    String expectedValue = "Number value is: 2\nPortfolio_2022-10-31_23:12:46\n"
        + "Portfolio_2022-10-30_23:12:46\nPortfolio_2022-10-29_23:12:46\nNumber value is: 1\n"
        + "Number value is: 2\nDate is: 2022-09-02\nDate value: 2022-09-02\nPortfolio name: "
        + "Portfolio_2022-10-31_23:12:46\nPortfolio_2022-10-31_23:12:46\nPortfolio_2022-10"
        + "-30_23:12:46\nPortfolio_2022-10-29_23:12:46\n";

    assertEquals(expectedValue, log.toString());
  }

  @Test
  public void testMockGetPortfolioComposition() throws IOException {
    Reader in = new StringReader("2\n1\n1\nM\nq\n");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 997),
        new ViewComponentImpl(out));
    controller.start();

    String expectedValue = "Number value is: 2\nPortfolio_2022-10-31_23:12:46\n"
        + "Portfolio_2022-10-30_23:12:46\n"
        + "Portfolio_2022-10-29_23:12:46\nNumber value is: 1\n"
        + "Number value is: 1\n"
        + "Portfolio name: Portfolio_2022-10-31_23:12:46\n"
        + "Portfolio_2022-10-31_23:12:46\nPortfolio_2022-10-30_23:12:46\n"
        + "Portfolio_2022-10-29_23:12:46\n";

    assertEquals(expectedValue, log.toString());
  }

  @Test
  public void testMockGetPortfolioList() throws IOException {
    Reader in = new StringReader("2\nM\nq\n");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 997),
        new ViewComponentImpl(out));
    controller.start();

    String expectedValue = "Number value is: 2\nPortfolio_2022-10-31_23:12:46\n"
        + "Portfolio_2022-10-30_23:12:46\nPortfolio_2022-10-29_23:12:46\n";

    assertEquals(expectedValue, log.toString());
  }

  @Test
  public void testMockAddStock() throws IOException {
    Reader in = new StringReader("DummyTest\n1996\nq\n");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 997),
        new ViewComponentImpl(out));
    controller.start();

    assertEquals("Number value is: DummyTest\nNumber value is: 1996\n", log.toString());
  }

  @Test
  public void voidMockViewShowPortfolios() throws IOException {
    Reader in = new StringReader("2\nM\nq");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 1997),
        new MockView(log, 997));
    controller.start();

    String expectedOutput = "Number value is: 2\nPortfolio_2022-10-31_23:12:46\n"
        + "Portfolio_2022-10-30_23:12:46\nPortfolio_2022-10-29_23:12:46\nPortfolio_2022-"
        + "10-31_23:12:46\nPortfolio_2022-10-30_23:12:46\nPortfolio_2022-10-29_23:12:46\n";

    assertEquals(expectedOutput, log.toString());
  }

  @Test
  public void voidMockViewSelectShowPortfolioOperation() throws IOException {
    Reader in = new StringReader("2\nM\nq");
    StringBuilder log = new StringBuilder();
    controller = new ControllerImpl(in, out, new MockModel(log, 1997),
        new MockView(log, 997));
    controller.start();

    String expectedOutput = "Number value is: 2\nPortfolio_2022-10-31_23:12:46\nPortfolio_2022-"
        + "10-30_23:12:46\nPortfolio_2022-10-29_23:12:46\nPortfolio_2022-10-31_23:12:46\n"
        + "Portfolio_2022-10-30_23:12:46\nPortfolio_2022-10-29_23:12:46\n";

    assertEquals(expectedOutput, log.toString());
  }

  @Test
  public void testSellStockWithLessQuantity() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";
    path += "Portfolio_test1.csv";

    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    Reader in = new StringReader(String.format("3\n%s\nFlexible\n4\n%d\n2\nAMZN\n2022-11-11\n90\n9"
            + "\nM\n2\n%d\n1\nM\nq",
        path, latestIndex, latestIndex));
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(), new APIRequestsImpl())
        .getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    portfolioList.append("Type M to go to main page.\nEnter portfolio number: ");

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): "
        + "File is successfully imported by name:" + list.get(latestIndex - 1) + "\n"
        + mainPageContents() + portfolioList + "Selected portfolio: "
        + list.get(latestIndex - 1) + ".\n\n" + modifyPortfolioContents()
        + "**********************************\nEnter details for the stock.\n****************"
        + "******************\nCompany Symbol: Please enter a date in (YYYY-MM-DD) format:"
        + " Company Name: Amazon.com Inc\nPrice: 100.79\n"
        + "Transaction quantity: Commission Fees % (Must be in range 1-50): Selling 90 stocks"
        + " of Amazon.com Inc for a total of $9071.10 at 9.00% commission.\n" + portfolioList
        + mainPageContents() + portfolioList + "Selected portfolio: "
        + list.get(latestIndex - 1) + ".\n\n" + showPortfolioContents() + "---------"
        + "-------------------------------------------------------------------------------"
        + "-------\n\n    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price"
        + "  |    Total Value  |Net Commission Fees\n\n--------------------------------"
        + "---------------------------------------------------------------\nAlphabet Inc - "
        + "Class C  |           GOOG  |             10  |          90.50  |         905.00"
        + "  |          18.10\n\n Amazon.com Inc  |           AMZN  |             10  |"
        + "          96.79  |         967.90  |        1009.98\n\n" + portfolioList
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCannotEnterDataInflexiblePortfolio() throws IOException {
    Reader in = new StringReader("q\n");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testImportFile() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/"
        + "Portfolio_test1.csv";

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    Reader in = new StringReader(String.format("3\n%s\n%s\nM\nq\n", path, pType.getValue()));
    controller = new ControllerImpl(in, out, new ModelComponentImpl(out,
        new PortfolioRepositoryImpl(), new APIRequestsImpl()),
        new ViewComponentImpl(out));
    controller.start();

    PortfolioRepository repo = new PortfolioRepositoryImpl();
    List<String> portfolioList = repo.getListOfPortfolio();

    boolean filePresent = false;

    for (String portfolio : portfolioList) {
      filePresent |= Objects.equals(portfolio, filename);
    }

    assertTrue(filePresent); // to check if the portfolio is getting saved.
  }

  @Test
  public void testImportFileInvalidPath() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";

    String filename = getFilename(PortfolioType.FLEXIBLE);

    Reader in = new StringReader(String.format("3\n%s\nFlexible\n%s\nFlexible\nM\nq\n",
        path + "Portfolio_test1", path + "Portfolio_test1.csv"));
    controller = new ControllerImpl(in, out, new ModelComponentImpl(out,
        new PortfolioRepositoryImpl(), new APIRequestsImpl()),
        new ViewComponentImpl(out));
    controller.start();

    PortfolioRepository repo = new PortfolioRepositoryImpl();
    List<String> portfolioList = repo.getListOfPortfolio();

    boolean filePresent = false;

    for (String portfolio : portfolioList) {
      filePresent |= Objects.equals(portfolio, filename);
    }

    assertTrue(filePresent); // to check if the portfolio is getting saved.
  }

  @Test
  public void testImportFileBuyAndSell() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/";

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File contains "
        + "invalid data. Please verify file content.\n" + mainPageContents() + quitMessage();

    String[] files = {"Portfolio_test2.csv", "Portfolio_test5.csv", "Portfolio_test6.csv"};

    for (String file : files) {
      out = new StringBuffer();
      Reader in = new StringReader(String.format("3\n%s\nInflexible\nq", path + file));
      controller = setupController(in);
      controller.start();

      assertEquals(expectedOutput, out.toString());
    }
  }

  @Test
  public void testShowPortfolioPerformanceInValidDateRange() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String filename = "Portfolio_" + dataFormat.format(new Date()) + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    int latestIndex = list.size() + 1;
    portfolioList.append(String.format("%d. %s\n", latestIndex, filename));
    String[] inValidDate = {"2022-10-10#2022-10-1",
        "2022-10-10#2022-11-1",
        "2022-10-1#2022-11-1",
        "2022-10-10#2029-01-01",
        "1996-10-10#2022-10-11",
        "2022-10-10#2018-10-11",
        "2011-10-40#2022-10-10",
        "2011-10-10#2022-10-40",
        "2011-13-10#2022-10-10",
        "2011-10-10#2022-13-10",
        "20113-10-10#2022-13-10",
        "20113-10-10#20201-13-10",
        "2011-10-102020-12-10"};

    String input = String.format("3\n%s\nFlexible\n2\n%d\n4\n%s",
        path, latestIndex, "121");
    for (String date : inValidDate) {
      input = String.format(input + "\n%s", date);
    }
    //  System.out.println(input);
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\nEnter portfolio number: Selected portfolio: " + filename
        + ".\n\n" + showPortfolioContents()
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) "
        + "format:Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + "Entered dateRange is invalid. Try Again!\n"
        + "Enter startDate and endDate date separated by # => (YYYY-MM-DD)#(YYYY-MM-DD) format:"
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowPortfolioPerformanceYearDisplay() throws IOException {

    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String filename = "Portfolio_" + dataFormat.format(new Date()) + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    int latestIndex = list.size() + 1;
    portfolioList.append(String.format("%d. %s\n", latestIndex, filename));

    String input = String.format("3\n%s\nFlexible\n2\n%d\n4\n%s",
        path, latestIndex, "2008-10-10#2022-10-10");
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();
    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\nEnter portfolio number: "
        + "Selected portfolio: " + filename + ".\n\n"
        + "1. Composition of the portfolio.\n"
        + "2. Value of the portfolio.\n"
        + "3. Cost basis of the portfolio.\n"
        + "4. Show portfolio performance.\n"
        + "What do you want? Enter startDate and endDate date separated by # => "
        + "(YYYY-MM-DD)#(YYYY-MM-DD) format:Performance of portfolio "
        + filename + " from 2008-10-10 to 2022-10-10\n\n"
        + "2008-12-31: *\n2009-12-31: **\n2010-12-31: **\n2011-12-30: **\n"
        + "2012-12-31: **\n2013-12-31: **\n2014-12-31: ***\n2015-12-31: ***\n"
        + "2016-12-30: *******\n"
        + "2017-12-29: *********\n2018-12-31: *********\n2019-12-31: **********\n"
        + "2020-12-31: *************\n2021-12-31: ********************\n2022-10-31: ********\n\n"
        + "Scale: * = $2199.19\n" + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: " + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowPortfolioPerformanceMonthDisplay() throws IOException {

    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String filename = "Portfolio_" + dataFormat.format(new Date()) + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    int latestIndex = list.size() + 1;
    portfolioList.append(String.format("%d. %s\n", latestIndex, filename));

    String input = String.format("3\n%s\nFlexible\n2\n%d\n4\n%s",
        path, latestIndex, "2021-10-10#2022-10-10");
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();
    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\nEnter portfolio number: "
        + "Selected portfolio: " + filename + ".\n\n"
        + "1. Composition of the portfolio.\n"
        + "2. Value of the portfolio.\n"
        + "3. Cost basis of the portfolio.\n"
        + "4. Show portfolio performance.\n"
        + "What do you want? Enter startDate and endDate date separated by # => "
        + "(YYYY-MM-DD)#(YYYY-MM-DD) format:Performance of portfolio "
        + filename + " from 2021-10-10 to 2022-10-10\n\n"
        + "2021-10-29: ********************\n"
        + "2021-11-30: ********************\n"
        + "2021-12-31: ********************\n"
        + "2022-01-31: *******************\n"
        + "2022-02-28: *******************\n"
        + "2022-03-31: *******************\n"
        + "2022-04-29: *****************\n"
        + "2022-05-31: ****************\n"
        + "2022-06-30: ***************\n"
        + "2022-07-29: ********\n"
        + "2022-08-31: ********\n"
        + "2022-09-30: *******\n"
        + "2022-10-31: ********\n"
        + "\nScale: * = $2240.58\n" + portfolioList
        + "Type M to go to main page.\n"
        + "Enter portfolio number: " + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testShowPortfolioPerformanceDatesDisplay() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String filename = "Portfolio_" + dataFormat.format(new Date()) + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);
    int latestIndex = list.size() + 1;
    portfolioList.append(String.format("%d. %s\n", latestIndex, filename));

    String input = String.format("3\n%s\nFlexible\n2\n%d\n4\n%s",
        path, latestIndex, "2022-09-10#2022-09-30");
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();
    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\nEnter portfolio number: "
        + "Selected portfolio: " + filename + ".\n\n"
        + showPortfolioContents() + "Enter startDate and endDate date separated by # => "
        + "(YYYY-MM-DD)#(YYYY-MM-DD) format:Performance of portfolio "
        + filename + " from 2022-09-10 to 2022-09-30\n\n"
        + "2022-09-12: ********************\n2022-09-13: ********************\n"
        + "2022-09-14: *******************\n2022-09-15: *******************\n"
        + "2022-09-16: *******************\n2022-09-19: *******************\n"
        + "2022-09-20: *******************\n2022-09-21: ******************\n"
        + "2022-09-22: ******************\n"
        + "2022-09-23: ******************\n2022-09-26: *****************\n"
        + "2022-09-27: *****************\n2022-09-28: ******************\n"
        + "2022-09-29: *****************\n2022-09-30: *****************\n\n"
        + "Scale: * = $834.16\n" + portfolioList
        + "Type M to go to main page.\nEnter portfolio number: " + mainPageContents()
        + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testInflexiblePortfolioDataManipulation() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";
    String filename =
        "Portfolio_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
            + "_Inflexible";

    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;
    StringBuilder portfolioList = getPortfolioListString(list);
    portfolioList.append(String.format("%d. %s\n", latestIndex, filename));
    String input = String.format("3\n%s\nInflexible\n4\n%d",
        path, latestIndex);

    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();
    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\n"
        + "Enter portfolio number: This file contains inflexible portfolio, you cannot edit it. "
        + "Please select another portfolio\n" + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }


  @Test
  public void testBuy() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";

    String filename =
        "Portfolio_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
            + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    String input = String.format("3\n%s\nFlexible\n4\n%d\n1\nAMZN\n2014-11-10\n10\n"
            + latestIndex + "\n1\nORCL\n2014-11-10\n10\n"
            + latestIndex + "\n1\nAMZN\n2014-11-10\n8\n"
            + latestIndex + "\n1\nAAP\n2014-11-10\n3\n"
            + latestIndex + "\n1\nORCL\n2022-11-10\n21\n"
            + latestIndex + "\n1\nAAP\n2012-11-10\n9\nM\n2\n"
            + latestIndex + "\n1",
        path, latestIndex);
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    String expectedOutput = "Advance Auto Parts Inc  |            AAP  |             16  | "
        + "        108.26  |        1732.24  |          71.27\n\n"
        + "Alphabet Inc - Class A  |          GOOGL  |              9  |         748.46  | "
        + "       6736.14  |         134.72\n\n"
        + "Deere & Company  |             DE  |             10  |          81.05  |        "
        + " 810.50  |          16.21\n\n"
        + "Ford Motor Company  |              F  |            100  |          14.91  |       "
        + " 1491.00  |          29.82\n\n"
        + "    Oracle Corp  |           ORCL  |            131  |          21.95  |       "
        + " 2876.07  |          57.52\n\n"
        + "     Dover Corp  |            DOV  |             15  |          74.93  |        "
        + "1123.95  |          22.48\n\n"
        + " Amazon.com Inc  |           AMZN  |             28  |         230.71  |        "
        + "6459.88  |         129.20";
    System.out.println(out.toString());
    assertEquals(expectedOutput, out.toString().substring(8930));
  }

  @Test
  public void testBuySameAndMultipleDays() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";

    String filename =
        "Portfolio_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
            + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    String input = String.format("3\n%s\nFlexible\n4\n%d\n1\nAMZN\n2022-10-10\n1\nM\n2\n%d\n1",
        path, latestIndex, latestIndex);
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\nEnter portfolio number: Selected portfolio: " + filename
        + ".\n\n" + modifyPortfolioContents()
        + "**********************************\nEnter details for the stock.\n"
        + "**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 113.67\n"
        + "Transaction quantity: Purchasing 1 stocks of Amazon.com Inc for a total of $113.67.\n"
        + portfolioList + "Type M to go to main page.\nEnter portfolio number: "
        + mainPageContents() + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + showPortfolioContents() + "------------------------------------------------"
        + "-----------------------------------------------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n"
        + "---------------------------------------------------------------------------------"
        + "--------------\n"
        + " Amazon.com Inc  |           AMZN  |            101  |          96.96  |        "
        + "9792.67  |         195.85\n\n"
        + portfolioList + "Type M to go to main page.\nEnter portfolio number: "
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }


  @Test
  public void testSell() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";

    String filename =
        "Portfolio_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
            + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    String input = String.format("3\n%s\nFlexible\n4\n%d\n2\nAMZN\n2022-10-10\n5\nM\n2\n%d\n1",
        path, latestIndex, latestIndex);
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:" + filename + "\n" + mainPageContents() + portfolioList
        + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + modifyPortfolioContents() + "**********************************\n"
        + "Enter details for the stock.\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 113.67\n"
        + "Transaction quantity: Selling 5 stocks of Amazon.com Inc for a total of $568.35.\n"
        + portfolioList + "Type M to go to main page.\nEnter portfolio number: "
        + mainPageContents() + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + showPortfolioContents() + "------------------------------------------------------"
        + "-----------------------------------------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n"
        + "-----------------------------------------------------------------------------------"
        + "------------\n"
        + " Amazon.com Inc  |           AMZN  |             95  |          96.79  |        9195.05"
        + "  |         204.95\n\n"
        + portfolioList + "Type M to go to main page.\nEnter portfolio number: "
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testBuyAndSellValidMultiple() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";

    String filename =
        "Portfolio_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
            + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    Integer latestIndex = list.size() + 1;

    String input = String.format("3\n%s\nFlexible\n4\n%d\n1\nORCL\n2014-11-10\n10\n"
            + latestIndex + "\n1\nORCL\n2014-11-10\n10\n"
            + latestIndex + "\n1\nAMZN\n2014-11-10\n10\n"
            + latestIndex + "\n1\nATO\n2018-11-10\n2\n"
            + latestIndex + "\n2\nAAP\n2014-11-10\n3\n"
            + latestIndex + "\n1\nORCL\n2016-11-10\n21\n"
            + latestIndex + "\n1\nATO\n2021-11-10\n1\n"
            + latestIndex + "\n2\nAAP\n2018-11-10\n1\n"
            + latestIndex + "\n2\nAMZN\n2022-11-15\n12\nM\n2\n"
            + latestIndex + "\n1",
        path, latestIndex);
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    String expectedOutput = mainPageContents() + "Type M to go to main page.\n"
        + "Enter file path:Enter portfolio type (Flexible/Inflexible): "
        + "File is successfully imported by name:"
        + filename + "\n" + mainPageContents() + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + modifyPortfolioContents() + "**********************************\n"
        + "Enter details for the stock.\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 113.67\n"
        + "Transaction quantity: Purchasing 1 stocks of Amazon.com Inc for a total of $113.67.\n"
        + portfolioList + "Type M to go to main page.\n" + "Enter portfolio number: "
        + mainPageContents() + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + showPortfolioContents() + " -------------------------------------------------"
        + "----------------------------------------------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n"
        + "--------------------------------------------------------------------------------"
        + "---------------\n"
        + "Advance Auto Parts Inc  |            AAP  |              4  |         147.76  |"
        + "         591.04  |          48.45\n\n"
        + "Alphabet Inc - Class A  |          GOOGL  |              9  |         748.46  |"
        + "        6736.14  |         134.72\n\n"
        + "Deere & Company  |             DE  |             10  |          81.05  |         "
        + "810.50  |          16.21\n\n"
        + "Ford Motor Company  |              F  |            100  |          14.91  |        "
        + "1491.00  |          29.82\n\n"
        + "    Oracle Corp  |           ORCL  |            110  |          11.41  |        "
        + "1255.50  |          25.11\n\n"
        + "     Dover Corp  |            DOV  |             15  |          74.93  |        "
        + "1123.95  |          22.48\n\n"
        + " Amazon.com Inc  |           AMZN  |             10  |          96.79  |         "
        + "967.90  |          19.36\n\n"
        + portfolioList + "Type M to go to main page.\n" + "Enter portfolio number: "
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testBuyAndSellInValidMultiple() throws IOException {
    String path = System.getProperty("user.dir") + "/test/controller/testFiles/Portfolio_test7.csv";

    String filename =
        "Portfolio_" + new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date())
            + "_Flexible";
    List<String> list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    int latestIndex = list.size() + 1;

    String input = String.format("3\n%s\nFlexible\n4\n%d\n10\nAMZN\n2022-10-10\n1\nM\n2\n%d\n1",
        path, latestIndex, latestIndex);
    Reader in = new StringReader(input + "\nM\nq\nM\nq\nM\nq\nM\nq\nM\nq");
    controller = setupController(in);
    controller.start();

    list = new ModelComponentImpl(out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl()).getPortfolioList();
    StringBuilder portfolioList = getPortfolioListString(list);

    String expectedOutput = mainPageContents() + "Type M to go to main page.\nEnter file "
        + "path:Enter portfolio type (Flexible/Inflexible): File is successfully "
        + "imported by name:"
        + filename + "\n" + mainPageContents() + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + modifyPortfolioContents() + "**********************************\n"
        + "Enter details for the stock.\n**********************************\n"
        + "Company Symbol: Please enter a date in (YYYY-MM-DD) format: Company Name: "
        + "Amazon.com Inc\nPrice: 113.67\n"
        + "Transaction quantity: Purchasing 1 stocks of Amazon.com Inc for a total of $113.67.\n"
        + portfolioList + "Type M to go to main page.\nEnter portfolio number: "
        + mainPageContents() + portfolioList + "Type M to go to main page.\n"
        + "Enter portfolio number: Selected portfolio: " + filename + ".\n\n"
        + showPortfolioContents() + "------------------------------------------------------"
        + "-----------------------------------------\n\n"
        + "    CompanyName  |  CompanySymbol  |       Quantity  |     Unit Price  |    "
        + "Total Value  |Net Commission Fees\n\n-----------------------------------------------"
        + "------------------------------------------------\n  Amazon.com Inc  |           "
        + "AMZN  |            101  |          96.96  |        9792.67  |         195.85\n\n"
        + portfolioList + "Type M to go to main page.\nEnter portfolio number: "
        + mainPageContents() + quitMessage();

    assertEquals(expectedOutput, out.toString());
  }
}