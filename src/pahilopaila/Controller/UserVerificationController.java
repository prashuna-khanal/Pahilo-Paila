package pahilopaila.Controller;

import pahilopaila.database.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserVerificationController {
    private final MySqlConnection dbConnector;

    public UserVerificationController() {
        this.dbConnector = MySqlConnection.getInstance();
    }

    public boolean userExists(String email) {
        Connection conn = dbConnector.openConnection();
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean storeOTP(String email, String otp, LocalDateTime expiryTime) {
        Connection conn = dbConnector.openConnection();
        String sql = "UPDATE users SET otp = ?, otp_expires_at = ? WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, otp);
            pstmt.setTimestamp(2, Timestamp.valueOf(expiryTime));
            pstmt.setString(3, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error storing OTP: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyOTP(String email, String enteredOtp) {
        Connection conn = dbConnector.openConnection();
        String sql = "SELECT otp, otp_expires_at FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedOtp = rs.getString("otp");
                Timestamp storedExpiryTs = rs.getTimestamp("otp_expires_at");
                if (storedOtp == null || storedExpiryTs == null) {
                    System.out.println("No OTP found for user: " + email);
                    return false;
                }
                LocalDateTime storedExpiry = storedExpiryTs.toLocalDateTime();
                if (storedOtp.equals(enteredOtp) && storedExpiry.isAfter(LocalDateTime.now())) {
                    clearOTP(email);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying OTP: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void clearOTP(String email) {
        Connection conn = dbConnector.openConnection();
        String sql = "UPDATE users SET otp = NULL, otp_expires_at = NULL WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error clearing OTP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean updatePassword(String email, String newHashedPassword) {
        Connection conn = dbConnector.openConnection();
        String sql = "UPDATE users SET user_password = ?, otp = NULL, otp_expires_at = NULL WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newHashedPassword);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String hashPassword(String password) {
        return String.valueOf(password.hashCode());
    }
}