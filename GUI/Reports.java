import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.math.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.*;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * Used to add a Reports panel to the ManagerUI. This houses buttons to generate various reports.
 */
public class Reports extends JFrame {

    private JPanel reports_panel;

    private static class OrderPair implements Comparable<OrderPair> {
        String item1;
        String item2;
        int count;

        public OrderPair (String name1, String name2) {
            item1 = name1;
            item2 = name2;
            count = 1;
        }
        @Override
        public int compareTo(OrderPair rhs) {
            if (count > rhs.count) return 1;
            if (count < rhs.count) return -1;
            else return 0;
        }
    }


    /**
     * Constructor for the reports frame, which is added to the managerUI frame on construction. This frame houses buttons to generate reports
     * for various things, including sales, excess and restocking. 
     */
    public Reports() {
        // create buttons, each button launches new frame that shows the respective report
        JButton sales_report = new JButton("Generate Sales Report");
        JButton xz_report = new JButton("Generate XZ Report");
        JButton excess_report = new JButton("Generate Excess Report");
        JButton restock_report = new JButton("Generate Restock Report");
        JButton sells_together_report = new JButton("Generate Sells Together Report");

        reports_panel = new JPanel();
        reports_panel.setLayout(new BoxLayout(reports_panel, BoxLayout.Y_AXIS));
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        // call load for all the reports
        load_sales(sales_report);
        load_xz(xz_report);
        load_sells_together(sells_together_report);
        load_excess(excess_report);
        load_restock(restock_report);

        // add the reports panel, which holds the buttons, to the frame
        this.add(reports_panel);

        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    /**
     * Generates a sales report to show sales per drink for the last day,
     * week, month, and a custom time interval. The custom time interval allows
     * you to select a date window to generate the report for, and sorting
     * alphabetically or by sales.
     * @param sales_report
     */
    private void load_sales(JButton sales_report) {
        sales_report.setSize(110, 40);
        sales_report.setAlignmentX((float) 0.5);
        sales_report.setForeground(Color.WHITE);
        sales_report.setBackground(Color.BLACK);
        reports_panel.add(sales_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        sales_report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                JFrame report_frame = new JFrame("Sales Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);

                // Fill in generating your report and adding to the report frame
                // CREATE TABBED PANE FOR SALES REPORT
                JTabbedPane sales_report_pane = new JTabbedPane();
                report_frame.add(sales_report_pane);

                // CREATE PANELS TO DISPLAY DIFFERENT TIME WINDOWS
                JPanel day_panel = new JPanel(new BorderLayout());
                JPanel week_panel = new JPanel(new BorderLayout());
                JPanel month_panel = new JPanel(new BorderLayout());
                JPanel custom_panel = new JPanel(new BorderLayout());

                sales_report_pane.add("Day", day_panel);
                sales_report_pane.add("Week", week_panel);
                sales_report_pane.add("Month", month_panel);
                sales_report_pane.add("Custom", custom_panel);

                // CREATE PANELS TO SERVE AS HEADER CONTAINER
                JPanel day_header_container = new JPanel(new GridLayout(2, 1));
                JPanel week_header_container = new JPanel(new GridLayout(2, 1));
                JPanel month_header_container = new JPanel(new GridLayout(2, 1));
                JPanel custom_header_container = new JPanel(new GridLayout(4, 1));

                day_panel.add(day_header_container, BorderLayout.NORTH);
                week_panel.add(week_header_container, BorderLayout.NORTH);
                month_panel.add(month_header_container, BorderLayout.NORTH);
                custom_panel.add(custom_header_container, BorderLayout.NORTH);

                // CREATE LABELS TO SERVE AS PANEL TITLE/HEADER
                JLabel day_label = new JLabel("Daily Sales");
                JLabel week_label = new JLabel("Weekly Sales");
                JLabel month_label = new JLabel("Monthly Sales");
                JLabel custom_label = new JLabel("Custom");

                day_header_container.add(day_label);
                week_header_container.add(week_label);
                month_header_container.add(month_label);
                custom_header_container.add(custom_label);

                day_label.setHorizontalAlignment(JLabel.CENTER);
                week_label.setHorizontalAlignment(JLabel.CENTER);
                month_label.setHorizontalAlignment(JLabel.CENTER);
                custom_label.setHorizontalAlignment(JLabel.CENTER);

                Font font = new Font(day_label.getFont().getName(), Font.PLAIN, 20);

                day_label.setFont(font);
                week_label.setFont(font);
                month_label.setFont(font);
                custom_label.setFont(font);

                // CREATE LABELS TO STORE INFORMATION ABOUT THE TIME WINDOW
                String first_date = "2022-01-01";
                LocalDate current_date = LocalDate.now();
                LocalDate week_ago = LocalDate.now().minusWeeks(1);
                LocalDate month_ago = LocalDate.now().minusMonths(1);

                String custom_start = first_date;
                String custom_end = "" + current_date;

                JLabel day_label_desc = new JLabel("for " + current_date);
                JLabel week_label_desc = new JLabel("for " + week_ago + " to " + current_date);
                JLabel month_label_desc = new JLabel("for " + month_ago + " to " + current_date);
                JLabel custom_label_desc = new JLabel("for " + custom_start + " to " + custom_end);

                day_header_container.add(day_label_desc);
                week_header_container.add(week_label_desc);
                month_header_container.add(month_label_desc);
                custom_header_container.add(custom_label_desc);

                day_label_desc.setHorizontalAlignment(JLabel.CENTER);
                week_label_desc.setHorizontalAlignment(JLabel.CENTER);
                month_label_desc.setHorizontalAlignment(JLabel.CENTER);
                custom_label_desc.setHorizontalAlignment(JLabel.CENTER);

                // CREATE TABLES TO STORE SALES INFO
                JTable day_table = new JTable();
                JTable week_table = new JTable();
                JTable month_table = new JTable();
                JTable custom_table = new JTable();

                DefaultTableModel day_table_model = new DefaultTableModel();
                day_table_model.addColumn("Item");
                day_table_model.addColumn("Sales");
                DefaultTableModel week_table_model = new DefaultTableModel();
                week_table_model.addColumn("Item");
                week_table_model.addColumn("Sales");
                DefaultTableModel month_table_model = new DefaultTableModel();
                month_table_model.addColumn("Item");
                month_table_model.addColumn("Sales");
                DefaultTableModel custom_table_model = new DefaultTableModel();
                custom_table_model.addColumn("Item");
                custom_table_model.addColumn("Sales");
                
                day_table.setModel(day_table_model);
                week_table.setModel(week_table_model);
                month_table.setModel(month_table_model);
                custom_table.setModel(custom_table_model);

                JScrollPane day_table_scroll = new JScrollPane(day_table);
                JScrollPane week_table_scroll = new JScrollPane(week_table);
                JScrollPane month_table_scroll = new JScrollPane(month_table);
                JScrollPane custom_table_scroll = new JScrollPane(custom_table);

                day_panel.add(day_table_scroll, BorderLayout.CENTER);
                week_panel.add(week_table_scroll, BorderLayout.CENTER);
                month_panel.add(month_table_scroll, BorderLayout.CENTER);
                custom_panel.add(custom_table_scroll, BorderLayout.CENTER);

                // ACTION LISTENER TO SELECT CUSTOM DATE RANGES
                JPanel custom_option_bar = new JPanel(new GridLayout(1, 3));

                JTextField custom_start_field = new JTextField("Start");
                JTextField custom_end_field = new JTextField("End");
                JButton go_button = new JButton("Go");

                JPanel custom_sorting_bar = new JPanel(new GridLayout(1, 2));
                
                JLabel sort_label = new JLabel("Sort by: ");
                sort_label.setHorizontalAlignment(SwingConstants.CENTER);
                String options[] = {"A-Z", "Z-A", "ASC", "DESC", "NONE"};
                JComboBox combo_box = new JComboBox(options);

                go_button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        custom_label_desc.setText("for " + custom_start_field.getText() + " to " + custom_end_field.getText());

                        custom_table_model.setRowCount(0);

                        Connection conn = null;
                        try {
                            Class.forName("org.postgresql.Driver");
                            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");

                            // custom sales
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                            java.util.Date start_date = sdf.parse(custom_start_field.getText());
                            java.util.Date end_date = sdf.parse(custom_end_field.getText());

                            java.sql.Date sql_start = new java.sql.Date(start_date.getTime());
                            java.sql.Date sql_end = new java.sql.Date(end_date.getTime());

                            PreparedStatement pstmt;
                            if (combo_box.getSelectedItem() == "ASC") {
                                pstmt = conn.prepareStatement("SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date BETWEEN ? AND ? GROUP BY menu_item_id ORDER BY COUNT(menu_item_id);");
                            }
                            else if (combo_box.getSelectedItem() == "DESC") {
                                pstmt = conn.prepareStatement("SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date BETWEEN ? AND ? GROUP BY menu_item_id ORDER BY COUNT(menu_item_id) DESC;");
                            }
                            else if (combo_box.getSelectedItem() == "A-Z") {
                                pstmt = conn.prepareStatement("SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date BETWEEN ? AND ? GROUP BY menu_item_id ORDER BY menu_item_id;");
                            }
                            else if (combo_box.getSelectedItem() == "Z-A") {
                                pstmt = conn.prepareStatement("SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date BETWEEN ? AND ? GROUP BY menu_item_id ORDER BY menu_item_id DESC;");
                            }
                            else {
                                pstmt = conn.prepareStatement("SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date BETWEEN ? AND ? GROUP BY menu_item_id;");
                            }
                            
                            pstmt.setDate(1, sql_start);
                            pstmt.setDate(2, sql_end);

                            ResultSet result = pstmt.executeQuery();

                            while (result.next()) {
                                custom_table_model.addRow(new Object[]{result.getString("menu_item_id"), result.getString("count")});
                            }
                        } 
                        catch (Exception exc) {
                            JOptionPane.showMessageDialog(report_frame, "Invalid Date.");
                        }
                    }
                });

                custom_option_bar.add(custom_start_field);
                custom_option_bar.add(custom_end_field);
                custom_option_bar.add(go_button);

                custom_header_container.add(custom_option_bar);

                custom_sorting_bar.add(sort_label);
                custom_sorting_bar.add(combo_box);

                custom_header_container.add(custom_sorting_bar);

                // GET INFORMATION FROM DATABASE TO POPULATE STATIC TABLES
                Connection conn = null;
                try {
                    Class.forName("org.postgresql.Driver");
                    conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");

                    // daily sales
                    Statement stmt = conn.createStatement();
                    String sqlStatement = "SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date::DATE = CURRENT_DATE GROUP BY menu_item_id;";
                    ResultSet result = stmt.executeQuery(sqlStatement);

                    while (result.next()) {
                        day_table_model.addRow(new Object[]{result.getString("menu_item_id"), result.getString("count")});
                    }

                    // weekly sales
                    sqlStatement = "SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date::DATE BETWEEN CURRENT_DATE - INTERVAL '1 week' AND CURRENT_DATE GROUP BY menu_item_id;";
                    result = stmt.executeQuery(sqlStatement);

                    while (result.next()) {
                        week_table_model.addRow(new Object[]{result.getString("menu_item_id"), result.getString("count")});
                    }

                    // monthly sales
                    sqlStatement = "SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item WHERE item_date::DATE BETWEEN CURRENT_DATE - INTERVAL '1 month' AND CURRENT_DATE GROUP BY menu_item_id;";
                    result = stmt.executeQuery(sqlStatement);

                    while (result.next()) {
                        month_table_model.addRow(new Object[]{result.getString("menu_item_id"), result.getString("count")});
                    }

                    // custom sales pre-set to all time
                    sqlStatement = "SELECT menu_item_id, COUNT(menu_item_id) FROM orders_by_item GROUP BY menu_item_id;";
                    result = stmt.executeQuery(sqlStatement);

                    while (result.next()) {
                        custom_table_model.addRow(new Object[]{result.getString("menu_item_id"), result.getString("count")});
                    }
                } 
                catch (Exception exc) {
                    exc.printStackTrace();
                    System.err.println(e.getClass().getName() + ": " + exc.getMessage());
                    System.exit(0);
                }
                
            }
        });
    } 
    /**
     * Generate x and z reports. X report shows sales for the day and Z report shows sales per day.
     * @param xz_report
     */
    private void load_xz(JButton xz_report) {
        xz_report.setSize(110, 40);
        xz_report.setAlignmentX((float) 0.5);
        xz_report.setForeground(Color.WHITE);
        xz_report.setBackground(Color.BLACK);
        reports_panel.add(xz_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        xz_report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTable table;
                Connection conn;
                PreparedStatement update_row;
                
                JFrame report_frame = new JFrame("XZ Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);

                // Fill in generating your report and adding to the report frame
                try {
                    conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_team_22_master", "0000");
                    table = new JTable();
                    table.getTableHeader().setReorderingAllowed(false);

                    Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    ResultSet order_summary = stmt.executeQuery("SELECT * FROM orders_summary");
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("report type");
                    model.addColumn("date");
                    model.addColumn("total_sales");

                    double total_sales = 0;
                    order_summary.next();
                    String[] prev_splitted = order_summary.getString(3).split(" ");
                    order_summary.previous();

                    while(order_summary.next()) {
                        String[] splitted = order_summary.getString(3).split(" ");
                        Object[] row = new Object[3];
                        LocalTime myobj = LocalTime.now();
                        ZoneId zonedId = ZoneId.of( "America/Chicago" );
                        LocalDate today = LocalDate.now( zonedId );
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedString = today.format(formatter); 

                        //handles old z reports 
                        //myobj.getHour() > 12 && myobj.getHour()-12 > 5
                        total_sales += order_summary.getDouble(4);
                        if(prev_splitted[0].equals(splitted[0]) == false) {
                            if(splitted[0].equals(formattedString)) {
                                row[0] = "x report";
                                row[1] = splitted[0];
                                row[2] = total_sales;
                                model.addRow(row);
                                prev_splitted = splitted;
                            }
                            else {
                                row[0] = "z report";
                                row[1] = splitted[0];                
                                row[2] = total_sales;
                                model.addRow(row);

                                total_sales = 0; 
                                prev_splitted=splitted;
                            }
                        }
                        //handles old x reports if they're needed 
                        }

                    table.setModel(model);
                    table.setRowHeight(30);
                    JTableHeader header = table.getTableHeader();
                    header.setBackground(Color.gray);
                    header.setForeground(Color.white);
                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setColumnHeaderView(header);

                    report_frame.add(scrollPane);
                    report_frame.setVisible(true);
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        });
        
    } 
    /**
     * Generates the excess report. This report shows inventory items that sold less
     * than 10% of inventory for specified time window. This would assume no restocks
     * have occured during that time window.
     * @param excess_report
     */
    private void load_excess(JButton excess_report) {

        excess_report.setSize(110, 40);
        excess_report.setAlignmentX((float) 0.5);
        excess_report.setForeground(Color.WHITE);
        excess_report.setBackground(Color.BLACK);
        reports_panel.add(excess_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));
    
        excess_report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame report_frame = new JFrame("Excess Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);
                
                
                String userDateInput = JOptionPane.showInputDialog(null, "Please enter a date (YYYY-MM-DD):");
            
                try {
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
                    
                    // Get inventory snapshot data for the user input date
                    String timestampQuery = "SELECT * FROM inventory_snapshot WHERE snapshot_date = ?::timestamp";
                    PreparedStatement timestampStmt = conn.prepareStatement(timestampQuery);
                    timestampStmt.setString(1, userDateInput);
                    ResultSet timestampRs = timestampStmt.executeQuery();
                    
                    // String currentSnapshotQuery = "SELECT * FROM inventory_snapshot WHERE snapshot_date = '2023-01-01'";
                    String currentSnapshotQuery = "SELECT * FROM inventory";
                    PreparedStatement currentSnapshotStmt = conn.prepareStatement(currentSnapshotQuery);
                    
                    // Execute the current snapshot query and retrieve the result set
                    ResultSet currentRs = currentSnapshotStmt.executeQuery();
                    
                    // Create a map to hold the current inventory levels of each product
                    Map<Integer, Integer> inventoryMap = new HashMap<Integer, Integer>();
                    
                    // Populate the inventory map with the current inventory levels from the result set
                    while (currentRs.next()) {
                        inventoryMap.put(currentRs.getInt("product_id"), currentRs.getInt("quantity"));
                    }
                    
                    // Create a vector to hold the column names for the table
                    Vector<String> columnName = new Vector<String>();
                    columnName.add("Product ID");
                    columnName.add("Product Name");
                    columnName.add("Timestamp Quantity");
                    columnName.add("Current Quantity");
                    
                    // Create a vector to hold the data for the table
                    Vector<Vector<Object>> excess_data = new Vector<Vector<Object>>();
                    
                    // Iterate over the timestamp result set and compare the timestamp quantity with the current quantity
                    while (timestampRs.next()) {
                        int productId = timestampRs.getInt("product_id");
                        String productName = timestampRs.getString("product_name");
                        int quantity = timestampRs.getInt("quantity");
                        
                        // Compare the timestamp quantity with the current quantity, if the product is in the inventory map
                        if (inventoryMap.containsKey(productId)) {
                            int currentQuantity = inventoryMap.get(productId);
                            
                
                            // Populate the excess data vector with the list of all items that are under 10%
                            if ((((quantity - currentQuantity) > 0) && (quantity - currentQuantity) < .1 * quantity) || quantity - currentQuantity == 0) {
                                Vector<Object> row = new Vector<Object>();
                                row.add(productId);
                                row.add(productName);
                                row.add(quantity);
                                row.add(currentQuantity);
                                excess_data.add(row);
                            }
                        }
                    }
            
                    DefaultTableModel model = new DefaultTableModel(excess_data, columnName);
                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);
            
                    report_frame.add(scrollPane);
                    report_frame.setVisible(true);
            
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    /**
    *Loads the Restock Report on click of the Restock Report button
    *@param restock_report JButton to load Restock Report
    */
    private void load_restock(JButton restock_report) {
        restock_report.setSize(110, 40);
        restock_report.setAlignmentX((float) 0.5);
        restock_report.setForeground(Color.WHITE);
        restock_report.setBackground(Color.BLACK);
        reports_panel.add(restock_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        restock_report.addActionListener(new ActionListener() {
            /**
            * Action listener for Restock Report button, generates Restock Report
            * @param e ActionEvent object
            */

            public void actionPerformed(ActionEvent e) {
                
                JFrame report_frame = new JFrame("Restock Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);

                // Fill in generating your report and adding to the report frame

                setTitle("Recomended Restock Report");
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Product ID");
                model.addColumn("Product Name");
                model.addColumn("Minimum Quantity");
                model.addColumn("Current Quantity");
        
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);
                report_frame.add(scrollPane);
                try {
                    // Connect to PostgreSQL database
                    Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
            
                    // Execute the query to get inventory snapshot
                    PreparedStatement statement = conn.prepareStatement(
                        "SELECT product_id, product_name, 0.25 * MAX(quantity) AS quarter_max_quantity " +
                        "FROM inventory_snapshot " +
                        /* The WHERE Statment should be replaced with:
                            "WHERE snapshot_date >= NOW() - INTERVAL '30 days' " +
                         In the case of a currently operational GUI
                        */
                        "WHERE snapshot_date >= '2022-12-02' AND snapshot_date <= '2023-01-01' " + 
                        "GROUP BY product_id, product_name");
                    ResultSet resultSet = statement.executeQuery();
            
                    while (resultSet.next()) {
                        int productId = resultSet.getInt("product_id");
                        String productName = resultSet.getString("product_name");
                        double quarterMaxQuantity = resultSet.getDouble("quarter_max_quantity");
                
                        // Find the current quantity from inventory table
                        PreparedStatement currQuantityStatement = conn.prepareStatement(
                            "SELECT quantity FROM inventory WHERE product_id = ?");
                        currQuantityStatement.setInt(1, productId);
                        ResultSet currQuantityResultSet = currQuantityStatement.executeQuery();
                
                        double currQuantity = 0.0;
                        if (currQuantityResultSet.next()) {
                            currQuantity = currQuantityResultSet.getDouble("quantity");
                        }
                
                        // Add the row to the table only if the current quantity is less than or equal to the 25% of max quantity
                        if (currQuantity <= quarterMaxQuantity) {
                            Vector<Object> row = new Vector<Object>();
                            row.add(productId);
                            row.add(productName);
                            row.add(quarterMaxQuantity);
                            row.add(currQuantity);
                            model.addRow(row);
                        }
                    }

                    
                    System.out.println(model.getRowCount());
                    conn.close();
                    report_frame.setVisible(true);

            
                } catch (SQLException ex1) {
                    ex1.printStackTrace();
                }
            }
        });
    } 
    /**
     * Generates report that shows which items sell together. It shows a list of menu
     * items that are most frequently sold together in a specified time window.
     * @param sells_together_report
     */
    private void load_sells_together(JButton sells_together_report) {
        sells_together_report.setSize(110, 40);
        sells_together_report.setAlignmentX((float) 0.5);
        sells_together_report.setForeground(Color.WHITE);
        sells_together_report.setBackground(Color.BLACK);
        reports_panel.add(sells_together_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        sells_together_report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                JFrame report_frame = new JFrame("Sells Together Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);

                JTable table = new JTable();
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Item 1");
                model.addColumn("Item 2");
                model.addColumn("Count");

                // Fill in generating your report and adding to the report frame

                //Initialize an Array of Pairs
                ArrayList<OrderPair> order_pairs = new ArrayList<OrderPair>();  

                //Initialize variables to iterate through the rows of query
                int prev_order_id = 0;
                int curr_order_id = 0;
                String prev_item = "";
                String curr_item = "";

                try {  //The Query

                    //Time stamp to get date range 
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String userDateInput = JOptionPane.showInputDialog(null, "Please enter a start date (YYYY-MM-DD):");
                    java.util.Date parsedDate = dateFormat.parse(userDateInput);
                    // Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    Timestamp from = new Timestamp(parsedDate.getTime());
                    userDateInput = JOptionPane.showInputDialog(null, "Please enter an end date (YYYY-MM-DD):");
                    parsedDate = dateFormat.parse(userDateInput);
                    Timestamp to = new Timestamp(parsedDate.getTime());

                    //Connection to database and query to get orders within a time frame
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
                    // String selectQuery = "SELECT * FROM orders_by_item WHERE item_date between '?' and '?'";
                    // selects unique order_ids for orders that have more than order_item in them
                    String selectQuery = "SELECT DISTINCT order_id FROM (SELECT a.* FROM orders_by_item a JOIN (SELECT order_id, COUNT(*) " 
                                            + "FROM orders_by_item GROUP BY order_id HAVING count(*) > 1) b ON a.order_id = b.order_id ORDER BY a.order_id) t " 
                                            + "WHERE t.item_date between ? and ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                    selectStmt.setTimestamp(1, from);
                    selectStmt.setTimestamp(2, to);
                    ResultSet multi_item_orders = selectStmt.executeQuery();

                    HashMap<String, OrderPair> pairs = new HashMap<String, OrderPair>();

                    String qry = "SELECT menu_item_id FROM orders_by_item WHERE order_id=?";
                    PreparedStatement get_item_names = conn.prepareStatement(qry);
                    //iterates through the unique order ids for orders with multiple items
                    while (multi_item_orders.next()) {
                        get_item_names.setInt(1, multi_item_orders.getInt("order_id"));
                        ResultSet mio_items = get_item_names.executeQuery();  // multi item orders (mio) items

                        // number of possible pairs = (n(n+1) / 2) - n == n choose 2
                        ArrayList<String> item_names = new ArrayList<String>();
                        while (mio_items.next()) {
                            item_names.add(mio_items.getString("menu_item_id"));
                        }
                        // sort by anything, here item names, so that when coming up with pairs, they appear in the same order every time
                        // so that they can be used to access the hash map
                        Collections.sort(item_names);
                        for (int i = 0; i < item_names.size() - 1; ++i) {
                            for (int j = i + 1; j < item_names.size(); ++j) {
                                // if the pair is already in there
                                String key = item_names.get(i) + item_names.get(j);
                                if (pairs.containsKey(key)) {
                                    pairs.get(key).count++;
                                }
                                else {
                                    pairs.put(key, new OrderPair(item_names.get(i), item_names.get(j)));
                                }
                            }
                        }
                    }
                    // convert hash map to array list for sorting by frequency (count)
                    ArrayList<OrderPair> final_pairs = new ArrayList<OrderPair>(pairs.values());
                    Collections.sort(final_pairs, Collections.reverseOrder());
                    for (OrderPair pr : final_pairs) {
                        model.addRow(new Object[]{pr.item1, pr.item2, pr.count});
                    }

                } 
                catch (Exception ex) 
                {
                    ex.printStackTrace();
                }

                table.setModel(model);

                table.setRowHeight(30);
                JTableHeader header = table.getTableHeader();
                header.setBackground(Color.gray);
                header.setForeground(Color.white);
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setColumnHeaderView(header);

                report_frame.add(scrollPane);
                report_frame.setVisible(true);
            }
        });
    } 
}