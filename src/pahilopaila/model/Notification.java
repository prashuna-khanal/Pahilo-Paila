package pahilopaila.model;

public class Notification {
    private int id;
    private int userId;
    private String message;
    private String timestamp;
    private boolean isImportant;
    private boolean isRead;

    // Existing constructor for creating new notifications
    public Notification(int id, int userId, String message, String timestamp, boolean isImportant) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.isImportant = isImportant;
        this.isRead = false;
    }

    // New constructor for retrieving notifications from database
    public Notification(int id, int userId, String message, String timestamp, boolean isImportant, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
        this.isImportant = isImportant;
        this.isRead = isRead;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    public boolean isImportant() { return isImportant; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean isRead) { this.isRead = isRead; }
}