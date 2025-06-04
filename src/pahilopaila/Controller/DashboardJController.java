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
public class DashboardJController implements Controller {
    
   
    final JFrame Applic;
    final JFrame Vacancy;
    private final Dashboard_JobSeekers view;
    
    
    public DashboardJController(Dashboard_JobSeekers view){
       this.view = view;
        
      
       CreateViews rs = new CreateViews(Dashboard_JobSeeker_Applications.class,Dashboard_JobSeeker_Vacancy.class);
      
       this.Applic = rs.getView(0);
      
       this.Vacancy= rs.getView(1);
     new ButtonController(view.dashlbl,view,view);
     new ButtonController(view.applicationslbl,view,Applic);
        new ButtonController(view.vacanylbl,view,Vacancy);
       
       
    }
    
     
    public void open(){
    view.setVisible(true);
    }
    public void close(){
    view.dispose();
    }
    
}
