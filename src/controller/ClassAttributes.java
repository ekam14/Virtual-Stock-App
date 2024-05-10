package controller;

import java.util.Objects;
import java.util.Scanner;

import model.ModelComponent;
import view.ViewComponent;

/**
 * This class represents common class attributes inside the Controller package.
 * It contains objects of the model component, view component, scanner and
 * appendable output.
 */
public class ClassAttributes {
  private final ModelComponent modelComponent;
  private final ViewComponent viewComponent;
  private final Scanner scan;
  private final Appendable out;

  /**
   * Constructs a ClassAttributes object and initializes all the class attributes.
   * @param in             input source.
   * @param out            output source.
   * @param modelComponent ModelComponent object.
   * @param viewComponent  ModelComponent object.
   */
  public ClassAttributes(Readable in, Appendable out, ModelComponent modelComponent,
                        ViewComponent viewComponent) {
    Objects.requireNonNull(modelComponent);
    Objects.requireNonNull(viewComponent);
    this.out = out;
    scan = new Scanner(in);
    this.modelComponent = modelComponent;
    this.viewComponent = viewComponent;
  }

  /**
   * Helper method to get the model component object.
   * @return model component object.
   */
  public ModelComponent getModelComponent() {
    return this.modelComponent;
  }

  /**
   * Helper method to get the view component object.
   * @return view component object.
   */
  public ViewComponent getViewComponent() {
    return this.viewComponent;
  }

  /**
   * Helper method to get the scanner object.
   * @return scanner object.
   */
  public Scanner getScanner() {
    return this.scan;
  }

  /**
   * Helper method to get the appendable object.
   * @return appendable object.
   */
  public Appendable getAppendable() {
    return out;
  }
}
