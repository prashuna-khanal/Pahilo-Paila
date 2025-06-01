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

/**
 *
 * @author abi
 */
public class DashboardJController {
    
    private final JFrame dashview;
    private final JFrame dashapps;
    private final JFrame Vacancy;
    public DashboardJController(){
       CreateViews cv = new CreateViews(Dashboard_JobSeekers.class,Dashboard_JobSeeker_Applications.class,Dashboard_JobSeeker_Vacancy.class);
       this.dashview = cv.getView(0);
       this.dashapps = cv.getView(1);
       this.Vacancy = cv.getView(2);
       ButtonController dash = new ButtonController(dashview.dashlbl,dashview,dashapps);
    }
    
    public void open(){
    view.setVisible(true);
    }
    public void close(){
    
    view.dispose();
        
   }
    
}
