package edu.cs3500.spreadsheets.view;

import java.io.IOException;

/**
 * Interface for the view of the spreadsheet.
 */
public interface WorksheetView {
  /**
   * Renders the view of the spreadsheet.
   *
   * @throws IOException if append throws IOException
   */
  void render() throws IOException;
}
