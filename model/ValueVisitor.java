package edu.cs3500.spreadsheets.model;

/**
 * Visitor pattern for values.
 *
 * @param <R> the return type of the visitor
 */
public interface ValueVisitor<R> {
  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  R visitBValue(boolean b);

  /**
   * Process a double value.
   *
   * @param d the value
   * @return the desired result
   */
  R visitDValue(double d);

  /**
   * Process a String value.
   *
   * @param s the value
   * @return the desired result
   */
  R visitSValue(String s);
}
