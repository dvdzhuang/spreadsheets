package edu.cs3500.spreadsheets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import edu.cs3500.spreadsheets.controller.GraphController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SimpleSpreadsheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.WorksheetGraphView;
import edu.cs3500.spreadsheets.view.WorksheetPanel;
import edu.cs3500.spreadsheets.view.WorksheetTextualView;
import edu.cs3500.spreadsheets.view.WorksheetVisualView;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    /*
      TODO: For now, look in the args array to obtain a filename and a cell name,
      - read the file and build a model from it,
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */
    if (args[0].equals("-in")) {
      try {
        FileReader f = new FileReader(args[1]);
        WorksheetReader.WorksheetBuilder<SimpleSpreadsheet> builder =
                new SimpleSpreadsheet.SimpleSpreadsheetBuilder();
        WorksheetReader reader = new WorksheetReader();
        SimpleSpreadsheet simple = reader.read(builder, f);
        switch (args[2]) {
          case "-eval":
            Coord coord = stringToCoord(args[3]);
            try {
              String v = simple.evaluateCell(coord);
              System.out.print(v);
            } catch (IllegalArgumentException e) {
              e.printStackTrace();
            }
            break;
          case "-edit":
            GraphController s = new GraphController(simple,
                    new WorksheetGraphView(new WorksheetPanel(simple), simple));
            break;
          case "-gui":
            try {
              new WorksheetVisualView(new WorksheetPanel(simple)).render();
            } catch (IOException e) {
              e.printStackTrace();
            }
            break;
          case "-save":
            String fileName = args[3];
            File newFile = new File(fileName);
            PrintWriter toFile = new PrintWriter(newFile);
            WorksheetTextualView view = new WorksheetTextualView(simple, toFile);
            try {
              view.render();
              toFile.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
            break;
          default:
            throw new IllegalArgumentException("Must enter a valid command");
        }
      } catch (FileNotFoundException e) {
        throw new IllegalArgumentException("File not found");
      }
    } else if (args[0].equals("-gui")) {
      WorksheetModel simple = new SimpleSpreadsheet(new HashMap<>());
      try {
        new WorksheetVisualView(new WorksheetPanel(simple)).render();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (args[0].equals("-edit")) {
      WorksheetModel simple = new SimpleSpreadsheet(new HashMap<>());
      GraphController con = new GraphController(simple,
              new WorksheetGraphView(new WorksheetPanel(simple), simple));
    } else {
      throw new IllegalArgumentException("Must enter a valid command");
    }
  }

  static Coord stringToCoord(String s) {
    StringBuilder coordr1 = new StringBuilder();
    StringBuilder coordc1 = new StringBuilder();
    boolean col1 = false;
    boolean row1 = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c >= 65 && c <= 90) {
        if (!row1) {
          col1 = true;
          coordc1.append(c);
        } else {
          throw new IllegalArgumentException("Symbol must be a reference or function");
        }
      } else if (c >= 48 && c <= 57) {
        if (col1) {
          row1 = true;
          coordr1.append(c);
        } else {
          throw new IllegalArgumentException("Symbol must be a reference or function");
        }
      } else {
        throw new IllegalArgumentException("Symbol must be a reference or function");
      }
    }
    return new Coord(Coord.colNameToIndex(coordc1.toString()),
            Integer.parseInt(coordr1.toString()));
  }
}
