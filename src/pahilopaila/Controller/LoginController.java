package pahilopaila.Controller;

import pahilopaila.view.LoginPageview;
// Assuming you will have separate controllers for these views
// import pahilopaila.Controller.RegistrationController;
// import pahilopaila.Controller.ForgotPasswordController;
import pahilopaila.Dao.UserDao;
import pahilopaila.model.LoginRequest;
import pahilopaila.model.UserData;
import pahilopaila.view.Dashboard_JobSeekers;
import pahilopaila.view.Dashboard_Recruiters;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import pahilopaila.view.RegistrationEmployee;
import pahilopaila.view.forgotpassview;

public class LoginController implements controller {

    private final LoginPageview view;
    private final UserDao userDao;

    public LoginController(LoginPageview view) {
        this.view = view;
        this.userDao = new UserDao();

        this.view.getLoginButton().addActionListener(getLoginActionListener());
        this.view.getNewToPahiloPailaLabel().addMouseListener(getRegisterMouseListener());
        this.view.getForgotPasswordLabel().addMouseListener(getForgotPasswordMouseListener());
    }

    @Override
    public void open() {
        view.setVisible(true);
    }

    @Override
    public void close() {
        view.dispose();
    }

    public ActionListener getLoginActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        };
    }

    public MouseAdapter getRegisterMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
                RegistrationEmployee registrationView = new RegistrationEmployee();
                // Assuming 'registrationController' is correctly capitalized
                registrationController Regcon = new registrationController(registrationView);
                Regcon.open();
            }
        };
    }

    public MouseAdapter getForgotPasswordMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
                forgotpassview forgotPasswordView = new forgotpassview();
                ForgetPasswordController forgotpasscon = new ForgetPasswordController(forgotPasswordView);
                forgotpasscon.open();
            }
        };
    }

    private void handleLogin() {
        String email = view.getEnteredEmail().trim();
        char[] passwordChars = view.getEnteredPassword();
        String rawPassword = new String(passwordChars);

        Arrays.fill(passwordChars, ' '); // Clear password array immediately for security

        if (email.isEmpty() || rawPassword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter both email and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            rawPassword = null;
            return;
        }

        // IMPORTANT: For real applications, String.hashCode() is NOT a secure cryptographic hash function.
        // It is prone to collisions and is easily reversible, making it unsuitable for password storage.
        // This is implemented here ONLY because you specified it as your current basic algorithm.
        String hashedInputPassword = String.valueOf(rawPassword.hashCode());

        rawPassword = null; // Clear the raw password string after hashing

        LoginRequest loginReq = new LoginRequest(email, hashedInputPassword);

        UserData loggedInUser = userDao.login(loginReq); // Call the login method from UserDao

        if (loggedInUser != null) {
          
            close(); // Close the login view

            // --- Check user_role and navigate to appropriate dashboard ---
            String userRole = loggedInUser.getUserRole(); // Assuming UserData has a getUserRole() method

            if ("Job Seeker".equals(userRole)) {
                Dashboard_JobSeekers jobSeekerDashboardView = new Dashboard_JobSeekers();
                // If you have a controller for Job Seeker Dashboard:
                // JobSeekerDashboardController jobSeekerController = new JobSeekerDashboardController(jobSeekerDashboardView);
                // jobSeekerController.open();
                jobSeekerDashboardView.setVisible(true); // Direct view opening if no controller yet
                System.out.println("Navigating to Job Seeker Dashboard");

            } else if ("Employer".equals(userRole)) {
                Dashboard_Recruiters recruiterDashboardView = new Dashboard_Recruiters();
                // If you have a controller for Recruiter Dashboard:
                // RecruiterDashboardController recruiterController = new RecruiterDashboardController(recruiterDashboardView);
                // recruiterController.open();
                recruiterDashboardView.setVisible(true); // Direct view opening if no controller yet
                System.out.println("Navigating to Employer Dashboard");

            } else {
                // Handle unexpected roles (e.g., if 'user_role' enum values change or are invalid)
                JOptionPane.showMessageDialog(view, "Unknown user role: " + userRole + ". Cannot navigate to dashboard.", "Navigation Error", JOptionPane.WARNING_MESSAGE);
                System.err.println("LoginController: Unknown user role: " + userRole);
            }

        } else {
            // Login failed (either user not found or password hash mismatch)
            JOptionPane.showMessageDialog(view, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}