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

    private static class OrderPair {
        String item1;
        String item2;
        int count;

        public OrderPair (String name1, String name2) {
            item1 = name1;
            item2 = name2;
            count = 0;
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
<<<<<<< HEAD
=======
        load_excess(excess_report);
>>>>>>> finished the excess report and updated brian's code for load_sells_together

        // add the reports panel, which holds the buttons, to the frame
        this.add(reports_panel);

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
    
    private void load_restock(JButton restock_report) {
        restock_report.setSize(110, 40);
        restock_report.setAlignmentX((float) 0.5);
        restock_report.setForeground(Color.WHITE);
        restock_report.setBackground(Color.BLACK);
        reports_panel.add(restock_report);
        reports_panel.add(Box.createRigidArea(new Dimension(25, 25)));

        restock_report.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                JFrame report_frame = new JFrame("Restock Report");
                report_frame.setSize(600, 800);
                report_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report_frame.setVisible(true);

                // Fill in generating your report and adding to the report frame
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

                // Fill in generating your report and adding to the report frame

                //Initialize an Array of Pairs
                ArrayList<OrderPair> order_pairs = new ArrayList<OrderPair>();  

                //Initialize variables to iterate through the rows of query
                int prev_order_id = 0;
                int curr_order_id = 0;
                String prev_item = "";
                String curr_item = "";

                try {  //The Query
<<<<<<< HEAD

                    //Time stamp to get date range 
                    Timestamp from = new Timestamp(System.currentTimeMillis());
                    Timestamp to = new Timestamp(System.currentTimeMillis());

                    //Connection to database and query to get orders within a time frame
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
                    String selectQuery = "SELECT * FROM orders_by_item WHERE item_date between '?' and '?'";
                    PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                    selectStmt.setTimestamp(1, from);
                    selectStmt.setTimestamp(2, to);
                    ResultSet resultSet = selectStmt.executeQuery();

                    
                    //iterates through the rows of the query 
                    while (resultSet.next()) {
                        curr_order_id = resultSet.getInt("order_id");
                        curr_item = resultSet.getString("menu_item_id");
                        boolean existing = false;

                        for (OrderPair pairs : order_pairs) {  //if pair exists, count will increment
                            if ((prev_item == pairs.item1 || prev_item == pairs.item2) && (curr_item == pairs.item1 || curr_item == pairs.item2)) {
                                pairs.count++;
                                existing = true;
                                break;
                            }
                        }

                        if (existing == false) {  //creates a new pair
                            OrderPair new_pair = new OrderPair(prev_item, curr_item);
                        }
                        
                        //makes the current row into the previous row so the next row is current
                        prev_item = curr_item;
                        prev_order_id = curr_order_id;
                    }
                } 
                catch (SQLException ex) 
                {
                    ex.printStackTrace();
                }

                //Collections.sort(order_pairs, OrderPair.SortPopular);

                JPanel sells_panel = new JPanel(new GridLayout(10,1));
=======

                    //Time stamp to get date range 
                    Timestamp from = new Timestamp(System.currentTimeMillis());
                    Timestamp to = new Timestamp(System.currentTimeMillis());

                    //Connection to database and query to get orders within a time frame
                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22","csce315331_team_22_master", "0000");
                    String selectQuery = "SELECT * FROM orders_by_item WHERE item_date between '?' and '?'";
                    PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                    selectStmt.setTimestamp(1, from);
                    selectStmt.setTimestamp(2, to);
                    ResultSet resultSet = selectStmt.executeQuery();

                    
                    //iterates through the rows of the query 
                    while (resultSet.next()) {
                        curr_order_id = resultSet.getInt("order_id");
                        curr_item = resultSet.getString("menu_item_id");
                        boolean existing = false;

                        for (OrderPair pairs : order_pairs) {  //if pair exists, count will increment
                            if ((prev_item == pairs.item1 || prev_item == pairs.item2) && (curr_item == pairs.item1 || curr_item == pairs.item2)) {
                                pairs.count++;
                                existing = true;
                                break;
                            }
                        }

                        if (existing == false) {  //creates a new pair
                            OrderPair new_pair = new OrderPair(prev_item, curr_item);
                        }
                        
                        //makes the current row into the previous row so the next row is current
                        prev_item = curr_item;
                        prev_order_id = curr_order_id;
                    }
                } 
                catch (SQLException ex) 
                {
                    ex.printStackTrace();
                }
>>>>>>> finished the excess report and updated brian's code for load_sells_together

                //Collections.sort(order_pairs, OrderPair.SortPopular);

<<<<<<< HEAD
                if (order_pairs.size() >= 10) {
                    for (int i = 0; i < 10; i++) {
                        JTextField text_pair = new JTextField("HI");
                        text_pair.setEditable(false);
                        sells_panel.add(text_pair);
                    }
                }
                else if (order_pairs.size() > 0 && order_pairs.size() < 10) {
                    for (int i = 0; i < order_pairs.size(); i++) {
                        JTextField text_pair = new JTextField("HI");
                        text_pair.setEditable(false);
                        sells_panel.add(text_pair);
                    }
                }
                //order_pairs.get(i).item1 + " " + order_pairs.get(i).item2

=======
                JPanel sells_panel = new JPanel(new GridLayout(10,1));


                if (order_pairs.size() >= 10) {
                    for (int i = 0; i < 10; i++) {
                        JTextField text_pair = new JTextField("HI");
                        text_pair.setEditable(false);
                        sells_panel.add(text_pair);
                    }
                }
                else if (order_pairs.size() > 0 && order_pairs.size() < 10) {
                    for (int i = 0; i < order_pairs.size(); i++) {
                        JTextField text_pair = new JTextField("HI");
                        text_pair.setEditable(false);
                        sells_panel.add(text_pair);
                    }
                }
                //order_pairs.get(i).item1 + " " + order_pairs.get(i).item2

>>>>>>> finished the excess report and updated brian's code for load_sells_together
                else if (order_pairs.size() == 0) {
                    JTextField text_pair = new JTextField("No Pairs");
                    text_pair.setEditable(false);
                    sells_panel.add(text_pair);
                }

                report_frame.add(sells_panel);
            }
        });
    } 
}