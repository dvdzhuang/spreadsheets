package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Represents a spreadsheet function.
 */
public interface SpreadsheetFunc {
  /**
   * Applies the function to arguments.
   *
   * @param args the value arguments being operated on
   * @param <T>  the type of value returned
   * @return the result after operating the function
   */
  <T> Value apply(List<Value<T>> args);
}
