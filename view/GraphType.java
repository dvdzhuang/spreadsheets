package edu.cs3500.spreadsheets.view;

import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Interface that represents graphs that can be displayed by the worksheet graph view.
 */
public interface GraphType {

  /**
   * Displays the graph.
   *
   * @param region the region graphed
   * @param model  the worksheet model used
   */
  void render(List<Coord> region, WorksheetModel<?> model);
}
