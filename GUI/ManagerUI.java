// This is the structure for the manager UI. All frames will be added to tabbed_pane

import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerUI extends JPanel{

    private JFrame frame;
    private FrameHandler fh;

    public ManagerUI(JFrame frame, FrameHandler fh){
        this.frame = frame;
        this.fh = fh;

        // frame = new JFrame("test");
        // frame.setSize(800,600);
        // frame.setLocationRelativeTo(null);
        // //this.setResizable(false);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Placeholder panels
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        //JPanel panel3 = new JPanel();

        OrderHistory orderHistory = new OrderHistory();
        InventoryAndShipping inventory = new InventoryAndShipping(fh);
        Menu menu = new Menu();
        Reports reports = new Reports();

        JTabbedPane tabbed_pane = new JTabbedPane();
        //tabbed_pane.setBounds(50,50,400,400);

        tabbed_pane.addTab("InventoryAndShipments", inventory.getContentPane());
        tabbed_pane.add("Order Trends",panel2);
        //tabbed_pane.add("Order History",panel3);
        tabbed_pane.addTab("Order History", orderHistory.getContentPane());
        tabbed_pane.addTab("Menu", menu.getContentPane());
        tabbed_pane.addTab("Reports", reports.getContentPane());


        this.add(tabbed_pane);
        // frame.setVisible(true);



    }

    // public static void main(String[] args){
    //     new ManagerUI(null, null);
    // }
}