package edu.brown.cs.student.main.parsing;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This is the parser class, which is responsible for converting the CSV into Lists of generic
 * type using a strategy pattern.
 */
public class Parser {
  private final BufferedReader br;
  private final List<String> stringList;
  private final List<List<String>> rowList;
  private final List<Object> parsedList;
  private final CreatorFromRow<?> rowCreator;
  private final List<Object> malformedList;

  /**
   * Parser class takes in a raw CSV and converts it into a List of Strings by reading it in
   * and processing it.
   *
   * @param read reader which the user passes in for reading the CSV
   */
  public Parser(Reader read) {
    this.br = new BufferedReader(read);
    this.stringList = new ArrayList<>();
    this.rowCreator = new DefaultCreator();
    this.parsedList = new ArrayList<>();
    this.rowList = new ArrayList<>();
    this.malformedList = new ArrayList<>();
  }

  /**
   * Alternative constructor for the Parser which also takes in a custom CreatorFromRow which
   * defines an alternative way of packaging rows into data structures.
   *
   * @param read Reader class which the user inputs to read the raw CSV
   * @param creatorFromRow implements CreatorFromRow which packages a row of the CSV into a data
   *                       structure
   */
  public Parser(Reader read, CreatorFromRow creatorFromRow) {
    this.br = new BufferedReader(read);
    this.stringList = new ArrayList<>();
    this.rowCreator = creatorFromRow; // this diff
    this.parsedList = new ArrayList<>();
    this.rowList = new ArrayList<>();
    this.malformedList = new ArrayList<>();
  }

  /**
   * Taken from the TestRegex class in the class repo, takes a line and splits and postprocesses it
   * for the purposes of parsing a line into a row.
   */
  private static class Processor {
    static final Pattern regexSplitCSVRow =
        Pattern.compile(",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))");

    /**
     * Eliminate a single instance of leading or trailing double-quote, and replace pairs of double
     * quotes with singles.
     *
     * @param arg the string to process
     * @return the postprocessed string
     */
    public static String postprocess(String arg) {
      return arg
          // Remove extra spaces at beginning and end of the line
          .trim()
          // Remove a beginning quote, if present
          .replaceAll("^\"", "")
          // Remove an ending quote, if present
          .replaceAll("\"$", "")
          // Replace double-double-quotes with double-quotes
          .replaceAll("\"\"", "\"");
    }
  }

  /**
   * Takes a given line of the CSV and processes it into a row data object, returning a list of
   * those row objects.
   *
   * @param hasHeader boolean specifying whether the CSV has headers
   * @return List of row data objects
   * @throws IOException exception thrown when there is an issue with input
   * @throws FactoryFailureException exception thrown when there is an issue with making the row
   *         objects
   */
  public List parse(boolean hasHeader) throws IOException, FactoryFailureException {
    // reads every line in the csv
    String line;
    do {
      line = this.br.readLine();
      if (line != null) {
        this.stringList.add(line);
      }
    } while (line != null);
    this.br.close();

    // process each line into Lists of strings, then add to parsedList
    int numCols = 0;
    for (String s : this.stringList) {
      List<String> row = parseRow(s);
      if (row.size() > numCols) {
        numCols = row.size();
      }
      this.rowList.add(row);
    }

    if (hasHeader) {
      try {
        numCols = this.rowList.get(0).size();
      } catch (IndexOutOfBoundsException e) {

        throw new IllegalArgumentException();
      }
    }
    for (List<String> strings : this.rowList) {
      if (strings.size() == numCols) {
        this.parsedList.add(this.rowCreator.create(strings));
      } else {
        this.malformedList.add(this.rowCreator.create(strings));
      }
    }

    return this.parsedList;
  }

  /**
   * Helper method for parsing a lien into a row data object.
   *
   * @param line String representing a line of the CSV
   * @return List of Strings representing the row CSV
   */
  private List<String> parseRow(String line) {
    String[] row = Processor.regexSplitCSVRow.split(line);
    for (int i = 0; i < row.length; i++) {
      row[i] = Processor.postprocess(row[i]);
    }
    return new ArrayList<>(Arrays.asList(row));
  }

  /**
   * Getter method for getting the list of malformed rows.
   *
   * @return list of Objects representing the list of malformed rows
   */
  public List<Object> getMalformedList() {
    return this.malformedList;
  }
}
