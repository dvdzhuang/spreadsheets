package edu.cs3500.spreadsheets.model;


import java.util.List;

/**
 * Represents a spreadsheet.
 *
 * @param <K> the type of cell
 */
public interface WorksheetModel<K> {

  /**
   * Gets the cell at the coordinate.
   *
   * @param coord Coordinate of the cell
   * @return The cell at the coordinate
   */
  Cell getCell(Coord coord);

  /**
   * Sets the value of the cell at the given coordinate.
   *
   * @param coord   Coordinate of the cell
   * @param content String representing the value the cell will be set to
   */
  void setValue(Coord coord, String content);

  /**
   * Checks if a cell's inputs are valid.
   *
   * @param cell The cell being checked
   * @return true if cell is valid, false otherwise
   * @throws IllegalArgumentException if the value self-references
   */
  boolean checkValid(Cell cell) throws IllegalArgumentException;

  /**
   * Evaluates the cell and gets the result as a String.
   *
   * @param c the position cell being evaluated
   * @return the result
   */
  String evaluateCell(Coord c);

  /**
   * Returns all coords that are accounted for by the model.
   *
   * @return a list of coord
   */
  List<Coord> getCoords();

  /**
   * Gets the original user input at a specific coord.
   *
   * @param c the coord of the input
   * @return the String input
   */
  String getInput(Coord c);
}
