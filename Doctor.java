import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {
    private Connection connection;
    public Doctor(Connection connection) {
        this.connection = connection;
    }

public void viewDoctors() {
    String query ="select * from doctors";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        System.out.println("Doctors:");
        System.out.println("+------------+---------------+---------------------+------------+");
        System.out.println("| Doctor Id  | Name          | specialization      | Contact     ");
        System.out.println("+------------+---------------+---------------------+------------+");
        while(resultSet.next()) {
            int id =resultSet.getInt("id");
            String name =resultSet.getString("name");
            String specialization =resultSet.getString("specialization");
            String contact=resultSet.getString("contact");
            System.out.printf("|%-12s|%-15s|%-21s|%-13s|\n",id,name,specialization,contact);
            System.out.println("+------------+---------------+---------------------+------------+");
  
        }

    }catch (SQLException e) {
        e.printStackTrace();
    }
}
public boolean getDoctorById(int id) {
    String query ="SELECT *FROM doctors WHERE id=?";
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
