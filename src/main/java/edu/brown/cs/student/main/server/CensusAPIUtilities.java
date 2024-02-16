package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains utility methods for handling soup objects and Json-encoded soup objects.
 * Primarily this means _serializing_ Soup objects to Json and _deserializing_ Soup objects from
 * Json
 *
 * <p>Use this as a reference for polymorphic serialization/deserialization. It shouldn't be
 * necessary on Sprint 2 functionality, and Sprint 2 testing only requires Maps for response types.
 *
 * <p>This class shows how to deserialize into complex types.
 */
public class CensusAPIUtilities {
  private CensusAPIUtilities() {}

  /**
   * Creates a menu of Soups from a JSON. This JSON is held locally, but in Sprint 2, it will likely
   * be received from online
   *
   * @param jsonList -- In this case, found at "data/menu.json"
   * @return
   * @throws IOException
   */
  public static HashMap<String, String> deserializeStateCodes(String jsonList) throws IOException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      // notice the type and JSONAdapter parameterized type match the return type of the method
      // Since List is generic, we shouldn't just pass List.class to the adapter factory.
      // Instead, let's be more precise. Java has built-in classes for talking about generic types
      // programmatically.
      // Building libraries that use them is outside the scope of this class, but we'll follow the
      // Moshi docs'
      // template by creating a Type object corresponding to List<Ingredient>:

      Type listType = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

      List<List<String>> deserializedMenu = adapter.fromJson(jsonList);

      HashMap<String, String> deserializedHashMap = new HashMap<>();

      // Turn it into a HM
      for(int i=1; i<deserializedMenu.size(); i++) {
        List<String> curr = deserializedMenu.get(i);
        deserializedHashMap.put(curr.get(0),curr.get(1));
      }

      return deserializedHashMap;
    }
    // From the Moshi Docs (https://github.com/square/moshi):
    //   "Moshi always throws a standard java.io.IOException if there is an error reading the JSON
    // document, or if it is malformed. It throws a JsonDataException if the JSON document is
    // well-formed, but doesn't match the expected format."
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

  public static HashMap<String, String> deserializeCountyCodes(String jsonList) throws IOException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      // notice the type and JSONAdapter parameterized type match the return type of the method
      // Since List is generic, we shouldn't just pass List.class to the adapter factory.
      // Instead, let's be more precise. Java has built-in classes for talking about generic types
      // programmatically.
      // Building libraries that use them is outside the scope of this class, but we'll follow the
      // Moshi docs'
      // template by creating a Type object corresponding to List<Ingredient>:

      Type listType = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

      List<List<String>> deserializedMenu = adapter.fromJson(jsonList);

      HashMap<String, String> deserializedHashMap = new HashMap<>();

      // Turn it into a HM
      for(int i=1; i<deserializedMenu.size(); i++) {
        List<String> curr = deserializedMenu.get(i);
        deserializedHashMap.put(curr.get(0),(curr.get(2)));
      }

      return deserializedHashMap;
    }


    // From the Moshi Docs (https://github.com/square/moshi):
    //   "Moshi always throws a standard java.io.IOException if there is an error reading the JSON
    // document, or if it is malformed. It throws a JsonDataException if the JSON document is
    // well-formed, but doesn't match the expected format."
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

  public static List<List<String>> deserializeBroadbandInfo(String jsonList) throws IOException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      // notice the type and JSONAdapter parameterized type match the return type of the method
      // Since List is generic, we shouldn't just pass List.class to the adapter factory.
      // Instead, let's be more precise. Java has built-in classes for talking about generic types
      // programmatically.
      // Building libraries that use them is outside the scope of this class, but we'll follow the
      // Moshi docs'
      // template by creating a Type object corresponding to List<Ingredient>:

      Type listType = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

      List<List<String>> deserializedBroadbandInfo = adapter.fromJson(jsonList);
      return deserializedBroadbandInfo;
    }


    // From the Moshi Docs (https://github.com/square/moshi):
    //   "Moshi always throws a standard java.io.IOException if there is an error reading the JSON
    // document, or if it is malformed. It throws a JsonDataException if the JSON document is
    // well-formed, but doesn't match the expected format."
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
}
