package edu.brown.cs.student.main.exceptions;

public class DuplicateHeaderException extends Exception {

  /**
   * Exception thrown when there are two headers of the same title in a CSV.
   *
   * @param message message which contains information about the error, that there are duplicate
   *                header titles
   */
  public DuplicateHeaderException(String message) {
    super(message);
  }
}
