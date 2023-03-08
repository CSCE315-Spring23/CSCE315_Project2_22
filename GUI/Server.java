import java.sql.*;
import java.text.Format;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Server extends JPanel {
    // class to store an item in an order
    private static class OrderItem {
        // TODOs
        String order_name;
        String order_price;

        public OrderItem (String name, String price) {
            order_name = name;
            order_price = price;

        }

    }

    private JFrame frame;
    private FrameHandler fh;

    Server (JFrame frame, FrameHandler fh) {
        this.frame = frame;
        this.fh = fh;
        
        initialize();
    }
    

    
    public void initialize() {
        // INFORMATION
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<ArrayList<String>> menu_items = new ArrayList<ArrayList<String>>();
        ArrayList<String> order_items = new ArrayList<String>();
        ArrayList<Double> order_prices = new ArrayList<Double>();
        ArrayList<String> item_solo = new ArrayList<String>();

        


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
			sqlStatement = "SELECT category, menu_item_id, price FROM menu;";
			result = stmt.executeQuery(sqlStatement);

			while (result.next()) {
                ArrayList<String> row_item = new ArrayList<String>();
                row_item.add(result.getString("category"));
                row_item.add(result.getString("menu_item_id"));
                row_item.add(result.getString("price"));

                menu_items.add(row_item);
			}
		} 
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error accessing Database.");
		}


        // CREATE MAIN FRAME, DEFAULT TO FULL SCREEN
        // JFrame main_frame = new JFrame("MAIN FRAME");

        // main_frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setLayout(new GridBagLayout());;
        // main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // main_frame.setVisible(true);


        // INITIALIZE MAIN PANELS
        JPanel left_panel = new JPanel();
        left_panel.setLayout(new BoxLayout(left_panel, BoxLayout.Y_AXIS)); // Set the layout manager to a vertical BoxLayout
        left_panel.setBackground(Color.gray);      
        left_panel.setPreferredSize(new Dimension(500, 190));
        

        JPanel right_panel = new JPanel(new GridLayout());      
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
        this.add(left_panel, gbc);

        // right section
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        gbc.insets = new Insets(10, 5, 10, 10);
        this.add(right_panel, gbc);


        // INITIALIZE SUB PANELS
        
        JTabbedPane menu_pane = new JTabbedPane();
        JTabbedPane order_pane = new JTabbedPane();

        JButton button_price = new JButton();
        JButton pay_now = new JButton();
        left_panel.add(button_price);

        pay_now = new JButton("Pay Now");
        left_panel.add(pay_now);

        

        for (String category : categories) {
            JPanel panel = new JPanel(new GridLayout(35, 1)); 
            JPanel order_panel = new JPanel (new GridLayout(1, 1));

            /*
            for (String ctest : categories) {
                JButton button = new JButton(ctest);
                panel.add(button);
            }  */
            menu_pane.add(category, panel); 
            for (ArrayList<String> items : menu_items) {
              	if (category.equals(items.get(0))) {
                  JButton button = new JButton(items.get(1));
                
                  panel.add(button);


                  button.addActionListener(new ActionListener() {

                      public void actionPerformed(ActionEvent e) {
                          //OrderItem order_item = new OrderItem(items.get(1),items.get(2));
                          //order_items.add(order_item); 

                          String order_item = items.get(1) + " " + items.get(2); //creates item to add to order list
                          //OrderItem singular_item = new OrderItem(items.get(1), items.get(2));
                          order_items.add(order_item);
                          item_solo.add(items.get(1));
                          DefaultListModel<String> order_list = new DefaultListModel<>();
                          order_list.addElement(order_item); //adds order to the list

                          JList<String> corder = new JList<>(order_list);  //creates displayed list 
                          //corder.setSize(150,150); 
                          left_panel.add(corder); //adds list to left panel

                          //double order_total = 0;
                          double single_price = Double.parseDouble(items.get(2));
                          //order_total += single_price;

                          order_prices.add(single_price);
                          double order_total = 0.0;
                          for (double prices : order_prices) {
                              order_total += prices;
                              String s_order_total = String.valueOf(order_total);
                              button_price.setText(s_order_total);
                          }

                          //String s_order_total = String.valueOf(order_total);


                          //JButton button_price = new JButton(String.valueOf(order_total));
                          //order_panel.add(button_price);

                      }
                  });
                }
                

               
                
            }
        }
        pay_now.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                JFrame payment_frame = new JFrame("Payment Information");
                payment_frame.setSize(400, 300);
                payment_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                payment_frame.setVisible(true);
        
                // create a panel to hold payment information
                JPanel payment_panel = new JPanel(new BorderLayout());
                payment_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
                // create a panel to hold the order items
                JPanel order_panel = new JPanel(new GridLayout(0, 1));
                order_panel.setBorder(BorderFactory.createTitledBorder("Order Items"));
        
                // add order items to order panel
                double order_total = 0;
                for (String items : order_items) {
                    JPanel item_panel = new JPanel(new GridLayout(1, 2));
                    JLabel order_item_label = new JLabel(items);
                    item_panel.add(order_item_label);
        
                    JLabel price_label = new JLabel("$" + String.format("%.2f", order_prices.get(order_items.indexOf(items))));
                    item_panel.add(price_label);
        
                    order_panel.add(item_panel);
        
                    order_total += order_prices.get(order_items.indexOf(items));
                }
        
                // add total label to order panel
                JButton total_label = new JButton("Total: $" + String.format("%.2f", order_total));
                order_panel.add(total_label);
                payment_panel.add(order_panel, BorderLayout.CENTER);
                payment_frame.add(payment_panel);
                total_label.addActionListener(new ActionListener () {
                    public void actionPerformed(ActionEvent e) 
                    {
                        
                        for (String item : item_solo) {
                            System.out.println(item);
                            try {
                                Connection conn = null;
                                conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
                                String selectQuery = "SELECT product_id, quantity FROM menu_item_ingredients WHERE menu_item_id = ?";
                                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                                selectStmt.setString(1, item);
                                ResultSet resultSet = selectStmt.executeQuery();
                                while (resultSet.next()) {
                                    int product_id = resultSet.getInt("product_id");
                                    Double quantity = resultSet.getDouble("quantity");
                                    //System.out.println("Product ID: " + product_id + ", Quantity: " + quantity);
                                    String updateQuery = "UPDATE inventory SET quantity = quantity - ? WHERE product_id = ?";
                                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                                    updateStmt.setDouble(1, quantity);
                                    updateStmt.setInt(2, product_id);
                                    updateStmt.executeUpdate();
                                }
                                String sqlStatement = "SELECT order_id FROM orders_summary ORDER BY order_id DESC LIMIT 1;";
                                PreparedStatement stmt = conn.prepareStatement(sqlStatement);
                                ResultSet result = stmt.executeQuery();
                                int order_id = -1;
                                while (result.next()) {
                                    order_id = result.getInt(1) + 1;
                                }
    
                                double order_total = 0.0;
                                for (double price : order_prices) {
                                    order_total += price;
                                }
    
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    
                                String insert_orders_summary = "INSERT INTO orders_summary (order_id, employee_id, order_date, total_price) " + "VALUES (?, ?, ?, ?);";
                                PreparedStatement query_insert_summary = conn.prepareStatement(insert_orders_summary);
                                query_insert_summary.setInt(1, order_id);
                                query_insert_summary.setInt(2, ((int) Math.random() * 6) + 1);
                                query_insert_summary.setTimestamp(3, timestamp);
                                query_insert_summary.setDouble(4, order_total);
                                query_insert_summary.executeUpdate();

                                
                            } 
                            catch (SQLException ex) 
                            {
                                ex.printStackTrace();
                            }
                        }
                        
                    }
                    
                });

            }
        });
        

        right_panel.add(menu_pane); 

    }
    

    
    
}