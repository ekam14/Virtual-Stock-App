import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Scanner;

import controller.ControllerGUI;
import controller.ControllerImpl;
import model.APIRequestsImpl;
import model.ModelComponentImpl;
import model.PortfolioRepositoryImpl;
import view.JFrameView;
import view.ViewComponentImpl;

/**
 * Main class of the Stock application. It allows to create and retrieve a stock portfolio.
 * Moreover, it creates the controller object along with passing Model and view component with it.
 * After controller object creation it gives control to it by calling go method on it.
 */
public class Stock {

  /**
   * Main class of the application.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    try {
      Scanner scan = new Scanner(System.in);

      System.out.print("Default view is Text based, enter 1 for GUI: ");
      String viewType = scan.next();

      if (Objects.equals(viewType, "1")) {
        new ControllerGUI(new ModelComponentImpl(System.out, new PortfolioRepositoryImpl(),
            new APIRequestsImpl()), new JFrameView("Stock Application"));
      } else {
        new ControllerImpl(new InputStreamReader(System.in), System.out,
            new ModelComponentImpl(System.out, new PortfolioRepositoryImpl(),
                new APIRequestsImpl()), new ViewComponentImpl(System.out)).start();

      }
    } catch (IOException e) {
      // pass;
    }
  }
}
