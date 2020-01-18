package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A Value class for representing double values.
 */
public class DValue extends Object implements Value<Double> {
  private final double value;

  public DValue(double value) {
    this.value = value;
  }

  @Override
  public Double getValue() {
    double d = value;
    return d;
  }

  @Override
  public <K> K accept(ValueVisitor<K> v) {
    return v.visitDValue(this.getValue());
  }

  @Override
  public List<Coord> getReferences(List<Coord> coords) {
    return new ArrayList<Coord>();
  }

  @Override
  public <T> Value evaluate(List<Coord> coords, HashMap<Coord, Cell> cells,
                            HashMap<Cell, Value> passed) {
    return this;
  }

  @Override
  public String toString() {
    return String.format("%f", value);
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
    DValue d = (DValue) o;
    return Math.abs(this.value - d.value) < 0.00001
            && Math.abs(this.value - d.value) >= 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
