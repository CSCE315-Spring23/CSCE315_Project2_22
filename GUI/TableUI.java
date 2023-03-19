import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class TableUI extends JPanel{
    // private JFrame frame;
    private JTable table;
    private JTabbedPane tabbedPane;
    private JFrame frame;
    private FrameHandler fh;

    public TableUI(JFrame frame, FrameHandler fh){
        // Initialize components
        this.frame = frame;
        this.fh = fh;
        table = new JTable();
        tabbedPane = new JTabbedPane();

        // Set up database connection

        try{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_hipp", "130004619");            
            String[] tableNames = {"inventory","inventory_snapshot","shipments"};
            for(String tableName : tableNames){
                JTable table = new JTable();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

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
                

                //Add table to tab
                tabbedPane.addTab(tableName, scrollPane);

                //Close connection
                rs.close();
                stmt.close();
            }


            conn.close();
        

        } catch (Exception e){
            e.printStackTrace();
        }

        //Add tabbed pane to frame
        this.add(tabbedPane);        
        
        // Set row heights
        table.setRowHeight(30);

        //Add horizontal and vertical grid lines
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);

        //Config frame
        this.setSize(frame.getSize());
    }
}