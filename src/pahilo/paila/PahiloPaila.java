/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pahilo.paila;

import Pahileo.Controller.DashboardJController;
import Pahileo.Controller.registrationController;
import pahilopaila.view.Dashboard_JobSeekers;
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
        Dashboard_JobSeekers view = new Dashboard_JobSeekers();
        DashboardJController con = new DashboardJController(view);
        con.open();
    }
    
}
