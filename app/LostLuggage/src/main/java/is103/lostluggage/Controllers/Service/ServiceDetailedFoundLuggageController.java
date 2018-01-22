package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceDataFound;
import is103.lostluggage.Model.Service.Data.ServiceDataMatch;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import is103.lostluggage.Model.Service.Instance.Details.FoundLuggageDetailsInstance;
import is103.lostluggage.Model.Service.Instance.Matching.FoundLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Interface.FoundLuggageFields;
import is103.lostluggage.Model.Service.Model.MatchLuggage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;



/**
 * FXML Controller class detailed found luggage
 *
 * @author Thijs Zijdel - 500782165
 */
public class ServiceDetailedFoundLuggageController implements Initializable, FoundLuggageFields {
    //get al the jfxtextfields (and a text area)
    @FXML private JFXTextField registrationNr;
    @FXML private JFXTextField luggageTag;
    @FXML private JFXTextField type;
    @FXML private JFXTextField brand;
    @FXML private JFXTextField mainColor;
    @FXML private JFXTextField secondColor;
    @FXML private JFXTextField size;
    @FXML private JFXTextField weight;    
    @FXML private JFXTextArea signatures;

    @FXML private JFXTextField passangerId;
    @FXML private JFXTextField passangerName;
    @FXML private JFXTextField address;        
    @FXML private JFXTextField place;
    @FXML private JFXTextField postalCode;
    @FXML private JFXTextField country;
    @FXML private JFXTextField email;   
    @FXML private JFXTextField phone;   
    
    @FXML private JFXTextField timeFound;
    @FXML private JFXTextField dateFound;
    @FXML private JFXTextField locationFound;
    @FXML private JFXTextField flight;
    
    //create a potentialmatche list 
    public ObservableList<MatchLuggage> potentialMatchesList = FXCollections.observableArrayList(); 
    
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceDetailedFoundLuggageView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //try to load initialize methode
        try {
            setFoundFields(getManualFoundLuggageResultSet());
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailedFoundLuggageController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
  
    }   
   
    
    
    /**  
     * Here will the resultSet of the found manual matching luggage be get 
     * For getting the right resultSet the correct instance id will be passed
     * 
     * @return resultSet     resultSet for the right luggage
     * @throws java.sql.SQLException
     */  
    @Override
    public ResultSet getManualFoundLuggageResultSet() throws SQLException{
        ServiceDataFound detailsItem = new ServiceDataFound();
        
        String id = FoundLuggageDetailsInstance.getInstance().currentLuggage().getRegistrationNr();
       
        ResultSet resultSet = detailsItem.getAllDetailsFound(id);
        return resultSet;
    }
    
    /**  
     * Here are all the detail fields been set with the right data from:
     * The resultSet given
     * 
     * @param resultSet         this will be converted to temp strings and integers
     * @throws java.sql.SQLException
     * @void no direct          the fields will be set within this method
     */    
    @FXML
    @Override
    public void setFoundFields(ResultSet resultSet) throws SQLException{ 
        //loop trough all the luggages in the resultSet
        //Note: there will be only one
        while (resultSet.next()) {            
            int getRegistrationNr =        resultSet.getInt("F.registrationNr");
            String getDateFound =          resultSet.getString("F.dateFound");
            String getTimeFound =          resultSet.getString("F.timeFound");

            String getLuggageTag =         resultSet.getString("F.luggageTag");
            String getLuggageType =        resultSet.getString("T."+MainApp.getLanguage()+"");
            String getBrand =              resultSet.getString("F.brand");
            String getMainColor =          resultSet.getString("c1."+MainApp.getLanguage()+"");
            String getSecondColor =        resultSet.getString("c2."+MainApp.getLanguage()+"");
            String getSize =               resultSet.getString("F.size");
            String getWeight =             resultSet.getString("F.weight");   
            String getOtherCharacteristics=resultSet.getString("F.otherCharacteristics");

            String getPassengerId =        resultSet.getString("F.passengerId");

            String getName =               resultSet.getString("P.name");
            String getAddress =            resultSet.getString("P.address");
            String getPlace =              resultSet.getString("P.place");
            String getPostalcode =         resultSet.getString("P.postalcode");
            String getCountry =            resultSet.getString("P.country");
            String getEmail =              resultSet.getString("P.email");
            String getPhone =              resultSet.getString("P.phone");

            String getFlight =             resultSet.getString("F.arrivedWithFlight"); 
            String getLocationFound =      resultSet.getString("L."+MainApp.getLanguage()+"");

        //set the fields with the right data    
        registrationNr.setText( Integer.toString(getRegistrationNr) );  
        luggageTag.setText(getLuggageTag);

        type.setText(getLuggageType);
        brand.setText(getBrand);

        mainColor.setText(getMainColor);
        secondColor.setText(getSecondColor);
        size.setText(getSize);
        weight.setText(getWeight);
        signatures.setText(getOtherCharacteristics);

        passangerId.setText( getPassengerId );
        passangerName.setText(getName);
        address.setText(getAddress);
        place.setText(getPlace);
        postalCode.setText(getPostalcode);
        country.setText(getCountry);
        email.setText(getEmail);
        phone.setText(getPhone);

        locationFound.setText(getLocationFound);
        dateFound.setText(getDateFound);
        timeFound.setText(getTimeFound);
        flight.setText(getFlight);
        }
    }
    
    
   
    /**  
     * When the 'view potentials ' button is clicked the potential list will be 
     * Initialized by getting the right data, clear the previous list,
     * getting the instance id and passing this to the data object
     * 
     * 
     * @param event                     when the button is clicked 
     * @throws java.io.IOException      switching views
     * @throws java.sql.SQLException    getting data from the db
     */ 
    @FXML
    protected void viewPotentials(ActionEvent event) throws IOException, SQLException{
        //get the right data object
        ServiceDataMatch data = ServiceHomeViewController.getMATCH_DATA();

        
        //clear the potential list if there are items in
        if (!potentialMatchesList.isEmpty()){
            potentialMatchesList.clear();
        }
        
        //reset the potential matching table and set the reset status
        //ServiceMatchingViewController.getInstance().resetPotentialMatchingTable(); 
        //MainApp.setPotentialResetStatus(true);
        
        //get the id of the current luggage
        String id = FoundLuggageDetailsInstance.getInstance().currentLuggage().getRegistrationNr();
        
        //initialize the potential matches for the given id 
        data.potentialMatchesForFoundLuggage(id);
        
        //if the user is not on the matching view, switch
        if (ServiceHomeViewController.isOnMatchingView()==false){
            MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
        }
                
        //set the right tab, 2 = potential matching tab
        ServiceMatchingViewController.getInstance().setMatchingTab(
                ServiceMatchingViewController.POTENTIAL_MATCHING_TAB_INDEX
        );
        //close the current stage
        closeStage();
    }
    
    
    /**  
     * When the 'manual match ' button is clicked the instance will be set
     * And the stage will be closed
     * 
     * @param event                     when the button is clicked 
     * @throws java.io.IOException      potentially switching views
     */  
    @FXML
    protected void manualMatching(ActionEvent event) throws IOException{
        //if the user is not on the matching view, switch to that view
        if (ServiceHomeViewController.isOnMatchingView()==false){
            MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
        }
        
        //initialize the instance with the current luggage
        FoundLuggage passObject =  FoundLuggageDetailsInstance.getInstance().currentLuggage();
        FoundLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(passObject.getRegistrationNr()); 
        
        //close the current stage
        closeStage();
    }
    
    @FXML
    public void openEditView() throws IOException{
        MainApp.switchView("/Views/Service/ServiceEditFoundLuggageView.fxml");
        closeStage();
    }
    
    
    /**  
     * Close the current stage by getting the window of a fields scene's
     * And casting this to a stage, and close this stage
     */ 
    public void closeStage(){
        Stage stage = (Stage) registrationNr.getScene().getWindow();
        stage.close();
    }

    
    

    
}
