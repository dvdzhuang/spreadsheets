package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import javax.swing.JFrame;

/**
 * Class for showing the spreadsheet using Java Swing. The view takes in a Worksheet Panel, which
 * contains the data from the model in a JTable. This class then renders the view.
 */
public class WorksheetVisualView extends JFrame implements WorksheetView {

  public WorksheetVisualView(WorksheetPanel panel) {
    this.setSize(1000, 600);
    this.add(panel);
  }

  @Override
  public void render() throws IOException {
    this.setVisible(true);
  }
}
