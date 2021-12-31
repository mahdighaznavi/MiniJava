import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import parser.Parser;

public final class Main {
  private Main() {
    throw new UnsupportedOperationException();
  }

  public static void main(String[] args) {

    Parser parser = new Parser();
    try {
      // start parsing
      parser.startParse(new Scanner(new File("src/main/resources/code")));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }


  }
}



