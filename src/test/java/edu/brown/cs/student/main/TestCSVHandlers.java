package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.CSVHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestCSVHandlers {
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run

    // In fact, restart the entire Spark server for every test!
    CSVHandler csvHandler = new CSVHandler();
    Spark.get("loadcsv", csvHandler);
    Spark.get("searchcsv", new SearchHandler(csvHandler));
    Spark.get("viewcsv", new ViewHandler(csvHandler));
    Spark.get("broadband", new BroadbandHandler());
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("order");
    Spark.unmap("activity");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    // The default method is "GET", which is what we're using here.
    // If we were using "POST", we'd need to say so.
    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testLoadFailure() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv?filePath=asdfasf");
    // TODO: error handle with this, shouldn't crash
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    CSVHandler.LoadFailureResponse response =
        moshi
            .adapter(CSVHandler.LoadFailureResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //        System.out.println(response);
    assert response != null;
    assertEquals(
        "LoadFailureResponse[responseType=error, error=asdfasf (No such file or directory)]",
        response.toString());
    clientConnection.disconnect();
  }

  @Test
  public void testBasicLoad() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    CSVHandler.LoadSuccessResponse response =
        moshi
            .adapter(CSVHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //        System.out.println(response);
    assert response != null;
    assertEquals("LoadSuccessResponse[responseType=success, responseMap={}]", response.toString());
    clientConnection.disconnect();
  }

  @Test
  public void testBasicView() throws IOException {
    HttpURLConnection clientConnection0 =
        tryRequest("loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true");
    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    CSVHandler.LoadSuccessResponse response0 =
        moshi
            .adapter(CSVHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection0.getInputStream()));

    HttpURLConnection clientConnection = tryRequest("viewcsv");
    assertEquals(200, clientConnection.getResponseCode());

    // We'll use okio's Buffer class here
    ViewHandler.LoadSuccessResponse response =
        moshi
            .adapter(ViewHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //        System.out.println(response);
    assert response != null;
    assertEquals(
        "LoadSuccessResponse[responseType=success, responseMap={result=[State, Data Type, Average Weekly Earnings, Number of Workers, Earnings Disparity, Employed Percent]\n"
            + "[RI, White,  $1,058.47 , 395773.6521, $1.00, 75%]\n"
            + "[RI, Black, $770.26, 30424.80376, $0.73, 6%]\n"
            + "[RI, Native American/American Indian, $471.07, 2315.505646, $0.45, 0%]\n"
            + "[RI, Asian-Pacific Islander,  $1,080.09 , 18956.71657, $1.02, 4%]\n"
            + "[RI, Hispanic/Latino, $673.14, 74596.18851, $0.64, 14%]\n"
            + "[RI, Multiracial, $971.89, 8883.049171, $0.92, 2%]\n"
            + "}]",
        response.toString());
    clientConnection.disconnect();
  }

  @Test
  public void testBasicSearch() throws IOException {
    HttpURLConnection clientConnection0 =
        tryRequest("loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true");
    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    CSVHandler.LoadSuccessResponse response0 =
        moshi
            .adapter(CSVHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection0.getInputStream()));

    HttpURLConnection clientConnection = tryRequest("searchcsv?target=Black");
    assertEquals(200, clientConnection.getResponseCode());

    // We'll use okio's Buffer class here
    ViewHandler.LoadSuccessResponse response =
        moshi
            .adapter(ViewHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //        System.out.println(response);
    assert response != null;
    assertEquals(
        "LoadSuccessResponse[responseType=success, responseMap={result=Row 2: [RI, Black, $770.26, 30424.80376, $0.73, 6%]\n"
            + "}]",
        response.toString());
    clientConnection.disconnect();
  }

  @Test
  public void testSearchwithCol() throws IOException {
    HttpURLConnection clientConnection0 =
        tryRequest("loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true");
    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    CSVHandler.LoadSuccessResponse response0 =
        moshi
            .adapter(CSVHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection0.getInputStream()));

    HttpURLConnection clientConnection = tryRequest("searchcsv?target=Black&col=1");
    assertEquals(200, clientConnection.getResponseCode());

    // We'll use okio's Buffer class here
    ViewHandler.LoadSuccessResponse response =
        moshi
            .adapter(ViewHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //        System.out.println(response);
    assert response != null;
    assertEquals(
        "LoadSuccessResponse[responseType=success, responseMap={result=Row 2: [RI, Black, $770.26, 30424.80376, $0.73, 6%]\n"
            + "}]",
        response.toString());
    clientConnection.disconnect();
  }

  @Test
  public void testSearchwithHeader() throws IOException {
    HttpURLConnection clientConnection0 =
        tryRequest("loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true");
    Moshi moshi = new Moshi.Builder().build();
    // We'll use okio's Buffer class here
    CSVHandler.LoadSuccessResponse response0 =
        moshi
            .adapter(CSVHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection0.getInputStream()));

    HttpURLConnection clientConnection = tryRequest("searchcsv?target=Black&header=Data%20Type");
    assertEquals(200, clientConnection.getResponseCode());

    // We'll use okio's Buffer class here
    ViewHandler.LoadSuccessResponse response =
        moshi
            .adapter(ViewHandler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    //        System.out.println(response);
    assert response != null;
    assertEquals(
        "LoadSuccessResponse[responseType=success, responseMap={result=Row 2: [RI, Black, $770.26, 30424.80376, $0.73, 6%]\n"
            + "}]",
        response.toString());
    clientConnection.disconnect();
  }
}
