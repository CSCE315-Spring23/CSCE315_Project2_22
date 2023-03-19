import javax.swing.*;
import java.awt.event.*;
import java.util.*;


public class FrameHandler implements ActionListener {
    public JFrame frame;
    public Login login;
    public RedirectScreen redirect;
    public ManagerUI manager;
    public Server server;

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
}