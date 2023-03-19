import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class Login extends JPanel  {
    private JFrame frame;
    private FrameHandler fh;
    private static JPanel panel;
    private static JTextField username;
    private static JButton submit;
    private static JPasswordField password;
    private static JLabel username_label, password_label;
    private static ArrayList<ArrayList<String>> employee_data;


    Login(JFrame frame, FrameHandler fh) {
        this.frame = frame;
        this.fh = fh;

        try {
            loadEmployees();
            loadLogin();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Boolean> checkLogin() {
        String un = username.getText();
        String pw = password.getText();
        ArrayList<Boolean> login = new ArrayList<Boolean>(); // {correct login, is manager}
        login.add(false);
        login.add(false);
        
        for (ArrayList<String> entry : employee_data) {
            if (entry.get(1).equals(un) && entry.get(2).equals(pw)) {
                //JOptionPane.showMessageDialog(null, "Logged in as " + entry.get(0));
                if (pw.equals("man_pass")) login.set(1, true);
                login.set(0, true);
                return login;
            }
        }

        JOptionPane.showMessageDialog(null, "Username or Password Incorrect!");
        return login;
    }


    private void loadEmployees() {
        // INITIALIZE DATABASE INFORMATION
        Connection conn = null;
        String teamNumber = "team_22";
        String dbName = "csce315331_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        // CONNECT TO THE DATABASE
        try {
            conn = DriverManager.getConnection(dbConnectionString, "csce315331_team_22_master", "0000");
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        // GET EMPLOYEE INFORMATION
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT first_name, email, password FROM employees";

            ResultSet result = stmt.executeQuery(sqlStatement);

            employee_data = new ArrayList<ArrayList<String>>();
            while (result.next()) {
                ArrayList<String> entry = new ArrayList<String>();
                entry.add(result.getString("first_name").trim());
                entry.add(result.getString("email").trim());
                entry.add(result.getString("password").trim());
                employee_data.add(entry);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        // CLOSE DATABASE CONNECTION
        try {
            conn.close();
        } 
        catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    private void loadLogin() {
        this.setLayout(null);

        int frame_width = frame.getSize().width, frame_height = frame.getSize().height;
        int centerx = Math.floorDiv(frame_width, 2);
        int centery = Math.floorDiv(frame_height, 2);
        int topy = (int) (frame_height * 0.2);

        username_label = new JLabel("Username");
        password_label = new JLabel("Password");

        username_label.setBounds(centerx - 100, topy + 8, 70, 20);
        password_label.setBounds(centerx - 100, topy + 60, 70, 20);

        this.add(username_label);
        this.add(password_label);

        username = new JTextField();
        username.setBounds(centerx - 100, topy + 27, 200, 35);
        this.add(username);

        password = new JPasswordField();
        password.setBounds(centerx - 100, topy + 80, 200, 35);
        this.add(password);

        submit = new JButton("Login");
        submit.setBounds(centerx - 45, topy + 130, 90, 25);
        submit.setForeground(Color.WHITE);
        submit.setBackground(Color.BLACK);
        submit.addActionListener(fh);
        this.add(submit);

        this.setSize(frame.getSize());
    }
}