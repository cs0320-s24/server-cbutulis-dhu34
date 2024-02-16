package edu.brown.cs.student.main.datasource;

import java.util.List;

/** Mock Datasource class which is for testing without sending copious API queries. */
public class MockDataSource implements Datasource {

  private final String californiaQuery;

  public MockDataSource() {
    this.californiaQuery = "[[\"NAME\",\"S2802_C03_022E\",\"state\",\"county\"],\n"
        + "[\"Orange County, California\",\"93.0\",\"06\",\"059\"]]";
  }

  /**
   * Method to send a request to a given API. Packages a list of parameters into an API request,
   * sends the request, and then returns the results of the request.
   *
   * @param params - List of Strings representing the parameters of the query
   * @return String representation of the result of the API query
   */
  public String sendRequest(List<String> params) {
    return this.californiaQuery;
  }
}
