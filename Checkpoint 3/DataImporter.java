import java.util.*;
import java.sql.*;

/**
* CPSC 5133 - Database II
* Checkpoint 3
* @author Dalen Ledingham
* @version 3/14/2021
*/

public class DataImporter {
  
  private Connection connect() {
    String url = "jdbc:sqlite:C://sqlite/hospital-database.sl3";
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
        System.out.println(rs.getString("snum") + "\t" + rs.getString("sname") + "\t"); 
      }
    }
    catch (SQLException e) {
      System.out.println(e.getMessage()); 
    }
  }
  
  public void insertSample(String snum, String sname, int status, String city) {
    String sql = "INSERT INTO Suppliers(snum, sname, status, city) VALUES (?,?,?,?);";
    
    try (Connection conn = this.connect();) {
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setString(1, snum);
      ps.setString(2, sname);
      ps.setInt(3, status);
      ps.setString(4, city);
      ps.executeUpdate();
      ps.close();
    }
    catch (SQLException e) {
      System.out.println(e.getMessage()); 
    }
  }
  
  public static void main(String[] args) {
    DataImporter app = new DataImporter();
    app.selectSample();
    app.insertSample("7", "Michael", 45, "Auburn");
    System.out.prinln("After Insert");
    app.selectSample();
  }
  
}
