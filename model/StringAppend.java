package edu.cs3500.spreadsheets.model;

import java.util.List;

/**
 * Function that appends all string arguments. Other types are ignored. Default case is empty
 * String.
 */
public class StringAppend implements SpreadsheetFunc, ValueVisitor<String> {

  @Override
  public <T> Value apply(List<Value<T>> args) {
    StringBuilder result = new StringBuilder();
    for (Value a : args) {
      if (a != null) {
        result.append(a.accept(this));
      }
    }
    return new SValue(result.toString());
  }

  @Override
  public String visitBValue(boolean b) {
    return "";
  }

  @Override
  public String visitDValue(double d) {
    return "";
  }

  @Override
  public String visitSValue(String s) {
    return s;
  }
}
