package edu.brown.cs.student.main.datasource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface Datasource {
  public String sendRequest(List<String> params)
      throws URISyntaxException, IOException, InterruptedException;
}
