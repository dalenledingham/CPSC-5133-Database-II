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
   
   
   //////////////////////////////////////////////////////
   // THE FOLLOWING METHODS ARE FOR READING TEXT FILES //
   //////////////////////////////////////////////////////
   
   
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
               insertRooms();
               insertPerson(data);
               insertDiagnosis(data);
               insertAdmission(data);
               //updateRoom(data);
            }
            // if person is employee
            else if (data.get(0).equals("D") || data.get(0).equals("N") || data.get(0).equals("T") || data.get(0).equals("A")){
               insertPerson(data);
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
   
   
   // Read data from treatment file
   public void readTreatmentFile(String filePath) {
      try (Scanner scan = new Scanner(new File(filePath))) {
         scan.useDelimiter(",");
         String line = null;
         String[] split = null;
      
         while (scan.hasNextLine()) {
            line = scan.nextLine();
            split = line.split(",");
            ArrayList<String> data = new ArrayList<String>();
         // copy array to arraylist
            for (String text: split) {
               data.add(text);
            }
         
            insertTreatment(data);
            insertPatientTreatment(data);
         }
         
         scan.close();
      }
      catch (IOException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   //////////////////////////////////////////////////////////////
   // THE FOLLOWING METHODS ARE FOR INSERTING DATA INTO TABLES //
   //////////////////////////////////////////////////////////////
   
   
   // Insert info into Persons table
   public void insertPerson(ArrayList<String> arr) {
      String sql = "INSERT INTO Persons(personType, pFirstName, lastName) VALUES (?,?,?);";
   
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
   
   
   // Insert info into Diagnoses table
   public void insertDiagnosis(ArrayList<String> arr) {
      String sql = "INSERT INTO Diagnoses(diagnosisName) VALUES (?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(9));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   

   // Insert info into Admissions table
   public void insertAdmission(ArrayList<String> arr) {
      String sql = "INSERT INTO Admissions(firstName, lastName, roomNum, diagnosis, primaryDoctor, insurancePolicy, insuranceCompany, ecName, ecPhone, admitDate, dischargeDate) VALUES (?,?,?,?,?,?,?,?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(1));
         ps.setString(2, arr.get(2));
         ps.setString(3, arr.get(3));
         ps.setString(4, arr.get(9));
         ps.setString(5, arr.get(8));
         ps.setString(6, arr.get(6));
         ps.setString(7, arr.get(7));
         ps.setString(8, arr.get(4));
         ps.setString(9, arr.get(5));
         ps.setString(10, arr.get(10));
         if (arr.size() == 12) {   // if patient has been discharged
            ps.setString(11, arr.get(11));
         }
         else {   // if patient has NOT been discharged
            ps.setString(11, null);
            updateRoom(arr);
         }
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // Update info in Rooms table
   public void updateRoom(ArrayList<String> arr) {
      String sql = "UPDATE Rooms SET isOccupied = ?,"
         + "firstName = ?,"
         + "lastName = ?,"
         + "admitDate = ?"
         + "WHERE roomNum = ?;";
         
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, "Y");
         ps.setString(2, arr.get(1));
         ps.setString(3, arr.get(2));
         ps.setString(4, arr.get(10));
         ps.setString(5, arr.get(3));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // Insert info into Rooms table
   public void insertRooms() {
      String sql = "INSERT INTO Rooms(roomNum, isOccupied, firstName, lastName, admitDate) VALUES (?,?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         for (int i = 1; i <= 20; i++) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, i);
            ps.setString(2, "N");
            ps.setString(3, null);
            ps.setString(4, null);
            ps.setString(5, null);
            ps.executeUpdate();
            ps.close();
         }
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }


   // Insert info into AdditionalDoctor table
   public void insertAdditionalDoctor(ArrayList<String> arr) {
      String sql = "INSERT INTO AdditionalDoctors(patient, doctor) VALUES (?,?);";
   
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
   
   
   // Insert info into Treatments table
   public void insertTreatment(ArrayList<String> arr) {
      String sql = "INSERT INTO Treatments(treatmentName, treatmentType) VALUES (?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(3));
         ps.setString(2, arr.get(2));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   // Insert info into PatientTreatments table
   public void insertPatientTreatment(ArrayList<String> arr) {
      String sql = "INSERT INTO PatientTreatments(lastName, treatment, orderingDoctor, administerTime) VALUES (?,?,?,?);";
   
      try (Connection conn = this.connect();) {
         PreparedStatement ps = conn.prepareStatement(sql);
         ps.setString(1, arr.get(0));
         ps.setString(2, arr.get(3));
         ps.setString(3, arr.get(1));
         ps.setString(4, arr.get(4));
         ps.executeUpdate();
         ps.close();
      }
      catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }
   
   
   /////////////////
   // MAIN METHOD //
   /////////////////
   
   
   // Main method
   public static void main(String[] args) {
      DataImporter app = new DataImporter();
      app.readPersonFile("H:\\Auburn CPSC\\CPSC 5133 - Database II\\person-data.txt");
      app.readDoctorFile("H:\\Auburn CPSC\\CPSC 5133 - Database II\\additional-doctor-data.txt");
      app.readTreatmentFile("H:\\Auburn CPSC\\CPSC 5133 - Database II\\treatment-data.txt");
   }
}
