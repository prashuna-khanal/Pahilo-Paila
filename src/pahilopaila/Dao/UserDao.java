package pahilopaila.Dao;

import pahilopaila.model.UserData;
import pahilopaila.model.LoginRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pahilopaila.database.MySqlConnection;

public class UserDao {
    private MySqlConnection mySql;

    public UserDao() {
        this.mySql = new MySqlConnection();
    }

    // Register a new user
    public boolean register(UserData user) {
        // CHANGE THIS LINE: from password_hash to password
        String query = "INSERT INTO users(name, email, password, user_role) VALUES(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmnt = null;
        try {
            conn = mySql.openConnection();
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, user.getName());
            stmnt.setString(2, user.getEmail());
            stmnt.setString(3, user.getPassword()); 
            stmnt.setString(4, user.getUserRole());
            
            int result = stmnt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Database error during registration: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmnt != null) stmnt.close();
                if (conn != null) mySql.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error closing resources after registration: " + ex.getMessage());
            }
        }
    }

    // Login a user
    public UserData login(LoginRequest loginReq) {
        // CHANGE THIS LINE: from password_hash to password
        String query = "SELECT user_id, name, email, password, user_role FROM users WHERE email=?";
        Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        try {
            conn = mySql.openConnection();
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, loginReq.getEmail());
            
            result = stmnt.executeQuery();
            
            if (result.next()) {
                // CHANGE THIS LINE: from "password_hash" to "password"
                String storedHashedPassword = result.getString("password");
                
                if (loginReq.getPassword().equals(storedHashedPassword)) {
                    int id = result.getInt("user_id");
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String userRole = result.getString("user_role");
                    // CHANGE THIS LINE: pass "password" to UserData constructor
                    return new UserData(id, name, email, storedHashedPassword, userRole);
                } else {
                    return null; // Password mismatch
                }
            } else {
                return null; // User not found
            }
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            return null;
        } finally {
            try {
                if (result != null) result.close();
                if (stmnt != null) stmnt.close();
                if (conn != null) mySql.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error closing resources after login: " + ex.getMessage());
            }
        }
    }

    // Check if a user with a given email already exists
    public boolean checkEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email=?";
        Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        try {
            conn = mySql.openConnection();
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, email);
            
            result = stmnt.executeQuery();
            
            if (result.next()) {
                return result.getInt(1) > 0;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Database error checking email existence: " + e.getMessage());
            return false;
        } finally {
            try {
                if (result != null) result.close();
                if (stmnt != null) stmnt.close();
                if (conn != null) mySql.closeConnection(conn);
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }
}