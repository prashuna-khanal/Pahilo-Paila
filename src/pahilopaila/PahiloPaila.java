/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pahilopaila;
import pahilopaila.Controller.LoginController;
import pahilopaila.view.LoginPageview;

/**
 *
 * @author LENOVO
 */
public class PahiloPaila {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LoginPageview loginView = new LoginPageview();
    LoginController loginController = new LoginController(loginView);
    loginController.open();
       
    }
    
}
