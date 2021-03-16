import java.io.*;
import java.util.*;

/**
* CPSC 5133 - Database II
* Checkpoint 3
* @author Dalen Ledingham
* @version 3/14/2021
*/

public class DataImporter {
  
  private Connection connect() {
    String url = "";
    Connection conn = null;
    try {
      conn = DriverManager.getConnection(url);
      
      conn.createStatement().executeUpdate("PRAGMA foreign_keys = ON;");
    }
    catch (SQLException e) {
      System.out.println(e.getMessage()); 
    }
    
    return conn;
  }
  
  public void selectSample() {
    String sql = "SELECT snum, sname FROM suppliers;";
    
    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
       
      while (rs.next()) {
        System.out..println(rs.getString("snum") + "\t" + rs.getString("sname") + "\t"); 
      }
    }
    catch (SQLException e) {
      System.out.println(e.getMessage()); 
    }
  }
  
}
