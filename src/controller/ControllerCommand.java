package controller;

import java.io.IOException;

/**
 * This interface represents all operations the Controller
 * of the application can perform.
 */
public interface ControllerCommand {
  /**
   * Method for performing a specific command.
   * @throws IOException for error in input and output source.
   */
  void followCommand() throws IOException;
}
