package edu.cs3500.spreadsheets.view;

import java.io.IOException;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;

/**
 * Class for representing a textual view of model for saving to file. Represents each cell in the
 * model as text as a coordinate followed by an s-expression formula. The data is stored in the
 * appendable field.
 */
public class WorksheetTextualView implements WorksheetView {
  private WorksheetModel<?> model;
  private Appendable ap;

  public WorksheetTextualView(WorksheetModel model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Coord c : model.getCoords()) {
      s.append(String.format("%s %s\n", c.toString(), model.getInput(c)));
    }
    return s.toString();
  }

  @Override
  public void render() throws IOException {
    ap.append(this.toString());
  }
}
