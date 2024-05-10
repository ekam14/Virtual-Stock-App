package model;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import model.dto.Pair;
import model.dto.PortfolioPerformanceDTO;
import model.enums.PortfolioType;
import model.enums.TimeUnit;
import model.enums.TransactionType;
import view.ViewComponent;
import view.ViewComponentImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A JUnit test class for the ModelComponentImpl class.
 */
public class ModelComponentImplTest {

  private ModelComponent modelComponent;

  private String getFilename(PortfolioType pType) {
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String date = dataFormat.format(new Date());
    return "Portfolio_" + date + "_" + pType.getValue();
  }

  @Before
  public void setup() {
    modelComponent = new ModelComponentImpl(System.out, new PortfolioRepositoryImpl(),
        new APIRequestsImpl());
  }

  @Test
  public void testObjectSuccess() throws IOException {
    assertNotNull(modelComponent);
  }

  @Test
  public void testCorrectCheckStock() {
    assertTrue(modelComponent.checkStock("ORCL"));
  }

  @Test
  public void testCompanyNameCannotBeNull() {
    assertFalse(modelComponent.checkStock(null));
  }

  @Test
  public void testCompanyNameCannotBeEmpty() {
    assertFalse(modelComponent.checkStock("  "));
  }

  @Test
  public void testCorrectStockSymbol() {
    assertTrue(modelComponent.checkStock("AMZN"));
  }

  @Test
  public void testPortfolioList() throws IOException {
    PortfolioRepository repo = new PortfolioRepositoryImpl();

    HashMap<String, CompanyStock> dummy = new HashMap<>();

    CompanyStock dummyStock = new CompanyStockImpl("2022-11-14", "Amazon.com Inc",
        "AMZN", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    dummy.put("AMZN", dummyStock);

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<CompanyStock> cs = new ArrayList<>();
    cs.add(dummyStock);

    repo.saveCompanyStock(pType.getValue(), cs);

    List<String> portfolioList = repo.getListOfPortfolio();

    boolean filePresent = false;

    for (String portfolio : portfolioList) {
      filePresent |= Objects.equals(portfolio, filename);
    }

    assertTrue(filePresent); // to check if the portfolio is getting saved.
  }

  @Test
  public void testSavePortfolio() throws IOException {
    PortfolioRepository repo = new PortfolioRepositoryImpl();

    HashMap<String, CompanyStock> dummy = new HashMap<>();

    CompanyStock dummyStock = new CompanyStockImpl("2022-11-14", "Amazon.com Inc",
        "AMZN", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    dummy.put("AMZN", dummyStock);

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<CompanyStock> cs = new ArrayList<>();
    cs.add(dummyStock);

    repo.saveCompanyStock(pType.getValue(), cs);

    List<String> portfolioList = repo.getListOfPortfolio();

    boolean filePresent = false;

    for (String portfolio : portfolioList) {
      filePresent |= Objects.equals(portfolio, filename);
    }

    assertTrue(filePresent); // to check if the portfolio is getting saved.

    StringBuffer out = new StringBuffer();
    ViewComponent viewComponent = new ViewComponentImpl(out);
    viewComponent.showPortfolioComposition(dummy);

    String expectedOutput = "---------------------------------------------------------------------"
        + "--------------------------\n\n    CompanyName  |  CompanySymbol  |       Quantity  "
        + "|     Unit Price  |    Total Value  |Net Commission Fees\n\n------------------------"
        + "-----------------------------------------------------------------------\n Amazon.com"
        + " Inc  |           AMZN  |           1997  |          14.00  |       27958.00  |"
        + "       27958.00\n\n";

    assertEquals(expectedOutput, out.toString());
  }

  @Test
  public void testCompanyStockValuesForNBuys() throws IOException {
    PortfolioRepository repo = new PortfolioRepositoryImpl();

    CompanyStock dummyStock1 = new CompanyStockImpl("2022-11-13", "Amazon.com Inc",
        "AMZN", 10.0, 14.0, TransactionType.BUY, 20.0);
    CompanyStock dummyStock2 = new CompanyStockImpl("2022-11-09", "Amazon.com Inc",
        "AMZN", 30.0, 16.0, TransactionType.BUY, 20.0);
    CompanyStock dummyStock3 = new CompanyStockImpl("2022-11-07", "Amazon.com Inc",
        "AMZN", 20.0, 12.0, TransactionType.BUY, 20.0);

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<CompanyStock> cs = new ArrayList<>();
    cs.add(dummyStock1);
    cs.add(dummyStock2);
    cs.add(dummyStock3);
    repo.saveCompanyStock(pType.getValue(), cs);

    CompanyStock c1 = modelComponent.getPortfolioValue("2022-11-13", filename).get("AMZN");
    assertEquals(c1.getBoughtPrice(), 100.79, 0.0000001);
    assertEquals(c1.getQuantity().doubleValue(), 60.0);
    assertEquals(c1.getCommissionFees(), 60, 0.000001);

    CompanyStock c2 = modelComponent.getPortfolioValue("2022-11-09", filename).get("AMZN");
    assertEquals(c2.getBoughtPrice(), 86.14, 0.0000001);
    assertEquals(c2.getQuantity().doubleValue(), 50.0);
    assertEquals(c2.getCommissionFees(), 40, 0.000001);

    CompanyStock c3 = modelComponent.getPortfolioValue("2022-11-07", filename).get("AMZN");
    assertEquals(c3.getBoughtPrice(), 90.53, 0.0000001);
    assertEquals(c3.getQuantity().doubleValue(), 20.0);
    assertEquals(c3.getCommissionFees(), 20, 0.000001);
  }

  @Test
  public void testCompanyStockValuesForNBuysNSells() throws IOException {
    PortfolioRepository repo = new PortfolioRepositoryImpl();

    CompanyStock dummyStock1 = new CompanyStockImpl("2022-11-13", "Amazon.com Inc",
        "AMZN", 10.0, 14.0, TransactionType.BUY, 100.0);
    CompanyStock dummyStock2 = new CompanyStockImpl("2022-11-09", "Amazon.com Inc",
        "AMZN", 30.0, 16.0, TransactionType.BUY, 10.0);
    CompanyStock dummyStock3 = new CompanyStockImpl("2022-11-07", "Amazon.com Inc",
        "AMZN", 20.0, 12.0, TransactionType.BUY, 20.0);
    CompanyStock dummyStock4 = new CompanyStockImpl("2022-11-13", "Amazon.com Inc",
        "AMZN", 30.0, 16.0, TransactionType.SELL, 30.0);
    CompanyStock dummyStock5 = new CompanyStockImpl("2022-11-11", "Amazon.com Inc",
        "AMZN", 10.0, 12.0, TransactionType.SELL, 40.0);

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<CompanyStock> cs = new ArrayList<>();
    cs.add(dummyStock1);
    cs.add(dummyStock2);
    cs.add(dummyStock3);
    cs.add(dummyStock4);
    cs.add(dummyStock5);
    repo.saveCompanyStock(pType.getValue(), cs);

    CompanyStock c1 = modelComponent.getPortfolioValue("2022-11-13", filename).get("AMZN");
    assertEquals(c1.getBoughtPrice(), 100.79, 0.0000001);
    assertEquals(c1.getQuantity().doubleValue(), 20.0);
    assertEquals(c1.getCommissionFees(), 200.0, 0.000001);

    CompanyStock c2 = modelComponent.getPortfolioValue("2022-11-11", filename).get("AMZN");
    assertEquals(c2.getBoughtPrice(), 100.79, 0.0000001);
    assertEquals(c2.getQuantity().doubleValue(), 40.0);
    assertEquals(c2.getCommissionFees(), 70.0, 0.000001);

    CompanyStock c3 = modelComponent.getPortfolioValue("2022-11-07", filename).get("AMZN");
    assertEquals(c3.getBoughtPrice(), 90.53, 0.0000001);
    assertEquals(c3.getQuantity().doubleValue(), 20.0);
    assertEquals(c3.getCommissionFees(), 20, 0.000001);
  }

  @Test
  public void testCostBasisConstant() throws IOException {
    PortfolioRepository repo = new PortfolioRepositoryImpl();

    CompanyStock dummyStock1 = new CompanyStockImpl("2022-11-13", "Amazon.com Inc",
        "AMZN", 10.0, 14.0, TransactionType.BUY, 10.0);
    CompanyStock dummyStock2 = new CompanyStockImpl("2022-11-09", "Amazon.com Inc",
        "AMZN", 30.0, 16.0, TransactionType.BUY, 20.0);

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<CompanyStock> cs = new ArrayList<>();
    cs.add(dummyStock1);
    cs.add(dummyStock2);
    repo.saveCompanyStock(pType.getValue(), cs);

    assertEquals(modelComponent.getCostBasis("2022-11-16", filename), 650.00, 0.0000001);
    assertEquals(modelComponent.getCostBasis("2022-11-14", filename), 650.00, 0.0000001);
    assertEquals(modelComponent.getCostBasis("2022-11-13", filename), 650.00, 0.0000001);
    assertEquals(modelComponent.getCostBasis("2022-11-09", filename), 500.00, 0.0000001);
  }

  @Test
  public void testCheckNumberCorrect() {
    String[] inputs = {"1", "2", "234", "12801", "4"};

    for (String el : inputs) {
      assertTrue(String.format("There must have been no error for %s.", el),
          modelComponent.checkNumber(el));
    }
  }

  @Test
  public void testCheckNumberWrong() {
    String[] inputs = {"pdp", "-0", "-1223", "12sq1", "ddqwd,", "%$! qxdxq", "hello"};

    for (String el : inputs) {
      assertFalse(String.format("There must have been an error for %s.", el),
          modelComponent.checkNumber(el));
    }
  }

  @Test
  public void testCheckDateCorrect() {
    String[] inputs = {"2022-10-21", "2022-10-20", "2022-10-12", "2022-10-02", "2022-10-30"};

    for (String el : inputs) {
      assertTrue(String.format("There must have been no error for %s.", el),
          modelComponent.checkDate(el));
    }
  }

  @Test
  public void testCheckDateWrong() {
    String[] inputs = {"2024-09-02", "-0", "-1223", "12sq1", "ddqwd,", "%$! qxdxq", "hello",
        "e", "09-21-22313", "99/1/", "//", "-1/", "/-01", "13/20/1234", "0/20/2022", "2022-12-31",
        "2022", "03-33-2022", "02/23/020", "02/23/2091-", "02/23/20212", "02/23/2025",
        "2025-02-23"};

    for (String el : inputs) {
      assertFalse(String.format("There must have been an error for %s.", el),
          modelComponent.checkDate(el));
    }
  }

  @Test
  public void testCompanyStockClass() {
    double rate = 10.0;

    CompanyStock stock = new CompanyStockImpl("2022-11-11", "Oracle", "ORCL",
        1000.0, 98.34, TransactionType.BUY, (rate * 1000 * 98.34) / 100.0);

    assertNotNull(stock);
    assertEquals("Oracle", stock.getCompanyName());
    assertEquals("ORCL", stock.getCompanySymbol());
    assertEquals(1000.0, stock.getQuantity().doubleValue());
    assertEquals(98.34, stock.getBoughtPrice(), 0.0001);
    assertEquals(98340, stock.getBoughtValue(), 0.0001);
    assertEquals(9834, stock.getCommissionFees(), 0.0001);
  }

  @Test
  public void testValidCompanySymbolInCache() {
    Cache.loadConfig();

    String[] inputs = {"GOOGL", "GOOG", "AAPL", "ORCL", "OGN", "OTIS", "PCAR", "PKG", "PH", "AMZN"};

    String[] output = {"Alphabet Inc - Class A", "Alphabet Inc - Class C", "Apple Inc",
        "Oracle Corp", "Organon & Company", "Otis Worldwide Corp", "Paccar Inc",
        "Packaging Corp Of America", "Amazon.com Inc"};

    int i = 0;
    for (String companySymbol : inputs) {
      assertEquals(output[i], Cache.companyList.get(companySymbol));
      i += 1;
    }
  }

  @Test
  public void testInValidCompanySymbolInCache() {
    Cache.loadConfig();
    String[] inputs = {"GOOGL1", "G1OG", "**", "QQQQ112", "@", "", "ADA1@", "@PKG", "@PH", "$AMZN"};

    for (String companySymbol : inputs) {
      assertFalse(Cache.companyList.containsValue(companySymbol));
    }
  }

  @Test
  public void testValidPortfolioInCache() {
    PortfolioRepository repo = new PortfolioRepositoryImpl();
    CompanyStock dummyStock = new CompanyStockImpl("2022-11-14", "Oracle Corp",
        "ORCL", 1997.0, 14.0, TransactionType.BUY, 27958.0);

    PortfolioType pType = PortfolioType.FLEXIBLE;
    String filename = getFilename(pType);

    List<CompanyStock> cs = new ArrayList<>();
    cs.add(dummyStock);

    repo.saveCompanyStock(pType.getValue(), cs);

    List<String> portfolioList = repo.getListOfPortfolio();

    boolean filePresent = false;

    for (String portfolio : portfolioList) {
      filePresent |= Objects.equals(portfolio, filename);
    }
    try {
      //Check if value is present in cache too
      filePresent &= Cache.objectStore.containsKey(filename);
    } catch (Exception e) {
      fail("testValidPortfolioInCache failed to get portfolio in cache.");
    }

    assertTrue(filePresent); // to check if the portfolio is getting saved.

  }

  @Test
  public void testInValidPortfolioInCache() {
    Cache.loadConfig();

    String[] inputs = {"Portfolio_2022-11-15_19:36:59_", "XYZ", "**", "QQQQ112", "@", "", "ADA1@",
        "e", "@PKG", "@PH", "$AMZN", "#AMCR", "Portfolio_2022-11-15_19:36:59_Flexible1"};

    for (String companySymbol : inputs) {
      assertFalse(Cache.objectStore.containsValue(companySymbol));
    }
  }

  @Test
  public void testPortfolioPerformanceMethodYearly() {
    HashMap<String, List<CompanyStock>> dummy = new HashMap<>();
    List<CompanyStock> cs = new ArrayList<>();
    CompanyStock dummyStock = new CompanyStockImpl("2010-11-14", "Amazon.com Inc",
        "AMZN", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs.add(dummyStock);
    dummy.put("AMZN", cs);
    dummyStock = new CompanyStockImpl("2010-12-14", "Oracle Corp",
        "ORCL", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs = new ArrayList<>();
    cs.add(dummyStock);
    dummy.put("ORCL", cs);
    dummyStock = new CompanyStockImpl("2010-14-14", "Advance Auto Parts Inc",
        "AAP", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs = new ArrayList<>();
    cs.add(dummyStock);
    dummy.put("AAP", cs);
    String filename = "Portfolio_2022-11-17_01:24:05_Flexible";
    Cache.objectStore.put(filename, dummy);
    PortfolioPerformanceDTO dto = modelComponent.getPerformanceOfPortfolio("2012-10-10#2022-10-10",
        filename);
    String[] dt = {"2.0", "3.0", "3.0", "5.0", "6.0", "8.0", "10.0", "12.0", "20.0", "20.0", "3.0"};
    int i = 0;
    String a = "";
    for (Pair p : dto.getData()) {
      assertEquals(dt[i++], p.getValue().toString());
    }
  }

  @Test
  public void testPortfolioPerformanceMethodMonthly() {
    HashMap<String, List<CompanyStock>> dummy = new HashMap<>();
    List<CompanyStock> cs = new ArrayList<>();
    CompanyStock dummyStock = new CompanyStockImpl("2010-11-14", "Amazon.com Inc",
        "AMZN", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs.add(dummyStock);
    dummy.put("AMZN", cs);
    dummyStock = new CompanyStockImpl("2010-12-14", "Oracle Corp",
        "ORCL", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs = new ArrayList<>();
    cs.add(dummyStock);
    dummy.put("ORCL", cs);
    dummyStock = new CompanyStockImpl("2010-14-14", "Advance Auto Parts Inc",
        "AAP", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs = new ArrayList<>();
    cs.add(dummyStock);
    dummy.put("AAP", cs);
    String filename = "Portfolio_2022-11-17_01:24:05_Flexible";
    Cache.objectStore.put(filename, dummy);
    PortfolioPerformanceDTO dto = modelComponent.getPerformanceOfPortfolio("2022-01-10#2022-05-10",
        filename);
    String[] dt = {"19.0", "19.0", "20.0", "16.0", "16.0"};
    int i = 0;
    for (Pair p : dto.getData()) {
      assertEquals(dt[i++], p.getValue().toString());
    }
  }

  @Test
  public void testPortfolioPerformanceMethodDaily() {
    HashMap<String, List<CompanyStock>> dummy = new HashMap<>();
    List<CompanyStock> cs = new ArrayList<>();
    CompanyStock dummyStock = new CompanyStockImpl("2010-11-14", "Amazon.com Inc",
        "AMZN", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs.add(dummyStock);
    dummy.put("AMZN", cs);
    dummyStock = new CompanyStockImpl("2010-12-14", "Oracle Corp",
        "ORCL", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs = new ArrayList<>();
    cs.add(dummyStock);
    dummy.put("ORCL", cs);
    dummyStock = new CompanyStockImpl("2010-14-14", "Advance Auto Parts Inc",
        "AAP", 1997.0, 14.0, TransactionType.BUY, 27958.0);
    cs = new ArrayList<>();
    cs.add(dummyStock);
    dummy.put("AAP", cs);
    String filename = "Portfolio_2022-11-17_01:24:05_Flexible";
    Cache.objectStore.put(filename, dummy);
    PortfolioPerformanceDTO dto = modelComponent.getPerformanceOfPortfolio("2022-05-10#2022-05-20",
        filename);
    int i = 0;
    String[] dt = {"19.0", "19.0", "19.0", "20.0", "20.0", "20.0", "19.0", "19.0", "19.0"};
    for (Pair p : dto.getData()) {
      assertEquals(dt[i++], p.getValue().toString());
    }
  }

  @Test
  public void testAPICall() {
    APIRequests api = new APIRequestsImpl();
    String monthly = api.getTimeSeriesData(TimeUnit.Monthly, "ORCL");
    String yearly = api.getTimeSeriesData(TimeUnit.Yearly, "ORCL");
    assertEquals(monthly, yearly);
    JSONObject jsonObject = new JSONObject(monthly);
    int i = 0;
    String closePrice = "";
    if (jsonObject != null
        && jsonObject.getJSONObject("Monthly Time Series") != null) {
      JSONObject myResponse = jsonObject.getJSONObject("Monthly Time Series");
      if (myResponse != null && myResponse.keySet() != null && myResponse.keySet().size() > 0) {
        ArrayList<String> stockPriceDates
            = new ArrayList<>(myResponse.keySet());
        Collections.sort(stockPriceDates, Collections.reverseOrder());
        for (String validDate : stockPriceDates) {
          if (validDate.equals("2022-10-31")) {
            JSONObject closeValue = (JSONObject) myResponse.get(validDate);
            closePrice = (String) closeValue.get("4. close");
            break;
          }
        }
        assertEquals("78.0700", closePrice);
      }
    }
    String daily = api.getTimeSeriesData(TimeUnit.Daily, "IBM");
    jsonObject = new JSONObject(daily);
    if (jsonObject.getJSONObject("Time Series (Daily)") != null) {
      JSONObject myResponse = jsonObject.getJSONObject("Time Series (Daily)");
      if (myResponse != null && myResponse.keySet() != null && myResponse.keySet().size() > 0) {
        ArrayList<String> stockPriceDates
            = new ArrayList<>(myResponse.keySet());
        Collections.sort(stockPriceDates, Collections.reverseOrder());
        for (String validDate : stockPriceDates) {
          if (validDate.equals("2022-11-16")) {
            JSONObject closeValue = (JSONObject) myResponse.get(validDate);
            closePrice = (String) closeValue.get("4. close");
            break;
          }
        }
        assertEquals("144.5200", closePrice);
      }
    }
  }


  @Test
  public void testDollarCostAveraging() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN");
    dollarData.put("weights", "10:90");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
    Cache.loadAppData(new PortfolioRepositoryImpl());
    Set<String> portfolioNames = Cache.objectStore.keySet();
    int len = portfolioNames.size();
    HashMap<String, List<CompanyStock>> protfolio = Cache.objectStore.get(
        portfolioNames.toArray()[len - 1]);
    Assert.assertEquals(protfolio.toString(),
        "{ORCL=[2013-10-31,Oracle Corp,ORCL,2.95,33.50,98.83,1.00,BUY, "
            + "2012-12-31,Oracle Corp,ORCL,2.97,33.32,98.96,1.00,BUY], "
            + "AMZN=[2013-10-31,Amazon.com Inc,AMZN,2.44,364.03,888.23,9.00,BUY, "
            + "2012-12-31,Amazon.com Inc,AMZN,3.55,250.87,890.59,9.00,BUY]}");
  }

  @Test
  public void testDollarCostAveragingNoEndDate() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN");
    dollarData.put("weights", "10:90");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2022-10-10#");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
    Cache.loadAppData(new PortfolioRepositoryImpl());
    Set<String> portfolioNames = Cache.objectStore.keySet();
    portfolioNames.stream().sorted(Comparator.reverseOrder());
    int len = portfolioNames.size();
    HashMap<String, List<CompanyStock>> protfolio = Cache.objectStore.get(
        portfolioNames.toArray()[len - 1]);
    Assert.assertEquals(protfolio.toString(),
        "{ORCL=[2013-10-31,Oracle Corp,ORCL,2.95,33.50,98.83,1.00,BUY, "
            + "2012-12-31,Oracle Corp,ORCL,2.97,33.32,98.96,1.00,BUY], "
            + "AMZN=[2013-10-31,Amazon.com Inc,AMZN,2.44,364.03,888.23,9.00,BUY, "
            + "2012-12-31,Amazon.com Inc,AMZN,3.55,250.87,890.59,9.00,BUY]}");
  }


  @Test
  public void testValidateAmountInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("companySymbol", "ORCL:AMZN");
    dollarData.put("weights", "90:10");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    String[] companySymbols = {"-1", "-100", "adasd",
        "@1213"};
    for (String companySymbol : companySymbols) {
      dollarData.put("amount", companySymbol);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidAmountMessage"),
          "Entered amount is invalid");
    }
  }

  @Test
  public void testValidateCompanySymbolInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("weights", "10:80:10");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    String[] companySymbols = {"ORCL:AMZN1:ORCL", "ORCL:AMZN:APP1", "ORCL:AMZN:APP",
        "ORCL:AMZN:APP"};
    for (String companySymbol : companySymbols) {
      dollarData.put("companySymbol", companySymbol);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidCompanySymbolMessage"),
          "Entered company symbol is invalid.");
    }
  }

  @Test
  public void testValidateWeightsInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN:AAP");
    dollarData.put("weights", "101:90");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    String[] wts = {"101:90:10", "0:90:0", "-1:90:10", "100:-1:0", "90:0:3", "-1:-1:2", "-1:1:100"};
    for (String wt : wts) {
      dollarData.put("weights", wt);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidWeightMessage"),
          "Sum of all the weights is not 100 or invalid value is provided.");
    }
  }

  @Test
  public void testCommissionFeeInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN:AAP");
    dollarData.put("weights", "10:80:10");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    String[] fees = {"-1", "53", "100", "1000"};
    for (String fee : fees) {
      dollarData.put("commissionFee", fee);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidFeesMessage"),
          "Given commission fees % is invalid (Must be between 1-50). Try Again.");
    }
  }

  @Test
  public void testDateRangeInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN:AAP");
    dollarData.put("weights", "10:80:10");
    dollarData.put("commissionFee", "1");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    String[] dates = {"2012-13-10#2013-10-10", "2012-01-10#201-10-10", "2014-10-10#2013-10-10",
        "2012-11-01#2013-10-32", "2012-13-10#2013-10-10", "2015-10-10#2015-02-09",
        "2016-10-10#2014-10-09"};
    for (String date : dates) {
      dollarData.put("dateRange", date);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidDateRangeMessage"),
          "Entered date Range is invalid.");
    }
  }

  @Test
  public void testFreqInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN:AAP");
    dollarData.put("weights", "10:10:80");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    dollarData.put("existingPortfolio", "");
    String[] freqs = {"Yearly1", "amonthly", "daily1", "@1231"};
    for (String freq : freqs) {
      dollarData.put("StrategyFrequency", freq);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidFreqMessage"),
          "Entered frequency is invalid.");
    }
  }

  @Test
  public void testExistsInInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN:AAP");
    dollarData.put("weights", "101:90");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2013-10-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Yearly");
    String[] wts = {"101:90:10", "0:90:0", "-1:90:10", "100:-1:0", "90:0:3", "-1:-1:2", "-1:1:100"};
    Cache.loadAppData(new PortfolioRepositoryImpl());
    Set<String> portfolioNames = Cache.objectStore.keySet();
    int len = portfolioNames.size();
    for (String wt : wts) {
      dollarData.put("weights", wt);
      HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
      Assert.assertEquals(companyStocksList.get("invalidWeightMessage"),
          "Sum of all the weights is not 100 or invalid value is provided.");
    }
  }

  @Test
  public void testInvalidDataInvestmentStrategies() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "-1000");
    dollarData.put("companySymbol", "ORCLAMZN");
    dollarData.put("weights", "90:10");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2025-10-10");
    dollarData.put("fileType", "flexible");
    dollarData.put("StrategyFrequency", "yearly");
    dollarData.put("portfolioName", "");

    HashMap<String, String> errors = modelComponent.applyStrategy(dollarData);

    Assert.assertEquals("true", errors.get("isError"));
    Assert.assertEquals("Entered date Range is invalid.",
            errors.get("invalidDateRangeMessage"));
    Assert.assertEquals("Entered amount is invalid", errors.get("invalidAmountMessage"));
    Assert.assertEquals("Entered number of company symbol is not equal to number weights.",
            errors.get("invalidWeightsMessage"));
    Assert.assertEquals("Entered frequency is invalid.", errors.get("invalidFreqMessage"));
  }

  @Test
  public void testInvalidDataInvestmentStrategies2() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "-1000");
    dollarData.put("companySymbol", "ORCL:AMZN");
    dollarData.put("weights", "92:10");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "21012-10-10#");
    dollarData.put("StrategyFrequency", "yearly");
    dollarData.put("portfolioName", "test");

    HashMap<String, String> errors = modelComponent.applyStrategy(dollarData);

    assertEquals("true", errors.get("isError"));
    assertEquals("Entered date Range is invalid.",
            errors.get("invalidDateRangeMessage"));
    assertEquals("Entered amount is invalid", errors.get("invalidAmountMessage"));
    assertNull(errors.get("invalidWeightsMessage"));
  }

  @Test
  public void testDollarCostAveragingMonthly() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN");
    dollarData.put("weights", "10:90");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2012-12-10");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Monthly");
    dollarData.put("portfolioName", "");
    HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
    Cache.loadAppData(new PortfolioRepositoryImpl());
    Set<String> portfolioNames = Cache.objectStore.keySet();
    int len = portfolioNames.size();
    HashMap<String, List<CompanyStock>> protfolio = Cache.objectStore.get(
            portfolioNames.toArray()[len - 1]);
    Assert.assertEquals(protfolio.toString(),
            "{ORCL=[2012-10-10,Oracle Corp,ORCL,1.00,30.58,30.58,0.31,BUY, "
                    + "2012-10-10#2012-10-10,Oracle Corp,ORCL,3.23,30.58,99.00,1.00,BUY], "
                    + "AMZN=[2022-10-10,Amazon.com Inc,AMZN,1.00,113.67,113.67,1.14,BUY, "
                    + "2012-10-10#2012-10-10,Amazon.com Inc,AMZN,3.63,244.99,891.00,9.00,BUY]}");
  }

  @Test
  public void testDollarCostAveragingDaily() throws IOException {
    Map<String, Object> dollarData = new HashMap<>();
    dollarData.put("amount", "1000");
    dollarData.put("companySymbol", "ORCL:AMZN");
    dollarData.put("weights", "10:90");
    dollarData.put("commissionFee", "1");
    dollarData.put("dateRange", "2012-10-10#2012-10-20");
    dollarData.put("fileType", "Flexible");
    dollarData.put("StrategyFrequency", "Daily");
    dollarData.put("portfolioName", "");
    HashMap<String, String> companyStocksList = modelComponent.applyStrategy(dollarData);
    Cache.loadAppData(new PortfolioRepositoryImpl());
    Set<String> portfolioNames = Cache.objectStore.keySet();
    int len = portfolioNames.size();
    HashMap<String, List<CompanyStock>> protfolio = Cache.objectStore.get(
            portfolioNames.toArray()[len - 1]);
    Assert.assertEquals(protfolio.toString(),
            "{ORCL=[2012-10-10,Oracle Corp,ORCL,1.00,30.58,30.58,0.31,BUY, "
                    + "2012-10-10#2012-10-10,Oracle Corp,ORCL,3.23,30.58,99.00,1.00,BUY], "
                    + "AMZN=[2022-10-10,Amazon.com Inc,AMZN,1.00,113.67,113.67,1.14,BUY, "
                    + "2012-10-10#2012-10-10,Amazon.com Inc,AMZN,3.63,244.99,891.00,9.00,BUY]}");
  }
}