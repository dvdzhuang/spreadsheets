package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Represents a bar graph that can be displayed by the worksheet view.
 */
public class BarGraph implements GraphType {

  private int max;
  private int increment;
  private List<Integer> values;
  private final ArrayList<Color> colors = new ArrayList<Color>(
          Arrays.asList(Color.red, Color.blue, Color.green, Color.magenta, Color.orange,
                  Color.cyan, Color.pink, Color.yellow));

  @Override
  public void render(List<Coord> region, WorksheetModel<?> model) {
    if (region.size() != 2) {
      throw new IllegalArgumentException("Invalid Region");
    }
    Coord c1 = region.get(0);
    Coord c2 = region.get(1);
    if (c1.col != c2.col - 1) {
      throw new IllegalArgumentException("Invalid Region");
    }
    List<String> headers = new ArrayList<String>();
    this.values = new ArrayList<Integer>();
    this.max = 0;
    for (int i = c1.row; i <= c2.row; i++) {
      headers.add(model.evaluateCell(new Coord(c1.col, i)));
      try {
        int j = (int) Double.parseDouble(model.evaluateCell(new Coord(c2.col, i)));
        if (j < 0) {
          throw new IllegalArgumentException("Negative Numbers not allowed");
        }
        max = Math.max(j, max);
        values.add(j);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid Region");
      }
    }
    increment = Math.max(1, max / 25);
    JTable table = new JTable(27, headers.size() + 1);
    for (int i = 1; i <= headers.size(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(new BarGraphCellRenderer());
      table.setValueAt(headers.get(i - 1), 26, i);
    }
    for (int i = 26; i >= 1; i--) {
      table.setValueAt(increment * (26 - i), i, 0);
    }
    JFrame frame = new JFrame();
    frame.setSize(new Dimension(1000, 500));
    frame.add(table);
    frame.setVisible(true);
  }

  /**
   * A new Table Cell Renderer for rendering a table.
   */
  public class BarGraphCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
      Component cell = super.getTableCellRendererComponent(table, value, isSelected,
              hasFocus, row, column);
      if (row < 26 && column > 0 && row > 25 - values.get(column - 1)) {
        cell.setBackground(colors.get(column % 8));
      } else if (row == 26) {
        cell.setBackground(Color.lightGray);
      } else {
        cell.setBackground(Color.white);
      }
      return cell;
    }
  }
}
