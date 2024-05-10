package model.dto;

import java.util.ArrayList;

/**
 * This is a wrapper class used to combine portfolio related data. This class has placeholder to
 * store array list of stock date and value pairs, name of portfolio and value by which stock prices
 * are scaled. This class is used in portfolio performance implementation.
 */
public class PortfolioPerformanceDTO {

  ArrayList<Pair> data;
  Double scaleFactor;
  String portfolioName;

  /**
   * This is a constructor method for PortfolioPerformanceDTO.
   *
   * @param data arraylist of stocks date and value pairs
   * @param scaleFactor scale factor for stock price
   * @param portfolioName portfolio name
   */
  public PortfolioPerformanceDTO(ArrayList<Pair> data, Double scaleFactor, String portfolioName) {
    this.data = data;
    this.scaleFactor = scaleFactor;
    this.portfolioName = portfolioName;
  }

  public ArrayList<Pair> getData() {
    return data;
  }

  public void setData(ArrayList<Pair> data) {
    this.data = data;
  }

  public String getPortfolioName() {
    return portfolioName;
  }

  public Double getScaleFactor() {
    return scaleFactor;
  }

}
