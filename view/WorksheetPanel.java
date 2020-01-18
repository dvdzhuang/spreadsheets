package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * The class for a panel that is made up of the table that the user sees, the scrollbars, the
 * headers, and the mouse listener that determines what cell the user clicks on.
 */
public class WorksheetPanel extends JScrollPane {
  private final WorksheetModel<?> model;
  private JTable table;
  private TableMouseListener tml;

  /**
   * Constructor that creates the appropriate panel for the given model.
   */
  public WorksheetPanel(WorksheetModel<?> model) {
    this.model = model;
    Coord max = getBottomRight(model);
    this.table = new JTable(new WorksheetTableModel(max.row, max.col));
    tml = new TableMouseListener(table);
    table.addMouseListener(tml);
    table.setGridColor(Color.BLACK);
    JTable rowHeader = new JTable(new RowHeaderModel(max.row));
    rowHeader.setBackground(Color.cyan);
    this.setViewportView(table);
    JScrollBar vertical = this.createVerticalScrollBar();
    JScrollBar horizontal = this.createHorizontalScrollBar();
    this.setVerticalScrollBar(vertical);
    this.setHorizontalScrollBar(horizontal);

    final int[] rowCounter = {max.row};
    final int[] rowDirection = {vertical.getValue()};

    vertical.addAdjustmentListener(new AdjustmentListener() {
      @Override
      public void adjustmentValueChanged(AdjustmentEvent e) {
        int addTo = vertical.getModel().getExtent();
        if (vertical.getValue() + addTo >= vertical.getMaximum()) {

          vertical.setMaximum(vertical.getMaximum() + 100);

          WorksheetTableModel wTable = (WorksheetTableModel) table.getModel();
          DefaultTableModel dTable = wTable.toDefaultTable();
          dTable.addRow(new Object[]{});
          table.setModel(new WorksheetTableModel(dTable));

          RowHeaderModel hTable = (RowHeaderModel) rowHeader.getModel();
          DefaultTableModel hDTable = hTable.toDefaultTable();
          hDTable.addRow(new Object[]{rowCounter[0]});
          rowCounter[0]++;
          rowHeader.setModel(new RowHeaderModel(hDTable));
        }
      }
    });

    horizontal.addAdjustmentListener(new AdjustmentListener() {
      @Override
      public void adjustmentValueChanged(AdjustmentEvent e) {
        int addTo = horizontal.getModel().getExtent();
        if (horizontal.getValue() + addTo >= horizontal.getMaximum()) {

          horizontal.setMaximum(horizontal.getMaximum() + 100);

          WorksheetTableModel wTable = (WorksheetTableModel) table.getModel();
          DefaultTableModel dTable = wTable.toDefaultTable();
          dTable.addColumn(new Object[]{});
          table.setModel(new WorksheetTableModel(dTable));
        }
      }
    });

    vertical.setVisible(true);
    horizontal.setVisible(true);
    table.setAutoResizeMode(0);
    this.setRowHeaderView(rowHeader);
    this.getRowHeader().setPreferredSize(
            new Dimension(50, this.getRowHeader().getViewSize().height));
  }

  /**
   * Exists solely for testing purposes.
   */
  public JTable getTable() {
    return table;
  }

  private class WorksheetTableModel extends DefaultTableModel {
    int row;
    int col;

    WorksheetTableModel(int row, int col) {
      this.row = row;
      this.col = col;
    }

    WorksheetTableModel(DefaultTableModel model) {
      this.row = model.getRowCount();
      this.col = model.getColumnCount();
    }

    @Override
    public int getRowCount() {
      return row;
    }

    @Override
    public int getColumnCount() {
      return col;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      try {
        return model.evaluateCell(new Coord(columnIndex + 1, rowIndex + 1));
      } catch (IllegalArgumentException e) {
        return "Error in value: cycle found";
      }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
      try {
        model.setValue(new Coord(column + 1, row + 1), aValue.toString());
      } catch (IllegalArgumentException e) {
        JFrame f = new JFrame();
        JOptionPane op = new JOptionPane();
        op.showMessageDialog(f, String.format("%s is not a valid input", aValue.toString()),
                "Invalid S-expression", JOptionPane.WARNING_MESSAGE);
      }
    }

    public DefaultTableModel toDefaultTable() {
      return new DefaultTableModel(this.row, this.col);
    }
  }

  private class TableMouseListener implements MouseListener {
    JTable table;
    JTextField text;
    Point p;

    public TableMouseListener(JTable table) {
      this.table = table;
      this.text = new JTextField();
      this.p = new Point(0, 0);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      p.setLocation(table.rowAtPoint(e.getPoint()) + 1,
              table.columnAtPoint(e.getPoint()) + 1);
      text.setText(getInput());
    }

    @Override
    public void mousePressed(MouseEvent e) {
      //not needed for this implementation.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      //not needed for this implementation.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      //not needed for this implementation.
    }

    @Override
    public void mouseExited(MouseEvent e) {
      //not needed for this implementation.
    }

    public String getInput() {
      return model.getInput(new Coord(p.y, p.x));
    }

    public JTextField getInputField() {
      if (this.text == null) {
        return new JTextField();
      }
      return this.text;
    }

    public Coord getCellSelected() {
      if (p.x > 0 && p.y > 0) {
        return new Coord(p.y, p.x);
      } else {
        return null;
      }
    }
  }

  /**
   * Gets the bottom-right-most cell of the model. Default size is 100 by 100.
   *
   * @return the bottom-right most cell or the default
   */
  Coord getBottomRight(WorksheetModel<?> model) {
    int maxCol = 100;
    int maxRow = 100;
    for (Coord c : model.getCoords()) {
      maxCol = Math.max(c.col, maxCol);
      maxRow = Math.max(c.row, maxRow);
    }
    return new Coord(maxCol, maxRow);
  }

  public JTextField getTextField() {
    return tml.getInputField();
  }

  public Coord getCellSelected() {
    return tml.getCellSelected();
  }
}
