package pahilopaila.Controller;

import pahilopaila.view.LoginPageview;
// Assuming you will have separate controllers for these views
// import pahilopaila.Controller.RegistrationController;
// import pahilopaila.Controller.ForgotPasswordController;
import pahilopaila.Dao.UserDao;
import pahilopaila.model.LoginRequest;
import pahilopaila.model.UserData;

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
        String rawPassword = new String(passwordChars); // Get raw password from the view

        // Clear password array immediately for security
        Arrays.fill(passwordChars, ' ');

        if (email.isEmpty() || rawPassword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter both email and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            rawPassword = null; // Clear raw password string
            return;
        }

        // Apply the same hashing algorithm that you used for storage to the input password.
        // This is crucial for comparison.
        // IMPORTANT: For real applications, String.hashCode() is NOT a secure cryptographic hash function.
        // It is prone to collisions and is easily reversible, making it unsuitable for password storage.
        // This is implemented here ONLY because you specified it as your current basic algorithm.
        String hashedInputPassword = String.valueOf(rawPassword.hashCode());

        rawPassword = null; // Clear the raw password string after hashing

        // Create a LoginRequest object using the HASHED input password.
        // Your UserDao.login method will then compare this hash directly.
        LoginRequest loginReq = new LoginRequest(email, hashedInputPassword);

        // Call the login method from UserDao
        UserData loggedInUser = userDao.login(loginReq);

        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(view, "Login successful! Welcome, " + loggedInUser.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            close();

            // TODO: Navigate to the main application dashboard by opening its controller
            // Example:
            // DashboardView dashboardView = new DashboardView();
            // DashboardController dashboardController = new DashboardController(dashboardView);
            // dashboardController.open();

        } else {
            // Login failed (either user not found or password hash mismatch)
            JOptionPane.showMessageDialog(view, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}