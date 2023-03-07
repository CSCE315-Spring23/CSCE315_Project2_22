import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.math.BigDecimal;
import javax.swing.event.*;
import javax.swing.table.TableModel;

import java.sql.*;

public class InventoryAndShipping extends JFrame {
    private int currentProductId = 0;
    private int beforeUpdateId = 0;
    public InventoryAndShipping() {

        
        try{
            //make connection:
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315331_team_22", "csce315331_team_22_master", "0000");
            
            // Create inventory table
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt. executeQuery("SELECT * FROM inventory");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // add Window Listener that closes connection on close
            addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent e){
                    try{
                        conn.close();
                        stmt.close();
                        rs.close();
                    }catch(SQLException ex){
                        ex.printStackTrace();
                    }

                    //exit application
                    System.exit(0);
                }
            });

            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public Class<?> getColumnClass(int columnIndex){
                    switch(columnIndex){
                        case 0:
                            return String.class;
                        case 1:
                            return String.class;
                        case 2:
                            return BigDecimal.class;
                        default:
                            return super.getColumnClass(columnIndex);
                    }
                }
            };
            for(int i = 1; i <= columnCount; i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            //model.addColumn("Button");
            //BigDecimal currentProductId = BigDecimal.Zero;
            //int currentProductId = 0;
            while (rs.next()){
                /*
                Object[] row = new Object[columnCount];//Object[columnCount+1];
                for (int i = 1; i <= columnCount; i++ ){
                    //if(columnCount == 3)
                    row[i-1] = rs.getObject(i);
                }
                */
                Object[] row = new Object[columnCount];
                for(int i = 1; i <= columnCount; i++){
                    switch(1){
                        case 1:
                            row[i-1] = rs.getString(i);
                            break;
                        case 2:
                            //row[i-1] = rs.getInt(i);
                            row[i-1] = rs.getString(i);
                            break;
                        default:
                            row[i - 1] = rs.getObject(i);
                            break;
                    }
                }
                //row[columnCount] = "auto fill";
                if(!row[2].equals(-1)){
                    model.addRow(row);
                }
                //model.addRow(row);
                currentProductId++;
                

            }

            beforeUpdateId = currentProductId;
            
            //DefaultTableModel model1 = new DefaultTableModel(data1, columnNames1);
            JTable table1 = new JTable(model) {
                
                @Override
                /*
                public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int columnIndex) {
                    
                    Component component = super.prepareRenderer(renderer, rowIndex, columnIndex);
                    if (columnIndex == 2) {
                        BigDecimal value = (BigDecimal)getValueAt(rowIndex, columnIndex);
                        
                        if (value != null && value.compareTo(new BigDecimal("30")) < 0) {
                            component.setBackground(Color.RED);
                        } else {
                            component.setBackground(getBackground());
                        }
                    } else {
                        component.setBackground(getBackground());
                    }
                    return component;
                
                }
                */

                public boolean isCellEditable(int row, int col){
                    switch(col){
                        case 0:
                            return false;
                        default:
                            return true;
                    }
                }
            };
            //table1.setEnabled(false);
            JScrollPane scrollPane1 = new JScrollPane(table1);

            JPanel buttonPanel = new JPanel();

            JButton addRowButton = new JButton("Add Row");
            addRowButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    //model.addRow(new Object[columnCount]);
                    Object[] row1 = new Object[columnCount];
                    for(int i = 0; i < columnCount; i++){
                        if(i == 0){
                            row1[i] = Integer.toString(++currentProductId);
                        }else{
                            row1[i] = null;
                        }
                    }
                    model.addRow(row1);
                }
            });
            buttonPanel.add(addRowButton);

            JButton deleteRowButton = new JButton("Delete Row");
            deleteRowButton = new JButton("Delete Row");
            deleteRowButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    int selectedRow = table1.getSelectedRow();
                    if(selectedRow != -1){
                        model.removeRow(selectedRow);
                    }
                }
            });
            buttonPanel.add(deleteRowButton);

            JButton updateDatabaseButton = new JButton("Update");
            updateDatabaseButton.addActionListener(new ActionListener(){
                /*
                @Override
                public void actionPerformed(ActionEvent e){
                    try{
                        Statement stmt = conn.createStatement();
                        stmt.executeUpdate("DELETE FROM inventory");

                        for(int i = 0; i < model.getRowCount(); i++){
                            String query = "INSERT INTO inventory VALUES (";
                            for(int j = 0; j < model.getColumnCount(); j++){
                                Object value = model.getValueAt(i,j);
                                if(value == null){
                                    query += "null, ";
                                }else{
                                    query += "'" + value.toString() + "',";
                                }
                            }
                            query = query.substring(0, query.length() - 1);
                            query += ")";
                            stmt.executeUpdate(query);
                        }

                        JOptionPane.showMessageDialog(InventoryAndShipping.this, "Database updated successfully.");
                    } catch (SQLException ex){
                        JOptionPane.showMessageDialog(InventoryAndShipping.this, "Error updating database: " + ex.getMessage());
                    }
                }
                */
                public void actionPerformed(ActionEvent e){
                    try{
                        conn.setAutoCommit(false);
                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE inventory SET product_name = ?, quantity = ? WHERE product_id = ?");
                        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO inventory (product_id, product_name, quantity) VALUES(?, ?, ?)");
                        PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM inventory WHERE product_id = ?");
                        PreparedStatement updateOldStmt = conn.prepareStatement("UPDATE inventory SET quantity = ? WHERE product_id = ?");
                        PreparedStatement deleteStmt = conn.prepareStatement("UPDATE inventory SET quantity = -1 WHERE product_id < ? AND quantity != -1");

                        // update existing rows
                        /*
                        for(int i = 0; i <model.getRowCount(); i++){
                            int productId = (int)model.getValueAt(i,0);
                            if(productId <= beforeUpdateId){
                                String product_name = model.getValueAt(i,1).toString();
                                int quan = Integer.parseInt(model.getValueAt(i,2).toString());
                                updateStmt.setString(1,product_name);
                                updateStmt.setInt(2, quan);
                                updateStmt.executeUpdate();
                            }
                            checkStmt.setInt(1,productId);
                            ResultSet rs = checkStmt.executeQuery();
                            rs.next();
                            int count = rs.getInt(1);
                            rs.close();
                            if(count == 0 && productId < beforeUpdateId){
                                updateOldStmt.setInt(1,-1);
                                updateOldStmt.setInt(2,productId);
                                updateOldStmt.executeUpdate();
                            }
                        }
                        */
                        // insert new rows for product_id > before Update_Id
                        for(int i = 0; i < model.getRowCount(); i++){
                            String productId = (String)model.getValueAt(i,0);
                            //int productId = tempproductId
                            if(Integer.parseInt(productId) > beforeUpdateId){
                                
                                String productName = (String)model.getValueAt(i,1);
                                String quan = (String)model.getValueAt(i,2);
                                if (productName != null || quan != null){
                                    insertStmt.setInt(1,Integer.parseInt(productId));
                                    insertStmt.setString(2, productName);
                                    insertStmt.setBigDecimal(3, new BigDecimal(quan));
                                    insertStmt.executeUpdate();
                                }
                                
                                
                            }
                        }
                        /*
                        // update existing rows with product_id < beforeUpdateId that have quantity = -1
                        deleteStmt.setInt(1, beforeUpdateId);
                        deleteStmt.executeUpdate();
                        */
                        conn.commit();
                        
                        JOptionPane.showMessageDialog(InventoryAndShipping.this, "Database updated successfully.");
                    }catch(SQLException ex){
                        try{
                            conn.rollback();
                        } catch (SQLException ex2){
                            ex2.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(InventoryAndShipping.this, "Error updating database: " + ex.getMessage());
                    }
                }
            });

            buttonPanel.add(updateDatabaseButton);

            // Create the second table
            String[] columnNames2 = {"product_id", "quantity", "subtotal"};
            Object[][] data2 = {
                    {"New York", "USA", 8623000},
                    {"","",}
            };
            DefaultTableModel model2 = new DefaultTableModel(data2, columnNames2);
            JTable table2 = new JTable(model2);
            JScrollPane scrollPane2 = new JScrollPane(table2);

            // Create a panel to hold the tables
            JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // add a 10 pixel margin around the panel
            panel.add(scrollPane1);
            panel.add(scrollPane2);
            panel.add(buttonPanel);

            // Add the panel to the frame
            getContentPane().add(panel); // add the panel directly to the frame's content pane

            //Close connection
            /*
            rs.close();
            stmt.close();
            conn.close();
            */

            // Set the frame properties
            setTitle("InventoryAndShipping");
            setSize(500, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //setVisible(true);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new InventoryAndShipping();
    }
}