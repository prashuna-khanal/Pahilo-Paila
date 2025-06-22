package pahilopaila.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private static MySqlConnection instance;
    private Connection connection;

    private final String url = "jdbc:mysql://localhost:3306/pahilopaila?allowPublicKeyRetrieval=true&useSSL=false";
    private final String username = "root"; // Replace with your MySQL username
    private final String password = "sabi@195"; // Replace with your MySQL password

    private MySqlConnection() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static MySqlConnection getInstance() {
        if (instance == null) {
            instance = new MySqlConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    
}