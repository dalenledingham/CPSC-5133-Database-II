import java.util.*;
import java.sql.*;
import java.io.*;

/**
* CPSC 5133 - Database II
* Checkpoint 3
* @author Dalen Ledingham
* @version 3/14/2021
*/

public class DataImporter {

   ArrayList<String> data = new ArrayList<String>();
   
   private Connection connect() {
      String url = "jdbc:sqlite:C://sqlite/test.db";
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
   
   // Read data from file -- use scanner and store in arraylist
   public void readFile(String filePath) {
      try (Scanner scan = new Scanner(new File(filePath))) {
         scan.useDelimiter(",");
         String line = null;
         String[] split = null;
         
         while (scan.hasNextLine()) {
            line = scan.nextLine();
            split = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            
            for (String text : split) {
               data.add(text);
            }
            
            System.out.println(line);
            handleInput(data);
         }
                     
         scan.close();
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
      }
   }
   
   public void handleInput(ArrayList<String> arr) {
      String sql = "INSERT INTO Names(type, firstName, lastName) VALUES (?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(0));
         ps.setString(2, arr.get(1));
         ps.setString(3, arr.get(2));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      
      }
   }
   
   public static void main(String[] args) {
      DataImporter app = new DataImporter();
      app.readFile("H:\\Auburn CPSC\\CPSC 5133 - Database II\\person-data.txt");
   }
}
