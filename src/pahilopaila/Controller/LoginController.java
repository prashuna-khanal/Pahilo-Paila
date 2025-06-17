package pahilopaila.Controller;

import pahilopaila.view.*;
import pahilopaila.Dao.UserDao;
import pahilopaila.model.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class LoginController {
    private final LoginPageview view;
    private final UserDao userDao;

    public LoginController(LoginPageview view) {
        this.view = view;
        this.userDao = new UserDao();
        System.out.println("LoginController initialized");
        initializeListeners();
    }

    private void initializeListeners() {
        view.getLoginButton().addActionListener(e -> {
            System.out.println("Login button clicked");
            handleLogin();
        });
        view.getNewToPahiloPailaLabel().addMouseListener(getRegisterMouseListener());
        view.getForgotPasswordLabel().addMouseListener(getForgotPasswordMouseListener());
    }

    private void handleLogin() {
        String email = view.getEnteredEmail().trim();
        char[] passwordChars = view.getEnteredPassword();
        String password = new String(passwordChars);
        System.out.println("Login attempt - Email: " + email + ", Password length: " + password.length());
        Arrays.fill(passwordChars, ' '); // Clear password array for security

        // Input validation
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Validation failed: Empty email or password");
            JOptionPane.showMessageDialog(view, "Please enter both email and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt login
        LoginRequest loginReq = new LoginRequest(email, password);
        UserData loggedInUser = userDao.login(loginReq);
        System.out.println("Login result: " + (loggedInUser != null ? "Success, User: " + loggedInUser.getName() : "Failed"));

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(view, "Login successful! Welcome, " + loggedInUser.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            view.dispose(); // Close login window

            // Navigate to appropriate dashboard based on user role
            try {
                if ("Job Seeker".equals(loggedInUser.getRoles())) {
                    System.out.println("Opening Job Seeker dashboard for user ID: " + loggedInUser.getId());
                    Dashboard_JobSeekers dashboardView = new Dashboard_JobSeekers();
                    Dashboard_JobseekersController dashboardController = new Dashboard_JobseekersController(dashboardView, loggedInUser.getId());
                    dashboardController.setUserInfo(loggedInUser.getUsername(), loggedInUser.getEmail(), loggedInUser.getId());
                    dashboardView.setVisible(true);
                } else if ("Employer".equals(loggedInUser.getRoles())) {
                    System.out.println("Opening Employer dashboard for user ID: " + loggedInUser.getId());
                    Dashboard_Recruiters dashboardView = new Dashboard_Recruiters();
                    Dashboard_RecruitersController dashboardController = new Dashboard_RecruitersController(dashboardView, loggedInUser.getId());
                    dashboardView.setUserInfo(loggedInUser.getUsername(), loggedInUser.getEmail());
                    dashboardView.setVisible(true);
                } else {
                    System.out.println("Unknown role: " + loggedInUser.getRoles());
                    JOptionPane.showMessageDialog(null, "Unknown user role: " + loggedInUser.getRoles(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.err.println("Error opening dashboard: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, "Failed to open dashboard. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Login failed: Invalid credentials");
            JOptionPane.showMessageDialog(view, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private MouseAdapter getRegisterMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Register label clicked");
                view.dispose();
                RegistrationEmployee registrationView = new RegistrationEmployee();
                registrationController regcon = new registrationController(registrationView);
                regcon.open();
            }
        };
    }

    private MouseAdapter getForgotPasswordMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Forgot Password label clicked");
                view.dispose();
                forgotpassview forgotPasswordView = new forgotpassview();
                ForgetPasswordController forgotpasscon = new ForgetPasswordController(forgotPasswordView);
                forgotpasscon.open();
            }
        };
    }

    public void open() {
        System.out.println("Opening login view");
        view.setVisible(true);
    }
}