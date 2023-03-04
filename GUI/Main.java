import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        FrameHandler frame_handler = new FrameHandler();
        
        JFrame frame = new JFrame("MainFrame");
        frame_handler.frame = frame;
        frame.setSize(new Dimension(600, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(new Point(500, 300));

        Login login_panel = new Login(frame, frame_handler);
        TableUI table_panel = new TableUI(frame, frame_handler);

        fh.login = login_panel;
        fh.tables = table_panel;

        frame.add(login_panel);
        frame.add(table_panel);
        frame.setContentPane(login_panel);
        frame.setVisible(true);
    }





}