/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pahilopaila;
import pahilopaila.Controller.registrationController;
import pahilopaila.view.RegistrationEmployee;

/**
 *
 * @author LENOVO
 */
public class PahiloPaila {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RegistrationEmployee view = new RegistrationEmployee();
        registrationController con1 = new  registrationController(view);
        con1.open();
       
    }
    
}
