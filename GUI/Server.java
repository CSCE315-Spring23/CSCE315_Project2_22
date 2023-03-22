import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * This class creates the user interface on the server side. It operates so that it can
 * function with the frame handler so that it can be accessed from a different window.
 * It creates the panels and frames to support a window with a left side displaying the 
 * items in an order, a delete button to delete the last item in an order, a clear button
 * delete all the items in an order, a total value, and a pay now button. The right side of
 * the screen shows the menu, displaying items by their category. By creating an order, users can
 * automatically update the database, updating the orders that are tracker, items that are sold,
 * and decrementing the ingredients that correspond to an item. If an item requries inventory
 * that is not in stock, the order will not be created.
 */
public class Server extends JPanel {
    // HANDLER FOR MULTIPLE FRAMES
    private JFrame frame;
    private FrameHandler fh;

    /**
     * This constructor sets the frame and frame handler for the particular instance of the 
     * server class. It initializes the frame and frame handler, then calls the initialize()
     * function which handles the rest of the user interface and database interaction.
     * @param frame
     * @param fh
     */
    Server (JFrame frame, FrameHandler fh) {
        this.frame = frame;
        this.fh = fh;
        
        initialize();
    }


    /**
     * This function initializes all key components of the server class. It creates all the necessary 
     * Java Swing components, puts them together, and handles action events. It also handles database
     * interaction for placing orders.
     */
    public void initialize() {
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


        // SET LAYOUT FOR MAIN FRAME
        this.setLayout(new GridBagLayout());;


        // INITIALIZE LEFT AND RIGHT CONTAINER PANELS (LEFT HOLDS ORDER INFORMATION, RIGHT HOLDS MENU INFORMATION)
        JPanel left_panel = new JPanel(new BorderLayout());     
        JPanel right_panel = new JPanel(new BorderLayout());      


        // GRID BAG STYLING FOR LEFT AND RIGHT PANELS
        GridBagConstraints gbc = new GridBagConstraints();

        // left section
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        this.add(left_panel, gbc);

        // right section
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.7;
        gbc.weighty = 1;
        this.add(right_panel, gbc);


        // CREATE PANEL CONTAINERS AND COMPONENTS FOR LEFT PANEL. HOLDS TOTAL, PAY NOW, AND ORDER ITEMS
        // order summary label
        JLabel order_summary_label = new JLabel("Order Summary");
        Font font = new Font(order_summary_label.getFont().getName(), Font.PLAIN, 20);
        order_summary_label.setFont(font);
        order_summary_label.setHorizontalAlignment(JLabel.CENTER);
        order_summary_label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        left_panel.add(order_summary_label, BorderLayout.NORTH);

        // table storing order items
        JTable order_items_table = new JTable();
        order_items_table.setRowSelectionAllowed(false);
        order_items_table.setColumnSelectionAllowed(false);
        order_items_table.setCellSelectionEnabled(false);
        order_items_table.setDefaultEditor(Object.class, null);

        DefaultTableModel oi_table_model = new DefaultTableModel();
        oi_table_model.addColumn("Item");
        oi_table_model.addColumn("Price");
        order_items_table.setModel(oi_table_model);

        JScrollPane oi_scroll_pane = new JScrollPane(order_items_table);
        oi_scroll_pane.setBorder(BorderFactory.createEtchedBorder());
        left_panel.add(oi_scroll_pane, BorderLayout.CENTER);

        // total price, pay now, and clear/delete functionality
        JPanel left_panel_south_container = new JPanel(new GridLayout(3, 1));
        left_panel.add(left_panel_south_container, BorderLayout.SOUTH);

        JLabel price_label = new JLabel("Total: $0.00");
        price_label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        left_panel_south_container.add(price_label);

        JPanel clear_and_delete_panel = new JPanel(new GridLayout(1, 2));
        left_panel_south_container.add(clear_and_delete_panel);

        JButton delete_button = new JButton("Delete");
        delete_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // remove last row
                int row_count = oi_table_model.getRowCount();

                if (row_count > 0) {
                    oi_table_model.removeRow(row_count - 1);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Items already empty.");
                }

                // update total price
                Double total_price = 0.00;

                for (int i = 0; i < order_items_table.getRowCount(); i++) {
                    Object temp_price = order_items_table.getValueAt(i, 1);
                    total_price += Double.parseDouble((String) temp_price);
                }

                price_label.setText("Total: $" + String.format("%.2f", total_price));
            }
        });
        clear_and_delete_panel.add(delete_button);

        JButton clear_button = new JButton("Clear");
        clear_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (oi_table_model.getRowCount() > 0) {
                    oi_table_model.setRowCount(0);
                    price_label.setText("Total: $0.00");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Items already empty.");
                }
            }
        });
        clear_and_delete_panel.add(clear_button);

        JButton pay_now_button = new JButton("Pay Now");
        left_panel_south_container.add(pay_now_button);


        // CREATE SIGN IN INFORMATION AND SIGN OUT BUTTON CONTAINER
        JPanel credential_panel = new JPanel(new BorderLayout());
        //credential_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        credential_panel.setBorder(BorderFactory.createEtchedBorder());
        right_panel.add(credential_panel, BorderLayout.NORTH);

        JLabel server_sign_in = new JLabel("Server");
        server_sign_in.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        server_sign_in.setFont(font);
        credential_panel.add(server_sign_in, BorderLayout.WEST);

        JButton home_button = new JButton("Home");
        home_button.addActionListener(fh);
        credential_panel.add(home_button, BorderLayout.EAST);

        
        // CREATE TABBED PANE FOR MENU ITEMS, MAIN ITEM IN THE RIGHT PANEL
        JTabbedPane menu_pane = new JTabbedPane();
        menu_pane.setBorder(BorderFactory.createEtchedBorder());
        right_panel.add(menu_pane, BorderLayout.CENTER);


        // ADD SMOOTHIE KING MENU ITEMS TO THE TABBED MENU PANE
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<ArrayList<String>> menu_items = new ArrayList<ArrayList<String>>();

        // get menu and category information from database to put into the tabbed menu pane
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


        // create panel for each category and add menu items to the proper category
        for (String category : categories) {
            // panel that holds menu item buttons
            JPanel panel = new JPanel(new GridLayout(25, 2)); 

            menu_pane.add(category, panel); 
            for (ArrayList<String> items : menu_items) {
              	if (category.equals(items.get(0))) {
                    // create a button for each menu item
                    JButton button = new JButton(items.get(1));
                    panel.add(button);

                    // when button is clicked it is added to the order
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // get the price of an item
                            String item_price =  new String();
                            for (ArrayList<String> items : menu_items) {
                                if (button.getText() == items.get(1)) {
                                    item_price = items.get(2);
                                }
                            }
                            
                            // add to order item table
                            oi_table_model.addRow(new Object[]{button.getText(), item_price});

                            // update value of total_price
                            Double total_price = 0.00;

                            for (int i = 0; i < order_items_table.getRowCount(); i++) {
                                Object temp_price = order_items_table.getValueAt(i, 1);
                                total_price += Double.parseDouble((String) temp_price);
                            }

                            price_label.setText("Total: $" + String.format("%.2f", total_price));
                        }
                    });
                }
            }
        }
        
        
        pay_now_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // check if there are actually items in the order
                if (oi_table_model.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(null, "No items in order.");
                    return;
                }

                // create new frame to display order confirmation
                JFrame payment_frame = new JFrame("Confirm Order");
                payment_frame.setSize(400, 300);
                payment_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                payment_frame.setVisible(true);
        
                // create a panel to hold order confirmation information
                JPanel order_confirmation_panel = new JPanel(new BorderLayout());
                order_confirmation_panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                payment_frame.add(order_confirmation_panel);
        
                // create a panel to hold the order items
                JPanel order_panel = new JPanel(new GridLayout(0, 1));
                order_panel.setBorder(BorderFactory.createTitledBorder("Order Items"));
                order_confirmation_panel.add(order_panel, BorderLayout.CENTER);

                // add order items to order panel
                for (int i = 0; i < order_items_table.getRowCount(); i++) {
                    JPanel item_panel = new JPanel(new GridLayout(1, 2));

                    Object item_name = order_items_table.getValueAt(i, 0);
                    JLabel order_item_label = new JLabel((String) item_name);
                    order_item_label.setHorizontalAlignment(JLabel.LEFT);
                    order_item_label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                    item_panel.add(order_item_label);

                    Object item_price = order_items_table.getValueAt(i, 1);
                    JLabel price_label = new JLabel("$" + (String) item_price);
                    price_label.setHorizontalAlignment(JLabel.RIGHT);
                    price_label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                    item_panel.add(price_label);
        
                    order_panel.add(item_panel);
                }

                // add button to order panel to place the order
                JButton place_order_button = new JButton("Place Order: $" + price_label.getText().substring(8, price_label.getText().length()));
                order_panel.add(place_order_button);
                
                // button to update the database
                place_order_button.addActionListener(new ActionListener () {
                    public void actionPerformed(ActionEvent e) {
                        // get ingredients for each item in the order and update the inventory quantity in database
                        for (int i = 0; i < order_items_table.getRowCount(); i++) {
                            try {
                                Connection conn = null;
                                conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");

                                // get ingredients
                                PreparedStatement selectStmt = conn.prepareStatement("SELECT product_id, quantity FROM menu_item_ingredients WHERE menu_item_id = ?");
                                selectStmt.setString(1, (String) order_items_table.getValueAt(i, 0));

                                ResultSet resultSet = selectStmt.executeQuery();
                                while (resultSet.next()) {
                                    int product_id = resultSet.getInt("product_id");
                                    Double quantity = resultSet.getDouble("quantity");

                                    // check to see if there is enough quantity in the inventory
                                    PreparedStatement invStmt = conn.prepareStatement("SELECT * FROM inventory WHERE product_id = ?");
                                    invStmt.setInt(1, product_id);

                                    ResultSet inv_results = invStmt.executeQuery();
                                    while (inv_results.next()) {
                                        Double remaining_quantity = inv_results.getDouble("quantity");
                                        if (remaining_quantity - quantity < 0) {
                                            JOptionPane.showMessageDialog(null, "Not enough '" + inv_results.getString("product_name") + "'.\nRemaining quantity = " + remaining_quantity + " oz.\nCan't place order.");
                                            payment_frame.dispose();
                                            oi_table_model.setRowCount(0);
                                            price_label.setText("Total: $0.00");
                                            return;
                                        }
                                    }

                                    // update inventory
                                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE inventory SET quantity = quantity - ? WHERE product_id = ?");
                                    updateStmt.setDouble(1, quantity);
                                    updateStmt.setInt(2, product_id);
                                    updateStmt.executeUpdate();
                                }
                            } 
                            catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }

                        // insert new order into orders_summary table
                        try {
                            Connection conn = null;
                            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");

                            // get the most recent order_id
                            PreparedStatement stmt = conn.prepareStatement("SELECT order_id FROM orders_summary ORDER BY order_id DESC LIMIT 1;");

                            ResultSet result = stmt.executeQuery();
                            int order_id = -1;
                            while (result.next()) {
                                order_id = result.getInt(1) + 1;
                            }

                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            Double order_total = 0.0;
                            for (int i = 0; i < order_items_table.getRowCount(); i++) {
                                order_total += Double.parseDouble((String) order_items_table.getValueAt(i, 1));
                            }

                            // insert new order into orders_summary table
                            PreparedStatement query_insert_summary = conn.prepareStatement("INSERT INTO orders_summary (order_id, employee_id, order_date, total_price) " + "VALUES (?, ?, ?, ?);");
                            query_insert_summary.setInt(1, order_id);
                            query_insert_summary.setInt(2, fh.active_employee);
                            query_insert_summary.setTimestamp(3, timestamp);
                            query_insert_summary.setDouble(4, order_total);
                            query_insert_summary.executeUpdate();
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        // insert order items into orders_by_item table
                        try {
                            Connection conn = null;
                            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");

                            // get most recent order_id from orders_by_item
                            String sqlStatement = "SELECT * FROM orders_by_item ORDER BY order_id DESC LIMIT 1;";
                            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders_by_item ORDER BY order_id DESC LIMIT 1;");
                            ResultSet result = stmt.executeQuery();

                            int order_id = -1;
                            while (result.next()) {
                                order_id = result.getInt(2) + 1;
                            }

                            for (int i = 0; i < order_items_table.getRowCount(); i++) {
                                // get most recent item_id
                                sqlStatement = "SELECT * FROM orders_by_item ORDER BY item_id DESC LIMIT 1;";
                                stmt = conn.prepareStatement(sqlStatement);
                                result = stmt.executeQuery();

                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                int item_id = -1;
                                while (result.next()) {
                                    item_id = result.getInt(1) + 1;
                                }

                                // insert items into orders_by_item
                                String insert_orders_summary = "INSERT INTO orders_by_item (item_id, order_id, menu_item_id, item_date, item_price) " + "VALUES (?, ?, ?, ?, ?);";
                                PreparedStatement query_insert_summary = conn.prepareStatement(insert_orders_summary);
                                query_insert_summary.setInt(1, item_id);
                                query_insert_summary.setInt(2, order_id);
                                query_insert_summary.setString(3, (String) order_items_table.getValueAt(i, 0));
                                query_insert_summary.setTimestamp(4, timestamp);
                                query_insert_summary.setDouble(5, Double.parseDouble((String) order_items_table.getValueAt(i, 1)));
                                query_insert_summary.executeUpdate();
                            }
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        // feedback for when order is placed
                        JOptionPane.showMessageDialog(null, "Order Placed Successfully!");
                        payment_frame.dispose();
                        oi_table_model.setRowCount(0);
                        price_label.setText("Total: $0.00");
                    }
                });
            }
        });
    }
}