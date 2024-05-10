package model.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * This enum represents possible portfolio types.
 */
public enum PortfolioType {
  FLEXIBLE("Flexible"),
  INFLEXIBLE("Inflexible");

  private final String value;

  /**
   * Initializes enum values to a string.
   *
   * @param value string value.
   */
  PortfolioType(String value) {
    this.value = value;
  }

  /**
   * Method for getting enum attribute string value.
   *
   * @return enum string value.
   */
  public String getValue() {
    return value;
  }

  /**
   * Static function for converting enum values to a list.
   * @return enum values in a list.
   */
  public static List<String> list() {
    List<String> strValues = new ArrayList<>();
    for (PortfolioType portfolioType : PortfolioType.values()) {
      strValues.add(portfolioType.getValue());
    }
    return strValues;
  }
}
