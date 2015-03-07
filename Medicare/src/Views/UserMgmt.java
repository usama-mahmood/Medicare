/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import DatabaseAccessLayer.DAO;
import Models.ButtonColumn;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.application.Task;

/**
 *
 * @author Usama Mahmood
 */
public class UserMgmt extends javax.swing.JFrame implements PropertyChangeListener {

    /**
     * Creates new form UserMgmt
     */
    private Vector<Vector<String>> data; //used for data from database
    private Vector<String> header; //used to store data header

    private Vector<Vector<String>> dataDentist; //used for data from database
    private Vector<String> headerDentist; //used to store data header

    private Vector<Vector<String>> dataEmergy; //used for data from database
    private Vector<String> headerEmergy; //used to store data header

    private Vector<Vector<String>> dataLabTest; //used for data from database
    private Vector<String> headerLabTest; //used to store data header
    private Task task;

    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */

        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {
                }
                //Make random progress.
                progress += random.nextInt(20);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            reportBtn.setEnabled(true);
            setCursor(null); //turn off the wait cursor
            try {

                Date date = datePicker.getDate();
                Map map = new HashMap();
                map.put("date", date);
                InputStream imagePath = this.getClass().getResourceAsStream("/resources/medicareTrust_Center.png");
                map.put("imagePath", imagePath);
                InputStream bdl = this.getClass().getResourceAsStream("/reports/DayEndSummary.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(bdl);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, DAO.getInstance().getCon());//new JRResultSetDataSource(DAO.getInstance().fetchDataSource())
                JasperViewer.viewReport(jasperPrint, false);
                JasperExportManager.exportReportToPdfFile(jasperPrint, "DayEndReport.pdf");

            } catch (Exception e) {
                e.printStackTrace();
            }
            setProgress(0);
            progress.setVisible(false);
            
        }
    }

    public UserMgmt() {

        data = DAO.getInstance().getServicesData("SERVICES");
        header = new Vector<String>();
        header.add("Services"); //service Type
        header.add("Charges"); // charges

        dataDentist = DAO.getInstance().getServicesData("DENTISTRY");
        headerDentist = new Vector<String>();
        headerDentist.add("Dentistry"); //service Type
        headerDentist.add("Charges"); // charges

        dataEmergy = DAO.getInstance().getServicesData("EMERGENCY");
        headerEmergy = new Vector<String>();
        headerEmergy.add("Emergency"); //service Type
        headerEmergy.add("Charges"); // charges

        dataLabTest = DAO.getInstance().getServicesData("LAB_TEST");
        headerLabTest = new Vector<String>();
        headerLabTest.add("Lab Test"); //service Type
        headerLabTest.add("Charges"); // chargesgetOtherServicesData

        if (DAO.getInstance().getLoggedInUser().equals("admin")) {
            header.add("Update");
            header.add("Remove");

            headerDentist.add("Update");
            headerDentist.add("Remove");

            headerEmergy.add("Update");
            headerEmergy.add("Remove");

            headerLabTest.add("Update");
            headerLabTest.add("Remove");
            initComponents();
            Action delete = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());

                    DefaultTableModel obj = (DefaultTableModel) table.getModel();
                    ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                }
            };

            Action update = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    JTable table = (JTable) e.getSource();
                    int modelRow = Integer.valueOf(e.getActionCommand());
                    DefaultTableModel obj = (DefaultTableModel) table.getModel();
                    Vector<Vector<String>> serviceData = obj.getDataVector();
                    new UpdateService(obj, serviceData.get(modelRow).get(0), Integer.parseInt(serviceData.get(modelRow).get(1)), modelRow).setVisible(true);
                    table.setModel(obj);
                }
            };

            ButtonColumn serviceDelete = new ButtonColumn(services, delete, 3);
            serviceDelete.setMnemonic(KeyEvent.VK_D);

            ButtonColumn serviceUpdate = new ButtonColumn(services, update, 2);
            serviceUpdate.setMnemonic(KeyEvent.VK_D);

            ButtonColumn dentistryDelete = new ButtonColumn(dentist, delete, 3);
            dentistryDelete.setMnemonic(KeyEvent.VK_D);

            ButtonColumn dentistryUpdate = new ButtonColumn(dentist, update, 2);
            dentistryUpdate.setMnemonic(KeyEvent.VK_D);

            ButtonColumn emecyDelete = new ButtonColumn(emergy, delete, 3);
            emecyDelete.setMnemonic(KeyEvent.VK_D);

            ButtonColumn emecyUpdate = new ButtonColumn(emergy, update, 2);
            emecyUpdate.setMnemonic(KeyEvent.VK_D);

            ButtonColumn labTstDelete = new ButtonColumn(labTest, delete, 3);
            labTstDelete.setMnemonic(KeyEvent.VK_D);

            ButtonColumn labTestUpdate = new ButtonColumn(labTest, update, 2);
            labTestUpdate.setMnemonic(KeyEvent.VK_D);
        } else {
            initComponents();
            this.save1.setVisible(false);
            this.save2.setVisible(false);
            this.save3.setVisible(false);
            this.save4.setVisible(false);
            this.add1.setVisible(false);
            this.add2.setVisible(false);
            this.add3.setVisible(false);
            this.add4.setVisible(false);
            this.pack();
        }

        table.setTitleAt(0, "Patients Info");
        table.setTitleAt(1, "Services");
        table.setTitleAt(2, "Dentistry");
        table.setTitleAt(3, "Emergency");
        table.setTitleAt(4, "Lab Tests");
        services.editingStopped(new ChangeEvent(services));

        this.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int height = screenSize.height;
        int width = screenSize.width;
        this.setSize(width / 2, height / 2);

        // center the jframe on screen
        this.setLocationRelativeTo(null);
        //Where the GUI is constructed:
        Border border = BorderFactory.createTitledBorder("Reading...");
        progress.setValue(0);
        progress.setStringPainted(true);
      //  progress.setBorder(border);
        progress.setVisible(false);
    }

    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progres = (Integer) evt.getNewValue();
            progress.setValue(progres);
            //taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
        }
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
        table = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        newPatient = new javax.swing.JButton();
        repeatPatient = new javax.swing.JButton();
        reportBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        progress = new javax.swing.JProgressBar(0,100);
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        services = new javax.swing.JTable();
        add1 = new javax.swing.JButton();
        save1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dentist = new javax.swing.JTable();
        save2 = new javax.swing.JButton();
        add2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        emergy = new javax.swing.JTable();
        save3 = new javax.swing.JButton();
        add3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        labTest = new javax.swing.JTable();
        save4 = new javax.swing.JButton();
        add4 = new javax.swing.JButton();
        logout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manage Patients");

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
                .addGap(65, 65, 65)
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(71, 71, 71)
                .addComponent(jLabel8)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        newPatient.setText("New Patient");
        newPatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newPatientActionPerformed(evt);
            }
        });

        repeatPatient.setText("Repeat Patient");
        repeatPatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatPatientActionPerformed(evt);
            }
        });

        reportBtn.setText("Generate Report");
        reportBtn.setActionCommand("Generate Report ");
        reportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBtnActionPerformed(evt);
            }
        });

        jLabel3.setText("Select Report Date");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addComponent(reportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(newPatient, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(repeatPatient)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repeatPatient, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newPatient, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        table.addTab("tab1", jPanel1);

        services.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        services.setModel(new javax.swing.table.DefaultTableModel(
            data,header
        ));
        jScrollPane1.setViewportView(services);

        add1.setText("Add Service");
        add1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add1ActionPerformed(evt);
            }
        });

        save1.setText("Save");
        save1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(save1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add1)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add1)
                    .addComponent(save1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        table.addTab("tab2", jPanel3);

        dentist.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        dentist.setModel(new javax.swing.table.DefaultTableModel(
            dataDentist,headerDentist
        ));
        jScrollPane2.setViewportView(dentist);

        save2.setText("Save");
        save2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save2ActionPerformed(evt);
            }
        });

        add2.setText("Add Service");
        add2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(save2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add2)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add2)
                    .addComponent(save2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        table.addTab("tab3", jPanel4);

        emergy.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        emergy.setModel(new javax.swing.table.DefaultTableModel(
            dataEmergy,headerEmergy
        ));
        jScrollPane3.setViewportView(emergy);

        save3.setText("Save");
        save3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save3ActionPerformed(evt);
            }
        });

        add3.setText("Add Service");
        add3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(save3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add3)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add3)
                    .addComponent(save3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        table.addTab("tab4", jPanel5);

        labTest.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        labTest.setModel(new javax.swing.table.DefaultTableModel(
            dataLabTest,headerLabTest
        ));
        jScrollPane4.setViewportView(labTest);

        save4.setText("Save");
        save4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save4ActionPerformed(evt);
            }
        });

        add4.setText("Add Service");
        add4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(save4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add4)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add4)
                    .addComponent(save4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        table.addTab("tab5", jPanel6);

        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(table, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(logout)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(logout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(table)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newPatientActionPerformed
        // TODO add your handling code here:

        this.dispose();
        new Registration().setVisible(true);
    }//GEN-LAST:event_newPatientActionPerformed

    private void repeatPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatPatientActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new SearchPatient().setVisible(true);
    }//GEN-LAST:event_repeatPatientActionPerformed

    private void reportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBtnActionPerformed
        if (datePicker.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please Enter Date First");
            return;
        }
        progress.setVisible(true);
        reportBtn.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }//GEN-LAST:event_reportBtnActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_logoutActionPerformed

    private void add1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel obj = (DefaultTableModel) services.getModel();

        AddService addServiceObj;
        addServiceObj = new AddService(obj);
        addServiceObj.setVisible(true);

        // addServiceObj.dispose();
    }//GEN-LAST:event_add1ActionPerformed

    private void add2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add2ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel obj = (DefaultTableModel) dentist.getModel();

        AddService addServiceObj;
        addServiceObj = new AddService(obj);
        addServiceObj.setVisible(true);
    }//GEN-LAST:event_add2ActionPerformed

    private void add3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add3ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel obj = (DefaultTableModel) emergy.getModel();

        AddService addServiceObj;
        addServiceObj = new AddService(obj);
        addServiceObj.setVisible(true);
    }//GEN-LAST:event_add3ActionPerformed

    private void add4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add4ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel obj = (DefaultTableModel) labTest.getModel();

        AddService addServiceObj;
        addServiceObj = new AddService(obj);
        addServiceObj.setVisible(true);
    }//GEN-LAST:event_add4ActionPerformed

    private void save1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel obj = (DefaultTableModel) services.getModel();
        Vector<Vector<String>> serviceData = obj.getDataVector();

        for (int i = 0; i < serviceData.size(); i++) {
            System.out.println("--->>" + serviceData.get(i).get(0));
            System.out.println("--->>" + serviceData.get(i).get(1));

        }
        JOptionPane.showMessageDialog(this, "Working On This Functionality");
    }//GEN-LAST:event_save1ActionPerformed

    private void save2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save2ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Working On This Functionality");

    }//GEN-LAST:event_save2ActionPerformed

    private void save3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save3ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Working On This Functionality");

    }//GEN-LAST:event_save3ActionPerformed

    private void save4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save4ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Working On This Functionality");

    }//GEN-LAST:event_save4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add1;
    private javax.swing.JButton add2;
    private javax.swing.JButton add3;
    private javax.swing.JButton add4;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JTable dentist;
    private javax.swing.JTable emergy;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable labTest;
    private javax.swing.JButton logout;
    private javax.swing.JButton newPatient;
    private javax.swing.JProgressBar progress;
    private javax.swing.JButton repeatPatient;
    private javax.swing.JButton reportBtn;
    private javax.swing.JButton save1;
    private javax.swing.JButton save2;
    private javax.swing.JButton save3;
    private javax.swing.JButton save4;
    private javax.swing.JTable services;
    private javax.swing.JTabbedPane table;
    // End of variables declaration//GEN-END:variables
}
