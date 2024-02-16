package edu.brown.cs.student.main.datasource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/** Mock Datasource class which is for testing without sending copious API queries */
public class MockDataSource implements Datasource {

  private String californiaQuery;

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
   * @throws URISyntaxException - Exception thrown when there is a syntactical error in the URI
   * @throws IOException - Exception thrown when there is an error with inputs to builders
   * @throws InterruptedException - Exception thrown when method is interrupted
   */
  public String sendRequest(List<String> params)
      throws URISyntaxException, IOException, InterruptedException {
    return this.californiaQuery;
  }
}
