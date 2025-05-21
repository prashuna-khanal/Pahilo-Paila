/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Database;

import java.sql.*;
import java.sql.ResultSet;

/**
 *
 * @author abi
 */
public class MySqlConnection implements DBconnection{

    @Override
    public Connection openConnection() {
       String username= "root";
       String password = "sabi@195";
       String database = "mydb";
       try{
           Class.forName("com.mysql.jdbc.Driver");
           Connection conn;
           conn = DriverManager.getConnection("jdbc:mysql://localhost:3306"+ database, username,password);
       return conn;}
       catch (Exception e){
           System.out.print("lol");
           return null;
       }
    }

    @Override
    public void closeConnection(Connection conn) {
        try{
            if(conn!=null && !conn.isClosed()){
            conn.close();
            }}catch(Exception e){
            }
    }


    
}
