package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * A Visitor that converts Sexp to CellContent that can be stored in a cell.
 */
public class SexpToCellContent implements SexpVisitor<Formula> {

  @Override
  public Formula visitBoolean(boolean b) {
    return new BValue(b);
  }

  @Override
  public Formula visitNumber(double d) {
    return new DValue(d);
  }

  @Override
  public Formula visitSList(List<Sexp> l) {
    SpreadsheetFunc func = l.get(0).accept(new SexpFuncVisitor());
    List<Formula> formulas = new ArrayList<Formula>();
    for (int i = 1; i < l.size(); i++) {
      formulas.add(l.get(i).accept(this));
    }
    return new Function(func, formulas);
  }

  @Override
  public Formula visitSymbol(String s) {
    StringBuilder coordr1 = new StringBuilder();
    StringBuilder coordc1 = new StringBuilder();
    StringBuilder coordr2 = new StringBuilder();
    StringBuilder coordc2 = new StringBuilder();
    boolean col1 = false;
    boolean row1 = false;
    boolean range = false;
    boolean col2 = false;
    boolean row2 = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c >= 65 && c <= 90) {
        if (!row1) {
          col1 = true;
          coordc1.append(c);
        } else if (range && !row2) {
          col2 = true;
          coordc2.append(c);
        } else {
          throw new IllegalArgumentException("Symbol must be a reference or function");
        }
      } else if (c >= 48 && c <= 57) {
        if (col1 && !range) {
          row1 = true;
          coordr1.append(c);
        } else if (col2) {
          row2 = true;
          coordr2.append(c);
        } else {
          throw new IllegalArgumentException("Symbol must be a reference or function");
        }
      } else if (c == ':' && !range) {
        range = true;
      } else {
        throw new IllegalArgumentException("Symbol must be a reference or function");
      }
    }
    List<Coord> coords = new ArrayList<Coord>();
    try {
      if (range) {
        int c1 = Coord.colNameToIndex(coordc1.toString());
        int c2 = Coord.colNameToIndex(coordc2.toString());
        int r1 = Integer.parseInt(coordr1.toString());
        int r2 = Integer.parseInt(coordr2.toString());
        if (c1 > c2 || r1 > r2) {
          throw new IllegalArgumentException("Symbol must be a reference or function");
        }
        for (int i = c1; i <= c2; i++) {
          for (int j = r1; j <= r2; j++) {
            coords.add(new Coord(i, j));
          }
        }
      } else {
        coords.add(new Coord(Coord.colNameToIndex(coordc1.toString()),
                Integer.parseInt(coordr1.toString())));
      }
    } catch (NumberFormatException e) {
      char firstCol = coordc1.charAt(0);
      StringBuilder firstColumn = new StringBuilder();
      firstColumn.append(firstCol);

      char secondCol = coordc1.charAt(1);
      StringBuilder secondColumn = new StringBuilder();
      secondColumn.append(secondCol);

      int c1 = Coord.colNameToIndex(firstColumn.toString());
      int c2 = Coord.colNameToIndex(secondColumn.toString());
      return new Reference(c1, c2);
    }
    return new Reference(coords);
  }

  @Override
  public Formula visitString(String s) {
    return new SValue(s);
  }


}
