// This is the structure for the manager UI. All frames will be added to tabbed_pane

import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerUI extends JFrame{

    public ManagerUI(){

        JFrame control = new JFrame("Manager");
        control.setSize(800,600);
        control.setLocationRelativeTo(null);
        //this.setResizable(false);
        control.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setLayout(null);

        // Placeholder panels
        //JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        //JPanel panel3 = new JPanel();

        InventoryAndShipping inventory = new InventoryAndShipping();
        OrderHistory orderHistory = new OrderHistory();



        JTabbedPane tabbed_pane = new JTabbedPane();
        //tabbed_pane.setBounds(50,50,400,400);

        //tabbed_pane.add("Inventory",panel1);
        tabbed_pane.addTab("InventoryAndShipments", inventory.getContentPane());
        tabbed_pane.add("Order Trends",panel2);
        //tabbed_pane.add("Order History",panel3);
    
        tabbed_pane.addTab("Order History", orderHistory.getContentPane());

        control.add(tabbed_pane);
        control.setVisible(true);

        



    }

    public static void main(String[] args){
        new ManagerUI();
    }
}