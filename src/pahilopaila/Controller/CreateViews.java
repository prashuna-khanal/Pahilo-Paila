package Pahileo.Controller;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class CreateViews {

    private final List<JFrame> views = new ArrayList<>();

    @SafeVarargs
    public CreateViews(Class<? extends JFrame>... viewClasses) {
        for (Class<? extends JFrame> viewClass : viewClasses) {
            try {
                JFrame view = viewClass.getDeclaredConstructor().newInstance();
                views.add(view);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace(); // or throw a runtime exception if it's critical
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
