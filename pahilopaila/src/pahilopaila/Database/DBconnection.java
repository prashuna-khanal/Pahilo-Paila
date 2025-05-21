/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pahilopaila.Database;
import java.sql.*;

/**
 *
 * @author abi
 */
public interface DBconnection {
    Connection openConnection();
    void closeConnection(Connection conn);
   
}
