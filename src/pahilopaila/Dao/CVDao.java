package pahilopaila.Dao;

import pahilopaila.database.MySqlConnection;
import java.sql.*;
import java.util.Date;

public class CVDao {
    private final Connection connection;

    public CVDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("CVDao initialized");
    }

    public boolean saveCV(int userId, String firstName, String lastName, Date dob, String contact,
                          String education, String skills, String experience) {
        String sql = "INSERT INTO cvs (user_id, first_name, last_name, dob, contact, education, skills, experience) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println("Saving CV for user_id: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setDate(4, new java.sql.Date(dob.getTime()));
            stmt.setString(5, contact);
            stmt.setString(6, education);
            stmt.setString(7, skills);
            stmt.setString(8, experience);
            int rows = stmt.executeUpdate();
            System.out.println("CV saved successfully, rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error saving CV: " + e.getMessage());
            return false;
        }
    }

    public ResultSet getCVByUserId(int userId) {
        String sql = "SELECT * FROM cvs WHERE user_id = ?";
        System.out.println("Retrieving CV for user_id: " + userId);
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error retrieving CV: " + e.getMessage());
            return null;
        }
    }
}