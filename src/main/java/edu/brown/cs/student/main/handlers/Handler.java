package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.Map;

/** Abstract class to contain the success and failure response loaders. */
public abstract class Handler {

  /**
   * Response object to send if a call to handle() succeeded.
   *
   * @param responseType the type of response (success or error)
   * @param responseMap the map containing our server response
   */
  public record LoadSuccessResponse(String responseType, Map<String, Object> responseMap) {

    /**
     * LoadSuccessResponse Constructor.
     *
     * @param responseMap the map containing out server responses
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
        return "Error Serializing LoadSuccessResponse";
      }
    }
  }

  /**
   * Response object to send if a call to handle() failed.
   *
   * @param responseType the type of response (success or error)
   * @param response the error message
   */
  public record LoadFailureResponse(String responseType, String response) {

    /** Constructor for LoadFailureResponse. */
    public LoadFailureResponse(String error) {
      this("error", error);
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
