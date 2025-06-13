/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.model;

/**
 *
 * @author Mibish
 */
public class LoginRequest {
    private String email; // Renamed 'name' to 'email' for clarity and consistency
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email; // Correctly assign the passed email to the 'email' field
        this.password = password;
    }

    // Setters
    public void setEmail(String email) { // Renamed setter to match 'email' field
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getters
    public String getEmail() { // Renamed getter to match 'email' field
        return email;
    }

    public String getPassword() {
        return password;
    }
}