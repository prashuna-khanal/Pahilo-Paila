package pahilopaila;

import pahilopaila.view.LoginPageview;
import pahilopaila.Controller.LoginController;

public class PahiloPaila {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            LoginPageview loginView = new LoginPageview();
            LoginController loginController = new LoginController(loginView);
            loginController.open();
        });
    }
}