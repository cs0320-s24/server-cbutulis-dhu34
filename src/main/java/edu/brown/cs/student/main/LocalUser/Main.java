package edu.brown.cs.student.main.LocalUser;

import java.io.IOException;

/** The Main class of our project. This is where execution begins. */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private Main(String[] args) {}

  private void run() {
    REPL repl = new REPL();
    try {
      repl.run();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
