package model.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.CompanyStock;
import model.CompanyStockImpl;
import model.dto.Pair;
import model.enums.TransactionType;

/**
 * This class represents the dollar cost averaging strategy.
 */
public class DollarCostAveraging implements Strategy {
  @Override
  public List<CompanyStock> apply(Map<String, Object> data) {
    Map<String, Pair> stocksDetails = (Map<String, Pair>) data.get("companyDetails");
    Double amount = Double.parseDouble((String) data.get("amount"));
    Double commissionFeePercent = Double.parseDouble((String) data.get("commissionFee"));
    Double fees = amount * commissionFeePercent * 0.01;
    amount = amount - fees;
    List<CompanyStock> list = new ArrayList<>();
    for (String companySymbol : stocksDetails.keySet()) {
      ArrayList<Pair> priceRange = (ArrayList<Pair>) data.get(companySymbol + "_priceList");
      Pair details = stocksDetails.get(companySymbol);
      Double x = details.getValue();
      x = x / 100.0;
      BigDecimal cost = new BigDecimal(amount).multiply(new BigDecimal(x));
      BigDecimal commissionCost = new BigDecimal(fees).multiply(new BigDecimal(x));
      for (Pair datePrice : priceRange) {
        BigDecimal numStocks = cost.divide(new BigDecimal(datePrice.getValue()), 2,
                RoundingMode.DOWN);
        Double quantity = numStocks.doubleValue();
        CompanyStock companyStock = new CompanyStockImpl(datePrice.getKey(),
                details.getKey(), companySymbol,
                quantity,
                datePrice.getValue(),
                cost.doubleValue(),
                TransactionType.BUY, commissionCost.doubleValue());
        list.add(companyStock);
      }
    }
    return list;
  }
}
