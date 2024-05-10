package model;

import model.enums.TransactionType;

/**
 * This class represents a company stock. A company stock has company name, company/stock symbol,
 * quantity, stock unit price and total bought value.
 */
public class CompanyStockImpl implements CompanyStock {

  private final String date;
  private final String companyName;
  private final String companySymbol;
  private final Double quantity;
  private Double boughtPrice;
  private Double boughtValue;
  private final TransactionType type;
  private final Double commissionFees;

  /**
   * Constructs a CompanyStock object and initializes the name of the company, company/stock symbol,
   * quantity, stock unit price, transaction type, commission fees.
   *
   * @param date           date of the transaction.
   * @param companyName    Name of the company.
   * @param companySymbol  Stock symbol.
   * @param quantity       Quantity purchased
   * @param boughtPrice    Stock unit price.
   * @param type           transaction type.
   * @param commissionFees commission fees.
   */
  public CompanyStockImpl(String date, String companyName, String companySymbol, Double quantity,
      Double boughtPrice, TransactionType type, Double commissionFees) {
    this.date = date;
    this.companyName = companyName;
    this.companySymbol = companySymbol;
    this.quantity = quantity;
    this.boughtPrice = boughtPrice;
    this.boughtValue = quantity * boughtPrice;
    this.type = type;
    this.commissionFees = commissionFees;
  }

  /**
   * Constructs a CompanyStock object and initializes the name of the company, company/stock symbol,
   * quantity, stock unit price, total bought value, transaction type, commission fees.
   *
   * @param date           date of the transaction.
   * @param companyName    Name of the company.
   * @param companySymbol  Stock symbol.
   * @param quantity       Quantity purchased.
   * @param boughtPrice    Stock unit price.
   * @param boughtValue    Total bought value.
   * @param type           transaction type.
   * @param commissionFees commission fees.
   */
  public CompanyStockImpl(String date, String companyName, String companySymbol, Double quantity,
      Double boughtPrice, Double boughtValue, TransactionType type, Double commissionFees) {
    this.date = date;
    this.companyName = companyName;
    this.companySymbol = companySymbol;
    this.quantity = quantity;
    this.boughtPrice = boughtPrice;
    this.boughtValue = boughtValue;
    this.type = type;
    this.commissionFees = commissionFees;
  }

  @Override
  public String getCompanyName() {
    return companyName;
  }

  @Override
  public String getCompanySymbol() {
    return companySymbol;
  }

  @Override
  public Double getQuantity() {
    return quantity;
  }

  @Override
  public Double getBoughtPrice() {
    return boughtPrice;
  }

  @Override
  public Double getBoughtValue() {
    return boughtValue;
  }

  @Override
  public Double getCommissionFees() {
    return commissionFees;
  }

  @Override
  public String getDate() {
    return this.date;
  }

  @Override
  public TransactionType getType() {
    return type;
  }

  @Override
  public void setBoughtPrice(double price) {
    this.boughtPrice = price;
    this.boughtValue = this.boughtPrice * this.quantity;
  }

  @Override
  public String toString() {
    return String.format("%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%s", date, companyName, companySymbol,
        quantity, boughtPrice, boughtValue, commissionFees, type);
  }
}
