package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a function that finds the product of all the inputs.
 */
public class Product implements SpreadsheetFunc, ValueVisitor<Double> {
  @Override
  public <T> Value apply(List<Value<T>> args) {
    boolean containsNum = false;
    double result = 1;
    for (Value<T> a : args) {
      if (a != null && a.accept(this) != null) {
        containsNum = true;
        result *= a.accept(this);
      }
    }
    if (!containsNum) {
      return new DValue(0.0);
    }
    return new DValue(result);
  }

  @Override
  public Double visitBValue(boolean b) {
    return null;
  }

  @Override
  public Double visitDValue(double d) {
    return d;
  }

  @Override
  public Double visitSValue(String s) {
    return null;
  }
}
