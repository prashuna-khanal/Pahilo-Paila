package pahilopaila.Controller;

import pahilopaila.view.forgotpassview; // Import your JForm View class
import pahilopaila.view.LoginPageview; // Import LoginPageview
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.swing.JOptionPane;



public class ForgetPasswordController implements ActionListener,controller {

    // --- Reference to the entire View ---
    private final forgotpassview view;

    // --- Backend Logic Instances ---
    private final UserVerificationController userVerificationController;

    // --- Data to be maintained across steps ---
    private String userEmailForReset; // Store the email after 'Send OTP' for later use

    /**
     * Constructor to link the controller with the entire view.
     * @param view The instance of your ForgetPasswordView JForm.
     */
    public ForgetPasswordController(forgotpassview view) {
        this.view = view;
        this.userVerificationController = new UserVerificationController();

        // --- Attach Action Listeners to Buttons through the view's getters ---
        this.view.getBtnSendOtp().addActionListener(this);
        this.view.getBtnResetPassword().addActionListener(this);

        // --- Initially disable OTP and password fields through the view's getters ---
        setOtpFieldsEnabled(false);
    }

    /**
     * Helper method to enable/disable OTP and password input fields.
     */
    private void setOtpFieldsEnabled(boolean enable) {
        view.getTxtOtp().setEnabled(enable);
        view.getTxtNewPassword().setEnabled(enable);
        view.getTxtConfirmNewPassword().setEnabled(enable);
        view.getBtnResetPassword().setEnabled(enable);
    }

    /**
     * Centralized action handling for all buttons.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnSendOtp()) {
            handleSendOtpAction();
        } else if (e.getSource() == view.getBtnResetPassword()) {
            handleResetPasswordAction();
        }
    }

    /**
     * Handles the logic for the "Send OTP" button click.
     */
    private void handleSendOtpAction() {
        userEmailForReset = view.getTxtEmail().getText().trim(); // Get email from textbox

        if (userEmailForReset.isEmpty()) {
            displayMessage("Please enter your email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Disable send OTP button to prevent multiple clicks
        view.getBtnSendOtp().setEnabled(false);
        displayMessage("Sending OTP...", "Info", JOptionPane.INFORMATION_MESSAGE);

        new Thread(() -> {
            boolean success = SMTPSMailSender.sendForgetPasswordOTP(userEmailForReset);
            javax.swing.SwingUtilities.invokeLater(() -> { // Update UI on EDT
                if (success) {
                    displayMessage("OTP sent to " + userEmailForReset + ". Please check your inbox and spam folder.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setOtpFieldsEnabled(true); // Enable OTP and password fields
                    view.getTxtEmail().setEnabled(false); // Disable email field after OTP is sent
                } else {
                    displayMessage("Failed to send OTP. Please check your email or try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    view.getBtnSendOtp().setEnabled(true); // Re-enable button on failure
                }
            });
        }).start();
    }

    /**
     * Handles the logic for the "Reset Password" button click.
     */
    private void handleResetPasswordAction() {
        String otpEntered = view.getTxtOtp().getText().trim();
        String newPassword = new String(view.getTxtNewPassword().getPassword());
        String confirmNewPassword = new String(view.getTxtConfirmNewPassword().getPassword());

        if (otpEntered.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            displayMessage("All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            displayMessage("New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.length() < 6) {
            displayMessage("Password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        displayMessage("Verifying OTP and resetting password...", "Info", JOptionPane.INFORMATION_MESSAGE);
        view.getBtnResetPassword().setEnabled(false); // Disable button to prevent multiple clicks

        new Thread(() -> {
            boolean otpVerified = userVerificationController.verifyOTP(userEmailForReset, otpEntered);

            javax.swing.SwingUtilities.invokeLater(() -> {
                if (otpVerified) {
                    String hashedNewPassword = userVerificationController.hashPassword(newPassword);

                    boolean passwordUpdated = userVerificationController.updatePassword(userEmailForReset, hashedNewPassword);

                    if (passwordUpdated) {
                        displayMessage("Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearFields();
                        setOtpFieldsEnabled(false);
                        view.getTxtEmail().setEnabled(true);
                        view.getBtnSendOtp().setEnabled(true);

                        // --- Navigate back to the login page ---
                        close(); // Close the forgot password view
                        LoginPageview loginView = new LoginPageview();
                        LoginController loginController = new LoginController(loginView);
                        loginController.open();
                        // ----------------------------------------
                    } else {
                        displayMessage("Failed to update password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        view.getBtnResetPassword().setEnabled(true);
                    }
                } else {
                    displayMessage("Invalid or expired OTP. Please request a new one.", "Error", JOptionPane.ERROR_MESSAGE);
                    view.getBtnResetPassword().setEnabled(true);
                }
            });
        }).start();
    }

    /**
     * Displays a message to the user, either in a JLabel or JOptionPane.
     */
    private void displayMessage(String message, String title, int messageType) {
        if (view.getLblMessage() != null) { // Check if the JLabel exists
            view.getLblMessage().setText(message);
        } else {
            JOptionPane.showMessageDialog(view, message, title, messageType); // Use 'view' as parent component
        }
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        view.getTxtEmail().setText("");
        view.getTxtOtp().setText("");
        view.getTxtNewPassword().setText("");
        view.getTxtConfirmNewPassword().setText("");
        if (view.getLblMessage() != null) {
            view.getLblMessage().setText("");
        }
    }


    
    public void open() {
        view.setVisible(true);
    }

    public void close() {
      view.dispose();
    }
}