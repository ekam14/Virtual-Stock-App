package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import model.enums.TimeUnit;

/**
 * This class represents an API request object. It has attributes and methods related to make an API
 * request.
 */
public class APIRequestsImpl implements APIRequests {
  private final String API_KEY;

  /**
   * Constructs an API request object, and it initializes the API_KEY and the searchURL required for
   * making a request to the API.
   */
  public APIRequestsImpl() {
    API_KEY = "UDJ8FEEUYB4NYVJ6";
  }

  /**
   * This method is used to get url String as per time unit and given company symbol.
   *
   * @param scale  time unit for fetching stocks price data
   * @param symbol company/Stock symbol
   * @return url string used to fetch price data of company stock.
   */
  private String getUrl(TimeUnit scale, String symbol) {
    String timeSeries = "TIME_SERIES_DAILY";
    switch (scale) {
      case Daily:
        timeSeries = "TIME_SERIES_DAILY";
        break;
      case Monthly:
      case Yearly:
        timeSeries = "TIME_SERIES_MONTHLY";
        break;
      default:
        break;
    }
    return "https://www.alphavantage.co/query?function=" + timeSeries + "&symbol=" + symbol
            + "&apikey=" + API_KEY + "&outputsize=full";
  }

  @Override
  public String getTimeSeriesData(TimeUnit scale, String symbol) {
    URL url = null;
    try {
      url = new URL(getUrl(scale, symbol));
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }
    InputStream in = null;
    StringBuilder output = new StringBuilder();
    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      // throw new IllegalArgumentException(String.format("No price data found for %s.", symbol));
    }
    return output.toString();
  }
}
