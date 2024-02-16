package edu.brown.cs.student.main.Handlers;

import edu.brown.cs.student.main.Datasource.CachedDatasource;
import edu.brown.cs.student.main.server.CensusAPIUtilities;
import edu.brown.cs.student.main.Datasource.Datasource;
import edu.brown.cs.student.main.server.Init;
import java.util.*;

import spark.Request;
import spark.Response;
import spark.Route;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class BroadbandHandler implements Route {
    private HashMap<String, String> stateCodes;
    private HashMap<String, String> countyCodes;
    private CachedDatasource datasource;

    public BroadbandHandler() {
        this.stateCodes = Init.getStateCodes();
        this.countyCodes = Init.getCountyCodes();
        this.datasource = new CachedDatasource(new Datasource());
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String stateName = request.queryParams("state");
        String countyName = request.queryParams("county");
        String stateCode = this.stateCodes.get(stateName);
        String countyCode = this.countyCodes.get(countyName);

        // https://api.census.gov/data/2021/acs/acs1/subject/variables?get=S2802_C03_022E&for=county:*&in=state:06

        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Sends a request to the API and receives JSON back
//            Datasource datasource = new Datasource();

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
