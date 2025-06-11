/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Dao; // Changed package name to conventional lowercase 'dao'

import pahilopaila.model.UserData; // Corrected import from UserData to User
import pahilopaila.model.LoginRequest; // Assuming LoginRequest is a separate class for login credentials
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pahilopaila.database.MySqlConnection; // Assuming this class properly handles DB connections

/**
 *
 * @author Mibish
 */
public class UserDao {
    private MySqlConnection mySql; // Use private for instances

    public UserDao() {
        this.mySql = new MySqlConnection();
    }

    // Register a new user
    public boolean register(UserData user) { // Accepts a User object
        // Corrected query to include email, password_hash, and user_role
        String query = "INSERT INTO users(name, email, password_hash, user_role) VALUES(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmnt = null;
        try {
            conn = mySql.openConnection();
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, user.getName());
            stmnt.setString(2, user.getEmail());
            stmnt.setString(3, user.getPasswordHash()); // Use hashed password
            stmnt.setString(4, user.getUserRole()); // Add user role
            
            int result = stmnt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Database error during registration: " + e.getMessage()); // Log error
            // For production, consider logging to a file or a proper logging framework
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
    public UserData login(LoginRequest loginReq) { // Changed return type to User
        // Select all necessary columns. Query by email as it's UNIQUE.
        String query = "SELECT user_id, name, email, password_hash, user_role FROM users WHERE email=?";
        Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        try {
            conn = mySql.openConnection();
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, loginReq.getEmail()); // Assuming LoginRequest has getEmail()
            
            result = stmnt.executeQuery();
            
            if (result.next()) {
                String storedHashedPassword = result.getString("password_hash");
                
                // IMPORTANT: VERIFY THE PASSWORD HERE USING A HASHING LIBRARY
                // Example with BCrypt:
                // if (BCrypt.checkpw(loginReq.getPassword(), storedHashedPassword)) {
                //     // Password matches, create and return User object
                //     int id = result.getInt("user_id");
                //     String name = result.getString("name");
                //     String email = result.getString("email");
                //     String userRole = result.getString("user_role");
                //     return new User(id, name, email, storedHashedPassword, userRole);
                // } else {
                //     // Password does not match
                //     return null;
                // }

                // TEMPORARY (NOT SECURE!): Direct comparison for testing, REMOVE IN PRODUCTION
                if (loginReq.getPassword().equals(storedHashedPassword)) {
                    int id = result.getInt("user_id");
                    String name = result.getString("name");
                    String email = result.getString("email");
                    String userRole = result.getString("user_role");
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
    public boolean checkEmailExists(String email) { // Changed to check email as it's UNIQUE
        String query = "SELECT COUNT(*) FROM users WHERE email=?";
        Connection conn = null;
        PreparedStatement stmnt = null;
        ResultSet result = null;
        try {
            conn = mySql.openConnection();
            stmnt = conn.prepareStatement(query);
            stmnt.setString(1, email); // Set the parameter for the query
            
            result = stmnt.executeQuery();
            
            if (result.next()) {
                return result.getInt(1) > 0; // If count > 0, email exists
            }
            return false; // Should not reach here if query is correct
            
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