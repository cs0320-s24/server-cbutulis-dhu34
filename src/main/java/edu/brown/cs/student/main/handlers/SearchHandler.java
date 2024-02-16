package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler class for the soup ordering API endpoint.
 *
 * <p>This endpoint is similar to the endpoint(s) you'll need to create for Sprint 2. It takes a
 * basic GET request with no Json body, and returns a Json object in reply. The responses are more
 * complex, but this should serve as a reference.
 */
public class SearchHandler implements Route {

  private final CSVHandler csvHandler;

  /**
   * Constructor accepts some shared state.
   */
  public SearchHandler(CSVHandler handler) {
    this.csvHandler = handler;
  }

  /**
   * Pick a convenient soup and make it. the most "convenient" soup is the first recipe we find in
   * the unordered set of recipe cards.
   *
   * <p>NOTE: beware this "return Object" and "throws Exception" idiom. We need to follow it
   * because
   * the library uses it, but in general this lowers the protection of the type system.
   *
   * @param request  the request to handle
   * @param response use to modify properties of the response
   * @return response content
   */
  @Override
  public Object handle(Request request, Response response) {
    // TODO 2: Right now, we only serialize the first soup, let's make it so you can choose which
    // one you want!
    // Get Query parameters, can be used to make your search more specific
    String target = request.queryParams("target");
    System.out.println(target);
    // Initialize a map for our informative response.
    Map<String, Object> responseMap = new HashMap<>();
    // Iterate through the soups in the menu and return the first one

    responseMap.put("result", this.csvHandler.getCsv().getTarget(target));
    System.out.println(responseMap);
    return new CSVHandler.LoadSuccessResponse(responseMap).serialize();
  }

  /**
   * Response object to send, containing a soup with certain ingredients in it.
   */
  public record LoadSuccessResponse(String responseType, Map<String, Object> responseMap) {

    public LoadSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        // Initialize Moshi which takes in this class and returns it as JSON!
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<LoadSuccessResponse> adapter = moshi.adapter(LoadSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        // For debugging purposes, show in the console _why_ this fails
        // Otherwise we'll just get an error 500 from the API in integration
        // testing.
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Response object to send if someone requested soup from an empty Menu.
   */
  public record LoadFailureResponse(String response_type) {

    public LoadFailureResponse() {
      this("error");
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(LoadFailureResponse.class).toJson(this);
    }
  }
}
