/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pahileo.Controller;

import javax.swing.JFrame;
import javax.swing.JLabel;
import pahilopaila.view.Dashboard_JobSeekers;
import pahilopaila.view.RegistrationEmployee;
import pahilopaila.view.Dashboard_JobSeeker_Applications;
import pahilopaila.view.Dashboard_JobSeeker_Vacancy;

/**
 *
 * @author abi
 */
public class DashboardJController {
    
    private final JFrame view;
    private final JFrame Applic;
    private final JFrame Vacancy;
    
    public DashboardJController(Dashboard_JobSeekers view){
       CreateViews cv = new CreateViews(Dashboard_JobSeeker_Applications.class,Dashboard_JobSeeker_Vacancy.class);
       this.view = view;
       this.Applic = cv.getView(0);
       this.Vacancy= cv.getView(1);
       ButtonController dash = new ButtonController(view.dashlbl,view,view);
       ButtonController applic = new ButtonController(view.applicationslbl,view,Applic);
       ButtonController vaca = new ButtonController(view.vacanylbl,view,Vacancy);
       
       
    }
    
    public void open(){
    view.setVisible(true);
    }
    public void close(){
    
    view.dispose();
        
   }
    
}
