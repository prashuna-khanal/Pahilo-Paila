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
public interface dbconnection {
    Connection openConnection();
    void closeConnection(Connection conn);
    ResultSet runQuery(Connection conn,String query);
    int executeUpdate(Connection conn, String query);
    
    
}
