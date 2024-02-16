package edu.brown.cs.student.main.datasource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Datasource interface which enables mocking.
 */
public interface Datasource {

  String sendRequest(List<String> params)
      throws URISyntaxException, IOException, InterruptedException;
}
