/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseAccessLayer;

import Models.AddServicesModel;
import Models.LoginModel;
import Models.PatientInfoModel;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Usama Mahmood
 */ 
public class DAO {
    Connection con;
    Statement stmt;
    ResultSet rs;

    boolean connectionFlag = true;
    private DAO() {
    }
    
    public static DAO getInstance() {
        return DAOHolder.INSTANCE;
    }
    
    private static class DAOHolder {

        private static final DAO INSTANCE = new DAO();
    }
    
    
    public boolean isConnectionFlag() {
        return connectionFlag;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }
    
     public void DoConnect( ) {

       try {
            InputStream bdl = this.getClass().getResourceAsStream("/resources/dbconfig.properties");
            Properties prop = new Properties();
            prop.load(bdl);
            String host = "jdbc:mysql://" + prop.getProperty("host") + "/" + prop.getProperty("dbname");
            String uName = prop.getProperty("user");
            String uPass = prop.getProperty("password");
            con =   DriverManager.getConnection(host, uName, uPass);
        }
        catch(Exception e) {
           // this.txtMsg.setText("Failed to connect; Please viewStack Trace");
            connectionFlag = false;
               StringBuilder sb = new StringBuilder(e.toString());
                for (StackTraceElement ste : e.getStackTrace()) {
                    sb.append("\n\tat ");
                    sb.append(ste);
                }
                String trace = sb.toString();
            JOptionPane.showMessageDialog(null, trace,"Message", JOptionPane.INFORMATION_MESSAGE);
        }

     }
    


     public String[] getHealthCareTypes( ) {
         String [] healthCarType;
         try
         {
            stmt = con.createStatement();   
            String sql = "select * from HEALTHCARE";
            rs= stmt.executeQuery(sql);
           
            ArrayList<String> result = new ArrayList<String>();
            
             while(rs.next())
            {
                result.add(rs.getString(2));
            }

            
            System.out.println(result.toString());
             return (String[]) result.toArray(new String[result.size()]);
                
         }catch(SQLException e)
         {
             e.printStackTrace();
         }
        return null;
     }
     
     public String[] getEmergecyTypes( ) {
        
         try
         {
            stmt = con.createStatement();   
            String sql = "select * from emer_svcs";
            rs= stmt.executeQuery(sql);
           
            ArrayList<String> result = new ArrayList<String>();
            
             while(rs.next())
            {
                result.add(rs.getString(2));
            }

            
            System.out.println(result.toString());
             return (String[]) result.toArray(new String[result.size()]);
                
         }catch(SQLException e)
         {
             e.printStackTrace();
         }
        return null;
     }

      public String[] getInPatientTypes( ) {
        
         try
         {
            stmt = con.createStatement();   
            String sql = "select * from indoor_patient";
            rs= stmt.executeQuery(sql);
           
            ArrayList<String> result = new ArrayList<String>();
            
             while(rs.next())
            {
                result.add(rs.getString(2));
            }

            
            System.out.println(result.toString());
             return (String[]) result.toArray(new String[result.size()]);
                
         }catch(SQLException e)
         {
             e.printStackTrace();
         }
        return null;
     }
        public String[] getOtherServices( ) {
        
         try
         {
            stmt = con.createStatement();   
            String sql = "select * from other_services";
            rs= stmt.executeQuery(sql);
           
            ArrayList<String> result = new ArrayList<String>();
            
             while(rs.next())
            {
                result.add(rs.getString(2));
            }

            
            System.out.println(result.toString());
             return (String[]) result.toArray(new String[result.size()]);
                
         }catch(SQLException e)
         {
             e.printStackTrace();
         }
        return null;
     }
        
        public ResultSet fetchDataSource()
        {
            
             
            try
            {
               stmt = con.createStatement();   
               String sql = "select (@row:=@row+1) AS ROW,hlth.`HEALTH_CARE_TYPE`,count(*) as no_of_patients,hlth.`CHARGES` as rate, (hlth.`CHARGES` * count(*) ) as cash,patient.`ZAKAT` as zakat, 0 as Donation, 0 as Other, ((hlth.`CHARGES` * count(*) )+patient.`ZAKAT`) as Total,act.`VISIT_DATE` as Date\n" +
                "from \n" +
                "healthcare hlth,activities act ,patient_info patient,(SELECT @row := 0) r \n" +
                "where hlth.`HEALTH_CARE_ID` = act.`HEALTH_CARE_ID`\n" +
                "and act.`PATIENT_ID` = patient.`PATIENT_ID`\n" +
                "group by hlth.`HEALTH_CARE_TYPE`\n" +
                "ORDER BY ROW Asc";
               rs= stmt.executeQuery(sql);

             return rs;

            }catch(SQLException e)
            {
                e.printStackTrace();
            }
            
            return null;
        }
     
      public boolean insertActiviy (AddServicesModel serviceModel)
     {
           
            String sql="";
         
        //put the rest of the sentence
          try {


                 sql="INSERT INTO activities (PATIENT_ID,HEALTH_CARE_ID,LAB_TEST_ID,EMER_SVC_ID,INDOOR_PATIENT_ID,SERVICE_ID,VISIT_DATE) VALUES (?,?,?,?,?,?,?)";

                PreparedStatement pdt = con.prepareStatement(sql);
               pdt.setInt(1, serviceModel.getPatientID());
                
                if (serviceModel.getHealthcareID()==-999) {
                    pdt.setNull(2, Types.INTEGER);
                } else {
                   
                   pdt.setInt(2, serviceModel.getHealthcareID());
                }
               
               
                if (serviceModel.getLabTestID()==-999) {
                    pdt.setNull(3, Types.INTEGER);
                } else {
                   
                   pdt.setInt(3, serviceModel.getLabTestID());
                }
                
                
                if (serviceModel.getEmergyID()==-999) {
                    pdt.setNull(4, Types.INTEGER);
                } else {
                   
                   pdt.setInt(4, serviceModel.getEmergyID());
                }
                 
                 
                if (serviceModel.getIndoorPatientSvcID()==-999) {
                    pdt.setNull(5, Types.INTEGER);
                } else {
                   
                   pdt.setInt(5, serviceModel.getIndoorPatientSvcID());
                }
                 
                 
                if (serviceModel.getOtherServiceID()==-999) {
                    pdt.setNull(6, Types.INTEGER);
                } else {
                   
                   pdt.setInt(6, serviceModel.getOtherServiceID());
                }
                 
                 
                 java.sql.Date date=   new java.sql.Date(serviceModel.getVisitDate().getTime());
                   pdt.setDate(7, date);
                
             
                //put the rest of the code
               int n1=pdt.executeUpdate();
               if(n1>0)
               {
                    JOptionPane.showMessageDialog(null,"Inserted Successfully!");
                    return true;
               }
            }catch (SQLException ex) {
                ex.printStackTrace();

            }
          return false;
     }
     
     public boolean insertPerson (PatientInfoModel person)
     {
                     int serviceType=0;
                     String sql="";
         
    //put the rest of the sentence
      try {
          
          
             sql="INSERT INTO PATIENT_INFO (FIRST_NAME,LAST_NAME,ZAKAT,NIC) VALUES (?,?,?,?)";

            PreparedStatement pdt = con.prepareStatement(sql);
           pdt.setString(1, person.getFirstName());
           pdt.setString(2, person.getLastName());
            pdt.setInt(3, person.getZakat());
           pdt.setLong(4, person.getNic());
          
            //put the rest of the code
           int n1=pdt.executeUpdate();
           if(n1>0)
           {
                JOptionPane.showMessageDialog(null,"Inserted Successfully!");
                return true;
           }
        }catch (SQLException ex) {
            ex.printStackTrace();

        }
      return false;
     }
     
     public boolean checkLogin(LoginModel login)
     {
         try
         {
            stmt = con.createStatement();   
            String sql = "select * from login";
            rs= stmt.executeQuery(sql);
           
           
            
             while(rs.next())
            {
                if(login.getUsername().equals(rs.getString("username")) && login.getPassword().equals(rs.getString("password")))
                {
                    return true;
                }
            }
                
         }catch(SQLException e)
         {
             e.printStackTrace();
         }
         
         return false;
     }
     
     public Vector getHealthCareServicesData()
    {
        try{
            Vector<Vector<String>> healthServices = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("select * from healthcare");

            ResultSet rs = pre.executeQuery();

            while(rs.next())
            {
                Vector<String> data = new Vector<String>();
                
                data.add(rs.getString(2)); //service name
                data.add(rs.getString(3)); //charges
                healthServices.add(data);
            }



            return healthServices;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
     
     
    public Vector getEmergencyServicesData()
    {
        try{
            Vector<Vector<String>> healthServices = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("select * from emer_svcs");

            ResultSet rs = pre.executeQuery();

            while(rs.next())
            {
                Vector<String> data = new Vector<String>();
                
                data.add(rs.getString(2)); //service name
                data.add(rs.getString(3)); //charges
                healthServices.add(data);
            }



            return healthServices;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
     public Vector getOtherServicesData()
    {
        try{
            Vector<Vector<String>> healthServices = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("select * from other_services");

            ResultSet rs = pre.executeQuery();

            while(rs.next())
            {
                Vector<String> data = new Vector<String>();
                
                data.add(rs.getString(2)); //service name
                data.add(rs.getString(3)); //charges
                healthServices.add(data);
            }



            return healthServices;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public Vector getIndoorPatientServicesData()
    {
        try{
            Vector<Vector<String>> healthServices = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("select * from indoor_patient");

            ResultSet rs = pre.executeQuery();

            while(rs.next())
            {
                Vector<String> data = new Vector<String>();
                
                data.add(rs.getString(2)); //service name
                data.add(rs.getString(3)); //charges
                healthServices.add(data);
            }



            return healthServices;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
      
     
     
     public Vector searchPatient(String firstName,String lastName,int nic)
     {
           try{
            Vector<Vector<Object>> search = new Vector<Vector<Object>>();
            PreparedStatement pre = con.prepareStatement("select * from patient_info where first_name like '%"+firstName+"%' or last_name like '%"+lastName+"%' or nic like '%"+nic+"%'");

            ResultSet rs = pre.executeQuery();
       
            while(rs.next())
            {
                Vector<Object> data = new Vector<Object>();
                
                
                data.add(rs.getString(1)); //ID
                data.add(Boolean.FALSE);//CHoice
                data.add(rs.getString(2)); //first name
                data.add(rs.getString(3)); //last Name
                data.add(rs.getString(4));// nic
                search.add(data);
            }

 

            return search;
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return null;
     }
     
     
     
     
     
     
     public void closeConnection() throws SQLException
     {
         /*Close the connection after use (MUST)*/
        try
        {
            if(con!=null)
            con.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
         
     }
}
