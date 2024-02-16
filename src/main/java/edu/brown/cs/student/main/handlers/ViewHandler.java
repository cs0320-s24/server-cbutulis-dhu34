package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler class for the viewhandler endpoint
 */
public class ViewHandler extends Handler implements Route {
  private final CSVHandler csvHandler;

  /**
   * Constructor uses a CSVHandler object to get necessary data.
   * @param handler
   */
  public ViewHandler(CSVHandler handler) {
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
    // Initialize a map for our informative response.
    Map<String, Object> responseMap = new HashMap<>();

    if(this.csvHandler.getCsv() == null) {
      return new LoadFailureResponse("No File Loaded").serialize();
    }
    responseMap.put("result", this.csvHandler.getCsv().toString());
    System.out.println(responseMap);
    return new CSVHandler.LoadSuccessResponse(responseMap).serialize();
  }
}
