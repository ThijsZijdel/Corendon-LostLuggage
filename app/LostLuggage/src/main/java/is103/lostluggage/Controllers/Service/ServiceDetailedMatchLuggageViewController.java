package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceDataFound;
import is103.lostluggage.Model.Service.Data.ServiceDataLost;
import is103.lostluggage.Model.Service.Instance.Details.FoundLuggageDetailsInstance;
import is103.lostluggage.Model.Service.Instance.Details.LostLuggageDetailsInstance;
import is103.lostluggage.Model.Service.Instance.Matching.FoundLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Instance.Matching.LostLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import is103.lostluggage.Model.Service.Model.LostLuggage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author thijszijdel
 */
public class ServiceDetailedMatchLuggageViewController implements Initializable {
    //al the lost fields
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
    
    @FXML private JFXTextField timeLost;
    @FXML private JFXTextField dateLost;
    @FXML private JFXTextField flight;
    
    
    //Al the found fields
    //Note: 1 -> so they are alternated 
    @FXML private JFXTextField registrationNr1;
    @FXML private JFXTextField luggageTag1;
    @FXML private JFXTextField type1;
    @FXML private JFXTextField brand1;
    @FXML private JFXTextField mainColor1;
    @FXML private JFXTextField secondColor1;
    @FXML private JFXTextField size1;
    @FXML private JFXTextField weight1;    
    @FXML private JFXTextArea signatures1;

    @FXML private JFXTextField passangerId1;
    @FXML private JFXTextField passangerName1;
    @FXML private JFXTextField address1;        
    @FXML private JFXTextField place1;
    @FXML private JFXTextField postalCode1;
    @FXML private JFXTextField country1;
    @FXML private JFXTextField email1;   
    @FXML private JFXTextField phone1;   
    
    @FXML private JFXTextField timeFound;
    @FXML private JFXTextField dateFound;
    @FXML private JFXTextField locationFound;
    @FXML private JFXTextField flight1;

    private final String LANGUAGE = MainApp.getLanguage();  
    
    
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceDetailedMatchLuggageView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //try to load initialize methode
        try {
            setLostFields(getDetailsLostLuggageResultSet());
            setFoundFields(getDetailsFoundLuggageResultSet());
        } catch (SQLException ex) {
            Logger.getLogger(ServiceDetailedFoundLuggageController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        

        
    }
    
    
    /**  
     * Here will the resultSet of the lost manual matching luggage be get 
     * For getting the right resultSet the correct instance id will be passed
     * 
     * @return resultSet     resultSet for the right luggage
     */  
    private ResultSet getDetailsLostLuggageResultSet() throws SQLException{
        String id = LostLuggageDetailsInstance.getInstance().currentLuggage().getRegistrationNr();

        ServiceDataLost detailsItem = new ServiceDataLost();
        ResultSet resultSet = detailsItem.getAllDetailsLost(id);
        return resultSet;
    }

    /**  
     * Here are all the detail fields been set with the right data from:
     * The resultSet given
     * 
     * @param resultSet         this will be converted to temp strings and integers
     * @void no direct          the fields will be set within this method
     */       
    @FXML
    private void setLostFields(ResultSet resultSet) throws SQLException{
        //loop trough all the luggages in the resultSet
        //Note: there will be only one 
        while (resultSet.next()) {             
            int getRegistrationNr =        resultSet.getInt("F.registrationNr");
            String getDateLost =           resultSet.getString("F.dateLost");
            String getTimeLost =           resultSet.getString("F.timeLost");

            String getLuggageTag =         resultSet.getString("F.luggageTag");
            String getLuggageType =        resultSet.getString("T."+MainApp.getLanguage()+"");
            String getBrand =              resultSet.getString("F.brand");
            String getMainColor =          resultSet.getString("c1."+MainApp.getLanguage()+"");
            String getSecondColor =        resultSet.getString("c2."+MainApp.getLanguage()+"");
            String getSize =               resultSet.getString("F.size");
            String getWeight =             resultSet.getString("F.weight");   
            String getOtherCharacteristics=resultSet.getString("F.otherCharacteristics");

            int getPassengerId =           resultSet.getInt("F.passengerId");
            String getName =               resultSet.getString("P.name");
            String getAddress =            resultSet.getString("P.address");
            String getPlace =              resultSet.getString("P.place");
            String getPostalcode =         resultSet.getString("P.postalcode");
            String getCountry =            resultSet.getString("P.country");
            String getEmail =              resultSet.getString("P.email");
            String getPhone =              resultSet.getString("P.phone");

            String getFlight =             resultSet.getString("F.Flight"); 

        //set al the fields
        registrationNr.setText( Integer.toString(getRegistrationNr) );  
        luggageTag.setText(getLuggageTag);

        type.setText(getLuggageType);
        brand.setText(getBrand);

        mainColor.setText(getMainColor);
        secondColor.setText(getSecondColor);
        size.setText(getSize);
        weight.setText(getWeight);
        signatures.setText(getOtherCharacteristics);

        passangerId.setText( Integer.toString(getPassengerId) );
        passangerName.setText(getName);
        address.setText(getAddress);
        place.setText(getPlace);
        postalCode.setText(getPostalcode);
        country.setText(getCountry);
        email.setText(getEmail);
        phone.setText(getPhone);

        //locationFound.setText(getLocationFound);
        dateLost.setText(getDateLost);
        timeLost.setText(getTimeLost);
        flight.setText(getFlight);
        }
        
    }  
    
    
    
    /**  
     * Here will the resultSet of the found manual matching luggage be get 
     * For getting the right resultSet the correct instance id will be passed
     * 
     * @return resultSet     resultSet for the right luggage
     */  
    private ResultSet getDetailsFoundLuggageResultSet() throws SQLException{
        String id = FoundLuggageDetailsInstance.getInstance().currentLuggage().getRegistrationNr();
        ServiceDataFound detailsItem = new ServiceDataFound();
        ResultSet resultSet = detailsItem.getAllDetailsFound(id);
            
        return resultSet;
    }
    
    /**  
     * Here are all the detail fields been set with the right data from:
     * The resultSet given
     * 
     * @param resultSet         this will be converted to temp strings and integers
     * @void no direct          the fields will be set within this method
     */    
    @FXML
    private void setFoundFields(ResultSet resultSet) throws SQLException{ 
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
        
        //set al the fields
        registrationNr1.setText( Integer.toString(getRegistrationNr) );  
        luggageTag1.setText(getLuggageTag);

        type1.setText(getLuggageType);
        brand1.setText(getBrand);

        mainColor1.setText(getMainColor);
        secondColor1.setText(getSecondColor);
        size1.setText(getSize);
        weight1.setText(getWeight);
        signatures1.setText(getOtherCharacteristics);

        passangerId1.setText( getPassengerId );
        passangerName1.setText(getName);
        address1.setText(getAddress);
        place1.setText(getPlace);
        postalCode1.setText(getPostalcode);
        country1.setText(getCountry);
        email1.setText(getEmail);
        phone1.setText(getPhone);

        locationFound.setText(getLocationFound);
        dateFound.setText(getDateFound);
        timeFound.setText(getTimeFound);
        flight1.setText(getFlight);
        }
    }
    
    
    /**  
     * When the 'manual match ' button is clicked the instances will be set
     * And the stage will be closed
     * 
     * @param event             when the button is clicked 
     * @throws java.io.IOException 
     */   
    @FXML
    protected void manualMatching(ActionEvent event) throws IOException{
        //if the user is not on the matching view, switch to that view
        if (ServiceHomeViewController.isOnMatchingView()==false){
            MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
        }
        
        //initialize the instances with the right luggage
        FoundLuggage passFoundLuggage =  FoundLuggageDetailsInstance.getInstance().currentLuggage();
        FoundLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(passFoundLuggage.getRegistrationNr());   
        
        
        LostLuggage passLostLuggage =  LostLuggageDetailsInstance.getInstance().currentLuggage();
        LostLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(passLostLuggage.getRegistrationNr());
        
        //close this stage
        closeStage();
    }
    
    
        
    @FXML
    public void openEditViewLost() throws IOException{
        MainApp.switchView("/Views/Service/ServiceEditLostLuggageView.fxml");
        closeStage();
    }
   
    @FXML
    public void  openEditViewFound() throws IOException{
        MainApp.switchView("/Views/Service/ServiceEditFoundLuggageView.fxml");
        closeStage();
    }
    
    /**  
     * Close the current stage by getting the window of a fields scene's
     * And casting this to a stage, and close this stage
     * 
     */ 
    public void closeStage(){
        Stage stage = (Stage) registrationNr.getScene().getWindow();
        stage.close();
    }
    
}
