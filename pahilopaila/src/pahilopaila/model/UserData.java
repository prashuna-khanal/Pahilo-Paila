/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.model;

/**
 *
 * @author abi
 */
public class UserData {
    private String name;
    private String email;
    private String password;
    private String user_type;
    public UserData(String name, String email, String password,String user_type){
    this.name = name;
    this.email =email;
    this.password = password;
    this.user_type = user_type;
    }
    public void setname(String name){
    this.name= name ;}
    }
    public void setemail(String email){
    this.email= email;
}
    public void setpass(String pasword){
        this.password= password ;}
    }
    public void setutype(String user_type){
        this.user_type= user_type;
    }

 public String getname(){
return this.name;
}

 public String getname(){
return this.name;}
    }

public String email(){
return this.email;}
    }
public String pass(){
return this.password;}

public String getutype(){
return this.user_type;}
    }

}
