package is103.lostluggage.Controllers.Manager;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.Admin.OverviewUserController;

import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Model.LostLuggage;

import java.io.IOException;
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
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import is103.lostluggage.Model.Service.Data.ServiceDataLost;
import is103.lostluggage.Model.Service.Data.ServiceGetDataFromDB;
import is103.lostluggage.Model.Service.Data.ServiceSearchData;
import is103.lostluggage.Model.Service.Interface.LostLuggageTable;
import is103.lostluggage.Model.Service.Interface.Search;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Ahmet
 * @author Thijs Zijdel - 500782165             
 */
public class ManagerLostViewController implements Initializable, LostLuggageTable, Search {
    //view title
    private final String TITLE = "Overview Lost Luggage";
    private final String TITLE_DUTCH = "Overzicht verloren bagage";
    
    //list of luggages for the lostTable
    public static ObservableList<LostLuggage> lostLuggageList;
    
    //double click
    private final int DOUBLE_CLICK = 2;
    
    
    //table view for the data
    @FXML private TableView<LostLuggage> lostTable;
    //TableView found luggage's colommen
    @FXML private TableColumn<LostLuggage, String>  managerLostRegistrationNr;
    @FXML private TableColumn<LostLuggage, String>  managerLostDateLost;
    @FXML private TableColumn<LostLuggage, String>  managerLostTimeLost;
    
    @FXML private TableColumn<LostLuggage, String>  managerLostLuggageTag;
    @FXML private TableColumn<LostLuggage, String>  managerLostLuggageType;
    @FXML private TableColumn<LostLuggage, String>  managerLostBrand;
    @FXML private TableColumn<LostLuggage, Integer> managerLostMainColor;
    @FXML private TableColumn<LostLuggage, String>  managerLostSecondColor;
    @FXML private TableColumn<LostLuggage, Integer> managerLostSize;
    @FXML private TableColumn<LostLuggage, String>  managerLostWeight;
    @FXML private TableColumn<LostLuggage, String>  managerLostOtherCharacteristics;
    @FXML private TableColumn<LostLuggage, Integer> managerLostPassengerId;
    
    @FXML private TableColumn<LostLuggage, String> managerLostFlight;
    
    @FXML private TableColumn<LostLuggage, String> managerLostEmployeeId;
    @FXML private TableColumn<LostLuggage, Integer> managerLostMatchedId;


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
    
    
    //date filter (from - to)
    @FXML JFXDatePicker fromDate;
    @FXML JFXDatePicker toDate;
     
    //display labels
    //results based on the filters set/ search and the total count of lost luggages
    @FXML private Label results;
    @FXML private Label total;    
    
    /**
     * @author Thijs Zijdel - 500782165
     * 
     * Initializing the controller class for the manager lost view 
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //To Previous Scene
        MainViewController.previousView = "/Views/ManagerHomeView.fxml";
        
        //set view title
         try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(TITLE_DUTCH);
            } else {
                MainViewController.getInstance().getTitle(TITLE);
            }
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        //initialize the filter (for columns/fields) combo box with data
        initializeComboFilterBox(); 
   
        //initialize the table so it can be set
        initializeLostLuggageTable();
        
        //try to set the lost luggage table with data
        try {
            //Initialize Table & obj lost
            dataListLost = new ServiceDataLost();
            setLostLuggageTable(dataListLost.getLostLuggage());
            
            //initialize the total amount of luggage s in the right display
            initializeTotalCountDisplay();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerLostViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //initialize the click listner for double clicking the table
        initializeOnLostRowDoubleClicked();
        
        //initialize the total amount results in the right display
        setResultCount();
        
    }
    
    
    
    /**  
     * @author Thijs Zijdel - 500782165
     * 
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
            
            //Reset the date picker since it is not configured with the only matched
            resetDatePickerFilter();
            
            //set result count in the display label
            setResultCount();
        
        //if the state of only matched was already true
        } else if (showMatchedLuggage == true){
            //set the state to false, so only non matched luggage's are shown
            showMatchedLuggage = false;

            //asign the right resultset to the matchedLuggageResultSet
            //note: this is based on the boolean status that's set previously
            matchedLuggageResultSet = dataListLost.getLostResultSet(showMatchedLuggage);
        
            //set the table with the right data, the resultset will be converted
            setLostLuggageTable(dataListLost.getObservableList(matchedLuggageResultSet));
            
            //set result count in the display label
            setResultCount();
        }  
    }
    /**  
     * @author Thijs Zijdel - 500782165
     * 
     * Here is lost luggage table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    @Override
    public void initializeLostLuggageTable(){
        managerLostRegistrationNr.setCellValueFactory(      new PropertyValueFactory<>("registrationNr"));
        managerLostDateLost.setCellValueFactory(            new PropertyValueFactory<>("dateLost"));
        managerLostTimeLost.setCellValueFactory(            new PropertyValueFactory<>("timeLost"));
        
        managerLostLuggageTag.setCellValueFactory(          new PropertyValueFactory<>("luggageTag"));
        managerLostLuggageType.setCellValueFactory(         new PropertyValueFactory<>("luggageType"));
        managerLostBrand.setCellValueFactory(               new PropertyValueFactory<>("brand"));
        managerLostMainColor.setCellValueFactory(           new PropertyValueFactory<>("mainColor"));
        managerLostSecondColor.setCellValueFactory(         new PropertyValueFactory<>("secondColor"));
        managerLostSize.setCellValueFactory(                new PropertyValueFactory<>("size"));
        managerLostWeight.setCellValueFactory(              new PropertyValueFactory<>("weight"));

        managerLostOtherCharacteristics.setCellValueFactory(new PropertyValueFactory<>("otherCharacteristics"));
        managerLostPassengerId.setCellValueFactory(         new PropertyValueFactory<>("passengerId"));
        
        managerLostFlight.setCellValueFactory(              new PropertyValueFactory<>("flight"));
        
        managerLostEmployeeId.setCellValueFactory(          new PropertyValueFactory<>("employeeId"));
        managerLostMatchedId.setCellValueFactory(           new PropertyValueFactory<>("matchedId"));
         
        //set place holder text when there are no results
        lostTable.setPlaceholder(new Label("No lost luggage's to display"));
    }
    
    /**  
     * @author Thijs Zijdel - 500782165
     * 
     * Here will the lost luggage table be set with the right data
     * The data (observable< lostluggage>list) comes from the dataListLost
     * 
     * @param list
     * @void - No direct output 
     * @call - set lostLuggageTable             
     */
    @Override
    public void setLostLuggageTable(ObservableList<LostLuggage> list){
        lostTable.setItems(list);  
    }
    
     
    
     /**  
     * @author Thijs Zijdel - 500782165
     * 
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
     * @author Thijs Zijdel - 500782165
     * 
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
        
        //get the final query without the last semicolin to extend it
        finalQuery = finalQuery.substring(0,finalQuery.lastIndexOf(";"));
        
        //check if there is a filter based on FROM date.
        if(fromDate.getValue() != null && !fromDate.getValue().toString().isEmpty()){
            //If the final qeury doesn't have any statments, add WHERE 
            if (!finalQuery.contains("WHERE")) {
                finalQuery += " WHERE ";
            } else {
                //If the final qeury already contains statments, add a AND 
                finalQuery += " AND ";
            }
            
            //add the filter from TO the query
            finalQuery += " L.dateLost > '"+fromDate.getValue().toString()+"' ";
        }
        
        //check if there is a filter based on TO date.
        if(toDate.getValue() != null && !toDate.getValue().toString().isEmpty()){
            //If the final qeury doesn't have any statments, add WHERE 
            if (!finalQuery.contains("WHERE")) {
                finalQuery += " WHERE ";
            } else {
                //If the final qeury already contains statments, add a AND 
                finalQuery += " AND ";
            }
            //als er maar een where en geen or bevat + and 
            //if (finalQuery.contains("WHERE") && )
            
            //add the filter from FROM the query
            finalQuery += " L.dateLost < '"+toDate.getValue().toString()+"' ";
        }        
        //close the query     
        finalQuery += ";";
        
        //clear the previous search result list 
        lostLuggageListSearchResults.clear();
        
        //try to execute the query from the database
        //@throws SQLException  
        try {
            //get the connection to the datbasec
            MyJDBC db = MainApp.getDatabase();
            //create a new resultSet and execute tche right query
            ResultSet resultSet = db.executeResultSetQuery(finalQuery);
            
            //get the observableList from the search object and asign this to the list
            lostLuggageListSearchResults =  ServiceDataLost.loopTroughResultSet(resultSet, showMatchedLuggage);

            //set this list on the table
            lostTable.setItems(lostLuggageListSearchResults);
            
            //set the right place holder message for when there are no hits
            lostTable.setPlaceholder(new Label("No hits based on your search"));
            
            //set result count in the display label
            setResultCount();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerLostViewController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

        
    /**  
     * @author Thijs Zijdel - 500782165
     * 
     * This method is for setting the total amount of lost luggage s in the db
     * This data will be gotten from a count query in the ServiceGetDataFromDB class
     * 
     * Note: this method is called when initializing the controller 
     * 
     */
    private void initializeTotalCountDisplay() throws SQLException {
        //create a ServiceGetDataFromDB object with the right fields
        //table: lostluggage   field: * (=all)  where: null (no statement)
        ServiceGetDataFromDB totalCount = new ServiceGetDataFromDB("lostluggage", "*", null);
        
        //set the total of hits in the total (label) display
        total.setText(Integer.toString(totalCount.countHits()));         
    }

    /**  
     * @author Thijs Zijdel - 500782165
     * 
     * This method is for setting the amount of results in the display
     * Note: this method is called when initializing the controller and searching
     * 
     */
    private void setResultCount() {
        //get the amount of items in the lost luggage table
        String hits = Integer.toString(lostTable.getItems().size());
        
        //if the amount of hits is zero, set it to 0 (to display)
        if (hits == null || "".equals(hits) || lostTable.getItems().isEmpty()){
            hits = "0";
        }
        
        //set the amount of hits in the results (label) display.
        results.setText(hits);
    }
    
    /**  
     * 
     * 
     * @author Ahmet Aksu
     * 
     * 
     */
    private void initializeOnLostRowDoubleClicked() {
        //method made by Ahmet Aksu, for making the lostTable double click'able
        lostTable.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == DOUBLE_CLICK) {
                Node node = ((Node) event.getTarget()).getParent();
                
                TableRow row;
                
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                System.out.println(row.getItem());
                
                try {
                    
                    MainApp.switchView("/Views/ManagerPassengerInfoView.fxml");
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(ManagerLostViewController.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
                
            }
        });
    }
    
    /**
     * This method is for clearing (resetting) the date picker filters
     * Note; i didn't had enough time to also configure this with showing only the matched luggage
     */
    private void resetDatePickerFilter() {
        toDate.setValue(null);
        fromDate.setValue(null);
    }
    
}