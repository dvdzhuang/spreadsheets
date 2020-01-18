package edu.cs3500.spreadsheets.model;

/**
 * Represents a value stored in a cell.
 *
 * @param <R> the type of value
 */
public interface Value<R> extends Formula {
  /**
   * Gets the value stored as a primitive or String.
   *
   * @return the value represented by the Value
   */
  R getValue();

  /**
   * Acceptor for visitor pattern.
   *
   * @param v   the visitor being used
   * @param <K> the return type of the visitor
   * @return the result of accepting the visitor
   */
  <K> K accept(ValueVisitor<K> v);
}
