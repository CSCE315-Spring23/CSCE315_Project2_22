import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Image;

public class Main {
    public static void main(String[] args) {
        // INITIALIZE THE FRAME HANDLER AND THE MAIN FRAME
        FrameHandler frame_handler = new FrameHandler();
        
        JFrame frame = new JFrame("Smoothie King");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_handler.frame = frame;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Image icon = Toolkit.getDefaultToolkit().getImage("../misc/smoothie_king.jpg");
        frame.setIconImage(icon);


        // INITIALIZE SUB PANELS AND ADD THEM TO THE MAIN FRAME
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