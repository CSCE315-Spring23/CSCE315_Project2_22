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









