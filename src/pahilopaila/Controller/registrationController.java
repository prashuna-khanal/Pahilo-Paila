package pahilopaila.Controller;

import pahilopaila.view.RegistrationEmployee;
import pahilopaila.Dao.UserDao;
import pahilopaila.model.UserData; // Or pahilopaila.model.UserData

import javax.swing.*; // Import JOptionPane
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Removed: import java.awt.Color; // Not needed for JOptionPane colors
import java.util.Arrays;

/**
 *
 * @author abi
 */
public class registrationController implements controller {

    private final RegistrationEmployee view;
    private final UserDao userDao;

    // Constructor: Now only takes the view (no messageLabel or fields passed here)
    public registrationController(RegistrationEmployee view) {
        this.view = view;
        this.userDao = new UserDao(); // Initialize your UserDao
        
         this.view.getRegisterEmployerButton().addActionListener(getRegisterEmployerActionListener());
        this.view.getRegisterJobSeekerButton().addActionListener(getRegisterJobSeekerActionListener());
    }

    @Override
    public void open() {
        view.setVisible(true);
    }

    @Override
    public void close() {
        view.dispose();
    }

    // --- Action Listeners for your Registration Buttons ---

    public ActionListener getRegisterEmployerActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration("Employer");
            }
        };
    }

    public ActionListener getRegisterJobSeekerActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration("Job Seeker");
            }
        };
    }

    // --- Core Registration Logic ---
    private void handleRegistration(String userRole) {
        String name = view.getEnteredName().trim();
        String email = view.getEnteredEmail().trim();
        char[] passwordChars = view.getEnteredPassword();
        char[] confirmPasswordChars = view.getEnteredConfirmPassword();
        String password = new String(passwordChars);
        String confirmPassword = new String(confirmPasswordChars);

        // Clear password arrays immediately after conversion for security
        Arrays.fill(passwordChars, ' ');
        Arrays.fill(confirmPasswordChars, ' ');

        // Basic validation - Now using JOptionPane
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(view, "Passwords do not match.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(view, "Please enter a valid email address.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email already exists
        if (userDao.checkEmailExists(email)) {
            JOptionPane.showMessageDialog(view, "Email already registered. Please use a different email or log in.", "Registration Failed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- PASSWORD HASHING (INSECURE: password.hashCode()) ---
        // Converts the password's hash code to a String.
        // **WARNING: This is NOT cryptographically secure and should only be used for temporary testing.**
        String hashedPassword = String.valueOf(password.hashCode());

        // Create a User object (or UserData) with the HASHED password
        UserData newUser = new UserData(name, email, hashedPassword, userRole);

        if (userDao.register(newUser)) {
            JOptionPane.showMessageDialog(view, "Registration successful as " + userRole + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            // Optionally clear fields in the view after success (you'd need to add a method to RegistrationEmployee)
            // view.clearRegistrationFields(); // Example
        } else {
            JOptionPane.showMessageDialog(view, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Clear String versions of passwords for security
        password = null;
        confirmPassword = null;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}