/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Usama Mahmood
 */
public class PatientInfoModel {

    Long nic;
    String firstName;//requred
    String lastName;//requred
    int zakat;
    Long phoneNum;
    String address;

    public PatientInfoModel(Long nic, String firstName, String lastName, int zakat) {
        this.nic = nic;
        this.firstName = firstName;
        this.lastName = lastName;
        this.zakat = zakat;

    }

    public Long getNic() {
        return nic;
    }

    public void setNic(Long nic) {
        this.nic = nic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getZakat() {
        return zakat;
    }

    public void setZakat(int zakat) {
        this.zakat = zakat;
    }

}
