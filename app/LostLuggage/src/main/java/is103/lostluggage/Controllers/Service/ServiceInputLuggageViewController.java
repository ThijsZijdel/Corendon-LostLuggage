package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.PdfDocument;
import is103.lostluggage.Model.Service.Model.Form;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class for the inputluggage view
 *
 * @author Arthur Krom
 */
public class ServiceInputLuggageViewController implements Initializable {

    @FXML
    private JFXComboBox missingFoundComboBox, flightJFXComboBox, typeJFXComboBox, colorJFXComboBox,
            secondColorJFXComboBox, locationJFXComboBox, airportJFXComboBox, destinationJFXComboBox;
    
    //Hashmap containing all the comboboxes
    private Map<String, JFXComboBox> comboBoxes = new LinkedHashMap<>();
    
    @FXML
    private GridPane mainGridPane,travellerInfoGridPane, luggageInfoGridPane ;
        
    @FXML
    private Label passengerInformationLbl;
    
    @FXML
    private JFXButton exportPDFBtn;
    
    @FXML
    private JFXDatePicker dateJFXDatePicker;
    
    @FXML
    private JFXTimePicker timeJFXTimePicker;
    
    @FXML
    private JFXTextField nameJFXTextField, addressJFXTextField, placeJFXTextField, 
            postalcodeJFXTextField, countryJFXTextField, phoneJFXTextField, emailJFXTextField, labelnumberJFXTextField, brandJFXTextField,
            characterJFXTextField, sizeJFXTextField, weightJFXTextField;
    
    //Hashmap containing all the text fields
    private Map<String, JFXTextField> textFields = new LinkedHashMap<>();
    
    //Form object
    private Form form;
    
    //HashMap that contains all the form values
    private Map<String, String> formValues = new LinkedHashMap<>();
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Define the previous view
        MainViewController.previousView = "/Views/Service/ServiceHomeView.fxml";
        
        //set the title of the view, default form to be displayed is Missing
        changeTitle("Lost luggage form");
        
        //Make a new form.
        this.form = new Form();
        
        //Method to initialize the hashmaps
        setHashMaps();
        
        try {
            //Import the form options
            Map<String, ArrayList<String>> formOptions = this.form.getFormOptions();

            //Add the all the options to the comboboxes
            missingFoundComboBox.getItems().addAll(formOptions.get("foundorlost"));
            colorJFXComboBox.getItems().addAll(formOptions.get("colors"));
            secondColorJFXComboBox.getItems().addAll(formOptions.get("colors"));
            locationJFXComboBox.getItems().addAll(formOptions.get("locations"));
            airportJFXComboBox.getItems().addAll(formOptions.get("airports"));
            flightJFXComboBox.getItems().addAll(formOptions.get("flights"));
            typeJFXComboBox.getItems().addAll(formOptions.get("luggagetypes"));
            destinationJFXComboBox.getItems().addAll(formOptions.get("airports"));
            
            //Set default value for the type of form combobox
            missingFoundComboBox.setValue("Lost");
            this.form.setType("Lost");
            
        } catch (SQLException ex) {
            Logger.getLogger(ServiceInputLuggageViewController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    //Method to change the title of the current view
    public void changeTitle(String title){
        try {
            MainViewController.getInstance().getTitle(title);
        } catch (IOException ex) {
            Logger.getLogger(ServiceInputLuggageViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Method to initialize all the hashmaps
    private void setHashMaps(){
        
        //initializing the combobox hashmap
        
        comboBoxes.put("missingfound", missingFoundComboBox);
        comboBoxes.put("flight", flightJFXComboBox);
        comboBoxes.put("type", typeJFXComboBox);
        comboBoxes.put("color", colorJFXComboBox);
        comboBoxes.put("secondcolor", secondColorJFXComboBox);
        comboBoxes.put("location", locationJFXComboBox);
        comboBoxes.put("airport", airportJFXComboBox);
        comboBoxes.put("destination", destinationJFXComboBox);
        
        //initializing the textfield hashmap
        
        textFields.put("name", nameJFXTextField);
        textFields.put("address", addressJFXTextField);
        textFields.put("place", placeJFXTextField);
        textFields.put("postalcode", postalcodeJFXTextField);
        textFields.put("country", countryJFXTextField);
        textFields.put("phone", phoneJFXTextField);
        textFields.put("email", emailJFXTextField);
        textFields.put("labelnumber", labelnumberJFXTextField);
        textFields.put("brand", brandJFXTextField);
        textFields.put("character", characterJFXTextField);
        textFields.put("size", sizeJFXTextField);
        textFields.put("weight", weightJFXTextField);

    }
    
    //Method that loops through all the fields checking whether the ones that are not allowed to be empty aren't
    public boolean checkFields(){
        
        boolean appropriate = true;
        
        //Date picker and timepicker cannot be looped through since theyre not inside an array
        
        //Check whether the date field has been filled in appropriately
        //Get the date, if its empty let the user know it has to be filled     
        if(dateJFXDatePicker.getValue() == null || dateJFXDatePicker.getValue().toString().isEmpty()){
            dateJFXDatePicker.setStyle("-fx-background-color: #f47142");
            appropriate =  false;
        }else{
            dateJFXDatePicker.setStyle(null);
        }
        
        //Get the time, if its empty let the user know it has to be filled
        if(timeJFXTimePicker.getValue() == null || timeJFXTimePicker.getValue().toString().isEmpty()){
            timeJFXTimePicker.setStyle("-fx-background-color: #f47142");
            appropriate =  false;
        }else{
            timeJFXTimePicker.setStyle(null);
        }
        
        //Loop through the comboboxes
        for(Map.Entry<String, JFXComboBox> entry: comboBoxes.entrySet()){
            
            String key = entry.getKey();
            JFXComboBox value = entry.getValue();
            
            //first check for the comboboxes that have to be selected in both forms
            if(key.equals("color") || key.equals("type") || key.equals("airport") || key.equals("flight")){
            
                if(value.getValue() == null || value.getValue().toString().isEmpty()){
                    value.setStyle("-fx-background-color: #f47142");
                    appropriate =  false;
                }else{
                    value.setStyle(null);
                }
            }
            
            //check the comboboxes for the lost form, in this case only the destination is an additional combobox
            //where an option has to be selected
            if(form.getType().equals("Lost") && key.equals("destination")){
                if(value.getValue() == null || value.getValue().toString().isEmpty()){
                    value.setStyle("-fx-background-color: #f47142");
                    appropriate =  false;
                }else{
                    value.setStyle(null);
                }
            }
        }
        
        //loop through the textfields
        for(Map.Entry<String, JFXTextField> entry: textFields.entrySet()){
            
            String key = entry.getKey();
            JFXTextField value = entry.getValue();
            
            //If its a lost form, the following fields cannot be empty
            if(form.getType().equals("Lost")){         
                if(key.equals("name") || key.equals("address") || key.equals("place")
                   || key.equals("postalcode") || key.equals("country") || key.equals("phone") || key.equals("email")){
                    if(value.getText() == null || value.getText().isEmpty()){
                        value.setStyle("-fx-background-color: #f47142");
                        appropriate =  false;
                    }else{
                        value.setStyle(null);
                    }
                }
            }
        }
        return appropriate;
    }
    
    @FXML
    //Method that will add the form to the database
    public void submitForm(ActionEvent event){
        
        
        //Check whether the fields have been filled in appropriately
        if(checkFields() == false){
            System.out.println("form not filled in properly");
            return;
        }
        
        
        //General information
        String formtype = missingFoundComboBox.getValue().toString();
        String date = dateJFXDatePicker.getValue().toString();
        String time = timeJFXTimePicker.getValue().toString();
        String airport = airportJFXComboBox.getValue().toString();
        
        //Default passenger information if the form is foundluggage, if its lostluggage it will
        //get check in the checkFields method.
        String name = "", address = "", place = "", postalcode = "", country = "", phone = "", email = "";

        //Luggage information
        String labelnumber = "", flight = "", type = "", brand = "", color = "", secondColor = "", size = "", characteristics = "", location = "", weight = "";

        //Get the passenger information
        name        = nameJFXTextField.getText();
        address     = addressJFXTextField.getText();
        place       = placeJFXTextField.getText();
        postalcode  = postalcodeJFXTextField.getText();
        country     = countryJFXTextField.getText();
        phone       = phoneJFXTextField.getText();
        email       = emailJFXTextField.getText();
        
        //Get the luggage information
        labelnumber = labelnumberJFXTextField.getText();
        type = typeJFXComboBox.getValue().toString();
        color = colorJFXComboBox.getValue().toString();
        brand = brandJFXTextField.getText();
        size = sizeJFXTextField.getText();
        characteristics = characterJFXTextField.getText();
        
        if(weight.isEmpty()){
            weight = "0";
        }
        
        if(flightJFXComboBox.getValue() != null && !flightJFXComboBox.getValue().toString().isEmpty()){
            flight = flightJFXComboBox.getValue().toString();
        }
       
        if(secondColorJFXComboBox.getValue() != null && !secondColorJFXComboBox.getValue().toString().isEmpty()){
            secondColor = secondColorJFXComboBox.getValue().toString();
        }
        
        if(locationJFXComboBox.getValue() != null && !locationJFXComboBox.getValue().toString().isEmpty()){
            location = locationJFXComboBox.getValue().toString();
        }

        if(weightJFXTextField.getText() != null && !weightJFXTextField.getText().isEmpty()){
              weight = weightJFXTextField.getText();
        }
   
        //Is it found luggage? else its missing
        if("Found".equals(formtype)){
    
            //If atleast one of the following fields is not empty the passenger will be added to the database
            if(!name.isEmpty() || !address.isEmpty() || !place.isEmpty() || !postalcode.isEmpty() ||!email.isEmpty() || !phone.isEmpty()){
                
                //Query to add the passenger
                String addPassengerQuery = "INSERT INTO passenger VALUES(NULL ,'"+name+"', '"+address+"', '"+place+"', '"+postalcode+"', '"+country+"', '"+email+"', '"+phone+"')";

                //Execute the query to add a passenger to the database
                int affectedRowsPassengerQuery = MainApp.getDatabase().executeUpdateQuery(addPassengerQuery);

                //select the id of the passenger we just added by email address
                //TODO: has to be changed to return generated keys from prepared statement
                String selectPassengerQuery = "SELECT passengerId FROM passenger ORDER BY passengerId DESC LIMIT 1";
                
                //execute the query to select the recently added passenger, the String we get back is the id of that user
                String passengerId = MainApp.getDatabase().executeStringQuery(selectPassengerQuery);
                
                //Query to add the missing luggage to the database
                String addFoundLuggageQuery = "INSERT INTO foundluggage VALUES(NULL, '"+date+"','"+time+"', '"+labelnumber+"', (SELECT luggageTypeId FROM luggagetype WHERE english ='"+type+"'), "
                        + "'"+brand+"'"
                        + ", (SELECT ralCode FROM color WHERE english = '"+color+"'), (SELECT ralCode FROM COLOR WHERE english = '"+secondColor+"'), '"+size+"', '"+weight+"', '"+characteristics+"', "
                        + "'"+flight+"', (SELECT locationId FROM location WHERE english ='"+location+"'), '"+MainApp.currentUser.getId()+"', '"+passengerId+"', NULL, (SELECT IATACode FROM destination WHERE airport =  '"+airport+"'), NULL, NULL )";

                //execute the missing luggage query
                int affectedRowsLuggageQuery = MainApp.getDatabase().executeUpdateQuery(addFoundLuggageQuery);
                
                this.exportPDFBtn.setDisable(false);

            }else{
            
            //Query to add the missing luggage to the database
            String addFoundLuggageQuery = "INSERT INTO foundluggage VALUES(NULL, '"+date+"','"+time+"', '"+labelnumber+"', (SELECT luggageTypeId FROM luggagetype WHERE english ='"+type+"'), "
                    + "'"+brand+"'"
                    + ", (SELECT ralCode FROM color WHERE english = '"+color+"'), (SELECT ralCode FROM COLOR WHERE english = '"+secondColor+"'), '"+size+"', '"+weight+"', '"+characteristics+"', "
                    + "'"+flight+"', (SELECT locationId FROM location WHERE english ='"+location+"'), '"+MainApp.currentUser.getId()+"', NULL, NULL, (SELECT IATACode FROM destination WHERE airport =  '"+airport+"'), NULL, NULL  )";
            
            //execute the missing luggage query
            int affectedRowsLuggageQuery = MainApp.getDatabase().executeUpdateQuery(addFoundLuggageQuery);
            
            this.exportPDFBtn.setDisable(false);
            }
            
        }else{

            //Query to add the passenger
            String addPassengerQuery = "INSERT INTO passenger VALUES(NULL ,'"+name+"', '"+address+"', '"+place+"', '"+postalcode+"', '"+country+"', '"+email+"', '"+phone+"')";
            
            //Execute the query to add a passenger to the database
            int affectedRowsPassengerQuery = MainApp.getDatabase().executeUpdateQuery(addPassengerQuery);


            //select the id of the passenger we just added by email address
            //TODO: has to be changed to return generated keys from prepared statement
            String selectPassengerQuery = "SELECT passengerId FROM passenger ORDER BY passengerId DESC LIMIT 1";

            //execute the query to select the recently added passenger, the String we get back is the id of that user
            String passengerId = MainApp.getDatabase().executeStringQuery(selectPassengerQuery);

            //Query to add the missing luggage to the database
            String addMissingLuggageQuery = "INSERT INTO lostluggage VALUES(NULL, '"+date+"','"+time+"', '"+labelnumber+"', (SELECT luggageTypeId FROM luggagetype WHERE english ='"+type+"'), "
                    + "'"+brand+"'"
                    + ", (SELECT ralCode FROM color WHERE english = '"+color+"'), (SELECT ralCode FROM COLOR WHERE english = '"+secondColor+"'), '"+size+"', '"+weight+"', '"+characteristics+"', "
                    + "'"+flight+"', '"+MainApp.currentUser.getId()+"', '"+passengerId+"', NULL, (SELECT IATACode FROM destination WHERE airport =  '"+airport+"'), NULL )";

            //execute the missing luggage query
            int affectedRowsLuggageQuery = MainApp.getDatabase().executeUpdateQuery(addMissingLuggageQuery);
            
            this.exportPDFBtn.setDisable(false);
        }
    }
    
    //Method to export the current form to pdf
    @FXML
    public void exportPDF() throws IOException, SQLException{
        
        //Check if all the fields have been filled in properly
        if(checkFields() == false){
            System.out.println("form not filled in properly");
            return;
        }
        
        //Fileobject
        File file = MainApp.selectFileToSave("*.pdf");
        
        //If fileobject has been initialized
        if(file != null){
            
            //get the location to store the file
            String fileName = file.getAbsolutePath();
            
            formValues.put("Form Type: ", form.getType());
            formValues.put("Registration number:", form.getLastId());
            formValues.put("Employee: ", MainApp.currentUser.getFirstName()+ " " + MainApp.currentUser.getLastName());
            formValues.put("Time: ", timeJFXTimePicker.getValue().toString());
            formValues.put("Date: ", dateJFXDatePicker.getValue().toString());
            formValues.put("Airport: ", airportJFXComboBox.getValue().toString());
                
            //If its lost then passenger info goes in first
            if(form.getType().equals("Lost")){
                
                formValues.put("passengerinfoline", "Passenger Information");
                formValues.put("Name: ", textFields.get("name").getText());
                formValues.put("Address: ", textFields.get("address").getText());
                formValues.put("Place of residence: ", textFields.get("place").getText());
                formValues.put("Postalcode: ", textFields.get("postalcode").getText());
                formValues.put("Country: ", textFields.get("country").getText());
                formValues.put("Phone: ", textFields.get("phone").getText());
                formValues.put("E-mail: ", textFields.get("email").getText());

                
                formValues.put("luggageinfoline", "Luggage Information");
                formValues.put("Labelnumber: ", checkTextField(textFields.get("labelnumber")));
                formValues.put("Flight: ", checkComboBox(comboBoxes.get("flight")));
                formValues.put("Destination: ", checkComboBox(comboBoxes.get("destination")));
                formValues.put("Type: ", checkComboBox(comboBoxes.get("type")));
                formValues.put("Brand: ", checkTextField(textFields.get("brand")));
                formValues.put("Color: ", checkComboBox(comboBoxes.get("color")));
                formValues.put("Second color: ", checkComboBox(comboBoxes.get("secondcolor")));
                formValues.put("Dimensions: ", checkTextField(textFields.get("size")));
                formValues.put("Weight: ", checkTextField(textFields.get("weight")));
                formValues.put("Character: ", checkTextField(textFields.get("character")));
                
            }else{
                
                formValues.put("luggageinfoline", "Luggage Information");
                formValues.put("Labelnumber: ", checkTextField(textFields.get("labelnumber")));
                formValues.put("Flight: ", checkComboBox(comboBoxes.get("flight")));
                formValues.put("Destination: ", checkComboBox(comboBoxes.get("destination")));
                formValues.put("Type: ", checkComboBox(comboBoxes.get("type")));
                formValues.put("Brand: ", checkTextField(textFields.get("brand")));
                formValues.put("Color: ", checkComboBox(comboBoxes.get("color")));
                formValues.put("Second color: ", checkComboBox(comboBoxes.get("secondcolor")));
                formValues.put("Dimensions: ", checkTextField(textFields.get("size")));
                formValues.put("Weight: ", checkTextField(textFields.get("weight")));
                formValues.put("Character: ", checkTextField(textFields.get("character")));
                formValues.put("Location: ", checkComboBox(comboBoxes.get("location")));
                
                formValues.put("passengerinfoline", "Passenger Information");
                formValues.put("Name: ", textFields.get("name").getText());
                formValues.put("Address: ", textFields.get("address").getText());
                formValues.put("Place of residence: ", textFields.get("place").getText());
                formValues.put("Postalcode: ", textFields.get("postalcode").getText());
                formValues.put("Country: ", textFields.get("country").getText());
                formValues.put("Phone: ", textFields.get("phone").getText());
                formValues.put("E-mail: ", textFields.get("email").getText());

                
            }
            
            //New pdf document with filebath in constructor
            PdfDocument Pdf = new PdfDocument(fileName);
            
            //set the values for the pdf
            Pdf.setPdfValues(formValues);
            
            //Save the pdf
            Pdf.savePDF();
            
        }else{
            System.out.println("Setting a location has been cancelled");
        }
    }
    
    @FXML
    public void emailPDF(){
        System.out.println("emailing the PDF");
    }
    

    
    @FXML
    //Method to switch between found/lost form
    public void foundOrMissing(){
        
      String value = missingFoundComboBox.getValue().toString();

        if("Found".equals(value)){
            
            this.form.setType("Found");
                  
            //Remove traveller and luggage info so they can change positions
            mainGridPane.getChildren().remove(travellerInfoGridPane);
            mainGridPane.getChildren().remove(luggageInfoGridPane);
            
            
            //Add them again
            mainGridPane.add(luggageInfoGridPane, 0, 1);
            mainGridPane.add(travellerInfoGridPane, 1, 1);
            
            passengerInformationLbl.setText("Passenger information is not required");
            
            //Change the title of the view
            changeTitle("Found luggage form");
            
            //show the location combobox
            locationJFXComboBox.setVisible(true);
        }
        
        if("Lost".equals(value)){

            this.form.setType("Lost");
            
            //Remove the gridpanes
            mainGridPane.getChildren().remove(travellerInfoGridPane);
            mainGridPane.getChildren().remove(luggageInfoGridPane);
            
            //Add them again on different positions
            mainGridPane.add(travellerInfoGridPane, 0, 1);
            mainGridPane.add(luggageInfoGridPane, 1, 1);
            
            passengerInformationLbl.setText("Passenger information");
            
            
            //Change the title of the view
            changeTitle("Lost luggage form");
            
            //hide the location combobox
            locationJFXComboBox.setVisible(false);     
        }
    }
    
    
    //Method to check whether a combobox has been filled
    public String checkComboBox(JFXComboBox comboBox){
        if(comboBox.getValue() != null && !comboBox.getValue().toString().isEmpty()){
            return comboBox.getValue().toString();
        }else{
            return "";
        }
    }
    
    //Method to check whether the textfield has been filled
    public String checkTextField(JFXTextField textField){
        if(textField.getText() != null && !textField.getText().isEmpty()){
              return textField.getText();
        }else{
            return "";
        }
    }
}
