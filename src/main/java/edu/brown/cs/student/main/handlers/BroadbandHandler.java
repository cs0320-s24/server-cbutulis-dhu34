package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.datasource.CachedDatasource;
import edu.brown.cs.student.main.datasource.Datasource;
import edu.brown.cs.student.main.server.CensusAPIUtilities;
import edu.brown.cs.student.main.server.Init;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** Handler class from the broadband endpoint. */
public class BroadbandHandler extends Handler implements Route {

  private HashMap<String, String> stateCodes;
  private HashMap<String, String> countyCodes;
  private final CachedDatasource datasource;
  private String errorMsg;

  /** Default constructor for the BroadbandHandler. Takes no arguments. */
  public BroadbandHandler(Datasource datasource) {
    this.datasource = new CachedDatasource(datasource);
    try {
      this.stateCodes = Init.getStateCodes();
      this.countyCodes = Init.getCountyCodes();
    } catch (URISyntaxException | IOException | InterruptedException e) {
      this.errorMsg = e.getMessage();
    }
  }

  /**
   * Method which handles the user's request and is called when the API endpoint is accessed. This
   * takes in the user's request, parses out its parameters, and then passes the params forward to
   * the Datasource class to get back a response from the API. It gets a JSON back from Datasource,
   * deserializes it to format the JSON, puts it in a response map, then appends a time stamp onto
   * the response map. This map of the responses is then returned.
   *
   * @param request - The user's query request
   * @param response - The response to the user's query
   * @return - the responseMap, a Map between strings and objects containing the API's response
   */
  @Override
  public Object handle(Request request, Response response) {

    // Check if the initial queries of state and county codes generated an error
    if (this.errorMsg != null) {
      return new LoadFailureResponse("error_bad_json");
    }

    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    String stateCode = this.stateCodes.get(stateName);
    String countyCode = this.countyCodes.get(countyName);

    Map<String, Object> responseMap = new HashMap<>();
    try {
      ArrayList<String> params = new ArrayList<>();
      params.add(countyCode);
      params.add(stateCode);
      String broadbandJson = this.datasource.sendRequest(params);

      List<List<String>> result = CensusAPIUtilities.deserializeBroadbandInfo(broadbandJson);
      result.get(0).set(0, "broadband access");
      result.get(1).set(0, result.get(1).get(0) + "%");
      result.get(1).set(1, stateName);
      result.get(1).set(2, countyName);

      responseMap.put("result", result);

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();

      responseMap.put("timestamp", dtf.format(now));
      return new LoadSuccessResponse(responseMap).serialize();
    } catch (Exception e) {
      return new LoadFailureResponse("error_bad_request").serialize();
    }
  }
}
