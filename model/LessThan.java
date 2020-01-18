package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a function that determines if the first value is less than the second.
 */
public class LessThan implements SpreadsheetFunc, ValueVisitor<Double> {
  @Override
  public <T> Value apply(List<Value<T>> args) {
    if (args.size() != 2) {
      throw new IllegalArgumentException("Must have 2 arguments");
    }
    return new BValue(args.get(0).accept(this) < args.get(1).accept(this));
  }

  @Override
  public Double visitBValue(boolean b) {
    throw new IllegalArgumentException("Must use number for this operation");
  }

  @Override
  public Double visitDValue(double d) {
    return d;
  }

  @Override
  public Double visitSValue(String s) {
    throw new IllegalArgumentException("Must use number for this operation");
  }
}
