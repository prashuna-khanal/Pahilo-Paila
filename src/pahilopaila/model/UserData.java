/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.model;

import java.sql.Timestamp; // Import for Timestamp

/**
 *
 * @author Mibish
 */
public class UserData {
    private int id; // Changed to int to match database
    private String name;
    private String email;
    private String passwordHash; // Renamed to reflect hashed password
    private String userRole; // New field for user role (Job Seeker, Employer)
    private String otp; // Optional: For OTP code
    private Timestamp otpGeneratedAt; // Optional: For OTP generation time
    private Timestamp otpExpiresAt; // Optional: For OTP expiration time

    // Constructor for new registrations (without ID, OTP)
    public UserData(String name, String email, String passwordHash, String userRole) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
    }

    // Constructor for retrieving from database (with ID)
    public UserData(int id, String name, String email, String passwordHash, String userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
    }
    
    // Optional: Constructor including OTP fields for comprehensive object creation
    public UserData(int id, String name, String email, String passwordHash, String userRole, String otp, Timestamp otpGeneratedAt, Timestamp otpExpiresAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userRole = userRole;
        this.otp = otp;
        this.otpGeneratedAt = otpGeneratedAt;
        this.otpExpiresAt = otpExpiresAt;
    }

    // Setters
    public void setId(int id) { // Changed parameter type to int
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) { // Renamed setter
        this.passwordHash = passwordHash;
    }

    public void setUserRole(String userRole) { // Setter for userRole
        this.userRole = userRole;
    }
    
    public void setOtp(String otp) { // Setter for OTP
        this.otp = otp;
    }

    public void setOtpGeneratedAt(Timestamp otpGeneratedAt) { // Setter for OTP generated time
        this.otpGeneratedAt = otpGeneratedAt;
    }

    public void setOtpExpiresAt(Timestamp otpExpiresAt) { // Setter for OTP expiration time
        this.otpExpiresAt = otpExpiresAt;
    }


    // Getters
    public int getId() { // Changed return type to int
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPasswordHash() { // Renamed getter
        return this.passwordHash;
    }

    public String getUserRole() { // Getter for userRole
        return this.userRole;
    }
    
    public String getOtp() { // Getter for OTP
        return this.otp;
    }

    public Timestamp getOtpGeneratedAt() { // Getter for OTP generated time
        return this.otpGeneratedAt;
    }

    public Timestamp getOtpExpiresAt() { // Getter for OTP expiration time
        return this.otpExpiresAt;
    }
}