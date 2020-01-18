package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * This is the class that contains the constructor and all the methods for the spreadsheet view that
 * the user can edit.
 */
public class WorksheetEditableView extends JFrame implements WorksheetView {
  private final WorksheetPanel panel;
  protected JTextField text;
  private final JButton check;
  private final JButton cancel;
  private final JButton save;
  private JTextField saveText;
  private final JButton load;
  protected JMenuBar menu;

  /**
   * This constructs a view that the user can edit.
   */
  public WorksheetEditableView(WorksheetPanel panel) {
    this.panel = panel;
    this.text = panel.getTextField();
    text.setPreferredSize(new Dimension(600, 24));
    this.check = new JButton("✔");
    this.check.setActionCommand("Check Button");
    this.cancel = new JButton("✘");
    this.cancel.setActionCommand("Cancel Button");
    this.save = new JButton("Save");
    this.save.setActionCommand("Save Button");
    this.saveText = new JTextField();
    saveText.setMaximumSize(new Dimension(200, 24));
    saveText.setPreferredSize(saveText.getMaximumSize());
    this.load = new JButton("Load");
    this.load.setActionCommand("Load Button");
    this.setSize(1400, 600);
    JScrollPane pane = new JScrollPane(panel);
    this.menu = new JMenuBar();
    menu.add(check);
    menu.add(cancel);
    menu.add(text);
    menu.add(save);
    menu.add(load);
    menu.add(saveText);
    pane.setColumnHeaderView(menu);
    this.add(pane);
  }

  @Override
  public void render() throws IOException {
    this.setVisible(true);
  }

  public void setText(String text) {
    this.text.setText(text);
  }

  public String getText() {
    return text.getText();
  }

  public String getSaveText() {
    return saveText.getText();
  }

  public String getLoadText() {
    return saveText.getText();
  }

  public Coord getCellSelected() {
    return panel.getCellSelected();
  }

  /**
   * This method handles the error that is thrown when the user inputs an invalid s-expression into
   * the input bar.
   */
  public void invalidSexp() {
    JFrame f = new JFrame();
    JOptionPane op = new JOptionPane();
    op.showMessageDialog(f, String.format("%s is not a valid input", text.getText()),
            "Invalid S-expression", JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Adds listeners to all of the buttons in order to use them.
   *
   * @param al the ActionLister being added
   */
  public void setListener(ActionListener al) {
    this.check.addActionListener(al);
    this.cancel.addActionListener(al);
    this.save.addActionListener(al);
    this.load.addActionListener(al);
  }

}
