/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Usama.Mahmood
 */
public class AddServicesModel {

    int patientID;
    Vector<Vector<String>> addedServiceID;
    int extraCharges;
    int zakat;
    Date visitDate; // its is sql date

    public AddServicesModel(int patientID, Vector<Vector<String>> addedServiceID, int extraCharges, int zakat, Date visitDate) {
        this.patientID = patientID;
        this.addedServiceID = addedServiceID;
        this.extraCharges = extraCharges;
        this.zakat = zakat;
        this.visitDate = visitDate;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public Vector<Vector<String>> getAddedServiceID() {
        return addedServiceID;
    }

    public void setAddedServiceID(Vector<Vector<String>> addedServiceID) {
        this.addedServiceID = addedServiceID;
    }

    public int getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(int extraCharges) {
        this.extraCharges = extraCharges;
    }

    public int getZakat() {
        return zakat;
    }

    public void setZakat(int zakat) {
        this.zakat = zakat;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

}
