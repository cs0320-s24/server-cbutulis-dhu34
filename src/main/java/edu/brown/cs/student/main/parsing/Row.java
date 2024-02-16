package edu.brown.cs.student.main.parsing;

import java.util.List;

/**
 * Record representing Row.
 *
 * @param content the list of strings comprising the row
 */
public record Row(List<String> content) {

  /**
   * Wrapper for the List of Strings representing the row.
   *
   * @param content List of Strings representing the row
   */
  public Row {
  }
}
