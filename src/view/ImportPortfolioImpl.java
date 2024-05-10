package view;

import java.awt.HeadlessException;
import java.awt.FlowLayout;
import java.awt.Container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;

import controller.Features;

/**
 * This class represents the import portfolio operation frame and its related operations.
 */
public class ImportPortfolioImpl extends AbstractPortfolioHelper implements
        ImportPortfolio, ViewComponentGUI {
  private List<String> options;
  private final JTextField filePath;
  private final JTextField fileType;
  private final JButton goMainPage;
  private final JButton importButton;
  private final JPanel buttonPanel;
  private final JLabel comment;

  /**
   * Constructs an import portfolio frame object in view component and initializes frame values.
   *
   * @param caption Title of the frame.
   */
  public ImportPortfolioImpl(String caption) throws HeadlessException {
    super(caption);

    options = new ArrayList<>();

    setSize(1000, 1000);
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    filePath = new JTextField(20);

    fileType = new JTextField(20);

    goMainPage = new JButton("Go to main page");
    importButton = new JButton("Import Portfolio");

    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    buttonPanel.add(importButton);
    buttonPanel.add(goMainPage);

    comment = new JLabel();
  }

  /**
   * Helper method to reset class component values.
   */
  private void initComponents() {
    filePath.setText("");
    fileType.setText("");
    comment.setText("");
  }

  @Override
  public void showFilePath() throws IOException {
    // pass
  }

  @Override
  public void showFileTypeOption(List<String> options) throws IOException {
    this.options = options;

    Container pane = this.getContentPane();
    pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

    pane.add(getHeading("Enter File Path:"));
    pane.add(filePath);

    pane.add(getHeading(String.format("Enter file type (%s/%s): ",
            options.get(0), options.get(1))));
    pane.add(fileType);

    pane.add(getHeading(comment.getText()));
    this.add(buttonPanel);

    pack();
    setVisible(true);
  }

  @Override
  public void showFileTypeError() throws IOException {
    // pass;
  }

  @Override
  public void showMessage(HashMap<String, String> errors) throws IOException {
    initComponents();
    resetFrame();

    if (Objects.equals(errors.get("error"), "")) {
      goMainPage.doClick();
      return;
    }

    comment.setText(errors.get("error"));
    showFileTypeOption(this.options);
  }

  @Override
  public void addFeatures(Features features) {
    importButton.addActionListener(event -> {
      try {
        features.importPortfolio(filePath.getText(), fileType.getText());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    goMainPage.addActionListener(event -> {
      initComponents();
      resetFrame();
      this.setVisible(false);
      features.goMainPage();
    });
  }
}
