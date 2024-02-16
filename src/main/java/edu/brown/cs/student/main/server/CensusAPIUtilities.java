package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

/**
 * This class contains utility methods for handling any data structures and Json-encoded data
 * structures.
 */
public class CensusAPIUtilities {

  /**
   * Creates a HashMap of state codes from a json.
   *
   * @param jsonList the raw json data from the Census API
   * @return a HashMap mapping States to Codes
   * @throws IOException if the json is invalid
   */
  public static HashMap<String, String> deserializeStateCodes(String jsonList) throws IOException {
      List<List<String>> stateCodesList = getMoshiAdapter(jsonList);

      HashMap<String, String> deserializedHashMap = new HashMap<>();

      // Convert the List into a HashMap
      for(int i=1; i< stateCodesList.size(); i++) {
        List<String> curr = stateCodesList.get(i);
        deserializedHashMap.put(curr.get(0),curr.get(1));
      }

      return deserializedHashMap;
  }

  /**
   * Creates a HashMap of county codes from a json.
   *
   * @param jsonList the raw json data from the Census API
   * @return a HashMap mapping Counties to Codes
   * @throws IOException if the json is invalid
   */
  public static HashMap<String, String> deserializeCountyCodes(String jsonList) throws IOException {
      List<List<String>> countyCodesList = getMoshiAdapter(jsonList);

      HashMap<String, String> deserializedHashMap = new HashMap<>();

      // Convert the List into a HashMap
      for(int i=1; i< countyCodesList.size(); i++) {
        List<String> curr = countyCodesList.get(i);
        deserializedHashMap.put(curr.get(0),(curr.get(2)));
      }

      return deserializedHashMap;
  }

  /**
   * Creates a List of broadband info from a json.
   *
   * @param jsonList the raw json data from the Census API
   * @return a List of a List of Strings
   * @throws IOException if the json is invalid
   */
  public static List<List<String>> deserializeBroadbandInfo(String jsonList) throws IOException {
    return getMoshiAdapter(jsonList);
  }

  /**
   * Helper method which returns a moshi adapter, or a list of a list of strings, which essentially
   * parses the json into a useful data structure.
   *
   * @param jsonList the raw json data from the Census API
   * @return a List of a List of Strings
   * @throws IOException if the json is invalid
   */
  public static List<List<String>> getMoshiAdapter(String jsonList) throws IOException {
    try {
      Moshi moshi = new Moshi.Builder().build();

      Type listType = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

      return adapter.fromJson(jsonList);
    }

    /* Moshi always throws a standard java.io.IOException if there is an error reading the JSON
    document, or if it is malformed. It throws a JsonDataException if the JSON document is
    well-formed, but doesn't match the expected format. */
    catch (IOException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("OrderHandler: string wasn't valid JSON.");
      throw e;
    } catch (JsonDataException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("OrderHandler: JSON wasn't in the right format.");
      throw e;
    }
  }

  /**
   * Helper method which sends an API request given a
   * @param get
   * @param location
   * @return
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   */
  public static String sendRequest(String get, String location)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildCensusApiRequest = HttpRequest.newBuilder()
        .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=" + get + "&for=" + location))
        .GET().build();
    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse = HttpClient.newBuilder().build()
        .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    return sentCensusApiResponse.body();

  }
}
