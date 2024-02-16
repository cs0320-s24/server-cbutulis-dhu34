package edu.brown.cs.student.main.parsing;

import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import java.util.List;

/** Class which constructors a record representing a row from a List of Strings. */
public class RecordCreator implements CreatorFromRow<Record> {
  /**
   * Testing class for custom CreatorFromRow objects.
   *
   * @param row representing the row of CSV
   * @return Record representing the row
   * @throws FactoryFailureException thrown when there is an error in creating the row
   */
  @Override
  public Record create(List<String> row) throws FactoryFailureException {
    // the DefaultCreator does nothing
    if (row == null) {
      throw new FactoryFailureException("Given list is null", null);
    }
    return new Row(row);
  }
}
