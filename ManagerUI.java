/**
* The ManagerUI class is responsible for creating and managing the graphical user interface
* for the restaurant manager's application. It extends the JPanel class and contains methods
* and variables for creating and displaying the GUI components.
* @author William C. Hipp
* @version 1.0
* @since 3/21/2023
*/
import javax.swing.*;
import java.awt.*;

public class ManagerUI extends JPanel{

    /** The JFrame that contains the ManagerUI JPanel */
    private JFrame frame;

    /** The FrameHandler that handles frame events for the ManagerUI */
    private FrameHandler fh;

    /**
    * Constructs a new ManagerUI object with a specified JFrame and FrameHandler.
    * 
    * @param frame The JFrame that contains the ManagerUI JPanel
    * @param fh The FrameHandler that handles frame events for the ManagerUI
    */

    public ManagerUI(JFrame frame, FrameHandler fh){
        this.frame = frame;
        this.fh = fh;

        // Placeholder panels
        //JPanel panel1 = new JPanel();
        //JPanel panel2 = new JPanel();
        

        OrderHistory orderHistory = new OrderHistory();
        InventoryAndShipping inventory = new InventoryAndShipping(fh);
        Menu menu = new Menu();
        Reports reports = new Reports();

        JTabbedPane tabbed_pane = new JTabbedPane();
        

        tabbed_pane.addTab("InventoryAndShipments", inventory.getContentPane());
        tabbed_pane.add("Order Trends",panel2);
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