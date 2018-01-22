package is103.lostluggage.Controllers.Manager;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author daron
 */
public class RetrievedLuggage {
    //inserts for ManagerTeruggebrachttebagage

    private int FormID, Koffer;
    private String Date, Customer, Employee, Deliverer;

    public RetrievedLuggage(int FormID,int Koffer, String Date, String Customer, String Employee, String Deliverer) {

        this.FormID = FormID;
        this.Date = Date;
        this.Customer = Customer;
        this.Employee = Employee;
        this.Deliverer = Deliverer;
        this.Koffer = Koffer;

    }

    public int getFormID() {
        return FormID;
    }

    public void setFormID(int FormID) {
        this.FormID = FormID;
    }
    public int getKoffer() {
        return Koffer;
    }

    public void setKoffer(int Koffer) {
        this.Koffer = Koffer;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String Customer) {
        this.Customer = Customer;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String Employee) {
        this.Employee = Employee;
    }

    public String getDeliverer() {
        return Deliverer;
    }

    public void setDeliverer(String Deliverer) {
        this.Deliverer = Deliverer;
    }

}
