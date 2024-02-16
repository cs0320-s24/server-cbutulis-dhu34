package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csvdatastorage.CSVData;
import edu.brown.cs.student.main.exceptions.DuplicateHeaderException;
import edu.brown.cs.student.main.exceptions.FactoryFailureException;
import edu.brown.cs.student.main.parsing.Parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler class for the loadCSV endpoint.
 */
public class CSVHandler implements Route {

  private CSVData csv;

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
    // Get Query parameters
    String filePath = request.queryParams("filePath");
    String hasHeader = request.queryParams("hasHeader");
    // Initialize a map for our informative response.
    Map<String, Object> responseMap = new HashMap<>();

    try {
      Parser parser = new Parser(new FileReader(filePath));
      boolean header = hasHeader.equals("true");
      this.csv = new CSVData(parser.parse(header), header);

      return new LoadSuccessResponse(responseMap).serialize();
    } catch (FileNotFoundException e) {
      System.err.println(e.getMessage());
    } catch (DuplicateHeaderException | IOException | FactoryFailureException e) {
      System.out.println(e.getMessage());
    }
    return new LoadFailureResponse().serialize();
  }


  /**
   * Response object which contains the result of the query and a success/failure state message.
   *
   * @param responseType
   * @param responseMap
   */
  public record LoadSuccessResponse(String responseType, Map<String, Object> responseMap) {

    public LoadSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    /**
     * Serializes a successful response from an API call.
     *
     * @return this response, serialized as Json
     */
    public String serialize() {
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
  public record LoadFailureResponse(String responseType) {

    public LoadFailureResponse() {
      this("error");
    }

    /**
     * Serializes a failed response from an API call.
     *
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(LoadFailureResponse.class).toJson(this);
    }
  }

  public CSVData getCsv() {
    return csv;
  }
}
