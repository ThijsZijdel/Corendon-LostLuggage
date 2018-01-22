package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceDataFound;
import is103.lostluggage.Model.Service.Data.ServiceDataLost;
import is103.lostluggage.Model.Service.Instance.Details.FoundLuggageDetailsInstance;
import is103.lostluggage.Model.Service.Instance.Details.LostLuggageDetailsInstance;
import is103.lostluggage.Model.Service.Instance.Matching.FoundLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Instance.Matching.LostLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Interface.FoundLuggageFields;
import is103.lostluggage.Model.Service.Interface.LostLuggageFields;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import is103.lostluggage.Model.Service.Model.LostLuggage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
 * FXML Controller class 
 *
 * @author Thijs Zijdel - 500782165
 */
public class ServiceConfirmedMatchLuggageViewController implements 
                                                            Initializable, 
                                                            FoundLuggageFields, 
                                                            LostLuggageFields {
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
    
    
    //Detail fields
    @FXML private JFXTextField detailsMatcheId;
    @FXML private JFXTextField detailsName;
    @FXML private JFXTextField detailsPhone;
    @FXML private JFXTextField detailsEmail;
    @FXML private JFXTextField detailsAddress;
    @FXML private JFXTextField detailsPlace;
    @FXML private JFXTextField detailsCode;
    @FXML private JFXTextField detailsCountry;
    
    @FXML private JFXComboBox detailsDeliverer;
    
    
    //getting the main language of the application
    private final String LANGUAGE = MainApp.getLanguage();  
    
    //page title 
    private final String TITLE = "Matched luggage";
     private final String TITLE_DUTCH = "Overeenkomende bagage";
    
    //conection to the main database
    private final MyJDBC DB = MainApp.getDatabase();
    
    
    //current date 
    private String currentDate;
    
    //the match id that will be generated and asigned
    private int matcheID;
    
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceConfirmedMatchLuggageView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Try to set the title of the page
        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(TITLE_DUTCH);
            } else {
                MainViewController.getInstance().getTitle(TITLE);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceMatchingViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        //getting the current data
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        currentDate = dtf.format(localDate);

        //try to load initialize and set methodes
        try {
            setLostFields(getManualLostLuggageResultSet());
            setFoundFields(getManualFoundLuggageResultSet());
            
            generateMatcheId();
            setDetails();
            setMatched();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceConfirmedMatchLuggageViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**  
     * Here will the matcheId be generated
     * Note: the checking will also be done here
     * 
     * @throws SQLException     getting a resultSet from the db 
     * @void no direct output 
     */
    private void generateMatcheId() throws SQLException{
        ResultSet resultSet = 
            DB.executeResultSetQuery("SELECT COUNT(*) AS 'count' FROM matched;");
        while (resultSet.next()){
            //get the amount of matches in the db
            int count = resultSet.getInt("count");
            
            //expect that the matcheId's are: 0,1,2,3.. etc.
            //So assign to the matcheID the amount of matches + 1
            matcheID = count++; 
        }
        //check the matche id at least one time
        do {
            matcheID++;
            //if the return (of the check) is not true
            //add 1 to the matcheId and check it again
        } while (!checkMatcheId()); 
        
    }
    
    /**  
     * Here will be checked if the (semi) generated matcheId already exists
     * 
     * @throws SQLException     getting a resultSet from the db 
     * @return boolean          if the match id already exist 
     */
    private boolean checkMatcheId() throws SQLException{
        //i would have prevered this query in a different place..
        ResultSet resultSetCheck = DB.executeResultSetQuery(""
                + "SELECT COUNT(*) AS 'check' "
                + "FROM matched WHERE matchedid = '"+matcheID+"';");
        
        //loop trough the resultset 
        while (resultSetCheck.next()){
            int check = resultSetCheck.getInt("check");
            
            //if the check does not exists, return true
            //The count of exsiting matches with the same id is than zero
            return check == 0;
        }
        //if there wasn't any resultset return false
        return false;
    }
    
    /**  
     * Here will all the detail fields be set
     * Note: the passenger data from the lost luggage is used
     * 
     * @void   no direct output 
     */
    private void setDetails() {
        detailsName.setText(passangerName.getText());
        detailsPhone.setText(phone.getText());
        detailsEmail.setText(email.getText());
        detailsAddress.setText(address.getText());
        detailsPlace.setText(place.getText());
        detailsCode.setText(postalCode.getText());
        detailsCountry.setText(country.getText());
        
        detailsMatcheId.setText(Integer.toString(matcheID));

        //list of deliverers (can be changed to a table in the db)
        ObservableList<String> deliverers = FXCollections.observableArrayList();
        deliverers.add("DHL");
        deliverers.add("Post NL");
        deliverers.add("Correndon");
        deliverers.add("Self service");
        //add the deliverers to the combo box 
        detailsDeliverer.getItems().addAll(deliverers);
    }
    
    
    /**  
     * Here will the lost and found luggage be added to the matched table
     * And the id will also be set in the lost & found tables 
     * 
     * @throws SQLException     updating data in the db 
     * @void   no direct output 
     */
    private void setMatched() throws SQLException {
        //get the right values to set in the DB
        String matchIDstring = Integer.toString(matcheID);
        String idLostLuggage = registrationNr.getText();
        String idFoundLuggage = registrationNr1.getText();
        
        //check if an user is logged in
        if (MainApp.currentUser.getId() != null){
            //insert the match in the matched table with the right data
            //note: choosen for a prepared statement (no user input involved)
            DB.executeInsertMatchQuery( matchIDstring,              //generated id
                                        idFoundLuggage,             //found id
                                        idLostLuggage,              //lost id
                                        MainApp.currentUser.getId(),//employee
                                        currentDate);               //current date       


            //update the lost luggage item with the right matcheId
            DB.executeUpdateLuggageFieldQuery(
                                        "lostluggage", 
                                        "matchedId", 
                                        matchIDstring, 
                                        idLostLuggage);


            //update the found luggage item with the right matcheId
            DB.executeUpdateLuggageFieldQuery(
                                        "foundluggage", 
                                        "matchedId", 
                                        matchIDstring, 
                                        idFoundLuggage);
        } else {
            System.out.println("No user logged in.");
        }
    }
    
    /**  
     * Here will the the deliverer be updated in the table on the current id
     * 
     * @throws SQLException     updating (setting) data in the db 
     * @void   no direct output 
     */
    @FXML
    protected void confirmDeliverer() throws SQLException {
        //check if an user is logged in
        if (MainApp.currentUser.getId() != null){
            //using a prepared statment to set the deliverer 
            DB.executeUpdateQueryWhere(
                    "matched",                  //table
                    "delivery",                 //field
                    "matchedId",                //where
                    detailsDeliverer.getValue().toString(),     //value (deliverer)
                    Integer.toString(matcheID));//id of match
        } else {
            System.out.println("No user logged in.");
        }
    }
    
    /**  
     * Here will the resultSet of the lost manual matching luggage be get 
     * For getting the right resultSet the correct instance id will be passed
     * 
     * @return resultSet     resultSet for the right luggage
     */  
    @Override
    public ResultSet getManualLostLuggageResultSet() throws SQLException{
        String id = LostLuggageManualMatchingInstance.getInstance().currentLuggage().getRegistrationNr();

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
    @Override
    public void setLostFields(ResultSet resultSet) throws SQLException{
        //loop trough all the luggages in the resultSet
        //Note: there will be only one 
        while (resultSet.next()) {             
            int getRegistrationNr =        resultSet.getInt("F.registrationNr");
            String getDateLost =           resultSet.getString("F.dateLost");
            String getTimeLost =           resultSet.getString("F.timeLost");

            String getLuggageTag =         resultSet.getString("F.luggageTag");
            String getLuggageType =        resultSet.getString("T."+LANGUAGE+"");
            String getBrand =              resultSet.getString("F.brand");
            String getMainColor =          resultSet.getString("c1."+LANGUAGE+"");
            String getSecondColor =        resultSet.getString("c2."+LANGUAGE+"");
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
    @Override
    public ResultSet getManualFoundLuggageResultSet() throws SQLException{
        String id = FoundLuggageManualMatchingInstance.getInstance().currentLuggage().getRegistrationNr();
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
    @Override
    public void setFoundFields(ResultSet resultSet) throws SQLException{ 
        //loop trough all the luggages in the resultSet
        //Note: there will be only one
        while (resultSet.next()) {             
            int getRegistrationNr =        resultSet.getInt("F.registrationNr");
            String getDateFound =          resultSet.getString("F.dateFound");
            String getTimeFound =          resultSet.getString("F.timeFound");

            String getLuggageTag =         resultSet.getString("F.luggageTag");
            String getLuggageType =        resultSet.getString("T."+LANGUAGE+"");
            String getBrand =              resultSet.getString("F.brand");
            String getMainColor =          resultSet.getString("c1."+LANGUAGE+"");
            String getSecondColor =        resultSet.getString("c2."+LANGUAGE+"");
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
            String getLocationFound =      resultSet.getString("L."+LANGUAGE+"");
        
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
     * @throws java.io.IOException  possible switching views
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
    
    @FXML 
    public void contactedPassenger(){
        // set switch before this   DISABLED
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