/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.model;

import java.sql.Timestamp;

/**
 *
 * @author Mibish
 */
public class UserData {
    private int id;
    private String name;
    private String email;
    private String password; 
    private String userRole;
    private String otp;
    // private Timestamp otpGeneratedAt; // REMOVED: No corresponding column in DB
    private Timestamp otpExpiresAt;

    // Constructor for new registrations (without ID, OTP)
    public UserData(String name, String email, String password, String userRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    // Constructor for retrieving from database (with ID)
    public UserData(int id, String name, String email, String password, String userRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
    
    // Optional: Constructor including OTP fields for comprehensive object creation
    // Adjusted constructor to remove otpGeneratedAt
    public UserData(int id, String name, String email, String password, String userRole, String otp, Timestamp otpExpiresAt) { 
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.otp = otp;
        // this.otpGeneratedAt = otpGeneratedAt; // REMOVED
        this.otpExpiresAt = otpExpiresAt;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }

    // public void setOtpGeneratedAt(Timestamp otpGeneratedAt) { // REMOVED
    //     this.otpGeneratedAt = otpGeneratedAt;
    // }

    public void setOtpExpiresAt(Timestamp otpExpiresAt) {
        this.otpExpiresAt = otpExpiresAt;
    }


    // Getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserRole() {
        return this.userRole;
    }
    
    public String getOtp() {
        return this.otp;
    }

    // public Timestamp getOtpGeneratedAt() { // REMOVED
    //     return this.otpGeneratedAt;
    // }

    public Timestamp getOtpExpiresAt() {
        return this.otpExpiresAt;
    }
}