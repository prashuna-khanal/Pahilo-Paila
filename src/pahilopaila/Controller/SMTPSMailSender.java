package pahilopaila.Controller;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SMTPSMailSender {
    private static final String host = "smtp.gmail.com";
    private static final String port = "587";
    private static final String email = "abi.mukhiya@gmail.com"; // Update with your email
    private static final String password = "cghj tjst vwdz pnxf"; // Update with your App Password
    private static final UserVerificationController userVerifier = new UserVerificationController();

    public static String generateOTP() {
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public static boolean sendMail(String recipient, String subject, String body) {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
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
            System.out.println("Mail sent successfully to " + recipient);
            return true;
        } catch (MessagingException mex) {
            System.err.println("Error sending mail: " + mex.getMessage());
            mex.printStackTrace();
            return false;
        }
    }

    public static boolean sendForgetPasswordOTP(String recipientEmail) {
        if (!userVerifier.userExists(recipientEmail)) {
            System.out.println("User with email " + recipientEmail + " does not exist.");
            return false;
        }

        String otp = generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plus(10, ChronoUnit.MINUTES);
        boolean otpStored = userVerifier.storeOTP(recipientEmail, otp, expiryTime);

        if (otpStored) {
            String subject = "Password Reset Request - Your OTP";
            String body = "Dear User,\n\nYour OTP for password reset is: " + otp +
                          "\n\nPlease use this OTP to reset your password. It is valid for 10 minutes." +
                          "\n\nIf you did not request this, please ignore this email.\n\nRegards,\nPahiloPaila Team";
            return sendMail(recipientEmail, subject, body);
        } else {
            System.err.println("Failed to store OTP for " + recipientEmail);
            return false;
        }
    }
}