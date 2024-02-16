package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.Map;

/**
 * Abstract class to contain the success and failure response loaders.
 */
public abstract class Handler {

  /**
   * Response object to send if a call to handle() succeeded
   *
   * @param responseType
   * @param responseMap
   */
  public record LoadSuccessResponse(String responseType, Map<String, Object> responseMap) {

    /**
     * LoadSuccessResponse Constructor.
     *
     * @param responseMap
     */
    public LoadSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    /**
     * Serializes the LoadSuccessResponse to display on web page.
     *
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
   *
   * @param responseType the type of response
   */
  public record LoadFailureResponse(String responseType) {

    /**
     * Constructor for LoadFailureResponse.
     */
    public LoadFailureResponse() {
      this("error");
    }

    /**
     * Serializes the LoadFailureResponse to display on web page.
     *
     * @return this response, serialized as json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(LoadFailureResponse.class).toJson(this);
    }
  }
}
