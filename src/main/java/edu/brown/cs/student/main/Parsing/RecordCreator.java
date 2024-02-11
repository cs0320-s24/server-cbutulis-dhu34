package edu.brown.cs.student.main.Parsing;

import edu.brown.cs.student.main.Exceptions.FactoryFailureException;

import java.util.List;

public class RecordCreator implements CreatorFromRow<Record> {
  /**
   * Testing class for custom CreatorFromRow objects
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
