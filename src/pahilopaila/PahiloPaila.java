/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pahilopaila;
import pahilopaila.Controller.ForgetPasswordController;
import pahilopaila.view.forgotpassview;

/**
 *
 * @author LENOVO
 */
public class PahiloPaila {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        forgotpassview view = new forgotpassview();
        ForgetPasswordController con1 = new  ForgetPasswordController(view);
        con1.open();
       
    }
    
}
