package is103.lostluggage.Controllers.Manager;


import is103.lostluggage.Controllers.MainViewController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class ManagerPassengerInfoViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MainViewController.previousView = "/Views/ManagerFoundView.fxml";
    }    
    
}
