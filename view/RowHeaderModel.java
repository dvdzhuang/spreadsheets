package edu.cs3500.spreadsheets.view;

import javax.swing.table.DefaultTableModel;

/** Class that is used to create the header for the rows in the spreadsheet views. The cells
 * in this class cannot be edited or changed by the user. */
public class RowHeaderModel extends DefaultTableModel {
  private int rows;

  public RowHeaderModel(int rows) {
    this.rows = rows;
  }

  public RowHeaderModel(DefaultTableModel model) {
    this.rows = model.getRowCount();
  }

  @Override
  public int getRowCount() {
    return rows;
  }

  @Override
  public int getColumnCount() {
    return 1;
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return rowIndex + 1;
  }

  public DefaultTableModel toDefaultTable() {
    return new DefaultTableModel(this.rows, 1);
  }
}
