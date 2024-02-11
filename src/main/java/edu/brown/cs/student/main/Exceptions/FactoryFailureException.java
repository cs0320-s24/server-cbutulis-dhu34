package edu.brown.cs.student.main.Exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 * Feel free to expand or supplement or use it for other purposes.
 */
public class FactoryFailureException extends Exception {
  final List<String> row;

  /**
   * Exception thrown when there is an error in creating an object from a row
   *
   * @param message message containing information about the exception
   * @param row the row that was being turned into a row data object
   */
  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }
}
