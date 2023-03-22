import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** 
 * Creates a JFrame that contains a table holding the order history.
 * It loads the information from the orders_summary data table and displays them on the screen for the manager to view.
 */
public class OrderHistory extends JFrame {

    private JTable table;

    /** 
     * This method opens a connection to the database and queries the information from the orders_summary data table.
     * It will then create a new table, creating the columns associated with the table and populating it with the information.
     */
    public OrderHistory(){
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_team_22_master", "0000");            

            table = new JTable();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM orders_summary");

            // Populate table model with data
            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int i = 1; i <= columnCount; i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            while (rs.next()){
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++ ){
                    row[i-1] = rs.getObject(i);
                }
                model.addRow(row);
            }
            table.setModel(model);

            table.setRowHeight(30);

            //Set column headers
                
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.gray);
            header.setForeground(Color.white);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setColumnHeaderView(header);

            //Close connection
            rs.close();
            stmt.close();
            conn.close();

            this.add(scrollPane);
            //Config frame
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(800,600);
            this.setLocationRelativeTo(null);
            //this.setVisible(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}