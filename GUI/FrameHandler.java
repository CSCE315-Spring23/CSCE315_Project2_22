import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;


public class FrameHandler implements ActionListener {

    public JFrame frame;
    public Login login;
    public TableUI tables;

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action == "Login" && login.checkLogin()) {
            frame.setContentPane(tables);
            frame.repaint();
            frame.revalidate();
        }

    }

    // public void addPanel(JPanel panel) {
    //     panels.add(panel);
    // }



}