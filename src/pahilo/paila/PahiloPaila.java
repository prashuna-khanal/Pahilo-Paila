/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pahilo.paila;

import Pahileo.Controller.ApplicationJS;
import Pahileo.Controller.DashboardJController;
import pahilopaila.Controller.forgetpasscon.registrationController;
import pahilopaila.view.Dashboard_JobSeeker_Applications;
import pahilopaila.view.Dashboard_JobSeekers;
import pahilopaila.view.RegistrationEmployee;
import Pahileo.Controller.controller;

/**
 *
 * @author LENOVO
 */
public class PahiloPaila {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Dashboard_JobSeeker_Applications view = new Dashboard_JobSeeker_Applications();
        ApplicationJS con1 = new  ApplicationJS(view);
        con1.open();
       
    }
    
}
