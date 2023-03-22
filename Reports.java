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


/**
 * Used to add a Reports panel to the ManagerUI. This houses buttons to generate various reports.
 */
public class Reports extends JFrame {

    private JPanel reports_panel;


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
        load_restock(restock_report);

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

                // Fill in generating your report and adding to the report frame
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

                    
            
                    conn.close();
            
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

                // Fill in generating your report and adding to the report frame
            }
        });
    } 
}









