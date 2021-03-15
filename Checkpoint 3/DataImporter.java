import java.io.*;
import java.util.*;

/**
* CPSC 5133 - Database II
* Checkpoint 3
* @author Dalen Ledingham
* @version 3/14/2021
*/

public class DataImporter {
  
  Scanner in = new Scanner(System.in);
  String fileName = in;
  File file = new File(fileName);
  
  public boolean importData() {
    try {
      Scanner scan = new Scanner(file);
      scan.useDelimiter(",");
  
      while (scan.hasNext()) {
        System.out.println(scan.next());
      }
      
      return true;
    }
    catch (FileNotFoundException e) {
      System.out.print("File not found.");
      return false;
    }
    scan.close();
  }
}
