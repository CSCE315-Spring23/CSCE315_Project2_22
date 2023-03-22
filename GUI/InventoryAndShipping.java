
/**
The InventoryAndShipping class extends JFrame and represents a GUI application
that displays and updates the inventory of products available for shipping.
The class makes a connection to a PostgreSQL database hosted on a remote server,
and populates a JTable with data from the "inventory" table.
Users can add, modify and delete rows from the JTable, and when they click
on the "Update" button, the changes are committed to the database.
@author William C. Hipp
@version 1.0
@since 3/21/2023
@see javax.swing.JFrame
*/

import java.awt.*;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

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


/**
 * Frame that handles the inventory. Allows the manager to update items in any way necessary. Add items, remove items, change quantities and so on.
 * Shipments handled by manually changing inventory quantities.
 */
public class InventoryAndShipping extends JFrame {
    private int currentProductId = 0;
    private int beforeUpdateId = 0;
    private Vector<String> removedIds = new Vector<String>();

    private FrameHandler fh;

    /**
    Creates a new instance of InventoryAndShipping with the specified FrameHandler.
    Initializes a connection to the PostgreSQL database and populates the JTable with data from the "inventory" table.
    Adds a WindowListener to the JFrame that closes the connection and exits the application when the window is closed.
    @param fh the FrameHandler instance that will handle user events.
    @see FrameHandler
    */

    public InventoryAndShipping(FrameHandler fh) {
        this.fh = fh;
        
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
            
            // Create a DefaultTableModel and populate it with data from the "inventory" table

            DefaultTableModel model = new DefaultTableModel(){
                @Override
                public Class<?> getColumnClass(int columnIndex){
                    switch(columnIndex){
                        case 0:
                            return String.class;
                        case 1:
                            return String.class;
                        case 2:
                            return String.class;
                        default:
                            return super.getColumnClass(columnIndex);
                    }
                }
            };
            for(int i = 1; i <= columnCount; i++){
                model.addColumn(rsmd.getColumnName(i));
            }
            
            while (rs.next()){
                Object[] row = new Object[columnCount];
                for(int i = 1; i <= columnCount; i++){
                    switch(1){
                        case 1:
                            row[i-1] = rs.getString(i);
                            break;
                        case 2:
                            row[i-1] = rs.getString(i);
                            break;
                        default:
                            row[i - 1] = rs.getObject(i);
                            break;
                    }
                }
                
                
                if(!row[2].equals("-1")){
                    model.addRow(row);
                }
                
                
                currentProductId++;
                

            }

            beforeUpdateId = currentProductId;
            
            
            JTable table1 = new JTable(model) {
                
                @Override
                public boolean isCellEditable(int row, int col){
                    switch(col){
                        case 0:
                            return false;
                        default:
                            return true;
                    }
                }
            };
            JScrollPane scrollPane1 = new JScrollPane(table1);

            JPanel buttonPanel = new JPanel();

            JButton home = new JButton("Home");
            home.addActionListener(fh);
            buttonPanel.add(home);
            
            JButton addRowButton = new JButton("Add Row");
            addRowButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
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
                        String id = (String) model.getValueAt(selectedRow,0);
                        removedIds.add(id);
                        model.removeRow(selectedRow);
                    }

                }
            });
            buttonPanel.add(deleteRowButton);

            JButton updateDatabaseButton = new JButton("Update");
            updateDatabaseButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    try{
                        conn.setAutoCommit(false);
                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE inventory SET product_name = ?, quantity = ? WHERE product_id = ?");
                        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO inventory (product_id, product_name, quantity) VALUES(?, ?, ?)");
                        PreparedStatement deleteStmt = conn.prepareStatement("UPDATE inventory SET quantity = -1 WHERE product_id = ?");

                        // update existing rows
                        for(int i = 0; i < model.getRowCount(); i++){
                            
                            
                            updateStmt.setString(1,(String)model.getValueAt(i,1));
                            updateStmt.setBigDecimal(2, new BigDecimal((String)model.getValueAt(i,2)));
                            updateStmt.setInt(3, Integer.parseInt((String)model.getValueAt(i,0)));
                            updateStmt.executeUpdate();
                        }
                        
                        // update deleted rows
                        for(int i = 0; i < removedIds.size(); i++){
                            deleteStmt.setInt(1, Integer.parseInt((String)removedIds.get(i)));
                            deleteStmt.executeUpdate();
                        }
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
                                    beforeUpdateId++;
                                }
                                
                                
                            }

                            
                        }
                        
                        
                        
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
            
            // Create a panel to hold the tables
            JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // add a 10 pixel margin around the panel
            panel.add(scrollPane1);
            //panel.add(scrollPane2);
            panel.add(buttonPanel);

            // Add the panel to the frame
            getContentPane().add(panel); // add the panel directly to the frame's content pane

            // Set the frame properties
            setTitle("InventoryAndShipping");
            setSize(500, 300);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}