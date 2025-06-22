package pahilopaila.Dao;

import pahilopaila.database.MySqlConnection;
import pahilopaila.model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {
    private final Connection connection;

    public NotificationDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("NotificationDao initialized");
    }

    public boolean saveNotification(int userId, String message, String timestamp, boolean isImportant) {
        String sql = "INSERT INTO notifications (user_id, message, timestamp, is_important, is_read) VALUES (?, ?, ?, ?, ?)";
        System.out.println("Saving notification for user_id: " + userId + ", message: " + message);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, message);
            stmt.setString(3, timestamp);
            stmt.setBoolean(4, isImportant);
            stmt.setBoolean(5, false);
            int rows = stmt.executeUpdate();
            System.out.println("Notification saved successfully, rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error saving notification for user_id: " + userId + ": " + e.getMessage());
            return false;
        }
    }

    public List<Notification> getNotificationsByUserId(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY timestamp DESC";
        System.out.println("Retrieving notifications for user_id: " + userId);
        List<Notification> notifications = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notification notification = new Notification(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getString("timestamp"),
                        rs.getBoolean("is_important"),
                        rs.getBoolean("is_read") // Added is_read
                    );
                    notifications.add(notification);
                }
                System.out.println("Retrieved " + notifications.size() + " notifications for user_id: " + userId);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving notifications for user_id: " + userId + ": " + e.getMessage());
        }
        return notifications;
    }

    public boolean markNotificationRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = ? WHERE id = ?";
        System.out.println("Marking notification as read for notification_id: " + notificationId);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, notificationId);
            int rows = stmt.executeUpdate();
            System.out.println("Notification marked as read, rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error marking notification as read for notification_id: " + notificationId + ": " + e.getMessage());
            return false;
        }
    }

    public boolean markAllNotificationsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = ? WHERE user_id = ?";
        System.out.println("Marking all notifications as read for user_id: " + userId);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, true);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            System.out.println("Notifications marked as read, rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error marking all notifications as read for user_id: " + userId + ": " + e.getMessage());
            return false;
        }
    }
}