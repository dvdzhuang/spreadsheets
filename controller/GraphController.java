package edu.cs3500.spreadsheets.controller;

import java.awt.event.ActionEvent;

import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.view.WorksheetGraphView;

/**
 * A new controller for the graph extra credit. Adds functionality to a graph button.
 */
public class GraphController extends SpreadsheetController {

  private WorksheetGraphView gview;

  public GraphController(WorksheetModel<?> model, WorksheetGraphView view) {
    super(model, view);
    this.gview = view;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Graph Button")) {
      gview.graph();
    } else {
      super.actionPerformed(e);
    }
  }
}
