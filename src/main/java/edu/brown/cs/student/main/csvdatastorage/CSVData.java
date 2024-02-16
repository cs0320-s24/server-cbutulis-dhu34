package edu.brown.cs.student.main.csvdatastorage;

import edu.brown.cs.student.main.exceptions.DuplicateHeaderException;
import java.util.HashMap;
import java.util.List;

public class CSVData {

  private final List<List<String>> data;
  private HashMap<String, Integer> headers;
  private int numCols;

  /**
   * This class is a wrapper for the raw List that represents the CSV. It contains a List of the
   * data in the CSV and a Hashmap which maps headers to their column indices. It also contains a
   * field which represents the number of columns in the CSV.
   *
   * @param rawData a List of Lists of Strings representing the raw parsed CSV
   * @param header  a boolean which specifies whether the CSV has headers
   * @throws DuplicateHeaderException is an exception that gets thrown when there are multiples
   *                                  headers of the same name
   */
  public CSVData(List rawData, boolean header) throws DuplicateHeaderException {
    // assumption that all objects are strings, if using the REPL they're using default
    // functionality
    this.data = rawData;
    if (header) {
      this.headers = new HashMap<>();
      this.populateHeaderHashmap();
    }

    // this calculates number of columns in csv
    if (!header) {
      for (int i = 1; i < this.data.size(); i++) {
        if (this.data.get(i).size() > this.numCols) {
          this.numCols = this.data.get(i).size();
        }
      }
    } else {
      this.numCols = this.data.get(0).size();
    }
  }

  /**
   * Method called in the constructor which populates the Hashmap that maps header names to their
   * column indices.
   *
   * @throws DuplicateHeaderException is an exception that gets thrown when there are multiple
   *                                  headers of the same name
   */
  private void populateHeaderHashmap() throws DuplicateHeaderException {
    for (int i = 0; i < this.data.get(0).size(); i++) {
      if (this.headers.containsKey(this.data.get(0).get(i))) {
        throw new DuplicateHeaderException("CSV has a duplicate header");
      }
      this.headers.put(this.data.get(0).get(i), i);
    }
  }

  /**
   * Search method which takes a String target, which represents the target of the user's query.
   * This method returns a String which represents the whole row in which the target was found.
   *
   * @param target - a String representing the element the user is searching for
   * @return a String representing the row in which the target was found
   */
  public String getTarget(String target) {
    // search through the whole thing, no args
    String searchReturn = "";
    for (int i = 0; i < this.data.size(); i++) {
      for (int j = 0; j < this.numCols; j++) {
        if (this.data.get(i).get(j).equals(target)) {
          searchReturn += "Row " + i + ": " + this.data.get(i).toString() + "\n";
        }
      }
    }
    return searchReturn;
  }

  /**
   * Overloaded method for getTarget, same functionality as the basic target, where the user queries
   * for a target and a String representing the whole row in which the target is found is returned,
   * except the user can specify a column in which to look for the target.
   *
   * @param target - String representing the target the user is looking for
   * @param col    - String representing the column index or header title the user is narrowing the
   *               search to
   * @return String representing the whole row in which the element was found
   */
  public String getTarget(String target, String col) {
    String searchReturn = "";
    int targetCol;
    // either will try to convert to number or try header
    try {
      targetCol = Integer.parseInt(col);
    } catch (NumberFormatException e) {
      // this will throw a null pointer if user inputted false on loading
      targetCol = this.headers.get(col);
    }
    for (int i = 0; i < this.data.size(); i++) {
      if (this.data.get(i).get(targetCol).equals(target)) {
        searchReturn += "Row " + i + ": " + this.data.get(i).toString() + "\n";
      }
    }
    return searchReturn;
  }

  /**
   * Override of the default toString() method. Formats the CSV printout into rows
   *
   * @return String representing the CSV file
   */
  @Override
  public String toString() {
    String formattedCSV = "";
    for (List<String> datum : this.data) {
      formattedCSV += datum + "\n";
    }
    return formattedCSV;
  }
}
