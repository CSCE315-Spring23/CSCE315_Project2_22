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



    
/*
    public static Comparator<OrderPair> SortPopular = new Comparator<OrderPair>() {
        public int compare (int count1, int count2) {
            return count1 - count2;
        }
    }; */



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
w
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

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
            }
        });
    } 
    private void load_xz(JButton xz_report) {
        xz_report.setSize(110, 40);
        xz_report.setAlignmentX((float) 0.5);
        xz_report.setForeground(Color.WHITE);
        xz_report.setBackground(Color.BLACK);
        reports_panel.add(xz_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        xz_report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                JFrame report_frame = new JFrame("XZ Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);

                // Fill in generating your report and adding to the report frame
            }
        });
        
    } 
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
                    

                    String currentSnapshotQuery = "SELECT * FROM inventory_snapshot WHERE snapshot_date = '2023-01-01'";
                    PreparedStatement currentSnapshotStmt = conn.prepareStatement(currentSnapshotQuery);
                    ResultSet currentRs = currentSnapshotStmt.executeQuery();
                    Map<Integer, Integer> inventoryMap = new HashMap<Integer, Integer>();
                    while(currentRs.next()) {
                        inventoryMap.put(currentRs.getInt("product_id"), currentRs.getInt("quantity"));
                    }
            
                    Vector<String> columnName = new Vector<String>();
                    columnName.add("Product ID");
                    columnName.add("Product Name");
                    columnName.add("Timestamp Quantity");
                    columnName.add("Current Quantity");
                    //columnName.add("test");
                    //columnName.add("test1");
                    Vector<Vector<Object>> excess_data = new Vector<Vector<Object>>();
                    while(timestampRs.next()) {
                        int productId = timestampRs.getInt("product_id");
                        String productName = timestampRs.getString("product_name");
                        int quantity = timestampRs.getInt("quantity");
                        if (inventoryMap.containsKey(productId)) {
                            int currentQuantity = inventoryMap.get(productId);
                            if ((((quantity - currentQuantity) > 0) && (quantity - currentQuantity) < .1 * quantity) || quantity - currentQuantity == 0) {
                                Vector<Object> row = new Vector<Object>();
                                row.add(productId);
                                row.add(productName);
                                row.add(quantity);
                                row.add(currentQuantity);
                                //row.add(quantity - currentQuantity);
                                //row.add(.1 * quantity);
                                excess_data.add(row);
                            }
                        }
                    }
            
                    DefaultTableModel model = new DefaultTableModel(excess_data, columnName);
                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);
            
                    report_frame.add(scrollPane);
            
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
                // report_frame.setLocationRelativeTo(null);
                report_frame.setVisible(true);
            }
        });
    } 
}