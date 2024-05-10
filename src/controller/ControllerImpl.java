package controller;

import java.io.IOException;
import java.util.Objects;

import model.ModelComponent;
import view.ViewComponent;

import static java.lang.Integer.parseInt;

/**
 * This class represents the controller of the application. It contains modelComponent and
 * viewComponent objects as class attributes as it directly communicates with them for different
 * application features. It also supports taking in various compatible input and output sources.
 */
public class ControllerImpl implements Controller {

  private final ClassAttributes attributesObj;

  /**
   * Constructs a ControllerImpl object and initializes all the class attributes.
   *
   * @param in             input source.
   * @param out            output source.
   * @param modelComponent ModelComponent object.
   * @param viewComponent  ModelComponent object.
   * @throws IOException for error in input and output source.
   */
  public ControllerImpl(Readable in, Appendable out, ModelComponent modelComponent,
      ViewComponent viewComponent) throws IOException {
    attributesObj = new ClassAttributes(in, out, modelComponent, viewComponent);
  }

  @Override
  public void start() throws IOException {
    final String[] setupOptions = {"1. Create portfolio.", "2. Show portfolios.",
        "3. Import portfolio.", "4. Modify portfolios.",
        "Enter q or quit to quit the application."};

    while (true) {
      attributesObj.getViewComponent().setUp(setupOptions);
      boolean stopExecution = false;
      String selection = "";
      while (true) {
        this.attributesObj.getAppendable().append("Enter option: ");
        selection = attributesObj.getScanner().nextLine();

        if (Objects.equals(selection, "q") || Objects.equals(selection.toLowerCase(), "quit")) {
          stopExecution = true;
          break;
        } else if (!attributesObj.getModelComponent().checkNumber(selection)
            || parseInt(selection) > 4) {
          attributesObj.getViewComponent().printWrongInputMessage();
        } else {
          break;
        }
      }

      if (stopExecution) {
        attributesObj.getViewComponent().printStopExecutionMessage();
        return;
      }

      switch (selection) {
        case "1":
          (new CreatePortfolio(attributesObj)).followCommand();
          break;
        case "2":
          (new ShowPortfolio(attributesObj)).followCommand();
          break;
        case "3":
          (new UploadPortfolio(attributesObj)).followCommand();
          break;
        case "4":
          (new ModifyPortfolio(attributesObj)).followCommand();
          break;
        default:
          break;
      }
    }
  }
}
