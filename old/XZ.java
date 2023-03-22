import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class XZ extends JFrame{
    //from what I can tell, x report is sales from last EOD till point of the current day x report is requested
    //z report is generated at EOD

    private JTable table;
    private Connection conn;
    private PreparedStatement update_row;
    
    public XZ(){
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_team_22_master", "0000");
            table = new JTable();
            table.getTableHeader().setReorderingAllowed(false);

            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet order_summary = stmt.executeQuery("SELECT * FROM orders_summary");
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("report type");
            model.addColumn("date");
            model.addColumn("total_sales");

            double total_sales = 0;
            order_summary.next();
            String[] prev_splitted = order_summary.getString(3).split(" ");
            order_summary.previous();

            while(order_summary.next()) {
                String[] splitted = order_summary.getString(3).split(" ");
                Object[] row = new Object[3];
                LocalTime myobj = LocalTime.now();
                ZoneId zonedId = ZoneId.of( "America/Montreal" );
                LocalDate today = LocalDate.now( zonedId );
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedString = today.format(formatter); 

                //handles old z reports 
                //myobj.getHour() > 12 && myobj.getHour()-12 > 5
                total_sales += order_summary.getDouble(4);
                if(prev_splitted[0].equals(splitted[0]) == false) {
                    // update_row = conn.prepareStatement("INSERT INTO z_reports VALUES (?, ?)");
                    // update_row.setString(1, prev_splitted[0]);
                    // update_row.setDouble(2, total_sales);
                    // update_row.executeUpdate();  
                    if(splitted[0].equals(formattedString)) {
                        row[0] = "x report";
                        row[1] = splitted[0];
                        row[2] = total_sales;
                        model.addRow(row);
                        prev_splitted = splitted;
                    }
                    else {
                        row[0] = "z report";
                        row[1] = splitted[0];                
                        row[2] = total_sales;
                        model.addRow(row);

                        total_sales = 0; 
                        prev_splitted=splitted;
                    }
                }
                //handles old x reports if they're needed 
               
                
                // else {
                    
                //     prev_splitted = splitted;
                //     //handles new z reports
                //     // update_row = conn.prepareStatement("INSERT INTO z_reports VALUES (?, ?)");
                //     // update_row.setString(1, prev_splitted[0]);
                //     // update_row.setDouble(2, total_sales);
                //     // update_row.executeUpdate();  
                //     row[0] = "x report";
                //     row[1] = prev_splitted[0];
                //     row[2] = total_sales;
                //     model.addRow(row);

                //     total_sales = 0; 
                //     }
                    //handles x reports
                    // else{
                    //     row[0] = "x report";
                    //     row[1] = prev_splitted[0];
                    //     row[2] = total_sales;
                    //     model.addRow(row);  
                    // }
                }
              
            
            table.setModel(model);
            table.setRowHeight(30);
            JTableHeader header = table.getTableHeader();
            header.setBackground(Color.gray);
            header.setForeground(Color.white);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setColumnHeaderView(header);

            this.add(scrollPane);
            //Config frame
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(800,600);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args){
    new XZ();
}

}