package edu.cs3500.spreadsheets.model;

import java.util.HashMap;
import java.util.List;

/**
 * Represents the content stored in a cell.
 */
public interface CellContent {
  /**
   * Gets the direct references for CellContent.
   *
   * @param coords reference list for checking for cycles
   * @return a List of Coords representing the references
   */
  List<Coord> getReferences(List<Coord> coords);

  /**
   * Returns the equivalent Value this CellContent evaluates to.
   *
   * @param coords List of Coords for checking for cycles
   * @param cells  the positions of the cells matched with coords
   * @param passed the cells already evaluated
   * @param <T>    the type of value returned
   * @return the value that results from evaluating this
   */
  <T> Value evaluate(List<Coord> coords, HashMap<Coord, Cell> cells, HashMap<Cell, Value> passed);
}
