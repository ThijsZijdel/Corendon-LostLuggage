package is103.lostluggage.Controllers.Service;

import com.jfoenix.controls.JFXButton;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Data.ServiceDataMatch;
import is103.lostluggage.Model.settings;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Thijs Zijdel - 500782165
 */
public class ServiceHomeViewController implements Initializable {

    //set the onMatchingView to false, the current page isn't the matching view
    public static boolean onMatchingView = false;
    
    //set the potentialMatchesRest to false, it isn't needed to reset now
    private static boolean potentialMatchesReSet = false;
    
    
    //Match object for getting the same data in service pages
    private static final ServiceDataMatch MATCH_DATA = ServiceDataMatch.getInstance();
    
    
    public static boolean resetMatching = true; //true= refresh       -> get's alternated in program
                                                //false= dont refresh
                                                //for: manual matching
    
    //page title
    private final String TITLE_DUTCH = "Service hoofd pagina ";
    private final String TITLE = "Servivce home";
    /**
     * This method is for getting the one and only instance of the class match 
     * 
     * @return ServiceDataMatch the instance of this class
     */
    public static ServiceDataMatch getMATCH_DATA() {
        return ServiceHomeViewController.MATCH_DATA;
    }
    
    /**
     * This is for setting the onMatchingView boolean
     * Note: used for example by the detailed views.
     * 
     * @param value         always false, only when on matching view = true
     */
    public static void setOnMatchingView(boolean value) {
        ServiceHomeViewController.onMatchingView = value;
    }

    /**
     * Getting the state of the boolean on matching view
     * 
     * @return boolean the state of this variable
     */
    public static boolean isOnMatchingView() {
        return ServiceHomeViewController.onMatchingView;
    }

    /**
     * This is for setting the status of the potential matching resetting table
     * This will be checked when opening the matching view 
     * 
     * @param b     true: if the potentialMatching needs to be reset
     */
    public static void setPotentialResetStatus(boolean b) {
        ServiceHomeViewController.potentialMatchesReSet = b;
    }
    /**
     * Get the status of there is a reset needed for the potential matching
     * For clearing the list.
     * 
     * @return boolean of the status
     */
    public static boolean getPotentialResetStatus() {
        return ServiceHomeViewController.potentialMatchesReSet;
    }

    /**
     * Initializes the controller class that adds all the needed functionality,
     * to the: ServiceHomeView.FXML view.
     * 
     * @param url location  used to resolve relative paths for the root object
     * @param rb resources   used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set switchable to prev view. (this)
        MainViewController.previousView = "/Views/Service/ServiceHomeView.fxml";
        MainApp.currentView = "/Views/Service/ServiceHomeView.fxml";
        
        //reset refreshing to auto
        resetMatching = true;
        
        //set the view's title
        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(TITLE_DUTCH);
            } else {
                MainViewController.getInstance().getTitle(TITLE);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceHomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        //set screen status
        setOnMatchingView(false);
    }
    
//    /**
//     * Set the right language to this view
//     */
//    private void setRightLanguage(){
//        //set the texts of the buttons to the right field of the array
//        //this data is gotten from the settings class
//        button_found.setText( settings.getLanguageFor( L_index_BUT_FOUND, languages) );
//        button_lost.setText(  settings.getLanguageFor( L_index_BUT_LOST,  languages) );
//        button_match.setText( settings.getLanguageFor( L_index_BUT_MATCH, languages) );
//        button_input.setText( settings.getLanguageFor( L_index_BUT_INPUT, languages) );
//   }
    
    /*-------------------------------------/
    /*            switch views            */
    /*------------------------------------*/
    @FXML
    protected void logOut(ActionEvent event) throws IOException {
        MainApp.switchView("/fxml/SelectUserRoleView.fxml");
        //logout methode is needed here!
    }

    @FXML
    protected void toInputView(ActionEvent event) throws IOException {
        if (checkIfServiceEmployee()){
            MainApp.switchView("/Views/Service/ServiceInputLuggageView.fxml");
        }
    }

    @FXML
    protected void toFoundLuggageView(ActionEvent event) throws IOException {
        if (checkIfServiceEmployee()){
            MainApp.switchView("/Views/Service/ServiceOverviewFoundView.fxml");
        }
    }

    @FXML
    protected void toMissedLuggageView(ActionEvent event) throws IOException {
        if (checkIfServiceEmployee()){
            MainApp.switchView("/Views/Service/ServiceOverviewLostView.fxml");
        }
    }

    @FXML
    protected void toMatchingView(ActionEvent event) throws IOException {
        if (checkIfServiceEmployee()){
            MainApp.switchView("/Views/Service/ServiceMatchingView.fxml");
        }
    }
    
    private boolean checkIfServiceEmployee(){
        boolean isLoggedIn = MainApp.currentUser != null;
        
        if (isLoggedIn == false) {
            System.out.println("No user logged in");
        } else if (isLoggedIn && !"Service".equals(MainApp.currentUser.getRole())) {
            System.out.println("No service employee logged in");
            isLoggedIn = false;
        }
        return isLoggedIn;
    }

}
