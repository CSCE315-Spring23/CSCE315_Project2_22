// This is the structure for the manager UI. All frames will be added to tabbed_pane

import javax.swing.*;

public class ManagerUI extends JPanel{

    private JFrame frame;
    private FrameHandler fh;

    public ManagerUI(JFrame frame, FrameHandler fh){
        this.frame = frame;
        this.fh = fh;

        // Placeholder panels
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        OrderHistory orderHistory = new OrderHistory();
        InventoryAndShipping inventory = new InventoryAndShipping();
        Menu menu = new Menu();

        JTabbedPane tabbed_pane = new JTabbedPane();

        tabbed_pane.addTab("InventoryAndShipments", inventory.getContentPane());
        tabbed_pane.add("Order Trends",panel2);
        tabbed_pane.addTab("Order History", orderHistory.getContentPane());
        tabbed_pane.addTab("Menu", menu.getContentPane());


        this.add(tabbed_pane);
    }
}