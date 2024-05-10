package model;

import model.enums.TransactionType;

/**
 * This interface represents all possible
 * operations on a company stock.
 */
public interface CompanyStock {
  /**
   * Helper method to get the stock's company name.
   * @return Stock's company name.
   */
  String getCompanyName();

  /**
   * Helper method to get the stock's symbol.
   * @return Stock's symbol.
   */
  String getCompanySymbol();

  /**
   * Helper method to get the stock's quantity
   * value.
   * @return Stock's quantity.
   */
  Double getQuantity();

  /**
   * Helper method to get unit price of the stock.
   * @return stock's unit price.
   */
  Double getBoughtPrice();

  /**
   * Helper method to get the total bought value.
   * @return total bought value.
   */
  Double getBoughtValue();

  void setBoughtPrice(double price);

  Double getCommissionFees();

  String getDate();

  TransactionType getType();




}
