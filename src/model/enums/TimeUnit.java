package model.enums;

/**
 * This enum represents possible timeunits.
 */
public enum TimeUnit {
  Yearly("Yearly"),
  Monthly("Monthly"),
  Daily("Daily");


  private final String value;

  /**
   * Initializes enum values to a string.
   *
   * @param value string value.
   */
  TimeUnit(String value) {
    this.value = value;
  }

  /**
   * Static function for converting enum values to a list.
   * @param val String value of the enum variable.
   * @return enum values in a list.
   */
  public static TimeUnit getTimeUnit(String val) {
    for (TimeUnit timeUnit : TimeUnit.values()) {
      if (timeUnit.value.equals(val)) {
        return timeUnit;
      }
    }
    return null;
  }
}
