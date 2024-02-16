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
public class CSVHandler extends Handler implements Route {

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
    } catch (DuplicateHeaderException | IOException | FactoryFailureException e) {
      return new LoadFailureResponse(e.getMessage()).serialize();
    }
  }

  /**
   * Returns the csv data.
   *
   * @return csv data
   */
  public CSVData getCsv() {
    return csv;
  }
}
