/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import DatabaseAccessLayer.DAO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.EventObject;
import java.util.Random;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Usama Mahmood
 */
class CustomTableModel extends AbstractTableModel {

    int rows, cols;
    String[] headings;
    Object[][] data;
    String firstName;
    String lastName;
    long nic;

    public CustomTableModel() {
        rows = 8;
        cols = 4;
        initModelData();
    }

    public CustomTableModel(String firstName, String lastName, long nic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        headings = new String[]{"Choice", "ID", "First Name", "Last Name", "NIC"};
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

    private void searchPatientModel(String firstName, String lastName, long nic) {
        try {
            cols = headings.length; // since our result header has 5 cols
            int rowCount = 0;
            PreparedStatement pre = DAO.getInstance().getCon().prepareStatement("call searchPatients(?,?,?)");
            if (firstName.equals("") || firstName == null) {
                pre.setNull(1, Types.NULL);
            } else {

                pre.setString(1, firstName);
            }

            if (lastName.equals("") || lastName == null) {
                pre.setNull(2, Types.NULL);
            } else {

                pre.setString(2, lastName);
            }
            if (nic == 0) {
                pre.setNull(3, Types.NULL);
            } else {

                pre.setLong(3, nic);
            }

            if ((firstName.equals("") || firstName == null) && (lastName.equals("") || lastName == null) && (nic == 0)) {
                pre.setString(1, "");
                pre.setString(2, "");
                pre.setLong(3, 0);
            }

            ResultSet rs = pre.executeQuery();
            rs.last();
            rows = rs.getRow();
            rs.beforeFirst();
            data = new Object[rows][cols];

            while (rs.next()) {
                data[rowCount][0] = Boolean.FALSE;
                for (int i = 1; i < cols; i++) {
                    data[rowCount][i] = rs.getString(i);
                }
                rowCount++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initModelData() {
        headings = new String[]{"index", "choice", "name", "device id"};
        data = new Object[rows][cols];
        Random seed = new Random();

        for (int row = 0; row < rows; row++) {
            int n = row + 1;
            data[row][0] = String.valueOf(n);
            data[row][1] = Boolean.FALSE;
            data[row][2] = "item " + n;
            data[row][3] = String.valueOf(seed.nextInt(4000));
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
    boolean state;
    String SelectedFirstName;
    String SelectedLastName;
    int selectedPatientId;

    public int getSelectedPatientId() {
        return selectedPatientId;
    }

    public void setSelectedPatientId(int selectedPatientId) {
        this.selectedPatientId = selectedPatientId;
    }

    public String getSelectedFirstName() {
        return SelectedFirstName;
    }

    public void setSelectedFirstName(String SelectedFirstName) {
        this.SelectedFirstName = SelectedFirstName;
    }

    public String getSelectedLastName() {
        return SelectedLastName;
    }

    public void setSelectedLastName(String SelectedLastName) {
        this.SelectedLastName = SelectedLastName;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public SelectionListener(JTable table) {
        this.table = table;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = table.getSelectedRow();
            state = ((Boolean) table.getValueAt(row, 0)).booleanValue();
            String selected = state ? "" : "not ";
            String s = " ID " + table.getValueAt(row, 1) + ", "
                    + " First name " + table.getValueAt(row, 2) + ", "
                    + " Last Name" + table.getValueAt(row, 3) + ", "
                    + " is " + selected + "selected";
            System.out.println(s);
            SelectedFirstName = (String) table.getValueAt(row, 2);
            SelectedLastName = (String) table.getValueAt(row, 3);
            selectedPatientId = Integer.parseInt(table.getValueAt(row, 1).toString());
        }
    }
}

public class SearchPatientResult extends javax.swing.JFrame {

    /**
     * Creates new form SearchPatientResult
     */
    String firstName;
    String LastName;
    long Nic;
    SelectionListener sl;

    public SearchPatientResult(String firstName, String LastName, long Nic) {
        this.firstName = firstName;
        this.LastName = LastName;
        this.Nic = Nic;

        initComponents();
        table.setModel(new CustomTableModel(this.firstName, this.LastName, this.Nic));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        sl = new SelectionListener(table);
        table.getSelectionModel().addListSelectionListener(sl);
        table.setDefaultRenderer(Boolean.class, new CustomCellRenderer());
        table.setDefaultEditor(Boolean.class, new CustomCellEditor());
        DefaultTableCellRenderer renderer
                = (DefaultTableCellRenderer) table.getDefaultRenderer(String.class);
        renderer.setHorizontalAlignment(JLabel.CENTER);
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        this.setSize(width / 2, height / 2);

        this.setLocationRelativeTo(null);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        select = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seach Patient Result");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Medicare Trust And Maternity Center");
        jLabel6.setPreferredSize(new java.awt.Dimension(178, 20));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/medicareTrust_Center.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel1.setText("Closed Match Found....");

        back.setText("Back");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(table);

        select.setText("Select");
        select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(select)
                        .addGap(18, 18, 18)
                        .addComponent(back)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(back)
                            .addComponent(select))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        // TODO add your handling code here:

        this.dispose();
        new UserMgmt().setVisible(true);
    }//GEN-LAST:event_backActionPerformed

    private void selectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectActionPerformed
        // TODO add your handling code here:
        if (this.sl.isState()) // Atleast One patient is choosed
        {
            this.dispose();
            new AssignServices(this.sl.getSelectedFirstName() + " " + this.sl.getSelectedLastName(), this.sl.getSelectedPatientId()).setVisible(true);
        } else {
            JOptionPane.showConfirmDialog(null, "Please Select Patient First", "Error", JOptionPane.CANCEL_OPTION);
            return;
        }
    }//GEN-LAST:event_selectActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton back;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton select;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
