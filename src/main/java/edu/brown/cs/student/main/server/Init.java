package edu.brown.cs.student.main.server;

import java.util.HashMap;
import java.util.Map;

/**
 * Init class, which is responsible for sending API queries to the Census Bureau in order to get
 * the list of county codes and state codes in HashMap format, with names mapping to codes.
 */
public class Init {

  /**
   * This method returns a list of codes representing counties attained through an API query to the
   * Census Bureau.
   *
   * @return returns a HashMap of the counties mapped to codes
   */
  public static HashMap<String, String> getCountyCodes() {

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String activityJson = CensusAPIUtilities.sendRequest("NAME", "county:*");
      return CensusAPIUtilities.deserializeCountyCodes(activityJson);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "Exception");
    }
    return null;
  }

  /**
   * This method returns a list of codes representing states attained through an API query to the
   * Census Bureau.
   *
   * @return returns a HashMap of the states mapped to codes
   */
  public static HashMap<String, String> getStateCodes() {
    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String activityJson = CensusAPIUtilities.sendRequest("NAME", "state:*");
      return CensusAPIUtilities.deserializeStateCodes(activityJson);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "Exception");
    }
    return null;
  }
}
