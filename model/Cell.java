package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A Class for representing a Cell in a spreadsheet.
 */
public class Cell {

  private final Coord coord;
  private CellContent value;
  private String input;

  /**
   * Constructor for Cell that takes in coord, value, and input.
   *
   * @param coord the Coord of the cell
   * @param value the value stored in the cell
   * @param input the original input for the content
   */
  public Cell(Coord coord, CellContent value, String input) {
    this.coord = coord;
    this.value = value;
    this.input = input;
  }

  public Cell(Coord coord) {
    this.coord = coord;
  }

  public CellContent getValue() {
    return this.value;
  }

  public Coord getCoord() {
    return this.coord;
  }

  public void setValue(CellContent value) {
    this.value = value;
  }

  /**
   * Gets the direct references contains in the CellContent stored.
   *
   * @return A List of Coords representing the references
   */
  public List<Coord> getReferences() {
    List<Coord> c = new ArrayList<Coord>();
    c.add(coord);
    return value.getReferences(c);
  }

  /**
   * Evaluates the exact Value that the CellContent is equal to.
   *
   * @param cells  the cells in the spreadsheet
   * @param passed the values that have already been calculated
   * @return the value of this cell
   */
  public Value evaluate(List<Coord> coords, HashMap<Coord, Cell> cells,
                        HashMap<Cell, Value> passed) {
    if (value == null) {
      return null;
    } else if (coords.contains(coord)) {
      StringBuilder error = new StringBuilder("Error in cell ");
      error.append(Coord.colIndexToName(coord.col));
      error.append("" + coord.row);
      error.append(": Circular reference");
      throw new IllegalArgumentException(error.toString());
    }
    if (!passed.containsKey(this)) {
      List<Coord> newCoords = new ArrayList<Coord>(Arrays.asList(coord));
      newCoords.addAll(coords);
      return this.value.evaluate(newCoords, cells, passed);
    } else {
      return passed.get(this);
    }
  }

  public String getInput() {
    String input = this.input;
    return input;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cell c = (Cell) o;
    return coord.equals(c.coord) && value.equals(c.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coord, value, input);
  }
}
