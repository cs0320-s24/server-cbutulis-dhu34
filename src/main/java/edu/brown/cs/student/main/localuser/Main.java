package edu.brown.cs.student.main.localuser;

import java.io.IOException;

/** The Main class of our project. This is where execution begins. */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main().run();
  }

  private Main() {}

  private void run() {
    Repl repl = new Repl();
    try {
      repl.run();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
