package edu.cs3500.spreadsheets.model;

/**
 * A class for representing formulas stored in cells.
 */
public interface Formula extends CellContent {
  /**
   * Checks if the formula is a reference.
   *
   * @return true if this is a reference, false otherwise
   */
  boolean isReference();

  boolean isColumnReference();
}
