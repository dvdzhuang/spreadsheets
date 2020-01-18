package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Represents a view that allows users to graph a selection of cells.
 */
public class WorksheetGraphView extends WorksheetEditableView {

  private JButton graph;
  private JTextField text;
  private final WorksheetModel<?> model;

  /**
   * This constructs a view that the user can edit and create graphs with.
   *
   * @param panel the panel used to display the worksheet
   */
  public WorksheetGraphView(WorksheetPanel panel, WorksheetModel<?> model) {
    super(panel);
    this.model = model;
    super.text.setPreferredSize(new Dimension(400, 24));
    graph = new JButton("Graph");
    graph.setActionCommand("Graph Button");
    super.menu.add(graph);
    text = new JTextField();
    super.menu.add(text);
  }

  /**
   * Gets the region that is to be graphed.
   *
   * @param input the user input
   * @return the top left and bottom right coords as a list
   */
  public List<Coord> getRegion(String input) {
    StringBuilder coordr1 = new StringBuilder();
    StringBuilder coordc1 = new StringBuilder();
    StringBuilder coordr2 = new StringBuilder();
    StringBuilder coordc2 = new StringBuilder();
    boolean col1 = false;
    boolean row1 = false;
    boolean range = false;
    boolean col2 = false;
    boolean row2 = false;
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c >= 65 && c <= 90) {
        if (!row1) {
          col1 = true;
          coordc1.append(c);
        } else if (range && !row2) {
          col2 = true;
          coordc2.append(c);
        } else {
          throw new IllegalArgumentException("Invalid region");
        }
      } else if (c >= 48 && c <= 57) {
        if (col1 && !range) {
          row1 = true;
          coordr1.append(c);
        } else if (col2) {
          row2 = true;
          coordr2.append(c);
        } else {
          throw new IllegalArgumentException("Invalid region");
        }
      } else if (c == ':' && !range) {
        range = true;
      } else {
        throw new IllegalArgumentException("Invalid region");
      }
    }
    if (col1 && col2 && row1 && row2) {
      int c1 = Coord.colNameToIndex(coordc1.toString());
      int r1 = Integer.parseInt(coordr1.toString());
      int c2 = Coord.colNameToIndex(coordc2.toString());
      int r2 = Integer.parseInt(coordr2.toString());
      if (c1 > c2 || r1 > r2) {
        throw new IllegalArgumentException("Invalid region");
      }
      List<Coord> coords = new ArrayList<Coord>();
      coords.add(new Coord(c1, r1));
      coords.add(new Coord(c2, r2));
      return coords;
    } else {
      throw new IllegalArgumentException("Invalid region");
    }
  }

  /**
   * Displays and error message when the inputted region is invalid.
   */
  public void invalidRegion() {
    JFrame f = new JFrame();
    JOptionPane op = new JOptionPane();
    op.showMessageDialog(f, String.format("%s is not a valid region", text.getText()),
            "Invalid Region", JOptionPane.WARNING_MESSAGE);
  }

  /**
   * Renders the graph for the data.
   */
  public void graph() {
    try {
      new BarGraph().render(this.getRegion(text.getText()), model);
    } catch (IllegalArgumentException e) {
      invalidRegion();
    }
  }

  @Override
  public void setListener(ActionListener al) {
    graph.addActionListener(al);
    super.setListener(al);
  }
}
