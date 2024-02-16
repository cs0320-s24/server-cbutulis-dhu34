package edu.brown.cs.student.main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.CSVHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHandlers {
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
        Spark.unmap("loadcsv");
        Spark.unmap("searchcsv");
        Spark.unmap("viewcsv");
        Spark.unmap("broadband");
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
        assertEquals(200, clientConnection.getResponseCode());

        Moshi moshi = new Moshi.Builder().build();
        // We'll use okio's Buffer class here
        CSVHandler.LoadFailureResponse response = moshi
                .adapter(CSVHandler.LoadFailureResponse.class)
                .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

//        System.out.println(response);
        assert response != null;
        assertEquals("LoadFailureResponse[responseType=error]", response.toString());
        clientConnection.disconnect();
    }

    @Test
    public void testBasicLoad() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv?filePath=data/census/dol_ri_earnings_disparity.csv&hasHeader=true");
        assertEquals(200, clientConnection.getResponseCode());

        Moshi moshi = new Moshi.Builder().build();
        // We'll use okio's Buffer class here
        CSVHandler.LoadSuccessResponse response = moshi
                .adapter(CSVHandler.LoadSuccessResponse.class)
                .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

//        System.out.println(response);
        assert response != null;
        assertEquals("LoadSuccessResponse[responseType=success, responseMap={}]", response.toString());
        clientConnection.disconnect();
    }

    @Test
    public void testBasicView() throws IOException {
        HttpURLConnection clientConnection = tryRequest("viewcsv");
        assertEquals(200, clientConnection.getResponseCode());

        Moshi moshi = new Moshi.Builder().build();
        // We'll use okio's Buffer class here
        CSVHandler.LoadSuccessResponse response = moshi
                .adapter(CSVHandler.LoadSuccessResponse.class)
                .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

//        System.out.println(response);
        assert response != null;
        assertEquals("LoadSuccessResponse[responseType=success, responseMap={}]", response.toString());
        clientConnection.disconnect();
    }
}
