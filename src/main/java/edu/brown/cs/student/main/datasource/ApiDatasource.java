package edu.brown.cs.student.main.datasource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/** Datasource class which is responsible for handling sending requests to query APIs. */
public class ApiDatasource implements Datasource {

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
    // 0 = county, 1 = state
    String county = params.get(0);
    String state = params.get(1);

    HttpRequest censusRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=S2802_C03_022E&for=county:"
                        + county
                        + "&in=state:"
                        + state))
            .GET()
            .build();
    // Send that API request then store the response in this variable
    HttpResponse<String> sentCensusRequest =
        HttpClient.newBuilder().build().send(censusRequest, HttpResponse.BodyHandlers.ofString());
    System.out.println(sentCensusRequest.body());
    return sentCensusRequest.body();
  }
}
