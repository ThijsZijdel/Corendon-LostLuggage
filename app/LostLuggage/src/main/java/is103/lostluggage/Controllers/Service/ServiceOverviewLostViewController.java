package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import is103.lostluggage.Model.Service.Model.LostLuggage;
import is103.lostluggage.Controllers.Admin.OverviewUserController;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.Model.Service.Data.ServiceDataLost;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceMoreDetails;
import is103.lostluggage.Model.Service.Data.ServiceSearchData;
import is103.lostluggage.Model.Service.Interface.LostLuggageTable;
import is103.lostluggage.Model.Service.Interface.Search;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class for the lost luggage overview 
 *
 * @author Thijs Zijdel - 500782165
 */
public class ServiceOverviewLostViewController implements Initializable, LostLuggageTable, Search {
    //page title
    private final String TITLE = "Overview Lost Luggage";
    private final String TITLE_DUTCH = "Overzicht verloren bagage";
    
    //stage for more details when double clicking on a table item
    private final Stage POPUP_STAGE_LOST = new Stage();  
    
    //list for the search results
    private static ObservableList<LostLuggage> lostLuggageListSearchResults 
            = FXCollections.observableArrayList();
    
    //text field for the users search input
    @FXML JFXTextField searchField;
    //combo box for a type/field/column search filter
    @FXML JFXComboBox searchTypeComboBox;
    
    //Object for getting the data
    private ServiceDataLost dataListLost;
    
    //show alos matched luggage state, by default on false
    private boolean showMatchedLuggage = false;
    //resultSet for the items when changing the state of the showMatchedLuggage
    private ResultSet matchedLuggageResultSet;
    
    //click counts
    private final int DOUBLE_CLICK = 2;
    
    /* -----------------------------------------
         TableView lost luggage's colommen
    ----------------------------------------- */
    @FXML private TableView<LostLuggage> lostLuggageTable;

    @FXML private TableColumn<LostLuggage, String>  lostRegistrationNr;
    @FXML private TableColumn<LostLuggage, String>  DateLost;
    @FXML private TableColumn<LostLuggage, String>  TimeLost;
    
    @FXML private TableColumn<LostLuggage, String>  lostLuggageTag;
    @FXML private TableColumn<LostLuggage, String>  lostLuggageType;
    @FXML private TableColumn<LostLuggage, String>  lostBrand;
    @FXML private TableColumn<LostLuggage, String>  lostMainColor;
    @FXML private TableColumn<LostLuggage, String>  lostSecondColor;
    @FXML private TableColumn<LostLuggage, String>  lostSize;
    @FXML private TableColumn<LostLuggage, Integer> lostWeight;
    @FXML private TableColumn<LostLuggage, String>  lostOtherCharacteristics;
    @FXML private TableColumn<LostLuggage, Integer> lostPassengerId;
    
    @FXML private TableColumn<LostLuggage, String>  lostFlight;
    @FXML private TableColumn<LostLuggage, Integer> lostMatchedId;
   
    @FXML private JFXButton button_input, button_match;
    @FXML private JFXToggleButton matchedLuggageToggle;
    
    
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceOverviewLostView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //for setting the resource bundle 
        MainApp.currentView = "/Views/Service/ServiceOverviewLostView.fxml";
        
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

        //try to initialize and set the lost luggage table
        try {
            //Initialize Table & obj lost 
            dataListLost = new ServiceDataLost();
            initializeLostLuggageTable();
            
            //set the table with the right data
            setLostLuggageTable(dataListLost.getLostLuggage());
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOverviewLostViewController.class.getName()).log(Level.SEVERE, null, ex);
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
            //set it to true
            showMatchedLuggage = true;
            
            //asign the right resultset to the matchedLuggageResultSet
            //note: this is based on the boolean status that's set previously
            matchedLuggageResultSet = dataListLost.getLostResultSet(showMatchedLuggage);
        
            //set the table with the right data, the resultset will be converted
            setLostLuggageTable(dataListLost.getObservableList(matchedLuggageResultSet));
        
        //if the state of only matched was already true
        } else if (showMatchedLuggage == true){
            //set the state to false, so only non matched luggage's are shown
            showMatchedLuggage = false;

            //asign the right resultset to the matchedLuggageResultSet
            //note: this is based on the boolean status that's set previously
            matchedLuggageResultSet = dataListLost.getLostResultSet(showMatchedLuggage);
        
            //set the table with the right data, the resultset will be converted
            setLostLuggageTable(dataListLost.getObservableList(matchedLuggageResultSet));
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
     * @call - getLostLuggageSearchList     the result list based on the resultSet
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
        String finalQuery = searchData.getSearchQuery(value, search, "lostluggage");
        
        //clear the previous search result list 
        lostLuggageListSearchResults.clear();
        
        //try to execute the query from the database
        //@throws SQLException  
        try {
            //get the connection to the datbase
            MyJDBC db = MainApp.getDatabase();
            //create a new resultSet and execute the right query
            ResultSet resultSet = db.executeResultSetQuery(finalQuery);
            
            //get the observableList from the search object and asign this to the list
            lostLuggageListSearchResults = ServiceDataLost.loopTroughResultSet(resultSet, showMatchedLuggage);

                    //searchData.getLostLuggageSearchList(resultSet);
                    
                    
            //set this list on the table
            lostLuggageTable.setItems(lostLuggageListSearchResults);  
            
            //set the right place holder message for when there are no hits
            lostLuggageTable.setPlaceholder(new Label("No hits based on your search"));
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOverviewLostViewController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    /**  
     * Here is lost luggage table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    @Override
    public void initializeLostLuggageTable(){
        lostRegistrationNr.setCellValueFactory(       new PropertyValueFactory<>("registrationNr"));
        DateLost.setCellValueFactory(            new PropertyValueFactory<>("dateLost"));
        TimeLost.setCellValueFactory(            new PropertyValueFactory<>("timeLost"));
        
        lostLuggageTag.setCellValueFactory(           new PropertyValueFactory<>("luggageTag"));
        lostLuggageType.setCellValueFactory(          new PropertyValueFactory<>("luggageType"));
        lostBrand.setCellValueFactory(                new PropertyValueFactory<>("brand"));
        lostMainColor.setCellValueFactory(            new PropertyValueFactory<>("mainColor"));
        lostSecondColor.setCellValueFactory(          new PropertyValueFactory<>("secondColor"));
        lostSize.setCellValueFactory(                 new PropertyValueFactory<>("size"));
        lostWeight.setCellValueFactory(               new PropertyValueFactory<>("weight"));

        lostOtherCharacteristics.setCellValueFactory( new PropertyValueFactory<>("otherCharacteristics"));
        lostPassengerId.setCellValueFactory(          new PropertyValueFactory<>("passengerId"));
        
        lostFlight.setCellValueFactory(               new PropertyValueFactory<>("flight"));
        lostMatchedId.setCellValueFactory(            new PropertyValueFactory<>("matchedId"));
         
        //set place holder text when there are no results
        lostLuggageTable.setPlaceholder(new Label("No lost luggage's to display"));
    }
    /**  
     * Here will the lost luggage table be set with the right data
     * The data (observable< lostluggage>list) comes from the dataListLost
     * 
     * @param list
     * @void - No direct output 
     * @call - set lostLuggageTable             
     */
    @Override
    public void setLostLuggageTable(ObservableList<LostLuggage> list){
        lostLuggageTable.setItems(list);  
    }
    
    
    
    /**  
     * Here is checked of a row in the lost luggage table is clicked
     * If this is the case, two functions will be activated. 
     * 
     * @void - No direct output 
     * @call - setDetailsOfRow           set the details of the clicked row
     * @call - setAndOpenPopUpDetails    opens the right more details pop up 
     */
    public void lostRowClicked() {
        lostLuggageTable.setOnMousePressed((MouseEvent event) -> {
                                //--> event         //--> double click
            if (event.isPrimaryButtonDown() && event.getClickCount() == DOUBLE_CLICK) {
                //Make a more details object called lostDetails.
                ServiceMoreDetails lostDetails = new ServiceMoreDetails();
                //Set the detailes of the clicked row and pass a stage and link
                lostDetails.setDetailsOfRow("lost", event, POPUP_STAGE_LOST, 
                            "/Views/Service/ServiceDetailedLostLuggageView.fxml");
                //Open the more details pop up.
                lostDetails.setAndOpenPopUpDetails(POPUP_STAGE_LOST, 
                            "/Views/Service/ServiceDetailedLostLuggageView.fxml", "lost");
                
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