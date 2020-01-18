package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.sexp.Sexp;
import edu.cs3500.spreadsheets.sexp.SexpVisitor;

/**
 * A Visitor that converts Sexp to a function to be used.
 */
public class SexpFuncVisitor implements SexpVisitor<SpreadsheetFunc> {

  @Override
  public SpreadsheetFunc visitBoolean(boolean b) {
    throw new IllegalArgumentException("Not a function");
  }

  @Override
  public SpreadsheetFunc visitNumber(double d) {
    throw new IllegalArgumentException("Not a function");
  }

  @Override
  public SpreadsheetFunc visitSList(List<Sexp> l) {
    throw new IllegalArgumentException("Not a function");
  }

  @Override
  public SpreadsheetFunc visitSymbol(String s) {
    switch (s) {
      case "SUM":
        return new Sum();
      case "PRODUCT":
        return new Product();
      case "<":
        return new LessThan();
      case "SAPPEND":
        return new StringAppend();
      default:
        throw new IllegalArgumentException("Not a function call");
    }
  }

  @Override
  public SpreadsheetFunc visitString(String s) {
    throw new IllegalArgumentException("Not a function");
  }
}
