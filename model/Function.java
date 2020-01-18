package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a function stored in a cell that operates on other formulas.
 */
public class Function implements Formula {
  private final SpreadsheetFunc func;
  private final List<Formula> args;

  public Function(SpreadsheetFunc func, List<Formula> args) {
    this.func = func;
    this.args = args;
  }

  @Override
  public List<Coord> getReferences(List<Coord> coords) {
    List<Coord> refs = new ArrayList<Coord>();
    for (Formula f : args) {
      List<Coord> fc = f.getReferences(coords);
      for (Coord c : coords) {
        if (fc.contains(c)) {
          StringBuilder error = new StringBuilder("Error in cell ");
          error.append(Coord.colIndexToName(c.col));
          error.append("" + c.row);
          error.append(": Circular reference");
          throw new IllegalArgumentException(error.toString());
        }
      }
      refs.addAll(fc);
    }
    return refs;
  }

  @Override
  public <T> Value evaluate(List<Coord> coords, HashMap<Coord, Cell> cells,
                            HashMap<Cell, Value> passed) {
    List<Value<T>> values = new ArrayList<Value<T>>();
    for (Formula f : args) {
      if (f.isReference() && !f.isColumnReference()) {
        List<Coord> allRefs = f.getReferences(coords);
        for (int i = 0; i < allRefs.size(); i++) {
          if (!passed.containsKey(cells.get(allRefs.get(i)))
                  && cells.get(allRefs.get(i)) != null) {
            Value value = cells.get(allRefs.get(i)).evaluate(coords, cells, passed);
            passed.put(cells.get(allRefs.get(i)), value);
            values.add(value);
          } else {
            values.add(passed.get(cells.get(allRefs.get(i))));
          }
        }
      } else if (f.isColumnReference()) {
        List<Coord> allRefs = new ArrayList<>();
        List<Integer> columns = new ArrayList<>();
        for (int i = 0; i < f.getReferences(coords).size(); i++) {
          if (columns.indexOf(f.getReferences(coords).get(i).col) == -1) {
            columns.add(f.getReferences(coords).get(i).col);
          }
        }
        Set<Coord> allColumns = cells.keySet();
        for (Coord c : allColumns) {
          if (columns.indexOf(c.col) != -1) {
            allRefs.add(c);
          }
        }
        for (int i = 0; i < allRefs.size(); i++) {
          if (!passed.containsKey(cells.get(allRefs.get(i)))
                  && cells.get(allRefs.get(i)) != null) {
            Value value = cells.get(allRefs.get(i)).evaluate(coords, cells, passed);
            passed.put(cells.get(allRefs.get(i)), value);
            values.add(value);
          } else {
            values.add(passed.get(cells.get(allRefs.get(i))));
          }
        }
      }
      else {
        values.add(f.evaluate(coords, cells, passed));
      }
    }
    return func.apply(values);
  }

  @Override
  public boolean isReference() {
    return false;
  }

  @Override
  public boolean isColumnReference() {
    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Function f = (Function) o;
    return func.getClass().equals(f.func.getClass()) && args.equals(f.args);
  }

  @Override
  public int hashCode() {
    return Objects.hash(func, args);
  }
}
