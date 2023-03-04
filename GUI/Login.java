import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;

//Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
//Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

public class Login extends JPanel  {  //implements ActionListener
    // load the employees table for checking passwords
    // add two password fields
    // figure out how to use username and password together
    // in username password field, get the associated password so it can be checked against password
    // config the frame and display
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

    public boolean checkLogin() {
        String un = username.getText();
        String pw = password.getText();
        
        for (ArrayList<String> entry : employee_data) {
            if (entry.get(1).equals(un) && entry.get(2).equals(pw)) {
                JOptionPane.showMessageDialog(null, "Logged in as " + entry.get(0));
                return true;
            }
        }
        JOptionPane.showMessageDialog(null, "Username or password incorrect.");
        return false;
    }

    private void loadEmployees() {
        Connection conn = null;
        String teamNumber = "team_22";
        String dbName = "csce315331_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        //Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, "csce315331_veselka", "729009874");
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

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
        try {
            conn.close();
        } 
        catch(Exception e) {
            System.out.println("Connection NOT Closed.");
        }
    }

    private void loadLogin() {
        // panel = new JPanel();  // default flow layout
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
        submit.setBounds(centerx - 45, topy + 120, 90, 25);
        submit.setForeground(Color.WHITE);
        submit.setBackground(Color.BLACK);
        submit.addActionListener(fh);
        this.add(submit);

        this.setSize(frame.getSize());
    }

    // public static void main(String[] args) {
    //     try {
    //         loadEmployees();
    //         showLogin();
    //     }
    //     catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}



