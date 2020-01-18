package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A Value class for String values.
 */
public class SValue implements Value<String> {
  private final String value;

  public SValue(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    String s = value;
    return s;
  }

  @Override
  public <K> K accept(ValueVisitor<K> v) {
    return v.visitSValue(this.getValue());
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
    return String.format("\"%s\"", value);
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
    SValue s = (SValue) o;
    return value.equals(s.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
