package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.datasource.CachedDatasource;
import edu.brown.cs.student.main.server.CensusAPIUtilities;
import edu.brown.cs.student.main.datasource.Datasource;
import edu.brown.cs.student.main.server.Init;
import java.util.*;

import spark.Request;
import spark.Response;
import spark.Route;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * Handler class from the broadband endpoint.
 */
public class BroadbandHandler implements Route {
    private HashMap<String, String> stateCodes;
    private HashMap<String, String> countyCodes;
    private CachedDatasource datasource;

    /**
     * Default constructor for the BroadbandHandler. Takes no arguments.
     */
    public BroadbandHandler() {
        this.stateCodes = Init.getStateCodes();
        this.countyCodes = Init.getCountyCodes();
        this.datasource = new CachedDatasource(new Datasource());
    }

    /**
     * Method which handles the user's request and is called when the API endpoint is accessed.
     * This takes in the user's request, parses out its parameters, and then passes the params
     * forward to the Datasource class to get back a response from the API. It gets a
     * JSON back from Datasource, deserializes it to format the JSON, puts it in a
     * response map, then appends a time stamp onto the response map. This map of the responses
     * is then returned.
     *
     * @param request - The user's query request
     * @param response - The response to the user's query
     * @return - the responseMap, a Map between strings and objects containing the API's response
     */
    @Override
    public Object handle(Request request, Response response) {
        String stateName = request.queryParams("state");
        String countyName = request.queryParams("county");
        String stateCode = this.stateCodes.get(stateName);
        String countyCode = this.countyCodes.get(countyName);

        Map<String, Object> responseMap = new HashMap<>();
        try {
            ArrayList<String> params = new ArrayList<>();
            params.add(countyCode);
            params.add(stateCode);
            String broadbandJson = this.datasource.sendRequest(params);

            List<List<String>> result = CensusAPIUtilities.deserializeBroadbandInfo(broadbandJson);
            result.get(0).set(0, "broadband access");
            result.get(1).set(0, result.get(1).get(0)+"%");
            result.get(1).set(1, stateName);
            result.get(1).set(2, countyName);

            responseMap.put("result", result);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            responseMap.put("timestamp", dtf.format(now));
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
}
