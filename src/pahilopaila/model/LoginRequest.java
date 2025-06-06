/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.model;

/**
 *
 * @author Mibish
 */
public class LoginRequest {
    private String name;
    private String password;
    public LoginRequest(String email, String password){
        this.name=name;
        this.password=password;
    }
    public void setName(String email){
        this.name=name;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
}
