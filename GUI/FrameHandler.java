import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;


/**
 * Handles actions for the GUI which switch between frames that are entirely separated from each other. For instance, from the login frame
 * to the RedirectScreen.
 */
public class FrameHandler implements ActionListener {

    public JFrame frame;
    public Login login;
    public RedirectScreen redirect;
    public ManagerUI manager;
    public Server server;
    public int active_employee;

    /**

    * Handles the different actions that can be performed on the panels. It listens
    * for the action command from the event, and checks if the user is logging in
    * correctly. If the user is logging in correctly and is a manager, it sets the
    * content pane to the RedirectScreen panel. Otherwise, it sets the content pane
    * to the Server panel. If the action command is for the ManagerView or ServerView,
    * it sets the content pane to the corresponding panel. Also handles the Home buttons
    * which take the user back to the login screen. After the content pane is
    * set, it repaints and revalidates the JFrame to display the new panel.
    * @param e The ActionEvent object that triggered the listener.
    */
    
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        ArrayList<Boolean> login_result = null;  // {correct login, is manager}
        if (action == "Login" && (login_result = login.checkLogin()).get(0)) {
            active_employee = login.active_employee;
            if (login_result.get(1)) {
                frame.setContentPane(redirect);
            }
            else {
                frame.setContentPane(server);
            }
        }
        else if (action == "ManagerView") {
            frame.setContentPane(manager);
        }
        else if (action == "ServerView") {
            frame.setContentPane(server);
        }
        else if (action == "Home") {
            frame.setContentPane(login);
        }

        frame.repaint();
        frame.revalidate();

    }

}