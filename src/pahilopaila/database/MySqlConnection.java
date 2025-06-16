/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.database;

import java.sql.*;
/**
 *
 * @author Mibish
 */
public class MySqlConnection implements dbconnection{

    @Override
    public Connection openConnection() {
        String username="root";
        String password="12345678";
        String database="marks";
        Connection conn = null; // Initialize conn to null
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+
                                database,username,password);

            // --- Code to create the table if it doesn't exist ---
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                                    "    user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                                    "    name VARCHAR(255) NOT NULL," +
                                    "    email VARCHAR(255) UNIQUE NOT NULL," +
                                    "    password VARCHAR(255) NOT NULL," +
                                    "    user_role ENUM('Job Seeker', 'Employer') NOT NULL, " +
                                    "    otp VARCHAR(10), " +
                                    "    otp_expires_at TIMESTAMP " +
                                    ");";
            Statement stmt = null;
            try {
                stmt = conn.createStatement();
                stmt.execute(createTableSQL);
                System.out.println("Table 'users' checked/created successfully.");
            } catch (SQLException e) {
                System.err.println("Error creating/checking 'users' table: " + e.getMessage());
                // Depending on your application's needs, you might want to
                // throw this exception or handle it more robustly.
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close(); // Ensure the statement is closed
                    } catch (SQLException e) {
                        System.err.println("Error closing statement: " + e.getMessage());
                    }
                }
            }
            // --- End of table creation code ---

            return conn;
        }catch(ClassNotFoundException e){
            System.err.println("JDBC Driver not found: " + e.getMessage());
            return null; // Return null if driver is not found
        } catch(SQLException e){
            System.err.println("SQL Connection Error: " + e.getMessage());
            return null; // Return null if connection fails
        }
    }

    @Override
    public void closeConnection(Connection conn) {
        try{
            if(conn!=null && !conn.isClosed()){
                conn.close();
            }
        }catch(SQLException e){
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}