package pahilopaila.Dao;

import pahilopaila.model.Notification;
import pahilopaila.database.MySqlConnection;
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
        System.out.println("Saving notification for user_id: " + userId);
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
            System.err.println("Error saving notification: " + e.getMessage());
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
                        rs.getBoolean("is_important")
                    );
                    notifications.add(notification);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving notifications: " + e.getMessage());
        }
        return notifications;
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
            System.err.println("Error marking notifications as read: " + e.getMessage());
            return false;
        }
    }
}