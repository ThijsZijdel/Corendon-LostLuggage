/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Object User
 * 
 * @author Michael de Boer
 */
public class User {

    private SimpleStringProperty id, lastName, firstName, location, role, status;

    public User(String id, String lastName, String firstName, String location, String role, String status) {

        this.id = new SimpleStringProperty(id);
        this.lastName = new SimpleStringProperty(lastName);
        this.firstName = new SimpleStringProperty(firstName);
        this.location = new SimpleStringProperty(location);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);

    }

    public String getId() {
        return id.get();
    }

    public void setId(SimpleStringProperty id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(SimpleStringProperty lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(SimpleStringProperty firstName) {
        this.firstName = firstName;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(SimpleStringProperty id) {
        this.location = id;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(SimpleStringProperty role) {
        this.role = role;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(SimpleStringProperty status) {
        this.status = status;
    }

}
