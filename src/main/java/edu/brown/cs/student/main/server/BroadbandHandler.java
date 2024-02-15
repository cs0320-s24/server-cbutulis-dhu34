package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {
    private HashMap<String, String> stateCodes;
    private HashMap<String, String> countyCodes;

    public BroadbandHandler() {
        this.stateCodes = Init.getStateCodes();
        this.countyCodes = Init.getCountyCodes();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String stateName = request.queryParams("state");
        String countyName = request.queryParams("county");
        System.out.println(countyName);
        String stateCode = this.stateCodes.get(stateName);
        String countyCode = this.countyCodes.get(countyName);

        // https://api.census.gov/data/2021/acs/acs1/subject/variables?get=S2802_C03_022E&for=county:*&in=state:06

        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Sends a request to the API and receives JSON back
            String broadbandJson = this.sendRequest(countyCode, stateCode);
            // Deserializes JSON into an Activity
//      Activity activity = ActivityAPIUtilities.deserializeActivity(activityJson);
            // Adds results to the responseMap
            responseMap.put("result", "success");
            responseMap.put("broadband", broadbandJson);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            // This is a relatively unhelpful exception message. An important part of this sprint will be
            // in learning to debug correctly by creating your own informative error messages where Spark
            // falls short.
            responseMap.put("result", "Exception");
        }
        return responseMap;
    }

    private static String sendRequest(String county, String state) throws URISyntaxException, IOException, InterruptedException {
        // Build a request to this BoredAPI. Try out this link in your browser, what do you see?
        // TODO 1: Looking at the documentation, how can we add to the URI to query based

        // on participant number?
        HttpRequest buildBoredApiRequest =
                HttpRequest.newBuilder()
                        .uri(new URI("https://api.census.gov/data/2021/acs/acs1/subject/variables?get=S2802_C03_022E&for=county:" +
                                county +
                                "&in=state:" +
                                state))
                        .GET()
                        .build();
        // Send that API request then store the response in this variable. Note the generic type.
        HttpResponse<String> sentBoredApiResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

        // What's the difference between these two lines? Why do we return the body? What is useful from
        // the raw response (hint: how can we use the status of response)?
        System.out.println(sentBoredApiResponse);
        System.out.println(sentBoredApiResponse.body());

        return sentBoredApiResponse.body();
    }
}
