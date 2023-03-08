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
        RedirectScreen redirect_panel = new RedirectScreen(frame, frame_handler);
        ManagerUI manager_panel = new ManagerUI(frame, frame_handler);
        Server server_panel = new Server(frame, frame_handler);

        frame_handler.login = login_panel;
        frame_handler.redirect = redirect_panel;
        frame_handler.manager = manager_panel;
        frame_handler.server = server_panel;

        frame.add(login_panel);
        frame.add(redirect_panel);
        frame.add(manager_panel);
        frame.add(server_panel);
        frame.setContentPane(login_panel);
        frame.setVisible(true);
    }





}