import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.lang.Math.*;
import java.util.*;

//Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
//Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

/**
 * A JPanel that handles the login process for the system.
 * It loads employee data from the database and creates a login interface.
 */
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

    /**
     * Constructs a new Login panel with the given JFrame and FrameHandler.
     * It loads employee data from the database and creates a login interface.
     *
     * @param frame The JFrame containing the Login panel.
     * @param fh The FrameHandler that handles button clicks on the Login panel.
     */
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

    /**
     * Checks if the entered username and password are correct and returns an ArrayList with two booleans:
     * the first boolean indicates if the login was successful or not, and the second boolean indicates if the
     * user is a manager or not.
     *
     * @return An ArrayList containing two booleans: [correct login, is manager].
     */
    public ArrayList<Boolean> checkLogin() {
        String un = username.getText();
        String pw = password.getText();
        ArrayList<Boolean> login = new ArrayList<Boolean>(); // {correct login, is manager}
        login.add(false);
        login.add(false);
        
        for (ArrayList<String> entry : employee_data) {
            if (entry.get(1).equals(un) && entry.get(2).equals(pw)) {
                JOptionPane.showMessageDialog(null, "Logged in as " + entry.get(0));
                if (pw.equals("man_pass")) login.set(1, true);
                login.set(0, true);
                return login;
            }
        }
        JOptionPane.showMessageDialog(null, "Username or password incorrect.");
        return login;
    }

    /**

    This method loads employee data from a PostgreSQL database by establishing a connection
    with the database using the provided database name and team number. The database is queried
    to retrieve employee data, including their first name, email address, and password. The retrieved
    data is stored in an ArrayList of ArrayLists of strings, where each sub-array represents an employee
    and contains their first name, email address, and password.
    @throws SQLException if an error occurs while connecting to or querying the database.
    @throws Exception if an error occurs while retrieving or storing employee data.
    */
    private void loadEmployees() {
        Connection conn = null;
        String teamNumber = "team_22";
        String dbName = "csce315331_" + teamNumber;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;
        //Connecting to the database
        try {
            conn = DriverManager.getConnection(dbConnectionString, "csce315331_team_22_master", "0000");
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

    /**

    This method creates a login panel for the GUI by setting the layout to null and positioning
    username and password labels and text fields, and a submit button on the panel. The position
    of these components is determined by the size of the frame and a set of calculated values.
    @throws NullPointerException if the frame object is null.
    */
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



