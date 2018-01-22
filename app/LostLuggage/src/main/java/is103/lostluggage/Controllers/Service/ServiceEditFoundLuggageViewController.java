package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.Model.Service.Data.ServiceGetDataFromDB;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceDataFound;
import is103.lostluggage.Model.Service.Data.ServiceDataMatch;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import is103.lostluggage.Model.Service.Instance.Details.FoundLuggageDetailsInstance;
import is103.lostluggage.Model.Service.Instance.Matching.FoundLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Interface.FoundLuggageFields;
import is103.lostluggage.Model.Validate;
import static is103.lostluggage.Model.Validate.isValidDate;
import static is103.lostluggage.Model.Validate.isValidInt;
import static is103.lostluggage.Model.Validate.isValidTime;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author Thijs Zijdel - 500782165
 */

public class ServiceEditFoundLuggageViewController implements Initializable, FoundLuggageFields {
        
    //get al the jfxtextFields, areas and comboboxes
    @FXML private JFXTextField registrationNr;
    @FXML private JFXTextField luggageTag;
    @FXML private JFXTextField brand;
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
    @FXML private JFXTextField flight;
    
    
    @FXML private JFXComboBox colorPicker1;
    @FXML private JFXComboBox colorPicker2;
    @FXML private JFXComboBox locationPicker;
    @FXML private JFXComboBox typePicker;
    
    @FXML private StackPane stackPane;
    
    @FXML private JFXButton saveEditings;
    
    //create dialog content/layout and a textflow for the body
    private final JFXDialogLayout CONTENT = new JFXDialogLayout();
    private final TextFlow ALERT_MESSAGE = new TextFlow();
    
     //view title
    private final String TITLE = "Edit Found Luggage";
    private final String TITLE_DUTCH ="Pas gevonden bagage aan";
    
    //alert message content (changes)
    private String changedFields = "";
    private int changes = 0;
    private int changeCountDoubleCheck = 0;
    
    //start values of the initialized text fields
    public String[] startValues;
    
    //language of the application
    private final String LANGUAGE = MainApp.getLanguage();
    
    //conection to the main database
    private final MyJDBC DB = MainApp.getDatabase();
    
    //colors 
    private final String UN_FOCUS_COLOR = "#ababab";
    private final String NOTICE_COLOR = "#4189fc";
    private final String ALERT_COLOR = "#e03636";
    
    //amount of field 
    private final int AMOUNT_OF_FIELDS = 21;
    
    int passengerCount=0;
    
    //the combo boxes will be converted to te following codes (before updating)
    int ralCode1, ralCode2, typeCode, locationCode;
    
    //validation object
    Validate validate = new Validate();
    
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceEditFoundLuggageView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //for setting the resource bundle 
        MainApp.currentView = "/Views/Service/ServiceEditFoundLuggageView.fxml";
         
        //try to set the page title
        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(TITLE_DUTCH);
            } else {
                MainViewController.getInstance().getTitle(TITLE);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceHomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
              
        //initialize and set al the comboboxes
        initializeComboBoxes();
        
        //try to set the found luggage fields
        try {           
            setFoundFields(getManualFoundLuggageResultSet());
        } catch (SQLException ex) {
            Logger.getLogger(ServiceEditFoundLuggageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //get start values of the fields
        startValues = getFields();
        
        //otherwise there will be a grey overlay (= not clickable)
        closeStackpane();
        
        //set screen status
        ServiceHomeViewController.setOnMatchingView(false);
        
        //passenger field check
        checkPassengerId();
    } 
    
    /**  
     * Here are all the combo boxes set with the right data
     * 
     * @void initializing
     */ 
    private void initializeComboBoxes(){
        //set 2 objects to get the right data for the database          //color
        ServiceGetDataFromDB colors = new ServiceGetDataFromDB("color", LANGUAGE, null);
                                                                        //types
        ServiceGetDataFromDB types = new ServiceGetDataFromDB("luggagetype", LANGUAGE, null);
                                                                                //location
        ServiceGetDataFromDB locations = new ServiceGetDataFromDB("location", LANGUAGE, null);
        
        //try to get the data from the database and set it 
        try {                    
            //get the right string list for each combo box         
            ObservableList<String> colorsStringList = colors.getStringList();
            ObservableList<String> luggageStringList = types.getStringList();
            
            ObservableList<String> locationStringList = locations.getStringList();
               
            //set the string lists to the combo boxes
            colorPicker1.getItems().addAll(colorsStringList);
            colorPicker2.getItems().addAll(colorsStringList);
           
            typePicker.getItems().addAll(luggageStringList);
            
            locationPicker.getItems().addAll(locationStringList);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceEditFoundLuggageViewController.class.getName()).log(Level.SEVERE, null, ex);
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
            int getRegistrationNr =         resultSet.getInt("F.registrationNr");
            String getDateFound =           resultSet.getString("F.dateFound");
            String getTimeFound =           resultSet.getString("F.timeFound");

            String getLuggageTag =          resultSet.getString("F.luggageTag");
            String getLuggageType =         resultSet.getString("T."+LANGUAGE);
            String getBrand =               resultSet.getString("F.brand");
            String getMainColor =           resultSet.getString("c1."+LANGUAGE);
            String getSecondColor =         resultSet.getString("c2."+LANGUAGE);
            String getSize =                resultSet.getString("F.size");
            String getWeight =              resultSet.getString("F.weight");   
            String getOtherCharacteristics= resultSet.getString("F.otherCharacteristics");

            int getPassengerId =            resultSet.getInt("F.passengerId");
            String getName =                resultSet.getString("P.name");
            String getAddress =             resultSet.getString("P.address");
            String getPlace =               resultSet.getString("P.place");
            String getPostalcode =          resultSet.getString("P.postalcode");
            String getCountry =             resultSet.getString("P.country");
            String getEmail =               resultSet.getString("P.email");
            String getPhone =               resultSet.getString("P.phone");

            String getFlight =              resultSet.getString("F.arrivedWithFlight"); 
            String getLocationFound =       resultSet.getString("L."+LANGUAGE);

            // -> set current luggage's data
            colorPicker1.setValue(getMainColor);
            colorPicker2.setValue(getSecondColor);
            locationPicker.setValue(getLocationFound);
            typePicker.setValue(getLuggageType);

            registrationNr.setText( Integer.toString(getRegistrationNr) );  
            luggageTag.setText(getLuggageTag);

            brand.setText(getBrand);
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

            dateFound.setText(getDateFound);
            timeFound.setText(getTimeFound);
            flight.setText(getFlight);
        }   
    }
    
    /**  
     * Here will all the values of the fields be get and stored in an array
     * 
     * @return String[]        all the string values of the fields
     */ 
    public String[] getFields(){
        String[] values = new String[AMOUNT_OF_FIELDS];

        values[0] = registrationNr.getText();
        values[1] = luggageTag.getText();

        values[2] = brand.getText();

        values[3] = size.getText();
        values[4] = weight.getText();
        values[5] = signatures.getText();

        values[6] = passangerId.getText();
        values[7] = passangerName.getText();
        values[8] = address.getText();
        values[9] = place.getText();
        values[10] = postalCode.getText();
        values[11] = country.getText();
        values[12] = email.getText();
        values[13] = phone.getText();

        values[14] = dateFound.getText();
        values[15] = timeFound.getText();
        values[16] = flight.getText();
        
        values[17] = colorPicker1.getValue().toString(); 
        values[18] = colorPicker2.getValue().toString();
        values[19] = locationPicker.getValue().toString();
        values[20] = typePicker.getValue().toString();
        
        return values;
    }

    
    /**  
     * This method is activated by the 'add to manual matching ' button
     * The view will be switched and the right instance be set
     * 
     * @throws java.io.IOException      switching views
     * @void no direct output 
     */ 
    @FXML public void manualMatch() throws IOException{
        //initialize the right id in the manual found instance 
        FoundLuggage passObject =  FoundLuggageDetailsInstance.getInstance().currentLuggage();
        FoundLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(passObject.getRegistrationNr());
        
        //set the reset status to false, no reset needed
        ServiceHomeViewController.resetMatching = false;
        
        //switch to the matching view
        MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
        
    }
    
    /**  
     * This method is activated by the 'save/ confirm changes ' button
     * The changes will be checked and if the changeCountDoubleCheck count is 2 or higher:
     *      update the luggage and switch the view 
     * 
     * @throws java.sql.SQLException
     * @throws java.io.IOException      (possible) switching views
     * @void no direct output 
     */ 
    @FXML
    public void saveEditings() throws SQLException, IOException{
        //chack changes of the fields
        checkChanges();
        
        if (changeCountDoubleCheck == 1){
            saveEditings.setText("Confirm again");
        }
        //is pressed two times - > confimered
        else if (changeCountDoubleCheck >= 2){
            //reset the label of the button
            saveEditings.setText("Save changes");
            
            //update the luggage 
            updateLuggage();
            
            //switch the view 
            MainApp.switchView("/Views/Service/ServiceOverviewFoundView.fxml");
        }  
    }

    
    public void checkChanges(){
        //reset changedfield string and changes count
        changedFields = "";
        changes = 0;
        
        //string array to compare fields
        String[] newValues = getFields();
        
        //loop trough al field values
        for (int i = 0; i < startValues.length; i++) {
            //if start value of .. field is the same as new value
            if (startValues[i].equals(newValues[i])){
                //use an switch to set that field to the right unFocusColor
                switch (i) {
                    case 0: registrationNr.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 1: luggageTag.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 2: brand.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 3: size.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 4: weight.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 5: signatures.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 6: passangerId.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 7: passangerName.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 8: address.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 9: place.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 10:postalCode.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 11:country.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 12:email.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 13:phone.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 14:dateFound.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 15:timeFound.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 16:flight.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 17:colorPicker1.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 18:colorPicker2.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 19:locationPicker.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                    case 20:typePicker.setUnFocusColor(Paint.valueOf(UN_FOCUS_COLOR));
                            break;
                }
            } else {
                //if the comparison is not equal, 1 changes made
                changes++;
                //use an switch to set that field to the right noticeColor
                //And add the right field to the changed field string for the alert
                switch (i) {
                    case 0: registrationNr.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += "registrationNr";
                            break;
                    case 1: luggageTag.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", luggageTag";
                            break;
                    case 2: brand.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", brand";
                            break;
                    case 3: size.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", size";
                            break;
                    case 4: weight.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", weight";
                            break;
                    case 5: signatures.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", signatures";
                            break;
                    case 6: passangerId.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", id ";
                            break;
                    case 7: passangerName.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", name";
                            break;
                    case 8: address.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", registrationNr";
                            break;
                    case 9: place.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", place";
                            break;
                    case 10:postalCode.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", postal code";
                            break;
                    case 11:country.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", registrationNr";
                            break;
                    case 12:email.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", Email";
                            break;
                    case 13:phone.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", phone";
                            break;
                    case 14:dateFound.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", date";
                            break;
                    case 15:timeFound.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", time";
                            break;
                    case 16:flight.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", flight";
                            break;
                    case 17:colorPicker1.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", main color";
                            break;
                    case 18:colorPicker2.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", secondary color";
                            break;
                    case 19:locationPicker.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", location";
                            break;
                    case 20:typePicker.setUnFocusColor(Paint.valueOf(NOTICE_COLOR));
                            changedFields += ", type";
                            break;
                    default://no more
                            break;
                }
            } 
            
        }
        
        //reset start fields
        startValues = getFields();
        
        //if amount of changes equals zero
        //no more changes made so change count gets ++
        if (changes == 0){
            changeCountDoubleCheck++;
        } else {
            saveEditings.setText("Save changes");
            //if there are some changes:
            //Alert this with an dialog
            alertDialog(changedFields, changes); 
            //reset change count double check 
            changeCountDoubleCheck = 0;
        }
    }
    
    /**  
     * Here will the luggage be updated 
     * 
     *      update the luggage and switch the view 
     * 
     * @param changedFields
     * @param changes
     * @void no direct output, only a alert dialog 
     */
    public void alertDialog(String changedFields, int changes){
        //Remove the first , (comma) and space
        changedFields = changedFields.substring(2);
        
        //Clear the previous message
        CONTENT.getHeading().clear();
        ALERT_MESSAGE.getChildren().clear();
        
        //Set heading of dialog
        CONTENT.setHeading(new Text("Warning"));
        
        //Set the right text for the dialog body.
        String introAlert,outroAlert;
        if (changes == 1){                                      //singular
            introAlert = "There is "+changes+" change made. \n"
                + "The changed field is the following: \n\n";
            outroAlert = "\n\nPlease check and confirm it.";
        } else {                                                //plural
            introAlert = "There are "+changes+" changes made. \n"
                + "The changed fields are the following: \n\n";
            outroAlert = "\n\nPlease check and confirm them.";
        }
        
        Text intro = new Text(introAlert);
        Text changed = new Text(changedFields);
        Text outro = new Text(outroAlert);
        
        //set text color of changed field string to red
        changed.setFill(Color.web(ALERT_COLOR));  
        
        //combine the text parts
        ALERT_MESSAGE.getChildren().addAll(intro, changed, outro);
        //set the text parts (alert message) in the content
        CONTENT.setBody(ALERT_MESSAGE);
        
        //create the dialog alert with the content
        //& place het in the center of the stackpane
        JFXDialog alert = new JFXDialog(stackPane, CONTENT, JFXDialog.DialogTransition.CENTER);
        
        //Create the 'ok'/close button
        JFXButton button = new JFXButton("ok");
        //set button action listner for closing the alert
        button.setOnAction((ActionEvent event) -> {
            alert.close();
            //hide the stackpane so the fields will be clickable again
            closeStackpane();
        });
        //set action button in content for alert
        CONTENT.setActions(button);
        
        //Show the alert message (dialog)
        alert.show();
        //show the stackpane so the dialog will be visible 
        stackPane.setVisible(true);
        
        //change the text on the 'save changes'  button
        saveEditings.setText("Confirm Changes");
    }
    
    
    /**  
     * Here will the all the fields be updated 
     * The unknown fields are also cleared 
     * 
     * 
     * @throws SQLException      executing multiple update query s 
     * @void no direct output 
     */ 
    public void updateLuggage() throws SQLException{
        String registrationNrString = registrationNr.getText();
        
        //Initializing 4 objects with the right fields to get the idCode
        //Note: To get the id the Where statement is also configured for each
        ServiceGetDataFromDB getRalCode1 = new ServiceGetDataFromDB
        ("color", "ralCode", "WHERE `"+LANGUAGE+"`='"+colorPicker1.getValue().toString()+"'");
        ralCode1 = getRalCode1.getIdValue();
        
        ServiceGetDataFromDB getRalCode2 = new ServiceGetDataFromDB
        ("color", "ralCode", "WHERE `"+LANGUAGE+"`='"+colorPicker2.getValue().toString()+"'");
        ralCode2 = getRalCode2.getIdValue();
        
        ServiceGetDataFromDB getType = new ServiceGetDataFromDB
        ("luggagetype", "luggageTypeId", "WHERE `"+LANGUAGE+"`='"+typePicker.getValue().toString()+"'");
        typeCode = getType.getIdValue();
        
        ServiceGetDataFromDB getLocation = new ServiceGetDataFromDB
        ("location", "locationId", "WHERE `"+LANGUAGE+"`='"+locationPicker.getValue().toString()+"'");
        locationCode = getLocation.getIdValue();
        
        
        
        //check all the fields that still needs an (potential) update and are empty
        //and ensure those fields are cleared properly (remove: unknown)
        checkForEmptyFields();
        
        
        
        //check if this field is not (still) un asigned
        if (typeCode != 0){
            //if it is asigned (so not 0) than update that field
            //note: use of prepared statements for preventing sql injection
            DB.executeUpdateLuggageFieldQuery("foundluggage", "luggageType",
                                    Integer.toString(typeCode), registrationNrString);
        }
        //repeat
        if (ralCode1 != 0){
            DB.executeUpdateLuggageFieldQuery("foundluggage", "mainColor",
                                    Integer.toString(ralCode1), registrationNrString);
        }
        if (ralCode2 != 0){
            DB.executeUpdateLuggageFieldQuery("foundluggage", "secondColor",
                                    Integer.toString(ralCode2), registrationNrString);
        }
        if (locationCode != 0){
            DB.executeUpdateLuggageFieldQuery("foundluggage", "locationFound",
                                    Integer.toString(locationCode), registrationNrString);
        }
        
        
        //validate the weight inputted
        int weightInt = isValidInt(weight.getText());
        if (weightInt != 0){ 
            //if the return wasn't 0, update the weight with a prepared statment
            DB.executeUpdateLuggageFieldQuery("foundluggage", "weight",
                                    Integer.toString(weightInt), registrationNrString);    
        }
        
        //validate the date inputted
        String dateString = isValidDate(dateFound.getText());
        if (dateString != null){
            //if the date is not null, than the date format is good
            //but still needs checking for invalid year, month and day..
     
                //update the date found with a prepared statment
                DB.executeUpdateLuggageFieldQuery("foundluggage", "dateFound",
                                        dateString, registrationNrString);  
        }
        
        //validate the time inputted
        String timeString = isValidTime(timeFound.getText());
        if (timeString != null){
            //update the time found with a prepared statment
            DB.executeUpdateLuggageFieldQuery("foundluggage", "timeFound",
                                    timeString, registrationNrString);    
        }
        
        //Update the luggage itself with the right data
        DB.executeUpdateLuggageQuery(
                luggageTag.getText(), 
                brand.getText(), 
                size.getText(), 
                signatures.getText(), 
                registrationNrString, 
                "foundluggage");

        
        //check if the passenger (id) is not empty
        if (!"".equals(passangerId.getText()) || null != passangerId.getText()){
            //Update the passenger with the right data
            DB.executeUpdatePassengerQuery(
                    passangerName.getText(), 
                    address.getText(), 
                    place.getText(), 
                    postalCode.getText(), 
                    country.getText(), 
                    email.getText(), 
                    phone.getText(), 
                    passangerId.getText());    
        }
    }
    
    /**  
     * Check if the passenger is set or not 
     * If there isn't a passenger set:
     *     disable all the passenger related fields
     *     so there can't be a new inserted
     * 
     * @void
     */  
    private void checkPassengerId(){
        //if the passenger id is empty or zero, disable them
        if ("".equals(passangerId.getText()) || 0 == Integer.parseInt(passangerId.getText())){
            passangerId.setDisable(true);
            passangerName.setDisable(true);
            address.setDisable(true);     
            place.setDisable(true);
            postalCode.setDisable(true);
            country.setDisable(true);
            email.setDisable(true);
            phone.setDisable(true);
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

        //set the reset status to true for resetting a possible previous list
        ServiceHomeViewController.setPotentialResetStatus(true);
        
        //get the id of the current luggage
        String id = registrationNr.getText();

        //initialize the potential matches for the given id 
        data.potentialMatchesForFoundLuggage(id);
        
        //switch to the matching view
        MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
        
        //set the right tab, 2 = potential matching tab
        ServiceMatchingViewController.getInstance().setMatchingTab(
                ServiceMatchingViewController.POTENTIAL_MATCHING_TAB_INDEX
        );

    }

    /**  
     * When closing the alert message the stackPane isn't disabled automatic
     * So this method is for closing the stack pane by chancing it's visibility 
     **/
    @FXML
    public void closeStackpane(){
        stackPane.setVisible(false); 
    }

    /**  
     * When updating the fields there must be checked if the fields maybe contain
     * The preset: unknown message 
     * 
     * Note: for optimizing is there a array of fields used here.
     **/
    private void checkForEmptyFields() {
        //check if one of the updated fields contains the "unknown" string
        if (size.getText().contains("unknown")){size.setText("0");}
        if (weight.getText().contains("unknown")){weight.setText("0");}
        if (signatures.getText().contains("unknown")){signatures.setText("");}
        
        //asign fields that can be checked in an loop in an array
        JFXTextField[] fields = {luggageTag,brand,passangerName,
            address,place,postalCode,country,email,phone,flight};
        
        //loop trough all the fields
        for (JFXTextField field : fields) {
            //check if it contains the unknown string
            if (field.getText().contains("unknown")){
                //clear it
                field.setText("");
            }
        }
    }
    
}