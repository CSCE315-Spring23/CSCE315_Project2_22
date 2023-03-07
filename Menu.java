import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.math.*;

public class Menu extends JFrame {

    private JTable table;

    public boolean isCellEditable(int row, int column) { 
        return true; 
    }

    public Menu(){
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn = null;
            try {
                //connects to this url w/ the given credentials
                conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_nair", "428008776");   
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }
            //creates a new jtable
            table = new JTable();
            //makes it so that user's cannot reorder the columns (helpful for editing table code further on)
            table.getTableHeader().setReorderingAllowed(false);

            //creates object for executing SQL statements
            Statement stmt = conn.createStatement();
            //records the output of the SQL statement
            ResultSet menu = stmt.executeQuery("SELECT * FROM menu");
            
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
            model.addColumn("ingredient_ids");
            //records the output of the SQL statement
            ResultSet menu_ingredients = stmt.executeQuery("SELECT menu_item_id,product_id FROM menu_item_ingredients");

            String menu_name_prev = null;
            Vector<String> row = new Vector<String>();
            //iterates through the table rows 
            while(menu_ingredients.next()) {
                //if the menu item column value is the same as the previous, add the ingredient value to the vector for that menu item
                if(menu_name_prev == null || menu_ingredients.getString(1) == menu_name_prev) {
                    menu_name_prev = menu_ingredients.getString(1);
                    row.add(menu_ingredients.getString(2));  
                }
                //if not, push that row to the column in model
                else {
                    //System.out.println(row);
                    model.addRow(row);
                    row.clear();
                }
            }
            
            //adds a "listener" that constantly checks about what the "mouse" is doing
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                //if the mouse is clicked & the mouse clicked a table cell
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    //set the row and col to be equal to the cell row and col
                    int row = table.rowAtPoint(evt.getPoint());
                    int col = table.columnAtPoint(evt.getPoint());
                    int prev_row = 0, prev_col = 0;
                    double prev_value = 0.0;
                    double new_value = 0.0;
                    String prev_name = null;
                    //if the col is the one that is supposed to be editable (price)
                    if (row >= 0 && col == 2) {
                        //make the cell editable
                        isCellEditable(row, col);
                        //get its current value
                        prev_value = ((BigDecimal) table.getValueAt(row, col)).doubleValue();
                        prev_name = (String) table.getValueAt(row, 0);
                        prev_row = row;
                        prev_col = col;
                    } 
                    //when cell is not being edited anymore and the cell value has changed
                    if (!table.isEditing() && prev_value != ((BigDecimal) table.getValueAt(row, col)).doubleValue()) {  
                        //System.out.println("clicked away");
                        if (prev_name == null) return;
                        //get the new value
                        table.setValueAt(new BigDecimal((String) table.getValueAt(row, col)), prev_row, prev_col);
                        new_value = ((BigDecimal) table.getValueAt(prev_row, prev_col)).doubleValue();
                        try {
                            //update database with SQL statement inputting in the new value into the correct column
                            Connection connec = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_nair", "428008776");
                            PreparedStatement ps = connec.prepareStatement("UPDATE menu SET price=? WHERE menu_item=?");
                            ps.setDouble(1, new_value);
                            ps.setString(2, prev_name);
                            ps.executeQuery();
                            // table.fireTableDataChanged();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    } 
                }
            });

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
            this.setVisible(true);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new Menu();
    }

}