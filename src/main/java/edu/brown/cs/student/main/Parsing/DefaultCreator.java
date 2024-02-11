package edu.brown.cs.student.main.Parsing;

import edu.brown.cs.student.main.Exceptions.FactoryFailureException;

import java.util.List;

public class DefaultCreator implements CreatorFromRow<List<String>> {
  /**
   * Class which represents the default creator for a row which implements the CreatorFromRow class.
   *
   * @param row the row to create a row object from
   * @return a List of String representing the row
   * @throws FactoryFailureException exception thrown when there is an error with creating a row
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    // the DefaultCreator does nothing
    if (row == null) {
      throw new FactoryFailureException("Given list is null", null);
    }
    return row;
  }
}
