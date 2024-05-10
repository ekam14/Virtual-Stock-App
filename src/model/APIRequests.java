package model;

import model.enums.TimeUnit;

/**
 * This class represents the API Request object required
 * to make an API request.
 */
public interface APIRequests {

  /**
   * Method to make a price API request for the given company.
   * @param scale date range.
   * @param symbol Company/Stock symbol.
   * @return stock price of the company.
   */
  String getTimeSeriesData(TimeUnit scale, String symbol);
}
