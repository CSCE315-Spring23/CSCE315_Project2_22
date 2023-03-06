import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Server {
    // class to store an item in an order
    private class OrderItem {
        // TODO
    }
    public static void main(String[] args) {
        // INFORMATION
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<ArrayList<String>> menu_items = new ArrayList<ArrayList<String>>();
        //ArrayList<String> order_items;


        // ESTABLISH DATABASE CONNECTION
        Connection conn = null;
        try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}


        // GET DATABASE INFORMATION
        try {
            // category information
			Statement stmt = conn.createStatement();
			String sqlStatement = "SELECT category FROM menu GROUP BY category;";
			ResultSet result = stmt.executeQuery(sqlStatement);

			while (result.next()) {
                categories.add(result.getString("category"));
			}

            // menu information
			sqlStatement = "SELECT category, menu_item_id FROM menu;";
			result = stmt.executeQuery(sqlStatement);

			while (result.next()) {
                ArrayList<String> row_item = new ArrayList<String>();
                row_item.add(result.getString("category"));
                row_item.add(result.getString("menu_item_id"));

                menu_items.add(row_item);
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error accessing Database.");
		}


        // CREATE MAIN FRAME, DEFAULT TO FULL SCREEN
        JFrame main_frame = new JFrame("MAIN FRAME");

        main_frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        main_frame.setLayout(new GridBagLayout());;
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setVisible(true);


        // INITIALIZE MAIN PANELS
        JPanel left_panel = new JPanel();      
        left_panel.setBackground(Color.gray);  

        JPanel right_panel = new JPanel(new BorderLayout());      
        right_panel.setBackground(Color.gray);


        // GRID STYLING
        GridBagConstraints gbc = new GridBagConstraints();

        // left section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        gbc.insets = new Insets(10,10, 10, 5);
        main_frame.add(left_panel, gbc);

        // right section
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 5, 10, 10);
        main_frame.add(right_panel, gbc);


        // INITIALIZE SUB PANELS
        JTabbedPane menu_pane = new JTabbedPane();  

        for (String category : categories) {
            JPanel panel = new JPanel(new FlowLayout());
            for (String ctest : categories) {
                JButton button = new JButton(ctest);
                panel.add(button);
            }
            menu_pane.add(category, panel);
        }
        
        right_panel.add(menu_pane);
    }
}
