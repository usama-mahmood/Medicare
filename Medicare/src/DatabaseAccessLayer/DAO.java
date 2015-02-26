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

    public void DoConnect() {

        try {
            InputStream bdl = this.getClass().getResourceAsStream("/resources/dbconfig.properties");
            Properties prop = new Properties();
            prop.load(bdl);
            String host = "jdbc:mysql://" + prop.getProperty("host") + "/" + prop.getProperty("dbname");
            String uName = prop.getProperty("user");
            String uPass = prop.getProperty("password");
            con = DriverManager.getConnection(host, uName, uPass);
        } catch (Exception e) {
            // this.txtMsg.setText("Failed to connect; Please viewStack Trace");
            connectionFlag = false;
            StringBuilder sb = new StringBuilder(e.toString());
            for (StackTraceElement ste : e.getStackTrace()) {
                sb.append("\n\tat ");
                sb.append(ste);
            }
            String trace = sb.toString();
            JOptionPane.showMessageDialog(null, trace, "Message", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public ResultSet fetchDataSource() {

        try {
            stmt = con.createStatement();
            String sql = "select (@row:=@row+1) AS ROW,svc.`SVC_NAME` as service_name,count(*) as no_of_patients,svc.`CHARGES` as rate, (svc.`CHARGES` * count(*) ) as cash,act.`ZAKAT` as zakat, 0 as Donation, 0 as Other, ((svc.`CHARGES`* count(*) )+act.`ZAKAT`) as Total,act.`VISIT_DATE` as Date\n"
                    + "from \n"
                    + "activities act ,services svc,(SELECT @row := 0) r \n"
                    + "where act.`SERVICE_ID` = svc.`SERVICE_ID`\n"
                    + "group by svc.`SVC_NAME`\n"
                    + "ORDER BY ROW Asc";
            rs = stmt.executeQuery(sql);

            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean insertActiviy(AddServicesModel serviceModel) {

        String sql = "";
        boolean insertFlag = false;

        //put the rest of the sentence
        try {

            for (int i = 0; i < serviceModel.getAddedServiceID().size(); i++) {

                sql = "call insertAcivity(?,?,?,?,?)";
                PreparedStatement pdt = con.prepareStatement(sql);

                pdt.setInt(1, serviceModel.getPatientID());
                pdt.setInt(2, Integer.parseInt(serviceModel.getAddedServiceID().get(i).get(0))); // for serviceID
                pdt.setInt(3, serviceModel.getExtraCharges());
                pdt.setInt(4, serviceModel.getZakat());
                java.sql.Date date = new java.sql.Date(serviceModel.getVisitDate().getTime());
                pdt.setDate(5, date);

                int n1 = pdt.executeUpdate();
                if (n1 > 0) {
                    insertFlag = true;
                } else {
                    insertFlag = false;
                }
            }

            if (insertFlag) {
                JOptionPane.showMessageDialog(null, "Inserted Successfully!");
                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return false;
    }

    public boolean insertPatient(PatientInfoModel person) {
        int serviceType = 0;
        String sql = "";

        //put the rest of the sentence
        try {

            sql = "call insertPatient(?,?,?)";

            PreparedStatement pdt = con.prepareStatement(sql);
            pdt.setString(1, person.getFirstName());
            pdt.setString(2, person.getLastName());
            pdt.setLong(3, person.getNic());

            //put the rest of the code
            int n1 = pdt.executeUpdate();
            if (n1 > 0) {
                JOptionPane.showMessageDialog(null, "Inserted Successfully!");
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return false;
    }

    public Vector<Vector<String>> getAllPatient() {
        try {
            Vector<Vector<String>> allPatients = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("call getAllPatients()");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                Vector<String> data = new Vector<String>();
                data.add(rs.getString(1));//patientID
                data.add(rs.getString(2)); //firstName
                data.add(rs.getString(3)); //LastName
                data.add(rs.getString(4)); //nic
                allPatients.add(data);
            }

            return allPatients;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean checkLogin(LoginModel login) {
        try {
            stmt = con.createStatement();
            String sql = "select * from login";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (login.getUsername().equals(rs.getString("username")) && login.getPassword().equals(rs.getString("password"))) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Vector getServicesData(String service) {
        try {
            Vector<Vector<String>> healthServices = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("call GetService('" + service + "')");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                Vector<String> data = new Vector<String>();

                data.add(rs.getString(1)); //service name
                data.add(rs.getString(2)); //charges
                healthServices.add(data);
            }

            return healthServices;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Vector getAllServices() {
        try {
            Vector<Vector<String>> allServices = new Vector<Vector<String>>();
            PreparedStatement pre = con.prepareStatement("call GetAllServices()");
            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                Vector<String> data = new Vector<String>();
                data.add(rs.getString(1));//serviceID
                data.add(rs.getString(2)); //service name
                data.add(rs.getString(3)); //charges
                allServices.add(data);
            }

            return allServices;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Vector searchPatient(String firstName, String lastName, int nic) {
        try {
            Vector<Vector<Object>> search = new Vector<Vector<Object>>();
            PreparedStatement pre = con.prepareStatement("call searchPatients(?,?,?)");

            if (firstName.equals("") || firstName == null) {
                pre.setNull(1, Types.CHAR);
            } else {

                pre.setString(1, firstName);
            }

            if (lastName.equals("") || lastName == null) {
                pre.setNull(2, Types.CHAR);
            } else {

                pre.setString(2, lastName);
            }

            pre.setLong(3, nic);

            ResultSet rs = pre.executeQuery();

            while (rs.next()) {
                Vector<Object> data = new Vector<Object>();

                data.add(rs.getString(1)); //ID
                data.add(Boolean.FALSE);//CHoice
                data.add(rs.getString(2)); //first name
                data.add(rs.getString(3)); //last Name
                data.add(rs.getString(4));// nic
                search.add(data);
            }

            return search;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void closeConnection() throws SQLException {
        /*Close the connection after use (MUST)*/
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
