package Pahileo.Controller;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.List;

public class CreateViews {

    private final List<JFrame> views = new ArrayList<>();

    
    public CreateViews(Class<? extends JFrame>... viewClasses) {
        for (Class<? extends JFrame> viewClass : viewClasses) {
            try {
                JFrame view = viewClass.getDeclaredConstructor().newInstance();
                views.add(view);
            } catch (Exception e) {
                System.err.println("Failed to create view: " + viewClass.getName());
                e.printStackTrace();
            }
        }
    }

    public List<JFrame> getViews() {
        return views;
    }

    public JFrame getView(int index) {
        return views.get(index);
    }

    public int size() {
        return views.size();
    }
}
