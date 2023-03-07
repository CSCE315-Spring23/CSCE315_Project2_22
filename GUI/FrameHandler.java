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
    public TableUI tables;

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        ArrayList<Boolean> login_result = null;  // {correct login, is manager}
        if (action == "Login" && (login_result = login.checkLogin()).get(0)) {
            System.out.println(login_result);
            if (login_result.get(1)) {
                frame.setContentPane(redirect);
            }
            else {
                // go to server side
            }
        }
        else if (action == "ManagerView") {
            frame.setContentPane(tables);
        }
        else if (action == "ServerView") {
            
        }

        frame.repaint();
        frame.revalidate();

    }

    // public void addPanel(JPanel panel) {
    //     panels.add(panel);
    // }



}