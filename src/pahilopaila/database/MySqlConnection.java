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
        String password="sabi@195";
        String database="marks";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+
                    database,username,password);
            return conn;
        }catch(ClassNotFoundException | SQLException e){
            return null;
        }
    }

    @Override
    public void closeConnection(Connection conn) {
        try{
            if(conn!=null && !conn.isClosed()){
                conn.close();
            }
        
        }catch(SQLException e){
        
        }
    }

    
    
}
