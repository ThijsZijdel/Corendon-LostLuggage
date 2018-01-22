package is103.lostluggage.Controllers.Manager;

import is103.lostluggage.Controllers.Admin.OverviewUserController;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.MainApp;
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
 * @author daron
 */
public class ManagerHomeViewController implements Initializable {
private String header = "Overview manager home";
private String headerDutch = "Overzicht manager home";

    /**
     * Initializes the controller class.
     */ 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
                try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(headerDutch);
            } else {
                MainViewController.getInstance().getTitle(header);

            }
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Set which view was previous 
        MainApp.currentView = "/Views/ManagerHomeView.fxml";
        
      
    }
    //switch between screens from managerhome to others

    @FXML
    protected void toFoundView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/ManagerFoundView.fxml");
    }

    @FXML
    protected void reportView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/ManagerReportView.fxml");
    }

    @FXML
    protected void toLostView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/ManagerLostView.fxml");
    }

    @FXML
    protected void toRetrievedView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/ManagerRetrievedView.fxml");
    }
}
