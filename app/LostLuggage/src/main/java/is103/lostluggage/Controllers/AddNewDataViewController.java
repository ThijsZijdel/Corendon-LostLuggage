/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Controllers;

import is103.lostluggage.Controllers.Admin.OverviewUserController;
import is103.lostluggage.Controllers.Service.ServiceEditFoundLuggageViewController;
import is103.lostluggage.Database.MyJDBC;
import static is103.lostluggage.MainApp.getDatabase;
import is103.lostluggage.Model.Service.Data.ServiceGetDataFromDB;
import is103.lostluggage.Model.Data;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Poek Ligthart
 */
public class AddNewDataViewController implements Initializable {
    //TableView colorTable kolommen.
    //colorTable
    public static ObservableList<Data> colorList;
    @FXML private TableView<Data> colorTable;
    @FXML private TableColumn<Data, String> ralCode;
    @FXML private TableColumn<Data, String> englishColor; 
    @FXML private TableColumn<Data, String> dutchColor; 
    //typeTable
    public static ObservableList<Data> typeList;
    @FXML private TableView<Data> typeTable;
    @FXML private TableColumn<Data, String> luggageTypeId;
    @FXML private TableColumn<Data, String> englishType; 
    @FXML private TableColumn<Data, String> dutchType; 
    //locationTable
    public static ObservableList<Data> locationList;
    @FXML private TableView<Data> locationTable;
    @FXML private TableColumn<Data, String> locationID;
    @FXML private TableColumn<Data, String> englishLocation; 
    @FXML private TableColumn<Data, String> dutchLocation; 
    //flightTable
    public static ObservableList<Data> flightList;
    @FXML private TableView<Data> flightTable;
    @FXML private TableColumn<Data, String> flightNr;
    @FXML private TableColumn<Data, String> airline;
    @FXML private TableColumn<Data, String> from; 
    @FXML private TableColumn<Data, String> to; 
    
      @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ralCode.setCellValueFactory(new PropertyValueFactory<>("ralCode"));
        englishColor.setCellValueFactory(new PropertyValueFactory<>("english"));
        dutchColor.setCellValueFactory(new PropertyValueFactory<>("dutch"));
        
       
        colorTable.setItems(colorList);
        
        ServiceGetDataFromDB colors = new ServiceGetDataFromDB("color", "*", null);
        
        try {
            ResultSet colorResultSet = colors.getServiceDetailsResultSet();
            while (colorResultSet.next()){
                //Database gegevens in de tableview variabelen plaatsen.
                String ralCode = colorResultSet.getString("ralCode");
                String english = colorResultSet.getString("english");
                String dutch = colorResultSet.getString("dutch");
                
                //De results toevagen aan de observable list
                colorList.add(new Data(
                       ralCode,
                       english,
                       dutch
                )); 
        
                
            } //end while loop
        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    
    
        
        
}

 // luggageTypeId.setCellValueFactory(new PropertyValueFactory<>("luggageType"));
      //  englishType.setCellValueFactory(new PropertyValueFactory<>("englishType"));
      //  dutchType.setCellValueFactory(new PropertyValueFactory<>("dutchType"));
        
       
        
       
        
      //  locationID.setCellValueFactory(new PropertyValueFactory<>("locationID"));
     //   englishLocation.setCellValueFactory(new PropertyValueFactory<>("englishLocation"));
     //   dutchLocation.setCellValueFactory(new PropertyValueFactory<>("dutchLocation"));
     
        
  
        
     //  flightNr.setCellValueFactory(new PropertyValueFactory<>("flightNr"));
     //  airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
     //  from.setCellValueFactory(new PropertyValueFactory<>("from"));
     //  to.setCellValueFactory(new PropertyValueFactory<>("to"));