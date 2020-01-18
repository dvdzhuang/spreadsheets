package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * Represents a simple spreadsheet.
 */
public class SimpleSpreadsheet implements edu.cs3500.spreadsheets.model.WorksheetModel<Cell> {

  HashMap<Coord, Cell> cells;

  public SimpleSpreadsheet(HashMap<Coord, Cell> cells) {
    this.cells = cells;
  }

  /**
   * Builder for SimpleSpreadsheet.
   */
  public static class SimpleSpreadsheetBuilder
          implements WorksheetReader.WorksheetBuilder<SimpleSpreadsheet> {
    private HashMap<Coord, Cell> cells = new HashMap<Coord, Cell>();

    @Override
    public WorksheetReader.WorksheetBuilder<SimpleSpreadsheet> createCell(int col, int row,
                                                                          String contents) {
      Coord c = new Coord(col, row);
      Sexp sexp;
      if (contents.charAt(0) == '=') {
        sexp = Parser.parse(contents.substring(1));
      } else {
        sexp = Parser.parse(contents);
      }
      CellContent cc = sexp.accept(new SexpToCellContent());
      cells.put(c, new Cell(c, cc, contents));
      return this;
    }

    @Override
    public SimpleSpreadsheet createWorksheet() {
      return new SimpleSpreadsheet(cells);
    }
  }

  @Override
  public Cell getCell(Coord coord) {
    if (cells.containsKey(coord)) {
      return cells.get(coord);
    } else {
      return null;
    }
  }

  @Override
  public void setValue(Coord coord, String content) {
    Sexp sexp;
    if (content.equals("")) {
      cells.remove(coord);
    } else {
      if (content.charAt(0) == '=') {
        sexp = Parser.parse(content.substring(1));
      } else {
        sexp = Parser.parse(content);
      }
      CellContent cc = sexp.accept(new SexpToCellContent());
      cells.put(coord, new Cell(coord, cc, content));
    }
  }

  @Override
  public boolean checkValid(Cell cell) throws IllegalArgumentException {
    List<Coord> reached = new ArrayList<Coord>();
    return checkValidHelp(cell, reached);
  }

  boolean checkValidHelp(Cell cell, List<Coord> reached) {
    if (reached.contains(cell.getCoord())) {
      return false;
    }
    try {
      for (Coord ref : cell.getReferences()) {
        List<Coord> c = new ArrayList<Coord>();
        c.add(cell.getCoord());
        c.addAll(reached);
        if (!checkValidHelp(cells.get(ref), c)) {
          return false;
        }
      }
    } catch (IllegalArgumentException e) {
      return false;
    }
    return true;
  }

  @Override
  public String evaluateCell(Coord c) {
    if (cells.containsKey(c)) {
      Value value = cells.get(c).evaluate(new ArrayList<Coord>(), cells,
              new HashMap<Cell, Value>());
      if (value == null) {
        return "";
      }
      return value.toString();
    } else {
      return "";
    }
  }

  @Override
  public List<Coord> getCoords() {
    ArrayList<Coord> coords = new ArrayList<Coord>();
    coords.addAll(cells.keySet());
    return coords;
  }

  @Override
  public String getInput(Coord c) {
    if (cells.containsKey(c)) {
      return cells.get(c).getInput();
    } else {
      return "";
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
    SimpleSpreadsheet ss = (SimpleSpreadsheet) o;
    try {
      for (Coord c : ss.getCoords()) {
        if (!this.getCell(c).equals(ss.getCell(c))) {
          return false;
        }
      }
      for (Coord c : this.getCoords()) {
        if (!ss.getCell(c).equals(this.getCell(c))) {
          return false;
        }
      }
    } catch (NullPointerException e) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cells);
  }
}
