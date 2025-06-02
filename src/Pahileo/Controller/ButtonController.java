package Pahileo.Controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ButtonController extends MouseAdapter {

    private final JLabel lbl;
    private final JFrame currentView;
    private final JFrame nextView;
    private final Color Fg;
    private final Color Bg;

    public ButtonController(JLabel lbl, JFrame currentView, JFrame nextView) {
        this.lbl = lbl;
        this.currentView = currentView;
        this.nextView = nextView;
        this.Fg = lbl.getForeground();
        this.Bg = lbl.getBackground();

        // Attach this listener to the label
        this.lbl.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        currentView.dispose();  // Close the current window
        nextView.setVisible(true);  // Open the next window
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
