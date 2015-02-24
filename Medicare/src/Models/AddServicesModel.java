/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Date;



/**
 *
 * @author Usama.Mahmood
 */
public class AddServicesModel {
    
    int patientID;
    int healthcareID;
    int labTestID;
    int emergyID;
    int indoorPatientSvcID;
    int otherServiceID;
    Date visitDate; // its is sql date

    public AddServicesModel(int patientID, int healthcareID, int labTestID, int emergyID, int indoorPatientSvcID, int otherServiceID, Date visitDate) {
        this.patientID = patientID;
        this.healthcareID = healthcareID;
        this.labTestID = labTestID;
        this.emergyID = emergyID;
        this.indoorPatientSvcID = indoorPatientSvcID;
        this.otherServiceID = otherServiceID;
        this.visitDate = visitDate;
    }

   

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public int getHealthcareID() {
        return healthcareID;
    }

    public void setHealthcareID(int healthcareID) {
        this.healthcareID = healthcareID;
    }

    public int getLabTestID() {
        return labTestID;
    }

    public void setLabTestID(int labTestID) {
        this.labTestID = labTestID;
    }

    public int getEmergyID() {
        return emergyID;
    }

    public void setEmergyID(int emergyID) {
        this.emergyID = emergyID;
    }

    public int getIndoorPatientSvcID() {
        return indoorPatientSvcID;
    }

    public void setIndoorPatientSvcID(int indoorPatientSvcID) {
        this.indoorPatientSvcID = indoorPatientSvcID;
    }

    public int getOtherServiceID() {
        return otherServiceID;
    }

    public void setOtherServiceID(int otherServiceID) {
        this.otherServiceID = otherServiceID;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }
    
    
    
}
