import java.sql.Connection;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class Patient {
        private Connection connection;
        private Scanner scanner;
        public Patient(Connection connection, Scanner scanner) {
            this.connection = connection;
            this.scanner = scanner;
        }
    public void addPatient() {
        System.out.println("Enter Patient name: ");
        String name=scanner.next();
        System.out.println("Enter Patient Age: ");
        int age = -1; 
        while (age < 0) { 
        System.out.println("Enter Patient Age (must be a positive number): ");
        while (!scanner.hasNextInt()) { 
            System.out.println("Invalid input! Please enter a valid age (positive number).");
            scanner.next(); 
        }
        age = scanner.nextInt(); 
        if (age < 0) {
            System.out.println("Age cannot be negative! Please enter a positive number.");
        }
    }
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        try {
            String query = "INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
            PreparedStatement preparedStatement =connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows =preparedStatement.executeUpdate();
            if(affectedRows>0) {
                System.out.println("Patient added Successfully!!");
            } else {
                System.out.println("Failed to add patient!");
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query ="select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients:");
            System.out.println("+-------------+----------------+-------------+------------+");
            System.out.println("| Patient Id |  Name            | Age        | Gender      ");
            System.out.println("+-------------+----------------+-------------+------------+");
            while(resultSet.next()) {
                int id =resultSet.getInt("id");
                String name =resultSet.getString("name");
                int age =resultSet.getInt("age");
                String gender=resultSet.getString("gender");
                System.out.printf("|%-13s|%-18s|%-12s|%-13s|\n",id,name,age,gender);
                System.out.println("+-------------+----------------+-------------+------------+");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getPatientById(int id) {
        String query ="SELECT *FROM patients WHERE id=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}