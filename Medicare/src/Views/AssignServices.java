/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import DatabaseAccessLayer.DAO;
import Models.AddServicesModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.apache.derby.client.am.ResultSet;

/**
 *
 * @author Usama Mahmood
 */
public class AssignServices extends javax.swing.JFrame {

    /**
     * Creates new form AssignServices
     */
    String selectedPatientName;
    int selectedPatientID;
    int selectedPatientZakat;
    private String selectedHealthType;
    private String selectedEmergencyType;
    private String selectedInPatientType;
    private String selectedOtherType;
    int healthCareId;
    int emergyId;
    int inPatientId;
    int otherServiceId;
    int labTestId;
     Statement stmt = DAO.getInstance().getStmt();
              Connection con = DAO.getInstance().getCon();
                  
    public AssignServices(String patienName,int patientID) {
        this.selectedPatientName = patienName;
        this.selectedPatientID = patientID;
        initComponents();
        patientName.setText(patienName);
        fetchZakat();
        zakatLabel.setText(selectedPatientZakat+"");
        initializeHealthCareTypeDropdown();
        initializeEmergencyDropdown();
        initializeInPatientDropdown();
        initializeOtherServicesDropdown();
        
    }
    
     public void initializeHealthCareTypeDropdown()
    {
        String [] healthCareTypes = DAO.getInstance().getHealthCareTypes();

        for(int i = 0;i<healthCareTypes.length;i++)
        {
           healthCareServices.addItem(healthCareTypes[i]); 
        }
    }
     
      public void initializeEmergencyDropdown()
    {
        String [] emerTypes = DAO.getInstance().getEmergecyTypes();

        for(int i = 0;i<emerTypes.length;i++)
        {
           emergency.addItem(emerTypes[i]); 
        }
    }
    
     public void initializeInPatientDropdown()
    {
        String [] inPatientTypes = DAO.getInstance().getInPatientTypes();

        for(int i = 0;i<inPatientTypes.length;i++)
        {
           inPatient.addItem(inPatientTypes[i]); 
        }
    }
        public void initializeOtherServicesDropdown()
    {
        String [] othrService = DAO.getInstance().getOtherServices();

        for(int i = 0;i<othrService.length;i++)
        {
           otherService.addItem(othrService[i]); 
        }
    }
        
     public boolean validateHealthServiceCombo()
        {
              try{//find the health care id against selected service and save it in peron info 
             
                  stmt = con.createStatement();   
             String sql = "select HEALTH_CARE_ID from HEALTHCARE where HEALTH_CARE_TYPE = '"+selectedHealthType+"'";
                  java.sql.ResultSet rs= stmt.executeQuery(sql);
            rs.next();
            healthCareId= rs.getInt("HEALTH_CARE_ID");
            return true;
         }catch(SQLException e)
         {
             //JOptionPane.showMessageDialog(null,"Choose A Valid Service ");
             return false;
         }
        }
     public boolean validateEmergencyServiceCombo()
        {
              try{//find the health care id against selected service and save it in peron info 
             
                  stmt = con.createStatement();   
             String sql = "select EMER_SVC_ID from emer_svcs where EMER_TYPE = '"+selectedEmergencyType+"'";
                  java.sql.ResultSet rs= stmt.executeQuery(sql);
            rs.next();
            emergyId= rs.getInt("EMER_SVC_ID");
            return true;
         }catch(SQLException e)
         {
             //JOptionPane.showMessageDialog(null,"Choose A Valid Service ");
             return false;
         }
        }
     public boolean validateInPatientCombo()
        {
              try{//find the health care id against selected service and save it in peron info 
             
                  stmt = con.createStatement();   
             String sql = "select INDOOR_PATIENT_ID from indoor_patient where PATIENT_CARE_TYPE = '"+selectedInPatientType+"'";
                  java.sql.ResultSet rs= stmt.executeQuery(sql);
            rs.next();
            inPatientId= rs.getInt("INDOOR_PATIENT_ID");
            return true;
         }catch(SQLException e)
         {
             //JOptionPane.showMessageDialog(null,"Choose A Valid Service ");
             return false;
         }
        }
     public boolean validateOtherServicesCombo()
        {
              try{//find the health care id against selected service and save it in peron info 
             
                  stmt = con.createStatement();   
             String sql = "select SERVICE_ID from other_services where SERVICE_TYPE = '"+selectedOtherType+"'";
                  java.sql.ResultSet rs= stmt.executeQuery(sql);
            rs.next();
            otherServiceId = rs.getInt("SERVICE_ID");
            return true;
         }catch(SQLException e)
         {
             //JOptionPane.showMessageDialog(null,"Choose A Valid Service ");
             return false;
         }
        }
     public boolean fetchZakat()
        {
              try{//find the health care id against selected service and save it in peron info 
             
                  stmt = con.createStatement();   
             String sql = "select zakat from patient_info where PATIENT_ID = '"+this.selectedPatientID+"'";
                  java.sql.ResultSet rs= stmt.executeQuery(sql);
            rs.next();
            selectedPatientZakat = rs.getInt("zakat");
            return true;
         }catch(SQLException e)
         {
             //JOptionPane.showMessageDialog(null,"Choose A Valid Service ");
             return false;
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        patientName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        healthCareServices = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        emergency = new javax.swing.JComboBox();
        jLabel = new javax.swing.JLabel();
        inPatient = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        otherService = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        save = new javax.swing.JButton();
        zakatLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Add Patient Services");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Patient's Name: ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Choose Services");

        jLabel4.setText("HealthCare Services");

        healthCareServices.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose Health Care Service" }));
        healthCareServices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                healthCareServicesActionPerformed(evt);
            }
        });

        jLabel5.setText("Emergency Services");

        emergency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose EmerGency Service" }));
        emergency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emergencyActionPerformed(evt);
            }
        });

        jLabel.setText("In Patient Services");

        inPatient.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose In-door Patient Service" }));
        inPatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inPatientActionPerformed(evt);
            }
        });

        jLabel9.setText("Other Services");

        otherService.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Choose Any Other Services" }));
        otherService.setToolTipText("");
        otherService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherServiceActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Zakat Recieved As : ");

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        zakatLabel.setText("jLabel3");

        jLabel3.setText("Date");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(patientName, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(save)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel)
                                    .addComponent(jLabel9))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(otherService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(inPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(emergency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(healthCareServices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)
                                .addComponent(zakatLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1)
                    .addComponent(patientName)
                    .addComponent(save))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4))
                            .addComponent(healthCareServices, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(emergency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel)
                            .addComponent(inPatient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(otherService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(zakatLabel)
                            .addComponent(jLabel3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        // TODO add your handling code here:
        this.labTestId = -999; // since it is not incorporated yet
        
        if(!validateHealthServiceCombo())//fetching ID to save if Service Choosed
        {
            healthCareId = -999;
        }
         if(!validateEmergencyServiceCombo())       
         {
             emergyId=-999;
         }
           if(!validateInPatientCombo())     
           {
               inPatientId=-999;
           }
             if(!validateOtherServicesCombo())     
           {
               otherServiceId=-999;
           }   
             
             
             if( healthCareId == -999 && emergyId==-999 && inPatientId==-999 && otherServiceId==-999)
             {
                 JOptionPane.showMessageDialog(null,"Atleast Choose One Service");
             }
             
             System.out.println("healthcareID : "+healthCareId+"  emerID: "+emergyId+" in patient : "+inPatientId+"  other: "+otherServiceId + "   patient ID: "+this.selectedPatientID);
             
             
             if(datePicker.getDate() == null)
             {
                 JOptionPane.showMessageDialog(null,"Enter Date First");
                 return;
             }
        
               Date date= datePicker.getDate();
             AddServicesModel addService = new AddServicesModel(selectedPatientID,healthCareId, labTestId , emergyId, inPatientId, otherServiceId, date);
             boolean activitySaved = DAO.getInstance().insertActiviy(addService);
             if(activitySaved)
             {
                 this.dispose();
                 new UserMgmt().setVisible(true);
             }
             else
             {
                 JOptionPane.showMessageDialog(null,"Cannot Save Service. Contact Administrator");
             }
        
    }//GEN-LAST:event_saveActionPerformed

    private void healthCareServicesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_healthCareServicesActionPerformed
        // TODO add your handling code here:
         JComboBox cb = (JComboBox)evt.getSource();
        selectedHealthType = (String)cb.getSelectedItem();
        
        System.out.println(selectedHealthType);
    }//GEN-LAST:event_healthCareServicesActionPerformed

    private void emergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emergencyActionPerformed
        // TODO add your handling code here:
          JComboBox cb = (JComboBox)evt.getSource();
        selectedEmergencyType = (String)cb.getSelectedItem();
        
        System.out.println(selectedEmergencyType);
    }//GEN-LAST:event_emergencyActionPerformed

    private void inPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inPatientActionPerformed
        // TODO add your handling code here:
          JComboBox cb = (JComboBox)evt.getSource();
        selectedInPatientType = (String)cb.getSelectedItem();
        
        System.out.println(selectedInPatientType);
    }//GEN-LAST:event_inPatientActionPerformed

    private void otherServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherServiceActionPerformed
        // TODO add your handling code here:
          JComboBox cb = (JComboBox)evt.getSource();
        selectedOtherType = (String)cb.getSelectedItem();
        
        System.out.println(selectedOtherType);
    }//GEN-LAST:event_otherServiceActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        new SearchPatient().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JComboBox emergency;
    private javax.swing.JComboBox healthCareServices;
    private javax.swing.JComboBox inPatient;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox otherService;
    private javax.swing.JLabel patientName;
    private javax.swing.JButton save;
    private javax.swing.JLabel zakatLabel;
    // End of variables declaration//GEN-END:variables
}
