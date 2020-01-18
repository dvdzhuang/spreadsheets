package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents the sum function.
 */
public class Sum implements SpreadsheetFunc, ValueVisitor<Double> {

  @Override
  public <T> Value apply(List<Value<T>> args) {
    double total = 0.0;
    for (Value<T> a : args) {
      if (a != null) {
        total += a.accept(this);
      }
    }
    return new DValue(total);
  }

  @Override
  public Double visitBValue(boolean b) {
    return 0.0;
  }

  @Override
  public Double visitDValue(double d) {
    return d;
  }

  @Override
  public Double visitSValue(String s) {
    return 0.0;
  }
}
