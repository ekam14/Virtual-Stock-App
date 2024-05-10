package model.dto;

/**
 * This is pair class used to pair relevant string and their double value. This class is used to
 * store date and price value of stocks for ease of data manipulation.
 */
public class Pair {

  String key;
  Double value;

  String strValue;

  /**
   * This is a constructor for pair class.
   *
   * @param key   date as string
   * @param value price of stock as double
   */
  public Pair(String key, Double value) {
    this.key = key;
    this.value = value;
  }

  public Pair(String key, String strValue) {
    this.key = key;
    this.strValue = strValue;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public void add(Double other) {
    this.value = this.value + other;
  }

  public String getStrValue() {
    return strValue;
  }

  public void setStrValue(String strValue) {
    this.strValue = strValue;
  }
}
