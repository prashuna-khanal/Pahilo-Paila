package pahilopaila.Controller;

import pahilopaila.view.RegistrationEmployee;
import pahilopaila.view.LoginPageview;
import pahilopaila.Dao.UserDao;

import javax.swing.*;
import java.util.Arrays;

public class registrationController {
    private final RegistrationEmployee view;
    private final UserDao userDao;

    public registrationController(RegistrationEmployee view) {
        this.view = view;
        this.userDao = new UserDao();
        System.out.println("registrationController initialized");
        this.view.getRegisterEmployerButton().addActionListener(e -> {
            System.out.println("Register Employer button clicked");
            handleRegistration("Employer");
        });
        this.view.getRegisterJobSeekerButton().addActionListener(e -> {
            System.out.println("Register Job Seeker button clicked");
            handleRegistration("Job Seeker");
        });
    }

    private void handleRegistration(String userRole) {
        System.out.println("Handling registration for role: " + userRole);
        String name = view.getEnteredName().trim();
        String email = view.getEnteredEmail().trim();
        char[] passwordChars = view.getEnteredPassword();
        char[] confirmPasswordChars = view.getEnteredConfirmPassword();
        String password = new String(passwordChars);
        String confirmPassword = new String(confirmPasswordChars);
        System.out.println("Input - Name: " + name + ", Email: " + email + ", Password length: " + password.length());

        Arrays.fill(passwordChars, ' ');
        Arrays.fill(confirmPasswordChars, ' ');

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            System.out.println("Validation failed: Empty fields");
            JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Validation failed: Passwords do not match");
            JOptionPane.showMessageDialog(view, "Passwords do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            System.out.println("Validation failed: Password too short");
            JOptionPane.showMessageDialog(view, "Password must be at least 6 characters.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            System.out.println("Validation failed: Invalid email");
            JOptionPane.showMessageDialog(view, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDao.checkEmailExists(email)) {
            System.out.println("Validation failed: Email already exists");
            JOptionPane.showMessageDialog(view, "Email already registered.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean registered = userDao.registerUser(name, email, password, userRole);
        System.out.println("Registration attempt result: " + registered);
        if (registered) {
            JOptionPane.showMessageDialog(view, "Registration successful as " + userRole + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.dispose();
            LoginPageview loginView = new LoginPageview();
            LoginController loginController = new LoginController(loginView);
            loginController.open();
        } else {
            System.out.println("Registration failed: Database error");
            JOptionPane.showMessageDialog(view, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        boolean valid = email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        System.out.println("Email validation result for " + email + ": " + valid);
        return valid;
    }

    public void open() {
        System.out.println("Opening registration view");
        view.setVisible(true);
    }
}