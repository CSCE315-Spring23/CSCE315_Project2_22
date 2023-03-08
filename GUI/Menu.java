import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.math.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;
import java.util.*;



public class Menu extends JFrame implements TableModelListener {

    private JTable table;
    private Connection conn;
    private PreparedStatement update_row;
    private ArrayList<ArrayList<Object>> menu_arr;

    public boolean isCellEditable(int row, int column) { 
        return true; 
    }

    public void tableChanged(TableModelEvent e) {
        int row = e.getFirstRow();
        int column = e.getColumn();
        DefaultTableModel model = (DefaultTableModel)e.getSource();
        String columnName = model.getColumnName(column);
        String menu_item_id = null;
        model.removeTableModelListener(this);  // otherwise updating the model will call this function again. add it back later
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_team_22_master", "0000");
            if (row > menu_arr.size()) {
                model.addTableModelListener(this);
                return;
            }
            if (row == menu_arr.size()) {  // adding an item
                ArrayList<Object> new_row = new ArrayList<Object>();
                new_row.add("0");new_row.add("0");new_row.add(0);
                menu_arr.add(new_row);
                update_row = conn.prepareStatement("INSERT INTO menu VALUES (0, 0, 0)");
                update_row.executeUpdate();
                Object[] table_row = new Object[4];
                model.addRow(table_row);    
                if (columnName.equals("price")) model.setValueAt(Double.parseDouble((String) model.getValueAt(row, column)), row, column);
                else model.setValueAt((String) model.getValueAt(row, column), row, column);

            }
            if (columnName.equals("product_id:quantity")) {
                // totally replace all ingredients and their quantities if they're edited
                update_row = conn.prepareStatement("DELETE FROM menu_item_ingredients WHERE menu_item_id=?");
                update_row.setString(1, (String) model.getValueAt(row, 0));
                update_row.executeUpdate();

                String prodid_quantity = (String) model.getValueAt(row, column);
                String[] id_quantity_arr = prodid_quantity.split(",");
                ArrayList<String> ids = new ArrayList<String>();
                ArrayList<String> quantities = new ArrayList<String>();
                if (!id_quantity_arr[0].equals("")) {
                    for (int i = 0; i < id_quantity_arr.length; ++i) {
                        String[] id_quant_pair = id_quantity_arr[i].split(":");
                        ids.add(id_quant_pair[0]);
                        quantities.add(id_quant_pair[1]);
                    }
                    for (int i = 0; i < ids.size(); ++i) {
                        update_row = conn.prepareStatement("INSERT INTO menu_item_ingredients VALUES (?, ?, ?)");
                        update_row.setString(1, (String) model.getValueAt(row, 0));
                        update_row.setInt(2, Integer.parseInt(ids.get(i)));
                        update_row.setDouble(3, Double.parseDouble(quantities.get(i)));
                        update_row.executeUpdate();
                    }
                }

                model.addTableModelListener(this);
                return;
            }   
            if (!(menu_item_id = (String) model.getValueAt(row, 0)).equals("")) {
                update_row = conn.prepareStatement("UPDATE menu SET " + columnName + "=? WHERE menu_item_id=?");
                String updated_val = (String) model.getValueAt(row, column);
                update_row.setString(2, (String) menu_arr.get(row).get(0));
                if (columnName.equals("price")) {
                    update_row.setDouble(1, Double.parseDouble(updated_val));
                    menu_arr.get(row).set(column, Double.parseDouble(updated_val));
                }
                else {
                    update_row.setString(1, updated_val);
                    menu_arr.get(row).set(column, updated_val);
                }
                update_row.executeUpdate();
            }
            else if (columnName.equals("menu_item_id") && (menu_item_id = (String) model.getValueAt(row, 0)).equals("")) {
                update_row = conn.prepareStatement("DELETE FROM menu_item_ingredients WHERE menu_item_id=?");
                update_row.setString(1, (String) menu_arr.get(row).get(0));
                update_row.executeUpdate();

                update_row = conn.prepareStatement("DELETE FROM menu WHERE menu_item_id=?");
                update_row.setString(1, (String) menu_arr.get(row).get(0));
                update_row.executeUpdate();


                menu_arr.remove(row);
                model.removeRow(row);
                table.revalidate();
            }
            model.addTableModelListener(this);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        
    }

   
    public Menu(){
        try{
            // Class.forName("org.postgresql.Driver");
            menu_arr = new ArrayList<ArrayList<Object>>();
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_team_22_master", "0000");   
                // conn = DriverManager.getConnection(dbConnectionString, "csce315331_veselka", "729009874");
            } 
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
                System.exit(0);
            }

            table = new JTable();
            table.getTableHeader().setReorderingAllowed(false);

            Statement stmt = conn.createStatement();
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
                ArrayList<Object> menu_row = new ArrayList<Object>();
                for (int i = 0; i < columnCount; ++i) {
                    menu_row.add(row[i]);
                }
                model.addRow(row);
                menu_arr.add(menu_row);
            }
            
            //only want to add the one column that contains the string of "product" (read-> ingredient ids) for a menu item
            model.addColumn("product_id:quantity");
            
            ResultSet menu_ingredients = stmt.executeQuery("SELECT menu_item_id,product_id,quantity FROM menu_item_ingredients");
            //gets the first row value of the column 1

            String menu_name_prev = null;
            int row_idx = 0;
            //iterates through the table rows 
            String ids = "";
            while(menu_ingredients.next()) {
                //if the menu item column value is the same as the previous, add the ingredient value to the vector for that menu item
                //if not, push that row to the column in model
                if ((menu_name_prev != null) && !menu_name_prev.equals(menu_ingredients.getString(1))) {
                    model.setValueAt(ids.substring(0, ids.length() - 1), row_idx, 3);
                    ids = "";
                    row_idx++;
                }
                menu_name_prev = menu_ingredients.getString(1);
                ids += menu_ingredients.getString(2) + ":" + menu_ingredients.getString(3) + ",";
            }
            model.addRow(new Object[4]);

            //table.setValueAt(menu_name_prev, columnCount, columnCount);
           
            
            table.setModel(model);
            table.setRowHeight(30);
            table.getModel().addTableModelListener(this);

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
            // this.setVisible(true);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    // public static void main(String[] args){
    //     new Menu();
    // }

}