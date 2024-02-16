package edu.brown.cs.student.main.localuser;

import edu.brown.cs.student.main.csvdatastorage.CSVData;
import edu.brown.cs.student.main.exceptions.DuplicateHeaderException;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.parsing.DefaultCreator;
import edu.brown.cs.student.main.parsing.Parser;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * REPL which the user can use to load and parse the CSVs, print them, and search them for specific
 * targets.
 */
public class Repl {

  private CSVData csv;
  private boolean loaded;

  /** REPL constructor. */
  public Repl() {}

  /**
   * Method which runs the basic functionality of the REPL, querying the user for input, in a
   * continuous while loop. The user can load a CSV with a file path, print it out, and query it for
   * targets.
   *
   * @throws IOException exception thrown when there is an input error
   */
  public void run() throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line;
    label:
    while (true) {
      System.out.print("$: ");

      line = br.readLine();
      String[] args = line.split(" ");
      String command = args[0];

      switch (command) {
        case "exit":
          break label;
        case "load":
          this.load(args);
          break;
        case "print":
          if (!this.loaded) {
            System.out.println("No CSV loaded!");
          } else {
            System.out.println(this.csv.toString());
          }
          break;
        case "search":
          if (!this.loaded) {
            System.out.println("No CSV loaded!");
          } else if (args.length >= 2) {
            // make it into three args
            this.search(line);
          } else {
            System.out.println("Usage: search [target] [optional: | + column index/header]");
          }
          break;
        default:
          System.out.println("Unrecognized command");
          break;
      }
    }
  }

  /**
   * Helper method for querying the CSVData object for a particular target, called when the user
   * types the search command in the REPL.
   *
   * @param line the line that represents the user's input
   */
  private void search(String line) {

    if (line.contains("|")) {
      // search using the col values or headers, user must have said "true" when loading
      String[] args = line.split(" \\| ");
      String firstArgs = args[0];
      String[] parsedFirstArgs = firstArgs.split(" ");

      try {
        String searchPrint = this.csv.getTarget(parsedFirstArgs[1], args[1]);
        if (searchPrint.isEmpty()) {
          System.out.println("Target not found!");
        } else {
          System.out.println(searchPrint);
        }
      } catch (NullPointerException e) {
        System.out.println("Header not found");
      }
    } else {
      // search the whole thing
      String[] args = line.split(" ");
      String searchPrint = this.csv.getTarget(args[1]);
      if (searchPrint.isEmpty()) {
        System.out.println("Target not found!");
      } else {
        System.out.println(searchPrint);
      }
    }
  }

  /**
   * Helper method for loading and parsing the CSV, called when the user types the load command in
   * the REPL.
   *
   * @param args String[] representing the user's command line input
   */
  private void load(String[] args) {
    if (args.length == 3) {
      // check that it's in the data folder and not do anything suspicious
      if (args[1].startsWith("data/") && !args[1].contains("../")) {
        try {
          Parser parser = new Parser(new FileReader(args[1]), new DefaultCreator());
          boolean header = args[2].equals("true"); // makes header into the user's input
          try {
            this.csv = new CSVData(parser.parse(header), header);
            System.out.println("CSV parsed!");
            this.loaded = true;
            int malformedListSize = parser.getMalformedList().size();
            if (malformedListSize != 0) {
              System.out.println("Warning, following rows malformed: " + parser.getMalformedList());
            }
          } catch (IOException | FactoryFailureException | DuplicateHeaderException e) {
            System.out.println(e.getMessage());
          } catch (IllegalArgumentException e) {
            System.out.println("Tried to make headers out of empty CSV!");
          }
        } catch (FileNotFoundException e) {
          System.out.println(e.getMessage());
        }
      } else {
        System.out.println("Error: potential access of data outside protected directory");
      }
    } else {
      System.out.println("Usage: load [csv file path] [optional header: true/false]");
    }
  }
}
