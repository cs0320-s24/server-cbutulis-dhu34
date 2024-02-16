package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.CSVHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import spark.Spark;

/**
 * Top-level class for the Server. Contains the main() method which starts Spark and runs the
 * various handlers.
 */
public class Server {

  // Deserialize menu, setup handlers,
  // What are the endpoints that we can access... What happens if you go to them?

  /**
   * Main method, where we set up handlers for endpoints, and generally initialize the server.
   *
   * @param args args to the program
   */
  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for
       the client to be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to
       cross-origin requests from any website. Instead, you should set this header to the origin of
       your client, or a list of origins that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP
       method. Again, it's generally better to be more specific here and only allow the methods you
       need, but for this demo we'll allow all methods.
    */
    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "*");
    });

    // Setting up the handler for the GET /loadcsv, /searchcsv, /viewcsv, and /broadband endpoints
    CSVHandler csvHandler = new CSVHandler();
    Spark.get("loadcsv", csvHandler);
    Spark.get("searchcsv", new SearchHandler(csvHandler));
    Spark.get("viewcsv", new ViewHandler(csvHandler));
    Spark.get("broadband", new BroadbandHandler());

    Spark.init();
    Spark.awaitInitialization();

    // Print out an info message that the server is started and endpoints are established
    System.out.println("Server started at http://localhost:" + port);
  }
}
