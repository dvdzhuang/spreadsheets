package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represents a reference to another cell stored in a cell.
 */
public class Reference implements Formula {
  private List<Coord> region;
  private final boolean columnRef;

  public Reference(List<Coord> region) {
    this.region = region;
    this.columnRef = false;
  }

  /**
   * A constructor for Column References.
   *
   * @param column1 the start column
   * @param column2 the end column
   */
  public Reference(int column1, int column2) {
    List<Coord> tempReg = new ArrayList<>();
    for (int i = column1; i <= column2; i++) {
      tempReg.add(new Coord(i, 1));
    }
    this.region = tempReg;
    this.columnRef = true;
  }

  public boolean contains(Coord c) {
    return region.contains(c);
  }

  @Override
  public List<Coord> getReferences(List<Coord> coords) {
    for (Coord c : coords) {
      if (region.contains(c) || columnRefCircular(c)) {
        StringBuilder error = new StringBuilder("Error in cell ");
        error.append(Coord.colIndexToName(c.col));
        error.append("" + c.row);
        error.append(": Circular reference");
        throw new IllegalArgumentException(error.toString());
      }
    }
    return region;
  }

  private boolean columnRefCircular(Coord c) {
    if (this.columnRef) {
      List<Integer> allCols = new ArrayList<>();
      for (int i = 0; i < this.region.size(); i++) {
        Integer current = this.region.get(i).col;
        if (allCols.indexOf(current) == -1) {
          allCols.add(current);
        }
      }
      if (allCols.indexOf(c.col) != -1) {
        return true;
      }
    }
    return false;
  }

  @Override
  public <T> Value evaluate(List<Coord> coords, HashMap<Coord, Cell> cells,
                            HashMap<Cell, Value> passed) {
    Coord ref = region.get(0);
    for (Coord c : coords) {
      if (c.equals(ref) || columnRefCircular(c)) {
        StringBuilder error = new StringBuilder("Error in cell ");
        error.append(Coord.colIndexToName(c.col));
        error.append("" + c.row);
        error.append(": Circular reference");
        throw new IllegalArgumentException(error.toString());
      }
    }
    if (cells.containsKey(ref)) {
      if (!passed.containsKey(ref)) {
        return cells.get(ref).evaluate(coords, cells, passed);
      } else {
        return passed.get(ref);
      }
    } else {
      return null;
    }
  }

  @Override
  public boolean isReference() {
    return true;
  }

  @Override
  public boolean isColumnReference() {
    return this.columnRef;
  }

  @Override
  public String toString() {
    if (region.size() == 1) {
      return region.get(0).toString();
    } else {
      return region.get(0).toString() + ":" + region.get(region.size() - 1).toString();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Reference r = (Reference) o;
    return region.equals(r.region);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region);
  }
}
