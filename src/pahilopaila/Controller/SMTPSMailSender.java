/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Controller;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author abi
 */
public class SMTPSMailSender {
    private static final String host = "smtp.gmail.com";
    private static final String port = "587";    
    private static final String email = "abi.mukhiya@gmail.com"; // **CHANGE THIS** Your sending email address
    private static String password = "cghj tjst vwdz pnxf "; // **CHANGE THIS** Your email password or app-specific password

    // Instantiate your new UserVerificationController
    private static final UserVerificationController userVerifier = new UserVerificationController();

    // Method to generate a random 6-digit OTP
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Send Email Method (remains unchanged as it's generic)
    public static boolean sendMail(String recipient, String subject, String body) {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);    
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Mail sent successfully! " + body);
            return true;
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return false;
        }
    }

    // Updated method for sending Forget Password OTP email, now using UserVerificationController
    public static boolean sendForgetPasswordOTP(String recipientEmail) {
        // 1. Check if user exists using the UserVerificationController
        if (!userVerifier.userExists(recipientEmail)) {
            System.out.println("User with email " + recipientEmail + " does not exist. OTP not sent.");
            return false; // User not found, do not send OTP
        }

        String otp = generateOTP(); // Generate the OTP
        LocalDateTime expiryTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES); // OTP valid for 10 minutes

        // 2. Store the OTP in the database using the UserVerificationController
        boolean otpStored = userVerifier.storeOTP(recipientEmail, otp, expiryTime);

        if (otpStored) {
            String subject = "Password Reset Request - Your OTP";
            String body = "Dear User,\n\nYou have requested a password reset. Your One-Time Password (OTP) is: " + otp + 
                          "\n\nPlease use this OTP to verify your identity and reset your password. This OTP is valid for a short period." +
                          "\n\nIf you did not request this, please ignore this email." +
                          "\n\nRegards,\nYour Application Team";
            
            System.out.println("Generated OTP for " + recipientEmail + ": " + otp); // For debugging

            // 3. Send the OTP email
            return sendMail(recipientEmail, subject, body);
        } else {
            System.err.println("Failed to store OTP in database for " + recipientEmail + ". Email not sent.");
            return false;
        }
    }
}

