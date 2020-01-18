package edu.cs3500.spreadsheets.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SimpleSpreadsheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetEditableView;
import edu.cs3500.spreadsheets.view.WorksheetPanel;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;

/**
 * The spreadsheet controller class that changes the spreadsheet model based on inputs from the
 * user.
 */
public class SpreadsheetController implements ActionListener {
  private WorksheetModel<?> model;
  private WorksheetEditableView view;
  private Coord coord;
  private final boolean testing;
  private String file;

  /**
   * Basic spreadsheet controller constructor that takes in a model and view.
   *
   * @param model the model to be changed
   * @param view  the view the user sees and enacts their changes on
   */
  public SpreadsheetController(WorksheetModel<?> model, WorksheetEditableView view) {
    this.model = model;
    this.view = view;
    this.testing = false;
    view.setListener(this);
    try {
      view.render();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Made to enable testing.
   */
  public SpreadsheetController(WorksheetModel<?> model,
                               WorksheetEditableView view, Coord coord, String file) {
    this.model = model;
    this.view = view;
    this.testing = true;
    this.coord = coord;
    this.file = file;
    view.setListener(this);
    try {
      view.render();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // only exists to test
    if (!testing) {
      coord = view.getCellSelected();
      file = view.getSaveText();
    }
    if (coord != null) {
      if (e.getActionCommand().equals("Check Button")) {
        try {
          model.setValue(coord, view.getText());
          this.view.repaint();
        } catch (IllegalArgumentException err) {
          view.invalidSexp();
        }
      } else if (e.getActionCommand().equals("Cancel Button")) {
        view.setText(model.getInput(coord));
      }
    }
    if (e.getActionCommand().equals("Save Button")) {
      File saveFile = new File(this.file);
      PrintWriter toFile = null;

      try {
        toFile = new PrintWriter(saveFile);
      } catch (FileNotFoundException ex) {
        ex.printStackTrace();
      }

      WorksheetTextualView view = new WorksheetTextualView(model, toFile);

      try {
        view.render();
        toFile.close();
      } catch (IOException ie) {
        ie.printStackTrace();
      }
    } else if (e.getActionCommand().equals("Load Button")) {
      String fileToLoad = this.view.getLoadText();
      try {
        FileReader f = new FileReader(fileToLoad);
        WorksheetReader.WorksheetBuilder<SimpleSpreadsheet> builder =
                new SimpleSpreadsheet.SimpleSpreadsheetBuilder();
        WorksheetReader reader = new WorksheetReader();
        SimpleSpreadsheet simple = reader.read(builder, f);
        SpreadsheetController s = new SpreadsheetController(simple,
                new WorksheetEditableView(new WorksheetPanel(simple)));
      } catch (FileNotFoundException f) {
        JFrame frame = new JFrame();
        JOptionPane op = new JOptionPane();
        op.showMessageDialog(frame, String.format("%s is not a valid file",
                fileToLoad), "Invalid File", JOptionPane.WARNING_MESSAGE);
      }
    }
  }
}
