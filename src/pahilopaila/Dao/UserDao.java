/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Dao;


import pahilopaila.model.UserData;
import pahilopaila.model.LoginRequest;
import java.sql.*;
import pahilopaila.database.MySqlConnection;

/**
 *
 * @author Mibish
 */
public class UserDao {
    MySqlConnection mySql = new MySqlConnection();
    public boolean register(UserData user){
        String query = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
        Connection conn = (Connection) mySql.openConnection();
        try{
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1,user.getName());
            stmnt.setString(2,user.getEmail());
            stmnt.setString(3,user.getPassword());
            int result = stmnt.executeUpdate();
            return result>0;
            
        }catch(SQLException e){
            return false;
        }finally{
            mySql.closeConnection(conn);
        }   
    }
    public UserData login(LoginRequest LoginReq) throws SQLException{
        String query= "SELECT FROM users where name=? and fpassword=?";
        Connection conn = mySql.openConnection();
        try{
            PreparedStatement stmnt = conn.prepareStatement(query);
            stmnt.setString(1,LoginReq.getName());
            stmnt.setString(2,LoginReq.getPassword());
            ResultSet result= stmnt.executeQuery();
            if(result.next()){
                String name = result.getString("fname");
                String password = result.getString("fpassword");
                String id = result.getString("id");
                UserData user = new UserData(id,name,password);
                return user;
            }else{
                return null;
            }
        }catch (SQLException e){
            return null; 
        }finally{
            mySql.closeConnection(conn);
        }
    }
    public boolean checkName(String name){
        String query = "SELECT * FROM users WHERE name=?";
        Connection conn= mySql.openConnection();
        try{
            PreparedStatement stmnt = conn.prepareStatement(query);
            ResultSet result = stmnt.executeQuery();
            if (result.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            return false;
        }finally{
            mySql.closeConnection(conn);
        }
    }
}
