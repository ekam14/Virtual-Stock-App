package controller;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import model.ModelComponent;

/**
 * This class represents all the operations possible when
 * upload portfolio command is invoked from the Controller.
 */
public class UploadPortfolio implements ControllerCommand {
  private final ModelComponent modelComponent;
  private final Scanner scan;
  private final Appendable out;
  private final IOMethods IOObj;

  /**
   * Constructs a UploadPortfolio object and initializes all
   * the class attributes.
   * @param attributesObj Class Attribute object.
   */
  UploadPortfolio(ClassAttributes attributesObj) {
    Objects.requireNonNull(attributesObj.getModelComponent());
    this.out = attributesObj.getAppendable();
    this.scan = attributesObj.getScanner();
    this.modelComponent = attributesObj.getModelComponent();
    this.IOObj = new IOMethods(attributesObj);
  }

  @Override
  public void followCommand() throws IOException {
    String path = "";
    boolean isInValid = true;
    do {
      this.out.append("Type M to go to main page.\n");
      this.out.append("Enter file path:");
      path = scan.nextLine();
      if (Objects.equals(path, "M")) {
        return;
      }
      if (modelComponent.validatePath(path)) {
        isInValid = false;
      } else {
        this.out.append("Entered path is invalid. Try Again.\n\n");
      }
    }
    while (isInValid);

    String fileType = IOObj.enterFileType();

    String importFileName = modelComponent.importPortfolio(fileType, path);

    if (importFileName == null) {
      this.out.append("File contains invalid data. Please verify file content.\n");
    } else {
      this.out.append("File is successfully imported by name:").append(importFileName).append("\n");
    }
  }
}