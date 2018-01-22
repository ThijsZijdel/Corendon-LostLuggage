/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Controllers.Admin;

import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.MainApp;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * HomeUserView Controller class
 *
 * @author Michael de Boer
 */
public class HomeUserViewController implements Initializable {

    @FXML
    private Label label1;
    @FXML
    private Button button;

    private final String header = "Home";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            MainViewController.getInstance().getTitle(header);
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //MainViewController.previousView = "";
        MainApp.currentView = "/Views/Admin/HomeUserView.fxml";

    }

    //Go to Overview User Scene
    @FXML
    protected void handleButtonAction(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/Admin/UserScene.fxml");
    }

    //Go to Add User Scene
    @FXML
    protected void viewAddUserWindow(ActionEvent event) throws IOException {
        AdminAddUserViewController.edit = false;

        MainApp.switchView("/Views/Admin/AdminAddUserView.fxml");

    }
    
    
    //Go to the extra data window
    @FXML
    protected void viewAddExtraDataWindow(ActionEvent event) throws IOException {
        AdminAddUserViewController.edit = false;

        MainApp.switchView("/Views/Admin/ExtraData.fxml");
    }

}
