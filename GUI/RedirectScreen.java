import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;

//Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
//Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

public class RedirectScreen extends JPanel  {  //implements ActionListener
    // load the employees table for checking passwords
    // add two password fields
    // figure out how to use username and password together
    // in username password field, get the associated password so it can be checked against password
    // config the frame and display
    private JFrame frame;
    private FrameHandler fh;
    private static JPanel panel;
    private static JButton server_view;
    private static JButton manager_view;

    private static ArrayList<ArrayList<String>> employee_data;

    RedirectScreen(JFrame frame, FrameHandler fh) {
        this.frame = frame;
        this.fh = fh;
        try {
            initialize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        // panel = new JPanel();  // default flow layout
        this.setLayout(null);

        int frame_width = frame.getSize().width, frame_height = frame.getSize().height;
        int centerx = Math.floorDiv(frame_width, 2);
        int centery = Math.floorDiv(frame_height, 2);

        server_view = new JButton("ServerView");
        server_view.setBounds(centerx - 165, centery - 25, 150, 50);
        server_view.setForeground(Color.WHITE);
        server_view.setBackground(Color.BLACK);
        server_view.addActionListener(fh);
        this.add(server_view);

        manager_view = new JButton("ManagerView");
        manager_view.setBounds(centerx + 15, centery - 25, 150, 50);
        manager_view.setForeground(Color.WHITE);
        manager_view.setBackground(Color.BLACK);
        manager_view.addActionListener(fh);
        this.add(manager_view);

        this.setSize(frame.getSize());
    }
}



