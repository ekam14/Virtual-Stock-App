package view;

import controller.Features;

/**
 * This interface represents operations specific to GUI.
 */
public interface ViewComponentGUI {
  /**
   * Method for adding feature methods defined in controller
   * to view component.
   *
   * @param features list of feature methods.
   */
  void addFeatures(Features features);
}
