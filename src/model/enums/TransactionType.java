package model.enums;

/**
 * This enum represents possible transaction types.
 */
public enum TransactionType {
  BUY("BUY"),
  SELL("SELL");

  private final String value;

  TransactionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
