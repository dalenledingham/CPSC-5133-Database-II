import java.util.*;
import java.sql.*;
import java.io.*;

/**
* CPSC 5133 - Database II
* Database Interface
* @author Dalen Ledingham
* @version 4/8/2021
*/

public class DatabaseInterface {

   // Establish connection to database file
   private Connection connect() {
      String url = "jdbc:sqlite:C://sqlite/hospital.db";
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
   
   
   // 1.1 List the rooms that are occupied, along with the associated patient names and the date the patient was admitted. 
   public void getOccupiedRooms() {
      try (Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs;
      
         rs = stmt.executeQuery("SELECT roomNum, firstName, lastName, admitDate FROM Rooms WHERE isOccupied = 'Y';");
         while (rs.next()) {
            String roomNum = rs.getString("roomNum");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String admitDate = rs.getString("admitDate");
            System.out.println(roomNum + " | " + firstName + " | " + lastName + " | " + admitDate);
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // 1.2. List the rooms that are currently unoccupied.
   public void getUnoccupiedRooms() {
      try (Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs;
      
         rs = stmt.executeQuery("SELECT roomNum FROM Rooms WHERE isOccupied = 'N';");
         while (rs.next()) {
            String roomNum = rs.getString("roomNum");
            System.out.println(roomNum);
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // 1.3. List all rooms in the hospital along with patient names and admission dates for those that are occupied.
   public void getAllRooms() {
      try (Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs;
      
         rs = stmt.executeQuery("SELECT * FROM Rooms;");
         while (rs.next()) {
            String roomNum = rs.getString("roomNum");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String admitDate = rs.getString("admitDate");
            System.out.println(roomNum + " | " + firstName + " | " + lastName + " | " + admitDate);
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // 2.1. List all patients in the database, with full personal information.
   public void getAllPatients() {
      try (Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs;
      
         rs = stmt.executeQuery("SELECT DISTINCT pID, pFirstName, lastName, insurancePolicy, insuranceCompany FROM Persons INNER JOIN Admissions USING(lastName);");
         while (rs.next()) {
            String pID = rs.getString("pID");
            String pFirstName = rs.getString("pFirstName");
            String lastName = rs.getString("lastName");
            String insurancePolicy = rs.getString("insurancePolicy");
            String insuranceCompany = rs.getString("insuranceCompany");
            System.out.println(pID + " | " + pFirstName + " | " + lastName + " | " + insurancePolicy + " | " + insuranceCompany);
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // 2.2. List all patients currently admitted to the hospital. List only patient identification number and name.
   public void getAllCurrentPatients() {
      try (Connection conn = this.connect();) {
         Statement stmt = conn.createStatement();
         ResultSet rs;
      
         rs = stmt.executeQuery("SELECT pID, firstName, lastName FROM Persons INNER JOIN Admissions USING(lastName) WHERE dischargeDate is NULL;");
         while (rs.next()) {
            String pID = rs.getString("pID");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            System.out.println(pID + " | " + firstName + " | " + lastName);
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   public static void main(String[] args) {
      DatabaseInterface app = new DatabaseInterface();
      //app.getOccupiedRooms();
      //app.getUnoccupiedRooms();
      //app.getAllRooms();
      //app.getAllPatients();
      //app.getAllCurrentPatients();
   }
   
}
