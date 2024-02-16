package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler class for the searchcsv endpoint.
 */
public class SearchHandler extends Handler implements Route {

  private final CSVHandler csvHandler;

  /**
   * Constructor uses a CSVHandler object to get necessary data.
   * @param handler the handler for csv parser
   */
  public SearchHandler(CSVHandler handler) {
    this.csvHandler = handler;
  }

  /**
   * Method which handles the user's request and is called when the API endpoint is accessed. This
   * takes in the user's request, parses out its parameters, and then passes them in to instantiate
   * a parser.
   *
   * @param request  - The user's query request
   * @param response - The response to the user's query
   * @return - the responseMap, a Map between strings and objects containing the API's response
   */
  @Override
  public Object handle(Request request, Response response) {
    // Get Query parameters, can be used to make your search more specific
    String target = request.queryParams("target");
    String header = request.queryParams("header");
    String col = request.queryParams("col");
    System.out.println(target);

    // Initialize a map for our informative response.
    Map<String, Object> responseMap = new HashMap<>();

    if(this.csvHandler.getCsv() == null) {
      return new LoadFailureResponse("No CSV has been loaded").serialize();
    }
    // Put the result in the response map
    if(header == null && col == null) {
      responseMap.put("result", this.csvHandler.getCsv().getTarget(target));
    }
    else if(header != null && col == null) {
      responseMap.put("result", this.csvHandler.getCsv().getTarget(target, header));
    }
    else if(header == null) {
      responseMap.put("result", this.csvHandler.getCsv().getTarget(target, col));
    }

    System.out.println(responseMap);

    return new LoadSuccessResponse(responseMap).serialize();
  }
}
