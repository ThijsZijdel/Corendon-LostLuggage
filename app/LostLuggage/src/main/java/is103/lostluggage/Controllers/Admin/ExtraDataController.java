/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Controllers.Admin;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Controllers.Service.ServiceInputLuggageViewController;
import is103.lostluggage.MainApp;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Arthur
 */
public class ExtraDataController implements Initializable {

    
    @FXML
    private JFXTextField iatacodeTF, airportTF, countryTF, timezoneTF;
    
    @FXML
    private JFXCheckBox daylightsavingCB;
    
    @FXML
    private Label msgLbl;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Define the previous view
        MainViewController.previousView = "/Views/Admin/HomeUserView.fxml";
        
        //set the title of the view, default form to be displayed is Missing
        changeTitle("Add Extra Data");
        
        MainApp.currentView = "/Views/Admin/ExtraData.fxml";
        
    }    
    
    
    //Method to change the title of the current view
    public void changeTitle(String title){
        try {
            MainViewController.getInstance().getTitle(title);
        } catch (IOException ex) {
            Logger.getLogger(ServiceInputLuggageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    public void addAirport() throws SQLException{
        
        //Get the values from the fields
        String IATACode, Airport, Country, TimeZone, DaylightSaving;
        
        IATACode = iatacodeTF.getText();
        Airport = airportTF.getText();
        Country = countryTF.getText();
        TimeZone = timezoneTF.getText();
        
        if(daylightsavingCB.isSelected()){
            DaylightSaving = "1";
        }else{
            DaylightSaving = "0";
        }
        
        //Check if IATACode exists
       ResultSet result = MainApp.getDatabase().executeResultSetQuery("SELECT IATACode FROM destination WHERE IATACode = '"+IATACode+"' ");
        
       if (!result.next() ) {
            int asdq = MainApp.getDatabase().executeUpdateQuery("INSERT into destination (IATACode, airport, country, timeZone, daylightSaving) "
                + "VALUES ('"+IATACode+"', '"+Airport+"', '"+Country+"', '"+TimeZone+"', '"+DaylightSaving+"')");
        
            msgLbl.setText(Airport + " Has been added");
       }else{
           msgLbl.setText("This IATACode is already registerred");
       }
        


        
    }
    
}
