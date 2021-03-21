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

  // Establish connection to database file
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
   
   
   // Read data from person text file
   public void readPersonFile(String filePath) {
      try (Scanner scan = new Scanner(new File(filePath))) {
         scan.useDelimiter(",");
         String line = null;
         String[] split = null;
         
         while (scan.hasNextLine()) {
            line = scan.nextLine();
            split = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            // copy array to arraylist
            for (String text : split) {
               data.add(text);
            }
            // if person is patient
            if (data.get(0).equals("P")) {
               insertPatient(data);
               insertRoom(data);
               insertDiagnosis(data);
            }
            // if person is employee
            else if (data.get(0).equals("D") || data.get(0).equals("N") || data.get(0).equals("T") || data.get(0).equals("A")){
               insertEmployee(data);
            }
            // else invalid
            else {
               throw new Exception("Invalid input (" + data.get(0) + ")");
            }
         }
                     
         scan.close();
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
      }
      catch (Exception e) {
         System.out.println(e.getMessage());
      }
   }
   
  
  // Read data from additional doctors file
   public void readDoctorFile(String filePath) {
      try (Scanner scan = new Scanner(new File(filePath))) {
         scan.useDelimiter(",");
         String line = null;
         String[] split = null;
         
         while (scan.hasNextLine()) {
            line = scan.nextLine();
            split = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
            // copy array to arraylist
            for (String text : split) {
               data.add(text);
            }
            
            insertAdditionalDoctor(data);
         }
         
         scan.close();
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
      }
   }
  
   
   // Insert patient info into Patient table
   public void insertPatient(ArrayList<String> arr) {
      String sql = "INSERT INTO Patients(pFirstName, pLastName, roomNum, ecName, ecPhone, insPolicy, insCompany, eLastName, diagnosisName, admitDate, dischargeDate) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(1));
         ps.setString(2, arr.get(2));
         ps.setInt(3, Integer.parseInt(arr.get(3)));
         ps.setString(4, arr.get(4));
         ps.setString(5, arr.get(5));
         ps.setString(6, arr.get(6));
         ps.setString(7, arr.get(7));
         ps.setString(8, arr.get(8));
         ps.setString(9, arr.get(9));
         ps.setString(10, arr.get(10));
         ps.setString(11, arr.get(11));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // Insert employee info into Employee table
   public void insertEmployee(ArrayList<String> arr) {
      String sql = "INSERT INTO Employees(eType, eFirstName, eLastName) VALUES (?,?,?);";
   
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
   
   
   // Insert room info into Rooms table
   public void insertRoom(ArrayList<String> arr) {
      String sql = "INSERT INTO Rooms(roomNum, pFirstName, pLastName, admitDate) VALUES (?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setInt(1, Integer.parseInt(arr.get(3)));
         ps.setString(2, arr.get(1));
         ps.setString(3, arr.get(2));
         ps.setString(4, arr.get(10));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // Insert diagnosis info into Diagnoses table
   public void insertDiagnosis(ArrayList<String> arr) {
      String sql = "INSERT INTO Diagnoses(diagnosisName, pFirstName, pLastName, admitDate, dischargeDate) VALUES (?,?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(9));
         ps.setString(2, arr.get(1));
         ps.setString(3, arr.get(2));
         ps.setString(4, arr.get(10));
         ps.setString(5, arr.get(11));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
  
  
  // Insert emergency contact info into EmergencyContacts table
   public void insertEmergencyContact(ArrayList<String> arr) {
      String sql = "INSERT INTO EmergencyContacts(pFirstName, pLastName, ecName, ecPhone) VALUES (?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(1));
         ps.setString(2, arr.get(2));
         ps.setString(3, arr.get(4));
         ps.setString(4, arr.get(5));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // Insert additional doctor info into AdditionalDoctors table
   public void insertAdditionalDoctor(ArrayList<String> arr) {
      String sql = "INSERT INTO AdditionalDoctors(pLastName, eLastName) VALUES (?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(0));
         ps.setString(2, arr.get(1));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
  
  
   // Insert treatment info into Treatment table
   public void insertTreatment(ArrayList<String> arr) {
      String sql = "INSERT INTO Treatment(pLastName, eLastName, treatmentType, treatmentName, administerTime) VALUES (?,?,?,?,?);";
     
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(0));
         ps.setString(2, arr.get(1));
         ps.setString(3, arr.get(2));
         ps.setString(4, arr.get(3));
         ps.setString(5, arr.get(4));
         ps.executeUpdate();
         ps.close();
      }
     catch (SQLException e) {
        System.out.println(e.getMessage());
     }
   }
   
   
   // Main method
   public static void main(String[] args) {
      DataImporter app = new DataImporter();
      app.readPersonFile("H:\\Auburn CPSC\\CPSC 5133 - Database II\\person-data.txt");
   }
}
