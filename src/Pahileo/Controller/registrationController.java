/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pahileo.Controller;

import pahilopaila.view.RegistrationEmployee;

/**
 *
 * @author abi
 */
public class registrationController {

    private final RegistrationEmployee view;
    public registrationController(RegistrationEmployee view){
        this.view = view;
    }
    
    public void open(){
    view.setVisible(true);
    }
    public void close(){
    
    view.dispose();
    
    
    }
}
