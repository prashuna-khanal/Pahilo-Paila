/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Controller;

import pahilopaila.database.MySqlConnection;
import java.sql.Timestamp; // <--- ADD THIS LINE// Import your existing database connection class
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Controller class responsible for user verification processes (OTP, password update, and current password verification).
 */
public class UserVerificationController {

    private final MySqlConnection dbConnector;

    public UserVerificationController() {
        this.dbConnector = new MySqlConnection();
    }

    /**
     * Checks if a user with the given email exists in the database.
     * This method is called before sending an OTP email.
     *
     * @param email The email to check.
     * @return true if the email exists, false otherwise.
     */
    public boolean userExists(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnector.openConnection();
            if (conn == null) {
                System.err.println("Failed to open DB connection for user existence check.");
                return false;
            }

            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Database error checking user existence for " + email + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { /* ignore */ }
            dbConnector.closeConnection(conn);
        }
        return false;
    }

    /**
     * Stores or updates the OTP and its expiry for a given email in the database.
     * This method is called after generating an OTP and before sending the email.
     *
     * @param email The user's email.
     * @param otp The generated OTP.
     * @param expiryTime The time when the OTP expires.
     * @return true if the OTP was successfully stored/updated, false otherwise.
     */
    public boolean storeOTP(String email, String otp, LocalDateTime expiryTime) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbConnector.openConnection();
            if (conn == null) {
                System.err.println("Failed to open DB connection for storing OTP.");
                return false;
            }

            String sql = "UPDATE users SET otp = ?, otp_expiry = ? WHERE email = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, otp);
            pstmt.setTimestamp(2, java.sql.Timestamp.valueOf(expiryTime));
            pstmt.setString(3, email);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error storing OTP for " + email + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { /* ignore */ }
            dbConnector.closeConnection(conn);
        }
    }

    /**
     * Verifies if the provided OTP matches the one stored for the email and is not expired.
     * This method is called when the user submits the OTP for verification.
     *
     * @param email The user's email.
     * @param enteredOtp The OTP entered by the user.
     * @return true if the OTP is valid and not expired, false otherwise.
     */
    public boolean verifyOTP(String email, String enteredOtp) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnector.openConnection();
            if (conn == null) {
                System.err.println("Failed to open DB connection for verifying OTP.");
                return false;
            }

            String sql = "SELECT otp, otp_expiry FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedOtp = rs.getString("otp");
                Timestamp storedExpiryTs = rs.getTimestamp("otp_expiry");

                if (storedExpiryTs == null || storedOtp == null) {
                    System.out.println("OTP or expiry not set for user: " + email);
                    return false;
                }

                LocalDateTime storedExpiry = storedExpiryTs.toLocalDateTime();

                if (storedOtp.equals(enteredOtp) && storedExpiry.isAfter(LocalDateTime.now())) {
                    clearOTP(email); // Clear OTP on successful verification
                    return true;
                } else {
                    System.out.println("OTP mismatch or expired for user: " + email);
                    return false;
                }
            } else {
                System.out.println("No OTP found for user: " + email);
            }
        } catch (SQLException e) {
            System.err.println("Database error verifying OTP for " + email + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { /* ignore */ }
            dbConnector.closeConnection(conn);
        }
        return false;
    }

    /**
     * Clears the OTP and its expiry from the database for a given email.
     * This should be called after successful OTP verification or password reset.
     *
     * @param email The user's email.
     */
    public void clearOTP(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbConnector.openConnection();
            if (conn == null) {
                System.err.println("Failed to open DB connection for clearing OTP.");
                return;
            }

            String sql = "UPDATE users SET otp = NULL, otp_expiry = NULL WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.executeUpdate();
            System.out.println("OTP cleared for " + email);
        } catch (SQLException e) {
            System.err.println("Database error clearing OTP for " + email + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { /* ignore */ }
            dbConnector.closeConnection(conn);
        }
    }

    /**
     * **NEW METHOD**
     * Verifies a user's current plain-text password against the hashed password stored in the database.
     *
     * @param email The user's email.
     * @param currentPassword The plain-text current password provided by the user.
     * @return true if the current password matches the one stored in the database, false otherwise.
     */
    public boolean verifyCurrentPassword(String email, String currentPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbConnector.openConnection();
            if (conn == null) {
                System.err.println("Failed to open DB connection for verifying current password.");
                return false;
            }

            String sql = "SELECT password FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String enteredHashedPassword = hashPassword(currentPassword); // Hash the entered password for comparison

                // **IMPORTANT: In production, use a secure password hashing library's comparison method (e.g., BCrypt.checkpw)**
                return storedHashedPassword != null && storedHashedPassword.equals(enteredHashedPassword);
            }
        } catch (SQLException e) {
            System.err.println("Database error verifying current password for " + email + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { /* ignore */ }
            dbConnector.closeConnection(conn);
        }
        return false;
    }

    /**
     * Updates the user's password in the database.
     * This method is called after successful OTP verification or current password verification.
     *
     * @param email The user's email.
     * @param newHashedPassword The new hashed password.
     * @return true if the password was updated, false otherwise.
     */
    public boolean updatePassword(String email, String newHashedPassword) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbConnector.openConnection();
            if (conn == null) {
                System.err.println("Failed to open DB connection for updating password.");
                return false;
            }

            String sql = "UPDATE users SET password = ?, otp = NULL, otp_expiry = NULL WHERE email = ?"; // Clear OTP after pass update
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, newHashedPassword);
            pstmt.setString(2, email);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Database error updating password for " + email + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { /* ignore */ }
            dbConnector.closeConnection(conn);
        }
    }

    /**
     * Hashes a password using a simple method (for demonstration).
     * In a real application, use strong hashing libraries like BCrypt.
     * @param password The plain text password.
     * @return A simple hash of the password.
     */
    public String hashPassword(String password) {
        // **IMPORTANT: For production, use a strong, industry-standard hashing library like BCrypt or Argon2.**
        // This is a very basic, insecure hash for demonstration purposes ONLY.
        return String.valueOf(password.hashCode());
    }
}

