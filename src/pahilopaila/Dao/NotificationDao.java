/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Dao;

import pahilopaila.database.MySqlConnection; // MODIFIED: Added import for MySqlConnection
import pahilopaila.model.Notification;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing notifications in the database.
 */
public class NotificationDao {
    private final Connection connection; // MODIFIED: Made final to match RatingDao

    public NotificationDao() {
        connection = MySqlConnection.getInstance().getConnection(); // MODIFIED: Use MySqlConnection singleton
        System.out.println("NotificationDao initialized"); // NEW: Added logging to match RatingDao
    }

    public boolean saveNotification(int userId, String message, String timestamp, boolean isImportant) {
        String sql = "INSERT INTO notifications (user_id, message, timestamp, is_important) VALUES (?, ?, ?, ?)";
        System.out.println("Saving notification for user_id: " + userId + " with message: " + message); // NEW: Added logging
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.setString(3, timestamp);
            stmt.setBoolean(4, isImportant);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Notification saved successfully, rows affected: " + rowsAffected); // NEW: Added logging
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage()); // MODIFIED: Match RatingDao error logging
            return false;
        }
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT id, user_id, message, timestamp, is_important FROM notifications WHERE user_id = ? ORDER BY timestamp DESC";
        System.out.println("Retrieving notifications for user_id: " + userId); // NEW: Added logging
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Notification notification = new Notification(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getString("timestamp"),
                    rs.getBoolean("is_important")
                );
                notifications.add(notification);
            }
            rs.close();
            System.out.println("Retrieved " + notifications.size() + " notifications for user_id: " + userId); // NEW: Added logging
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage()); // MODIFIED: Match RatingDao error logging
        }
        return notifications;
    }
}