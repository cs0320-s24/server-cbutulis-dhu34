package edu.brown.cs.student.main.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class Datasource {

    public String sendRequest(List<String> params) throws URISyntaxException, IOException, InterruptedException {
        // 0 = county, 1 = state
        String county = params.get(0);
        String state = params.get(1);

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
        return sentBoredApiResponse.body();
    }
}
