package is103.lostluggage.Controllers.Service;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.Admin.OverviewUserController;

import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Model.Service.Data.ServiceDataFound;
import is103.lostluggage.MainApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.Model.Service.Data.ServiceMoreDetails;
import is103.lostluggage.Model.Service.Data.ServiceSearchData;
import is103.lostluggage.Model.Service.Interface.FoundLuggageTable;
import is103.lostluggage.Model.Service.Interface.Search;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Thijs Zijdel - 500782165
 */
public class ServiceOverviewFoundViewController implements Initializable, FoundLuggageTable, Search {
    /** 
     * View title
     **/
    private final String TITLE = "Overview Found Luggage";
    private final String TITLE_DUTCH = "Overzicht gevonden bagage";
    
    //stage for more details when double clicking on a table item
    private final Stage POPUP_STAGE_FOUND = new Stage(); 
    
    //list for the search results
    private static ObservableList<FoundLuggage> foundLuggageListSearchResults 
            = FXCollections.observableArrayList();
    
    //text field for the users search input
    @FXML JFXTextField searchField;
    //combo box for a type/field/column search filter
    @FXML JFXComboBox searchTypeComboBox;
    
    //Object for getting the data
    private ServiceDataFound dataListFound;
    
    //show alos matched luggage state, by default on false
    private boolean showMatchedLuggage = false;
    //resultSet for the items when changing the state of the showMatchedLuggage
    private ResultSet matchedLuggageResultSet;
    
    //click counts
    private final int DOUBLE_CLICK = 2;
    
    /* -----------------------------------------
         TableView found luggage's colommen
    ----------------------------------------- */
    @FXML private TableView<FoundLuggage> foundLuggageTable;

    @FXML private TableColumn<FoundLuggage, String>  foundRegistrationNr;
    @FXML private TableColumn<FoundLuggage, String>  foundDateFound;
    @FXML private TableColumn<FoundLuggage, String>  foundTimeFound;
    
    @FXML private TableColumn<FoundLuggage, String>  foundLuggageTag;
    @FXML private TableColumn<FoundLuggage, String>  foundLuggageType;
    @FXML private TableColumn<FoundLuggage, String>  foundBrand;
    @FXML private TableColumn<FoundLuggage, String>  foundMainColor;
    @FXML private TableColumn<FoundLuggage, String>  foundSecondColor;
    @FXML private TableColumn<FoundLuggage, String>  foundSize;
    @FXML private TableColumn<FoundLuggage, Integer> foundWeight;
    @FXML private TableColumn<FoundLuggage, String>  foundOtherCharacteristics;
    @FXML private TableColumn<FoundLuggage, Integer> foundPassengerId;
    
    @FXML private TableColumn<FoundLuggage, String>  foundArrivedWithFlight;
    @FXML private TableColumn<FoundLuggage, Integer> foundLocationFound;
    @FXML private TableColumn<FoundLuggage, Integer> foundMatchedId;

    
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceOverviewFoundView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //for setting the resource bundle 
        MainApp.currentView = "/Views/Service/ServiceOverviewFoundView.fxml";
         
        //set the view's title, and catch a possible IOException
        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(TITLE_DUTCH);
            } else {
                MainViewController.getInstance().getTitle(TITLE);
            }
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        //so the user can change the filter
        initializeComboFilterBox();
        
        //try to initialize and set the found luggage table
        try {
            //Initialize Table & obj found 
            dataListFound = new ServiceDataFound();
            initializeFoundLuggageTable();
            
            //set the table with the right data
            setFoundLuggageTable(dataListFound.getFoundLuggage());
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOverviewFoundViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //set screen status
        ServiceHomeViewController.setOnMatchingView(false);
    }
    
    /**  
     * This method is for initializing the filter combo box
     * The comboBox is used for filtering to a specific:  
     *                                               column/ field/ detail
     *                                                          When searching.
     * @void - No direct output 
     */
    private void initializeComboFilterBox() {
        //add all the filterable fields
        searchTypeComboBox.getItems().addAll(
            "All fields",
            "RegistrationNr", 
            "LuggageTag", 
            "Brand",
            "Color",
            "Weight",
            "Date",
            "Passenger",
            "Location",
            "Characteristics");
        //set the standard start value to all fields
        searchTypeComboBox.setValue("All fields");
    }
        
    /**  
     * This method is for toggle showing only the matched luggage
     * Note: this is being called by the -fx- toggle button
     * 
     * @throws java.sql.SQLException        getting data from the database
     * @void - No direct output             only changing results in the table
     */
    @FXML
    protected void showOnlyMatchedLuggage() throws SQLException{
        //if state of show only matched is false
        if (showMatchedLuggage == false){
            //set it to trough
            showMatchedLuggage = true;
            
            //asign the right resultset to the matchedLuggageResultSet
            //note: this is based on the boolean status that's set previously
            matchedLuggageResultSet = dataListFound.getFoundResultSet(showMatchedLuggage);
        
             //set the table with the right data, the resultset will be converted
            setFoundLuggageTable(dataListFound.getObservableList(matchedLuggageResultSet));
        
         //if the state of only matched was already true
        } else if (showMatchedLuggage == true){
            //set the state to false, so only non matched luggage's are shown
            showMatchedLuggage = false;

            //asign the right resultset to the matchedLuggageResultSet
            //note: this is based on the boolean status that's set previously
            matchedLuggageResultSet = dataListFound.getFoundResultSet(showMatchedLuggage);
            
            //set the table with the right data, the resultset will be converted
            setFoundLuggageTable(dataListFound.getObservableList(matchedLuggageResultSet));
        }
    }
    
    /**  
     * This method is for searching in the table
     * There will be searched on the typed user input and filter value
     * Note: this method is called after each time the user releases a key
     * 
     * @throws java.sql.SQLException        searching in the database
     * @void - No direct output             only changing results in the table
     * @call - getSearchQuery               for getting the search query
     * @call - executeResultSetQuery        for getting the resultSet
     * @call - getFoundLuggageSearchList    the result list based on the resultSet
     */ 
    @FXML
    @Override
    public void search() throws SQLException{
        //get the value of the search type combo box for filtering to a specific column
        String value = searchTypeComboBox.getValue().toString();
        //get the user input for the search
        String search = searchField.getText();
         
        //Create a new searchData object
        ServiceSearchData searchData = new ServiceSearchData();

        //Get the right query based on the users input
        String finalQuery = searchData.getSearchQuery(value, search, "foundluggage");
        
        //clear the previous search result list 
        foundLuggageListSearchResults.clear();
        
        //try to execute the query from the database
        //@throws SQLException 
        try {
            //get the connection to the datbase
            MyJDBC db = MainApp.getDatabase();
            //create a new resultSet and execute the right query
            ResultSet resultSet = db.executeResultSetQuery(finalQuery);
            
            //get the observableList from the search object and asign this to the list
            foundLuggageListSearchResults = ServiceDataFound.loopTroughResultSet(resultSet, showMatchedLuggage);
            //set this list on the table
            foundLuggageTable.setItems(foundLuggageListSearchResults);   
            
            //set the right place holder message for when there are no hits
            foundLuggageTable.setPlaceholder(new Label("No hits based on your search"));
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOverviewFoundViewController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    /**  
     * Here is found luggage table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    @Override
    public void initializeFoundLuggageTable() {
        foundRegistrationNr.setCellValueFactory(       new PropertyValueFactory<>("registrationNr"));
        foundDateFound.setCellValueFactory(            new PropertyValueFactory<>("dateFound"));
        foundTimeFound.setCellValueFactory(            new PropertyValueFactory<>("timeFound"));
        
        foundLuggageTag.setCellValueFactory(           new PropertyValueFactory<>("luggageTag"));
        foundLuggageType.setCellValueFactory(          new PropertyValueFactory<>("luggageType"));
        foundBrand.setCellValueFactory(                new PropertyValueFactory<>("brand"));
        foundMainColor.setCellValueFactory(            new PropertyValueFactory<>("mainColor"));
        foundSecondColor.setCellValueFactory(          new PropertyValueFactory<>("secondColor"));
        foundSize.setCellValueFactory(                 new PropertyValueFactory<>("size"));
        foundWeight.setCellValueFactory(               new PropertyValueFactory<>("weight"));

        foundOtherCharacteristics.setCellValueFactory( new PropertyValueFactory<>("otherCharacteristics"));
        foundPassengerId.setCellValueFactory(          new PropertyValueFactory<>("passengerId"));
        
        foundArrivedWithFlight.setCellValueFactory(    new PropertyValueFactory<>("arrivedWithFlight"));
        foundLocationFound.setCellValueFactory(        new PropertyValueFactory<>("locationFound"));
        foundMatchedId.setCellValueFactory(            new PropertyValueFactory<>("matchedId"));

        //set place holder text when there are no results
        foundLuggageTable.setPlaceholder(new Label("No found luggage's to display"));
    }
    /**  
     * Here will the found luggage table be set with the right data
     * The data (observable< foundluggage>list) comes from the dataListFound
     * 
     * @param list
     * @void - No direct output 
     * @call - set foundLuggageTable             
     */
    @Override
    public void setFoundLuggageTable(ObservableList<FoundLuggage> list) {
         foundLuggageTable.setItems(list);
    }
    
    
    /**  
     * Here is checked of a row in the found luggage table is clicked
     * If this is the case, two functions will be activated. 
     * 
     * @void - No direct output 
     * @call - setDetailsOfRow           set the details of the clicked row
     * @call - setAndOpenPopUpDetails    opens the right more details pop up 
     */
    public void foundRowClicked() {
        foundLuggageTable.setOnMousePressed((MouseEvent event) -> {
                                //--> event         //--> double click
            if (event.isPrimaryButtonDown() && event.getClickCount() == DOUBLE_CLICK) {
                //Make a more details object called foundDetails.
                ServiceMoreDetails foundDetails = new ServiceMoreDetails();
                //Set the detailes of the clicked row and pass a stage and link
                foundDetails.setDetailsOfRow("found", event, POPUP_STAGE_FOUND, 
                            "/Views/Service/ServiceDetailedFoundLuggageView.fxml");
                //Open the more details pop up.
                foundDetails.setAndOpenPopUpDetails(POPUP_STAGE_FOUND, 
                            "/Views/Service/ServiceDetailedFoundLuggageView.fxml", "found");
               
            }
        });
    }


    /*--------------------------------------------------------------------------
                              Switch view buttons
    --------------------------------------------------------------------------*/    
    @FXML
    protected void switchToInput(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/Service/ServiceInputLuggageView.fxml");
    }
    
    @FXML
    protected void switchToMatching(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
    }
 
}