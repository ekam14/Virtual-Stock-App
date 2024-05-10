package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This class represents a Cache object which is used to retrieve and load up previously accessed
 * stock data, thus decreasing net I/O time.
 */
class Cache {
  static Properties properties = new Properties();

  static Map<String, String> companyList = new HashMap<>();

  static Map<String, HashMap<String, List<CompanyStock>>> objectStore = new HashMap<>();

  /**
   * This method load configuration from config file.
   */
  static void loadConfig() {
    try {
      InputStream config = ClassLoader.getSystemClassLoader()
              .getResourceAsStream("config.properties");
      properties.load(config);
    } catch (IOException ex) {
      throw new RuntimeException();
    }
  }

  /**
   * Helper method to load the cache contents.
   *
   * @param portfolioRepo Portfolio repo for which we need data.
   */
  static void loadAppData(PortfolioRepository portfolioRepo) {
    try {
      //cache to store ticker
      InputStream appData = ClassLoader.getSystemClassLoader()
              .getResourceAsStream("appData/listing_status.csv");
      assert appData != null;
      BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(appData, StandardCharsets.UTF_8));
      String ticker;
      while ((ticker = bufferedReader.readLine()) != null) {
        String[] data = ticker.split(",");
        if (data != null && data.length >= 2 && !data[1].isEmpty()) {
          companyList.put(data[0], data[1]);
        }
      }
      bufferedReader.close();

      List<String> portfolioData = portfolioRepo.getListOfPortfolio();
      for (String portfolioName : portfolioData) {
        objectStore.put(portfolioName, portfolioRepo.getPortfolio(portfolioName));
      }
    } catch (IOException ex) {
      // pass;
    }
  }
}
