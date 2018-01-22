/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Controllers;

import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Michael de Boer
 */
public class SelectUserRoleViewController implements Initializable {

    @FXML
    public Button btnService;
    
    @FXML         
    Button btnAdmin;
    
    @FXML
    Button btnManager;
    

    @FXML
    public void buttonPressed(ActionEvent event) throws IOException {

        if (event.getSource() == btnService) {
            MainApp.switchView("/Views/Service/ServiceHomeView.fxml");
            
        }
        if (event.getSource() == btnAdmin) {
            
        }
        if (event.getSource() == btnManager) {
            
        }

        }

    
    @FXML 
    protected void toServiceView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/Service/ServiceHomeView.fxml");
    }
    @FXML 
    protected void toAdminView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/Admin/HomeUserView.fxml");
    }
    @FXML 
    protected void toManagerView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/ManagerHomeView.fxml");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //database aanmaken 
        //(zodat hij niet bij elke switch naar service home weer word aangemaakt)
        ///MyJDBC.createLostLuggageDatabase("LostLuggage");
    }

}
