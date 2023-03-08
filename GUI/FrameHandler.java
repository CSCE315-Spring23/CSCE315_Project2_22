import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;


public class FrameHandler implements ActionListener {

    public JFrame frame;
    public Login login;
    public RedirectScreen redirect;
    public ManagerUI manager;
    public Server server;

    /**

    Handles the different actions that can be performed on the panels. It listens

    for the action command from the event, and checks if the user is logging in

    correctly. If the user is logging in correctly and is a manager, it sets the

    content pane to the RedirectScreen panel. Otherwise, it sets the content pane

    to the Server panel. If the action command is for the ManagerView or ServerView,

    it sets the content pane to the corresponding panel. After the content pane is

    set, it repaints and revalidates the JFrame to display the new panel.

    @param e The ActionEvent object that contains the action command.
    */
    
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        ArrayList<Boolean> login_result = null;  // {correct login, is manager}
        if (action == "Login" && (login_result = login.checkLogin()).get(0)) {
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

        frame.repaint();
        frame.revalidate();

    }

    // public void addPanel(JPanel panel) {
    //     panels.add(panel);
    // }
}