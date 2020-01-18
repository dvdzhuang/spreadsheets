package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A Value class for representing boolean values.
 */
public class BValue implements Value<Boolean> {
  private final boolean value;

  public BValue(boolean value) {
    this.value = value;
  }

  @Override
  public Boolean getValue() {
    boolean b = value;
    return b;
  }

  @Override
  public <K> K accept(ValueVisitor<K> v) {
    return v.visitBValue(this.getValue());
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
    if (value) {
      return "true";
    }
    return "false";
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
    BValue b = (BValue)  o;
    return this.value == b.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
