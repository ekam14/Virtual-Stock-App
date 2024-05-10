package view;

import java.io.IOException;
import java.util.Objects;

import javax.swing.JLabel;

import controller.Features;
import model.enums.TransactionType;

import static java.lang.Integer.parseInt;

/**
 * This class represents the create portfolio operation frame and its related operations.
 */
public class CreatePortfolioImpl extends AbstractPortfolioHelper implements CreatePortfolio,
        ViewComponentGUI {
  private final JLabel portfolioName;
  private TransactionType type;

  /**
   * Constructs a create portfolio frame object in view component and initializes frame values.
   * @param caption Title of the frame.
   */
  public CreatePortfolioImpl(String caption) {
    super(caption);
    portfolioName = new JLabel();
    type = TransactionType.BUY;
  }

  @Override
  public void createPortfolio(String portfolioName, TransactionType type) throws IOException {
    this.portfolioName.setText(portfolioName);
    this.type = type;

    if (Objects.equals(portfolioName, "")) {
      enterNumberOfStocksToEnter();
    } else {
      getEnteredStockDetailsMessage(1);
    }
  }

  @Override
  public void getEnteredStockDetailsMessage(int stockNumber) throws IOException {
    enterStocksDetails(stockNumber);
  }

  @Override
  public void enterNumberOfStocksToEnter() throws IOException {
    enterStocksNum();
  }

  protected void resetComponents() {
    super.resetComponents();
  }

  @Override
  public void addFeatures(Features features) {
    linkFeatures(features);

    qnty.addActionListener(event -> {
      boolean ok = features.checkQnty(qnty.getText());

      if (!ok) {
        qnty.setText("");
        invalidQntyMessage.setText("Entered quantity is invalid.");
      } else if (this.type == TransactionType.SELL) {
        invalidQntyMessage.setText(features.checkSellQnty(qnty.getText(), date.getText(),
                portfolioName.getText()));
      }

      if (ok && (Objects.equals(invalidQntyMessage.getText(), "ok")
              || this.type == TransactionType.BUY )) {
        enterQnty.setText(String.format("Transaction quantity: %s", qnty.getText()));
        invalidQntyMessage.setText("");
      } else {
        qnty.setText("");
        if (Objects.equals(invalidQntyMessage.getText(), "There are no shares of this company "
                + "in the portfolio for this date.")) {
          this.setVisible(false);

          resetComponents();
          resetFrame();

          try {
            features.modifyPortfolio();
            return;
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }

      resetFrame();
      enterStocksDetails(this.stockNumber);
    });

    buy.addActionListener(event -> {
      try {
        features.saveStock(qnty.getText(), fees.getText(), this.type);

        resetFrame();

        if (Objects.equals(numStocks.getText(), "")
                || this.stockNumber == parseInt(numStocks.getText())) {
          numStocks.setText("");
          this.stockNumber = 1;
          this.setVisible(false);
          resetComponents();
          features.savePortfolio(this.portfolioName.getText());
        } else {
          resetComponents();
          enterStocksDetails(++this.stockNumber);
          return;
        }

        features.goMainPage();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
