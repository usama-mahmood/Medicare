    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import DatabaseAccessLayer.DAO;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.Random;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Usama Mahmood
 */
public class CustomTableModel extends AbstractTableModel {

    String[] headings = {"Choice", "ID", "First Name", "Last Name", "NIC"};
    int rows, cols;
    Object[][] data;
    String firstName;
    String lastName;
    int nic;

    public CustomTableModel(String firstName, String lastName, int nic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        searchPatientModel(firstName, lastName, nic);
    }

    public int getRowCount() {
        return rows;
    }

    public int getColumnCount() {
        return cols;
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int column) {
        return data[0][column].getClass();
    }

    public String getColumnName(int column) {
        return headings[column];
    }

    public boolean isCellEditable(int row, int col) {
        return (col == 0);
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
        if (col == 0 && value.equals(Boolean.TRUE)) {
            resetNonSelectedValues(row, col);
        }
    }

    /**
     * This will give the behavior of a ButtonGroup
     */
    private void resetNonSelectedValues(int newRow, int col) {
        for (int row = 0; row < rows; row++) {
            if (getValueAt(row, col).equals(Boolean.TRUE) && row != newRow) {
                setValueAt(Boolean.FALSE, row, col);
                fireTableCellUpdated(row, col);
            }
        }
    }

    private void searchPatientModel(String firstName, String lastName, int nic) {
        try {
            cols = headings.length; // since our result header has 5 cols
            int rowCount = 0;
            PreparedStatement pre = DAO.getInstance().getCon().prepareStatement("select * from patient_info where first_name like '%" + firstName + "%' or last_name like '%" + lastName + "%' or nic like '%" + nic + "%'");

            ResultSet rs = pre.executeQuery();
            rs.last();
            rows = rs.getRow();
            rs.beforeFirst();
            data = new Object[rows][cols];

            while (rs.next()) {
                data[rowCount][0] = Boolean.FALSE;
                for (int i = 1; i < headings.length; i++) {
                    data[rowCount][i] = rs.getString(i);
                }

                rowCount++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class CustomCellRenderer implements TableCellRenderer, UIResource {

    JRadioButton radioButton;
    Border emptyBorder;

    public CustomCellRenderer() {
        radioButton = new JRadioButton();
        radioButton.setHorizontalAlignment(JRadioButton.CENTER);
        radioButton.setBorderPainted(true);
        emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int col) {
        if (isSelected) {
            radioButton.setBackground(table.getSelectionBackground());
            radioButton.setForeground(table.getSelectionForeground());
        } else {
            radioButton.setBackground(table.getBackground());
            radioButton.setForeground(table.getForeground());
        }
        if (hasFocus) {
            radioButton.setBorder(
                    UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            radioButton.setBorder(emptyBorder);
        }

        radioButton.setSelected(((Boolean) value).booleanValue());
        return radioButton;
    }
}

/**
 * Adapted from source code of javax.swing.DefaultCellEditor
 */
class CustomCellEditor extends AbstractCellEditor implements TableCellEditor {

    JRadioButton radioButton;
    protected int clickCountToStart = 1;

    public CustomCellEditor() {
        radioButton = new JRadioButton();
        radioButton.setHorizontalAlignment(JRadioButton.CENTER);
        radioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // do not allow de-selection of radioButton
                // this is provided for in the method
                //     resetNonSelectedValues
                // of the CustomTableModel class
                if (!radioButton.isSelected()) {
                    cancelCellEditing();
                }
                stopCellEditing();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        radioButton.setSelected(((Boolean) value).booleanValue());
        return radioButton;
    }

    public Component getComponent() {
        return radioButton;
    }

    public int getClickCountToStart() {
        return clickCountToStart;
    }

    public Object getCellEditorValue() {
        return Boolean.valueOf(radioButton.isSelected());
    }

    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing(EventObject anEvent) {
        fireEditingStopped();
        return true;
    }

    public void cancelCellEditing() {
        fireEditingCanceled();
    }
}

class SelectionListener implements ListSelectionListener {

    JTable table;

    public SelectionListener(JTable table) {
        this.table = table;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = table.getSelectedRow();
            boolean state = ((Boolean) table.getValueAt(row, 0)).booleanValue();
            String selected = state ? "" : "not ";
            String s = " ID " + table.getValueAt(row, 1) + ", "
                    + " First name " + table.getValueAt(row, 2) + ", "
                    + " Last Name" + table.getValueAt(row, 3) + ", "
                    + " is " + selected + "selected";
            System.out.println(s);
        }
    }
}
