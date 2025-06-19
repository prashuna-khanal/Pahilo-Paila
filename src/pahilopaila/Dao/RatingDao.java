/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Dao;

import pahilopaila.database.MySqlConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Acer
 */
public class RatingDao {
    private final Connection connection;

    public RatingDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("RatingDao initialized");
    }

    public boolean saveRating(int userId, int rating) {
        String sql = "INSERT INTO ratings (user_id, rating) VALUES (?, ?)";
        System.out.println("Saving rating for user_id: " + userId + " with rating: " + rating);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, rating);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rating saved successfully, rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    public ResultSet getRatingByUserId(int userId) {
        String sql = "SELECT * FROM ratings WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        System.out.println("Retrieving latest rating for user_id: " + userId);
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error retrieving rating: " + e.getMessage());
            return null;
        }
    }
}