package pahilopaila.Controller;

import pahilopaila.view.forgotpassview;
import pahilopaila.view.LoginPageview;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgetPasswordController implements ActionListener, controller {
    private final forgotpassview view;
    private final UserVerificationController userVerificationController;
    private String userEmailForReset;

    public ForgetPasswordController(forgotpassview view) {
        this.view = view;
        this.userVerificationController = new UserVerificationController();
        this.view.getBtnSendOtp().addActionListener(this);
        this.view.getBtnResetPassword().addActionListener(this);
        setOtpFieldsEnabled(false);
    }

    private void setOtpFieldsEnabled(boolean enable) {
        view.getTxtOtp().setEnabled(enable);
        view.getTxtNewPassword().setEnabled(enable);
        view.getTxtConfirmNewPassword().setEnabled(enable);
        view.getBtnResetPassword().setEnabled(enable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnSendOtp()) {
            handleSendOtpAction();
        } else if (e.getSource() == view.getBtnResetPassword()) {
            handleResetPasswordAction();
        }
    }

    private void handleSendOtpAction() {
        userEmailForReset = view.getTxtEmail().getText().trim();
        if (userEmailForReset.isEmpty()) {
            displayMessage("Please enter your email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        view.getBtnSendOtp().setEnabled(false);
        displayMessage("Sending OTP...", "Info", JOptionPane.INFORMATION_MESSAGE);

        new Thread(() -> {
            boolean success = SMTPSMailSender.sendForgetPasswordOTP(userEmailForReset);
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (success) {
                    displayMessage("OTP sent to " + userEmailForReset + ". Check your inbox/spam.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    setOtpFieldsEnabled(true);
                    view.getTxtEmail().setEnabled(false);
                } else {
                    displayMessage("Failed to send OTP. Check email or try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    view.getBtnSendOtp().setEnabled(true);
                }
            });
        }).start();
    }

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
            displayMessage("Password must be at least 6 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        displayMessage("Verifying OTP and resetting password...", "Info", JOptionPane.INFORMATION_MESSAGE);
        view.getBtnResetPassword().setEnabled(false);

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
                        close();
                        LoginPageview loginView = new LoginPageview();
                        LoginController loginController = new LoginController(loginView);
                        loginController.open();
                    } else {
                        displayMessage("Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
                        view.getBtnResetPassword().setEnabled(true);
                    }
                } else {
                    displayMessage("Invalid or expired OTP.", "Error", JOptionPane.ERROR_MESSAGE);
                    view.getBtnResetPassword().setEnabled(true);
                }
            });
        }).start();
    }

    private void displayMessage(String message, String title, int messageType) {
        if (view.getLblMessage() != null) {
            view.getLblMessage().setText(message);
        } else {
            JOptionPane.showMessageDialog(view, message, title, messageType);
        }
    }

    private void clearFields() {
        view.getTxtEmail().setText("");
        view.getTxtOtp().setText("");
        view.getTxtNewPassword().setText("");
        view.getTxtConfirmNewPassword().setText("");
        if (view.getLblMessage() != null) {
            view.getLblMessage().setText("");
        }
    }

    @Override
    public void open() {
        view.setVisible(true);
    }

    @Override
    public void close() {
        view.dispose();
    }
}