package model.strategy;

import java.util.List;
import java.util.Map;
import model.CompanyStock;

/**
 * This class represents an investment strategy.
 */
public interface Strategy {
  /**
   * Method for applying an investment strategy using the given data.
   * @param data  data such as symbols, weights, frequency etc. required to apply the strategy.
   * @return list of company stocks bough using this investment strategy.
   */
  List<CompanyStock> apply(Map<String, Object> data);
}
