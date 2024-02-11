package edu.brown.cs.student.main.Parsing;

import java.util.List;

public record Row(List<String> content) {
  /**
   * Wrapper for the List of Strings representing the row
   * @param content List of Strings representing the row
   */
  public Row(List<String> content) {
    this.content = content;
  }
}
