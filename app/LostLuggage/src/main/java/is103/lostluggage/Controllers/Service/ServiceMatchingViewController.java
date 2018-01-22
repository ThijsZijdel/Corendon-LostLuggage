package is103.lostluggage.Controllers.Service;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Model.Service.Data.ServiceDataFound;
import is103.lostluggage.Model.Service.Data.ServiceDataLost;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceDataMatch;
import is103.lostluggage.Model.Service.Data.ServiceMoreDetails;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import is103.lostluggage.Model.Service.Instance.Matching.FoundLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Instance.Matching.LostLuggageManualMatchingInstance;
import is103.lostluggage.Model.Service.Interface.FoundLuggageTable;
import is103.lostluggage.Model.Service.Interface.LostLuggageTable;
import is103.lostluggage.Model.Service.Model.MatchLuggage;
import is103.lostluggage.Model.Service.Model.LostLuggage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * FXML Controller class for the matching view
 * 
 * @author Thijs Zijdel - 500782165
 */
public class ServiceMatchingViewController implements Initializable, FoundLuggageTable, LostLuggageTable {
    //main match data object
    public ServiceDataMatch data = ServiceHomeViewController.getMATCH_DATA();
     
    //view title
    private final String TITLE = "Matching";
    private final String TITLE_DUTCH = "Overeenkomend";
    
    //popup stage
    private final Stage POPUP_STAGE_FOUND = new Stage();   
    private final Stage POPUP_STAGE_LOST = new Stage(); 
    private final Stage POPUP_STAGE_MATCH = new Stage(); 
    
    //refresh rate                           
    private static final long REFRESH_TIME = 1; //s
    
    //setup for manual matching -> and stopping the loop
    private final int RESET_VALUE = 9999; //random value (!= registrationNr) 
    private int idCheckFound = RESET_VALUE;
    private int idFound      = RESET_VALUE;
    
    private int idCheckLost = RESET_VALUE;
    private int idLost      = RESET_VALUE;
    
    //luggage's lists
//    private  static ObservableList<FoundLuggage> foundLuggageList;
//    private static ObservableList<LostLuggage> lostLuggageList;
    
    //potential matches list
    private ObservableList<MatchLuggage> potentialList  
            = FXCollections.observableArrayList(); 
    
    //Matching tabs 
    @FXML private TabPane matchingTabs;
    public static final int AUTO_MATCHING_TAB_INDEX = 0;
    public static final int MANUAL_MATCHING_TAB_INDEX = 1;
    public static final int POTENTIAL_MATCHING_TAB_INDEX = 2;

    //Pannels for manual matching
    @FXML private Pane foundPane;
    @FXML private Pane lostPane;
    
    //click counts
    private final int DOUBLE_CLICK = 2;
    
    //for displaying a match
    private final int MINIMUM_AUTOMATIC_MATCH_PERCENTAGE = 10;
    
    //--------------------------------
    //    Table Found initializen
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
    @FXML private TableColumn<FoundLuggage, Integer> foundPassengerId;
    
    @FXML private TableColumn<FoundLuggage, String>  foundArrivedWithFlight;
    @FXML private TableColumn<FoundLuggage, Integer> foundLocationFound;
    

    
    
    //--------------------------------
    //    Table Lost initializen
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
    @FXML private TableColumn<LostLuggage, Integer> lostPassengerId;
    
    @FXML private TableColumn<LostLuggage, String>  lostFlight;
    
    
    
    
    //--------------------------------
    //    Table Matches initializen
    @FXML private TableView<MatchLuggage> matchTabbleView;

    @FXML private TableColumn<MatchLuggage, String>  matchIdLost;
    @FXML private TableColumn<MatchLuggage, String>  matchIdFound;
    @FXML private TableColumn<MatchLuggage, String>  matchTag;
    
    @FXML private TableColumn<MatchLuggage, String>  matchPercentage;
    @FXML private TableColumn<MatchLuggage, String>  matchType;
    @FXML private TableColumn<MatchLuggage, String>  matchBrand;
    @FXML private TableColumn<MatchLuggage, String>  matchMainColor;
    @FXML private TableColumn<MatchLuggage, String>  matchSecondColor;
    @FXML private TableColumn<MatchLuggage, String>  matchSize;
    @FXML private TableColumn<MatchLuggage, String>  matchWeight;
    @FXML private TableColumn<MatchLuggage, Integer> matchId;

    
    
 
    //--------------------------------
    //    Table potential initializen
    @FXML public TableView<MatchLuggage> potentialMatchingTable;

    @FXML private TableColumn<MatchLuggage, String>  potentialIdLost;
    @FXML private TableColumn<MatchLuggage, String>  potentialIdFound;
    @FXML private TableColumn<MatchLuggage, String>  potentialTag;
    
    @FXML private TableColumn<MatchLuggage, String>  potentialPercentage;
    @FXML private TableColumn<MatchLuggage, String>  potentialType;
    @FXML private TableColumn<MatchLuggage, String>  potentialBrand;
    @FXML private TableColumn<MatchLuggage, String>  potentialMainColor;
    @FXML private TableColumn<MatchLuggage, String>  potentialSecondColor;
    @FXML private TableColumn<MatchLuggage, String>  potentialSize;
    @FXML private TableColumn<MatchLuggage, String>  potentialWeight;
    
    //Create one instance of this class
    public static ServiceMatchingViewController instance = null;
    
    //Get instance
    public static ServiceMatchingViewController getInstance() {
        //check if the instance already is setted
        if (instance == null) {
            synchronized(ServiceMatchingViewController.class) {
                if (instance == null) {
                    instance = new ServiceMatchingViewController();
                }
            }
        }
        return instance;
    }
    
    /*--------------------------------------------------------------------------
                  Initialize   Service Matching View Controller
    --------------------------------------------------------------------------*/
    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceMatchingView.FXML view.
     * 
     * The following setups will be started:
     * - Set the back button 
     * - Set the title of the view
     * - Make a instance
     * - Setup a refresh timer and start this
     * - Make a Lost and Found data object
     * - Initialize the different tables
     * - Set the tables with the right data
     * - Stop the tables from being moved
     * //Reset manual matching
     * - Set the on matching view to true
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //for setting the resource bundle 
        MainApp.currentView = "/Views/Service/ServiceMatchingView.fxml";
         
        //switch to prev view.
        MainViewController.previousView = "/Views/Service/ServiceHomeView.fxml";
        
        //try to set the titel above the view
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
        
        //initialize instance
        instance = this;
        
        //Refresh timer setup
        Timeline refreshTimeLine = new Timeline(new KeyFrame(Duration.seconds(REFRESH_TIME), ev -> {
            //methode to call all timer required methods.
            callMethods();
        })); 
        refreshTimeLine.setCycleCount(Animation.INDEFINITE);    //cycle count-> no end
        refreshTimeLine.play();                                 //start timeline
        
        

        ServiceDataLost dataListLost;
        ServiceDataFound dataListFound;
        try {
            //Initialize PotentialMatchingTable();
            initializePotentialLuggageTable();
            
            //Initialize Table & obj lost
            dataListLost = new ServiceDataLost();
            initializeLostLuggageTable();
            setLostLuggageTable(dataListLost.getLostLuggage());
        
            //Initialize Table & obj found 
            dataListFound = new ServiceDataFound();
            initializeFoundLuggageTable();
            setFoundLuggageTable(dataListFound.getFoundLuggage());
            
            //Initialize matching table 
            //  & set with dataListFound and dataListLost
            initializeMatchingLuggageTable();
            setMatchingLuggageTable(dataListFound,dataListLost);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceMatchingViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }


        //Stop Tables from being able to re position/ order
        //Placed them in an array for using same methode for each table
        TableView[] fixingTables = {
            foundLuggageTable, 
            lostLuggageTable, 
            matchTabbleView, 
            potentialMatchingTable
        };
        for (TableView fixingTable : fixingTables) { fixedTableHeader(fixingTable);}
        
        //reset the manual matching
        resetManualMatching();
        
        //setOnMatchingView status == current view
        ServiceHomeViewController.setOnMatchingView(true);
        //set reset status to true for clearing the list
        ServiceHomeViewController.setPotentialResetStatus(true);
  
    } 
    
    /*--------------------------------------------------------------------------
                                Methods 
    --------------------------------------------------------------------------*/
    
    /**  
     * Here will be checked what the value is of the reset status
     * If the status equals true, than de manual matching instances will be cleared
     * This way will the manual matching fields in this view empty
     * 
     * @void - No direct output 
     */
    public void resetManualMatching(){
        if (ServiceHomeViewController.resetMatching == true){
        FoundLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(null);
        LostLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(null);
        }
    }
    
    /**  
     * Here are all the functions that are called by the refresh timer
     * Note: this will happen on an interval of the: REFRESH_TIME
     * 
     * @void - No direct output 
     * @call - setpotentialMatchingTable    refresh this table by setting it. 
     * @call - addToManualFound & Lost      refresh the manual screens by adding
     */
    public void callMethods(){
        try {
            setPotentialMatchingTable();
        } catch (SQLException ex) {
            Logger.getLogger(ServiceMatchingViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        //Methodes calling at rate of --> int:  timeRate   //2s
        addToManualFound();
        addToManualLost();
    }
    
    /**  
     * Here is the matching tabs set to the right index
     * The index of the tabs are as following:
     *      0:   automatic matching tab 
     *      1:   manual matching tab
     *      2:   potential matching tab
     * 
     * @param tab  index of the wanted matching tab
     * @void - No direct output 
     */
    public void setMatchingTab(int tab){
        //check the index (tab) given
        if (tab > 3 || tab < 0){
            tab = AUTO_MATCHING_TAB_INDEX; //set tab to automatich matching
        }
        //get selection of matching tabs
        SingleSelectionModel<Tab> matchingSelectionTabs = matchingTabs.getSelectionModel(); 
        
        //set the right tab
        matchingSelectionTabs.select(tab); 
    }
    
    /*--------------------------------------------------------------------------
                     Initialize and setting all the tables
    --------------------------------------------------------------------------*/
    /**  
     * Here is lost luggage table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    @Override
    public void initializeLostLuggageTable(){
        lostRegistrationNr.setCellValueFactory(      new PropertyValueFactory<>("registrationNr"));
        DateLost.setCellValueFactory(            new PropertyValueFactory<>("dateLost"));  //-> lost
        TimeLost.setCellValueFactory(            new PropertyValueFactory<>("timeLost"));
        
        lostLuggageTag.setCellValueFactory(           new PropertyValueFactory<>("luggageTag"));
        lostLuggageType.setCellValueFactory(          new PropertyValueFactory<>("luggageType"));
        lostBrand.setCellValueFactory(                new PropertyValueFactory<>("brand"));
        lostMainColor.setCellValueFactory(            new PropertyValueFactory<>("mainColor"));
        lostSecondColor.setCellValueFactory(          new PropertyValueFactory<>("secondColor"));
        lostSize.setCellValueFactory(                 new PropertyValueFactory<>("size"));
        lostWeight.setCellValueFactory(               new PropertyValueFactory<>("weight"));

        lostPassengerId.setCellValueFactory(          new PropertyValueFactory<>("passengerId"));
        
        lostFlight.setCellValueFactory(               new PropertyValueFactory<>("flight"));
        
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
     * Here is found luggage table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    @Override
    public void initializeFoundLuggageTable(){
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

        foundPassengerId.setCellValueFactory(          new PropertyValueFactory<>("passengerId"));
        
        foundArrivedWithFlight.setCellValueFactory(    new PropertyValueFactory<>("arrivedWithFlight"));
        foundLocationFound.setCellValueFactory(        new PropertyValueFactory<>("locationFound"));
        
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
     * Here is automatic matching table overview initialized with the right values
     * Also the matching tabs will be set on the right tab
     * 
     * @void - No direct output 
     */
    public void initializeMatchingLuggageTable(){
        matchIdLost.setCellValueFactory(               new PropertyValueFactory<>("registrationNrLost"));
        matchIdFound.setCellValueFactory(              new PropertyValueFactory<>("registrationNrFound"));
        matchTag.setCellValueFactory(                  new PropertyValueFactory<>("luggageTag"));
        
        matchPercentage.setCellValueFactory(           new PropertyValueFactory<>("matchPercentage"));
        matchType.setCellValueFactory(                 new PropertyValueFactory<>("luggageType"));
        matchBrand.setCellValueFactory(                new PropertyValueFactory<>("brand"));
        matchMainColor.setCellValueFactory(            new PropertyValueFactory<>("mainColor"));
        matchSecondColor.setCellValueFactory(          new PropertyValueFactory<>("secondColor"));
        matchSize.setCellValueFactory(                 new PropertyValueFactory<>("size"));
        matchWeight.setCellValueFactory(               new PropertyValueFactory<>("weight"));

        matchId.setCellValueFactory(                   new PropertyValueFactory<>("matchedId"));
        
        //set place holder text when there are no results
        matchTabbleView.setPlaceholder(new Label("No automatic matches found"));
        
        //sort on match percentage
        matchTabbleView.getSortOrder().add(matchPercentage);
        
        //set right matching 
        setMatchingTab(0);
    }
    
    /**  
     * Here will the (automatic) matching luggage table be set with the right data
     * The data (observable< matchluggage>list) comes from the dataListMatch
     * 
     * @param dataListFound
     * @param datalistDataLost
     * @throws java.sql.SQLException
     * @void - No direct output 
     * @call - set matching table              
     */
    public void setMatchingLuggageTable(
                                ServiceDataFound dataListFound, 
                                ServiceDataLost datalistDataLost) 
                                                throws SQLException{
        
        ServiceDataMatch matchData = new ServiceDataMatch();
        matchTabbleView.setItems(
            matchData.autoMatching(
                dataListFound.getFoundLuggage(), 
                datalistDataLost.getLostLuggage(), 
                MINIMUM_AUTOMATIC_MATCH_PERCENTAGE)
            ); 
    }
    
    
    
    
    /*--------------------------------------------------------------------------
                          Double click functionality 
    --------------------------------------------------------------------------*/
    
    
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
    
    /**  
     * Here is checked of a row in the automatic match or potential luggage table is clicked
     * If this is the case, two functions will be activated. 
     * 
     * @void - No direct output 
     * @call - setDetailsOfRow           set the details of the clicked row
     * @call - setAndOpenPopUpDetails    opens the right more details pop up 
     */
    public void matchRowClicked()  {
        //Automatic matching table
        matchTabbleView.setOnMousePressed((MouseEvent event) -> {
                                //--> event         //--> double click
            if (event.isPrimaryButtonDown() && event.getClickCount() == DOUBLE_CLICK) {  
                //Make a more details object called matchDetails.
                // note: this is a match row so a lost and found id will be setted
                ServiceMoreDetails matchDetails = new ServiceMoreDetails();
                //Set the detailes of the clicked row and pass a stage and link
                matchDetails.setDetailsOfRow("match", event, POPUP_STAGE_MATCH, 
                            "/Views/Service/ServiceDetailedMatchLuggageView.fxml");
                //Open the more details pop up.
                matchDetails.setAndOpenPopUpDetails(POPUP_STAGE_MATCH, 
                            "/Views/Service/ServiceDetailedMatchLuggageView.fxml", "match");
                  
            }
        });
        //Potential matching table
        potentialMatchingTable.setOnMousePressed((MouseEvent event) -> {
                                //--> event         //--> double click
            if (event.isPrimaryButtonDown() && event.getClickCount() == DOUBLE_CLICK) {  
                //Make a more details object called matchDetails.
                // note: this is a match row so a lost and found id will be setted
                ServiceMoreDetails matchDetails = new ServiceMoreDetails();
                //Set the detailes of the clicked row and pass a stage and link
                matchDetails.setDetailsOfRow("match", event, POPUP_STAGE_MATCH, 
                            "/Views/Service/ServiceDetailedMatchLuggageView.fxml");
                matchDetails.setAndOpenPopUpDetails(POPUP_STAGE_MATCH, 
                            "/Views/Service/ServiceDetailedMatchLuggageView.fxml", "match");
                  
            }
        });
    }

    
    
    /*--------------------------------------------------------------------------
                      Manual matching related method s (add, remove)
    --------------------------------------------------------------------------*/
    /**  
     * When the addToManualFound button is clicked a function is called
     * There will also a id be set
     * 
     * @void - No direct output 
     * @set  - idManualMatching     get instance of found luggage manual matching
     * @call - addToManualMatching  return this to idCheckFound
     */
    public void addToManualFound() {
        //id manual matching wil be setted from the FoundLuggageManualMatching Instance
        String idManualMatching = FoundLuggageManualMatchingInstance.getInstance().currentLuggage().getRegistrationNr();
        //idCheckFound will be setted trough addToManualMatching
        //Note: This id will be checked in this function. 
        idCheckFound = addToManualMatching(
                foundPane,                  //Found pane
                MANUAL_MATCHING_TAB_INDEX,        //Tab index of manual matching
                idCheckFound,               //The id that will be checked
                idFound,                    //The id that will be changed
                idManualMatching,           //The id of the instance 
                "/Views/Service/ServiceManualMatchingFoundView.fxml"
        );
    
    }
    
    
    /**  
     * When the addToManualLost button is clicked a function is called
     * There will also a id be set
     * 
     * @void - No direct output 
     * @set  - idManualMatching     get instance of lost luggage manual matching
     * @call - addToManualMatching  return this to idCheckLost
     */
    public void addToManualLost() {
        //id manual matching wil be setted from the LostLuggageManualMatching Instance
        String idManualMatching = LostLuggageManualMatchingInstance.getInstance().currentLuggage().getRegistrationNr();
        //idCheckLost will be setted trough addToManualMatching
        //Note: This id will be checked in this function. 
        idCheckLost = addToManualMatching(
                lostPane, 
                MANUAL_MATCHING_TAB_INDEX, 
                idCheckLost, 
                idLost, 
                idManualMatching, 
                "/Views/Service/ServiceManualMatchingLostView.fxml"
        );
    } 
    
    
    
    /**  
     * When the remove from manual matching  (found) button is clicked:
     * The instance will be set on null
     * The idCheckFound will be reset
     * The found (manual matching) pane will be cleared
     * 
     * @void - No direct output 
     */
    public void removeManualFound() {
        //FoundLuggageManualMatchingInstance instance wille be cleared
        FoundLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(null);
        //check id will be resetted so the add to manual matching loop stops
        idCheckFound = RESET_VALUE;
        //manual matching found pane will be cleared
        foundPane.getChildren().clear();
    }
    
    
    /**  
     * When the remove from manual matching  (lost) button is clicked:
     * The instance will be set on null
     * The idCheckLostwill be reset
     * The lost (manual matching) pane will be cleared
     * 
     * @void - No direct output 
     */
    public void removeManualLost() {
        //LostLuggageManualMatchingInstance instance wille be cleared
        LostLuggageManualMatchingInstance.getInstance().currentLuggage().setRegistrationNr(null);
        //check id will be resetted so the add to manual matching loop stops
        idCheckLost = RESET_VALUE;
        //manual matching lost pane will be cleared
        lostPane.getChildren().clear();
        
    } 
   
    
    /**  
     * Methode for setting the right data in the right place for:
     *                     MANUAL MATCHING
     * 
     * -(Checks used for not resetting the views)
     * @param   paneType = id of pane were the view needs to be loaden
     * @param   tab      = tab that needs to be setted.
     * @param   idCheck  = standard value that in the loop gets checked & returned
     * @param   idObj    = id thats been set (from id) in the loop for checking 
     * @param   idDataObj= String id of the added luggage -> used for checking 
     * @param   viewLink = link of fxml that needs to be set in the @paneType
     * @return  idCheck to stop the loop from going to far 
     */
    public int addToManualMatching(Pane paneType, int tab, int idCheck, int idObj, 
                                                String idDataObj, String viewLink){
        //if found luggage added to manual matching-> asign: iD found to this.id
        if (idDataObj != null) {
            idObj = Integer.parseInt(""+idDataObj+"");
        }
        
        if (idDataObj == null) {
            //No found luggage added to manual matching
        } else {
            //if id of luggage added to manual matching is null
            //check if this id is not the same id
            if (idCheck != idObj) {
                //if this is true, than asaign idcheckfound to this 
                //(so the loop is stoped next time)
                idCheck = idObj;
                
                //now try to load the manualMatchingFoundView in the right (found) pane
                try {
                    //get the right source for MaualFoundView.FXML
                    Pane SourceLink = FXMLLoader.load(getClass()
                          .getResource(viewLink));
                    
                    //clear the found pane
                    paneType.getChildren().clear();
                    
                    //Before asigning -> set matching tab to right page
                    setMatchingTab(1);   
                    
                    //Asign the source to the right pane: foundPane
                    paneType.getChildren().add(SourceLink);
                    return idCheck;
                    
                } catch (IOException ex) {
                    Logger.getLogger(ServiceMatchingViewController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            } else {
                //id is the same
                return idCheck;
            }
            return idCheck;
        }  
        return idCheck;
    }
    
    
    
    
    /*--------------------------------------------------------------------------
                 Potential matching table functions & initializing 
    --------------------------------------------------------------------------*/
    /**  
     * Here is potential matching table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    public void initializePotentialLuggageTable(){
        potentialIdLost.setCellValueFactory(               new PropertyValueFactory<>("registrationNrLost"));
        potentialIdFound.setCellValueFactory(              new PropertyValueFactory<>("registrationNrFound"));
        potentialTag.setCellValueFactory(                  new PropertyValueFactory<>("luggageTag"));
        
        potentialPercentage.setCellValueFactory(           new PropertyValueFactory<>("matchPercentage"));
        potentialType.setCellValueFactory(                 new PropertyValueFactory<>("luggageType"));
        potentialBrand.setCellValueFactory(                new PropertyValueFactory<>("brand"));
        potentialMainColor.setCellValueFactory(            new PropertyValueFactory<>("mainColor"));
        potentialSecondColor.setCellValueFactory(          new PropertyValueFactory<>("secondColor"));
        potentialSize.setCellValueFactory(                 new PropertyValueFactory<>("size"));
        potentialWeight.setCellValueFactory(               new PropertyValueFactory<>("weight"));
        
        //set place holder text when there are no results
        potentialMatchingTable.setPlaceholder(new Label("No potential matches found"));
    }
    
    /**  
     * Here will the potential matching luggage table be set with the right data
     * The data (observable< matchluggage>list) comes from the potentialList
     * 
     * @throws java.sql.SQLException
     * @void - No direct output 
     * @call - getPotentialMatchesList
     * @call - set potential matching table              
     */
    public void setPotentialMatchingTable() throws SQLException{
        //if new potential list is not the same
        if (data.getPotentialMatchesList() != potentialList){
            //if the potential list is not empty- clear it.
            if (!potentialList.isEmpty()){
                potentialList.clear();
            }
            //get the new potential list
            potentialList = data.getPotentialMatchesList();
            
            //set the potential matching table with the right list 
            potentialMatchingTable.setItems( potentialList); 
        }
            
        //if the potential reset status is true, reset the table
        if (ServiceHomeViewController.getPotentialResetStatus()){
            //function that cleares the potential matching table 
            
            //System.out.println("called .    iffff ");
            resetPotentialMatchingTable();
        }
    }
    /**
     * Here will the potential matching luggage table be reset
     * 
     * @void - No direct output next to clearing the table          
     */
    public void resetPotentialMatchingTable() {
        //System.out.println("reset     status:"+ServiceHomeViewController.getPotentialResetStatus());
            
        ServiceHomeViewController.setPotentialResetStatus(false);
        
    }
    
    /**
     * Confirming the manual matches.
     * Note: the instances and panes will be checked if they are not empty
     * 
     * @param event                 on click of the confirm match button
     * @throws java.io.IOException
     * @void No direct output       only switching the view        
     */
    @FXML
    protected void confirmMatch(ActionEvent event) throws IOException {
        //check the panes != empty
        if (FoundLuggageManualMatchingInstance.getInstance().currentLuggage().getRegistrationNr()!=null &
            LostLuggageManualMatchingInstance.getInstance().currentLuggage().getRegistrationNr() !=null ) {
            
            MainApp.switchView("/Views/Service/ServiceConfirmedMatchLuggageView.fxml");
        }
    }

    /*--------------------------------------------------------------------------
                              Switch view buttons
    --------------------------------------------------------------------------*/
    @FXML
    protected void switchToInput(ActionEvent event) throws IOException {
        MainViewController.previousView = "/Views/Service/ServiceMatchingView.fxml";
        MainApp.switchView("/Views/Service/ServiceInputView.fxml");
    }
    @FXML
    protected void switchToFoundOverview(ActionEvent event) throws IOException {
        MainViewController.previousView = "/Views/Service/ServiceMatchingView.fxml";
        MainApp.switchView("/Views/Service/ServiceOverviewFoundView.fxml");
    }
    @FXML
    protected void switchToLostOverview(ActionEvent event) throws IOException {
        MainViewController.previousView = "/Views/Service/ServiceMatchingView.fxml";
        MainApp.switchView("/Views/Service/ServiceOverviewLostView.fxml");
    }

   
    
    // Solution by: Alexander Chingarev
    // for stopping the fx tables to resize and move collumns 
    //
    //link:
    // https://stackoverflow.com/questions/22202782/how-to-prevent-tableview-from-doing-tablecolumn-re-order-in-javafx-8?answertab=active#tab-top
    public void fixedTableHeader(TableView table){
        table.widthProperty().addListener((ObservableValue
                <? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue
                    <? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });
    }
    
    
}
