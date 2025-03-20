import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HospitalManagementSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root"; // Change if needed
    private static final String PASSWORD = "Rishi@123"; // Your MySQL password
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection =DriverManager.getConnection(URL, USER, PASSWORD);
            Patient patient= new Patient(connection,scanner);
            Doctor doctor= new Doctor(connection);
            while(true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointement");
                System.out.println("5. Exit");
                System.out.println("Enter yor choice: ");
                int choice =scanner.nextInt();
                scanner.nextLine();
                switch(choice){
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatients();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("THANK YOU!!FOR USING HOSPITAL MANAGEMENT SYSTEM");
                       return;
                    default:
                        System.out.println("Enter valid choice!!");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner) {
        System.out.print("Enter Patient Id: ");
        int patientId=scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId =scanner.nextInt();
        System.out.print("Enter appointment date(YYYY-MM-DD): ");
        String appointmentDate=scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)) {
                String appointmetQuery ="INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
                try {
                    PreparedStatement preparedStatement=connection.prepareStatement(appointmetQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected=preparedStatement.executeUpdate();
                    if(rowsAffected>0) {
                        System.out.println("Appointment Booked");
                    } else {
                        System.out.println("Failed  to Book Appointment ");
                    }
                }catch(SQLException e) {
                    e.printStackTrace();
                } 
            }else {
                System.out.print("Doctor not available on this date!! ");
            }
        } else {
            System.out.print("Either doctor or patient does not exsist!! ");
        }
    }
    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection) {
        String query ="SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()) {
                int count = resultSet.getInt(1);
                if(count==0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
