/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pahilopaila.Controller;

import java.util.function.Supplier;
import javax.swing.JFrame;
import pahilopaila.Controller.forgetpasscon.ButtonController;
import pahilopaila.view.Dashboard_JobSeeker_Applications;
import pahilopaila.view.Dashboard_JobSeeker_Vacancy;
import pahilopaila.view.Dashboard_JobSeekers;

/**
 *
 * @author abi
 */
public class ApplicationJS implements controller  {

    private final Dashboard_JobSeeker_Applications view;

    
 
    
    
    
    public ApplicationJS(Dashboard_JobSeeker_Applications view) {
       this.view =view;
       
       
        CreateViews cv = new CreateViews(Dashboard_JobSeekers.class,Dashboard_JobSeeker_Vacancy.class);
       
       
      ButtonController lol = new ButtonController(view.applicationslbl,view,view);
      ButtonController lol1 = new ButtonController(view.dashlbl,view,cv.getView(0),  new DashboardJController(cv.getView(0)));
      ButtonController lol2 = new ButtonController(view.vacanylbl,view,cv.getView(1));
       
       
    }
     
  
    
    public void open(){
    view.setVisible(true);
    }
    public void close(){
    view.dispose();
    }

   
    
}
