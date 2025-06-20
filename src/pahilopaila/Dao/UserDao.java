package pahilopaila.Dao;

import pahilopaila.database.MySqlConnection;
import pahilopaila.model.LoginRequest;
import pahilopaila.model.UserData;
import java.sql.*;

public class UserDao {
    private final Connection connection;

    public UserDao() {
        connection = MySqlConnection.getInstance().getConnection();
        System.out.println("UserDao initialized");
    }

    public UserData login(LoginRequest loginReq) {
        String sql = "SELECT id, username, email, user_password, roles FROM users WHERE email = ? AND user_password = ?";
        System.out.println("Executing login query for email: " + loginReq.getEmail());
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, loginReq.getEmail());
            stmt.setString(2, String.valueOf(loginReq.getPassword().hashCode()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                UserData user = new UserData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("roles")
                );
                System.out.println("Login successful for user: " + user.getName());
                return user;
            } else {
                System.out.println("No user found or password mismatch");
            }
        } catch (SQLException e) {
            System.err.println("SQL error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        System.out.println("Checking if email exists: " + email);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                System.out.println("Email exists: " + exists);
                return exists;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during email check: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerUser(String name, String email, String password, String role) {
        String sql = "INSERT INTO users (username, email, user_password, roles) VALUES (?, ?, ?, ?)";
        System.out.println("Registering user: " + name + ", Email: " + email + ", Role: " + role);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, String.valueOf(password.hashCode()));
            stmt.setString(4, role);
            int rows = stmt.executeUpdate();
            System.out.println("Registration rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("SQL error during registration: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyPassword(int userId, String password) {
        String sql = "SELECT user_password FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("user_password");
                String inputHash = String.valueOf(password.hashCode());
                return storedHash.equals(inputHash);
            }
        } catch (SQLException e) {
            System.err.println("SQL error during password verification: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(int userId, String username, String email, String newPassword) {
        String sql = "UPDATE users SET username = ?, email = ?, user_password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, String.valueOf(newPassword.hashCode()));
            stmt.setInt(4, userId);
            int rows = stmt.executeUpdate();
            System.out.println("Update user rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("SQL error during user update: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public UserData getUserById(int userId) {
        String sql = "SELECT id, username, email, roles FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UserData(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("roles")
                );
            }
        } catch (SQLException e) {
            System.err.println("SQL error during user fetch: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}