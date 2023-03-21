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
import java.time.LocalDate;
import java.text.SimpleDateFormat;


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
            }
        });
    } 
}