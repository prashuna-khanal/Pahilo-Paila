package pahilopaila.Controller.forgetpasscon;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Supplier;
import javax.swing.JFrame;
import javax.swing.JLabel;
import pahilopaila.view.Dashboard_JobSeeker_Applications; // Keep this if used elsewhere in your project

public class ButtonController extends MouseAdapter {

    private final JLabel lbl;
    private final JFrame currentView;
    private final JFrame nextView;
    private final Color Fg;
    private final Color Bg;
    private final controller ncon; 

    public ButtonController(JLabel lbl, JFrame currentView, JFrame nextView, Supplier<Controller> nconSupplier) {
        this.lbl = lbl;
        this.currentView = currentView;
        this.nextView = nextView;
        this.Fg = lbl.getForeground();
        this.Bg = lbl.getBackground();
        
        // Correctly get the Controller instance from the Supplier
        this.ncon = nconSupplier.get(); 
        
        this.lbl.addMouseListener(this);
    }

    public ButtonController(JLabel lbl, JFrame currentView, JFrame nextView) {
        this.lbl = lbl;
        this.currentView = currentView;
        this.nextView = nextView;
        this.Fg = lbl.getForeground();
        this.Bg = lbl.getBackground();
        this.ncon = null; // No new controller supplied
        
        this.lbl.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentView.dispose(); // Close the current view

        if (ncon == null) {
            // If no new controller, just show the next view directly
            nextView.setVisible(true);
        } else {
            // If a new controller is provided, let it handle opening the next view
            ncon.open(); 
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Swap foreground and background colors on hover
        lbl.setForeground(Bg);
        lbl.setBackground(Fg);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Revert to original colors when mouse exits
        lbl.setForeground(Fg);
        lbl.setBackground(Bg);
    }
}