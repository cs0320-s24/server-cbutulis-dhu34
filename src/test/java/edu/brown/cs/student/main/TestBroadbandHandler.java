package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.datasource.ApiDatasource;
import edu.brown.cs.student.main.datasource.CachedDatasource;
import edu.brown.cs.student.main.datasource.Datasource;
import edu.brown.cs.student.main.datasource.MockDataSource;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.Handler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestBroadbandHandler {
  @BeforeAll
  public static void setup_before_everything() {
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run
    // In fact, restart the entire Spark server for every test!
    Datasource mockSource = new MockDataSource();
    Spark.get("broadbandmock", new BroadbandHandler(mockSource));
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
  public void testBasicBroadband() throws IOException {
    Spark.get("broadband", new BroadbandHandler(new CachedDatasource(new ApiDatasource())));

    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County,%20California");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Handler.LoadSuccessResponse response =
        moshi
            .adapter(Handler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assert response != null;
    assertTrue(
        response
            .toString()
            .startsWith(
                "LoadSuccessResponse[responseType=success, responseMap={result=[[broadband access, state, county], [93.0%, California, Orange County, California]]"));
    clientConnection.disconnect();
  }

  @Test
  public void testMocked() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadbandmock?state=Massachusetts&county=Orange%20County,%20California");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Handler.LoadSuccessResponse response =
        moshi
            .adapter(Handler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assert response != null;
    System.out.println(response);
    assertTrue(
        response
            .toString()
            .startsWith(
                "LoadSuccessResponse[responseType=success, responseMap={result=[[broadband access, S2802_C03_022E, state, county], [Orange County, California%, Massachusetts, Orange County, California, 059]]"));
    clientConnection.disconnect();
  }

  @Test
  public void verifyCaching() throws IOException, InterruptedException {
    CachedDatasource datasource = new CachedDatasource(new ApiDatasource());
    Spark.get("broadband", new BroadbandHandler(datasource));

    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County,%20California");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Handler.LoadSuccessResponse response =
        moshi
            .adapter(Handler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));


    clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County,%20California");
    Handler.LoadSuccessResponse response1 =
        moshi
            .adapter(Handler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));


    assertEquals(response1, response);

    clientConnection.disconnect();
  }

  @Test
  public void countyDoesNotExist() throws IOException {
    Spark.get("broadband", new BroadbandHandler(new CachedDatasource(new ApiDatasource())));

    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Blue%20County,%20California");
    assertEquals(200, clientConnection.getResponseCode());

    Moshi moshi = new Moshi.Builder().build();
    Handler.LoadSuccessResponse response =
        moshi
            .adapter(Handler.LoadSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

    assert response != null;
    System.out.println(response);
    assertTrue(
        response
            .toString()
            .startsWith("LoadSuccessResponse[responseType=error, responseMap=null]"));
    clientConnection.disconnect();
  }
}
