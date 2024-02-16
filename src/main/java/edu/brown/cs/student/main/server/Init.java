package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Init class, which is responsible for sending API queries to the Census Bureau in order to get the
 * list of county codes and state codes in HashMap format, with names mapping to codes.
 */
public class Init {

  /**
   * Method returns a list of codes representing counties attained through an API query to the
   * Census Bureau.
   *
   * @return returns a HashMap of the counties mapped to codes
   */
  public static HashMap<String, String> getCountyCodes()
      throws URISyntaxException, IOException, InterruptedException {

    // Sends a request to the API and receives JSON back
    String activityJson = CensusAPIUtilities.sendRequest("NAME", "county:*");
    return CensusAPIUtilities.deserializeCountyCodes(activityJson);
  }

  /**
   * Method returns a list of codes representing states attained through an API query to the Census
   * Bureau.
   *
   * @return returns a HashMap of the states mapped to codes
   */
  public static HashMap<String, String> getStateCodes()
      throws IOException, URISyntaxException, InterruptedException {

    // Sends a request to the API and receives JSON back
    String activityJson = CensusAPIUtilities.sendRequest("NAME", "state:*");
    return CensusAPIUtilities.deserializeStateCodes(activityJson);
  }
}
