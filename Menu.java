import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class Menu extends JFrame {

    private JTable table;

    public Menu(){
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_nair", "428008776");            

            table = new JTable();
            Statement stmt = conn.createStatement();
            ResultSet menu = stmt.executeQuery("SELECT * FROM menu");
            ResultSet menu_ingredients = stmt.executeQuery("SELECT menu_item_id,product_id FROM menu_item_ingredients");

            // Populate table model with menu data
            DefaultTableModel model = new DefaultTableModel();
            ResultSetMetaData rsmd = menu.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int i = 1; i <= columnCount; i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            while (menu.next()){
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++ ){
                    row[i-1] = menu.getObject(i);
                }
                model.addRow(row);
            }

            //only want to add the one column that contains the string of "product" (read-> ingredient ids) for a menu item
            model.addColumn("product_ids");
            Vector<String> row = new Vector<String>();
            //gets the first row value of the column 0
            String menu_name_prev = menu_ingredients.getString(1);
            //iterates through the table rows 
            while(menu_ingredients.next()) {
                //if the menu item column value is the same as the previous, add the ingredient value to the vector for that menu item
                if(menu_ingredients.getString(1) == menu_name_prev) {
                    menu_name_prev = menu_ingredients.getString(1);
                    row.add(menu_ingredients.getString(2));  
                }
                //if not, push that row to the column in model
                else {
                    model.addRow(row);
                }
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
            menu.close();
            menu_ingredients.close();
            stmt.close();
            conn.close();

            this.add(scrollPane);
            //Config frame
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setSize(800,600);
            this.setLocationRelativeTo(null);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new Menu();
    }

}