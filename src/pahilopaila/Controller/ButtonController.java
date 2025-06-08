package pahilopaila.Controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;
import javax.swing.JFrame;
import javax.swing.JLabel;
import pahilopaila.view.Dashboard_JobSeeker_Applications;

public class ButtonController extends MouseAdapter {

    private final JLabel lbl;
    private final JFrame currentView;
    private final JFrame nextView;
    private final Color Fg;
    private final Color Bg;
    private final Controller ncon;
    

    public ButtonController(JLabel lbl, JFrame currentView, JFrame nextView,Supplier<Controller> ncon) {
        this.lbl = lbl;
        this.currentView = currentView;
        this.nextView = nextView;
        this.Fg = lbl.getForeground();
        this.Bg = lbl.getBackground();
        
        this.ncon = (Controller) ncon;
        
        this.lbl.addMouseListener(this);
    }
    public ButtonController(JLabel lbl, JFrame currentView, JFrame nextView) {
        this.lbl = lbl;
        this.currentView = currentView;
        this.nextView = nextView;
        this.Fg = lbl.getForeground();
        this.Bg = lbl.getBackground();
        this.ncon = null;
        
        
        this.lbl.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentView.dispose();
        if(ncon == null)
        {
        currentView.setVisible(true);
        }
        else { 
         
        ncon.open(); 
                }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        lbl.setForeground(Bg);
        lbl.setBackground(Fg);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        lbl.setForeground(Fg);
        lbl.setBackground(Bg);
    }
}

