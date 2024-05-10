package controller;

import java.io.IOException;

/**
 * This interface represents the Controller of the
 * application.
 */
public interface Controller {
  /**
   * Main method of the controller which starts the
   * stock application and takes in various user inputs.
   *
   * @throws IOException for error in input and output source.
   */
  void start() throws IOException;
}