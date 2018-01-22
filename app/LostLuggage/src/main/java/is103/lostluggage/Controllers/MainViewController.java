package is103.lostluggage.Controllers;

import com.jfoenix.controls.JFXButton;
import is103.lostluggage.MainApp;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * MainView Controller class
 *
 * @author Michael de Boer
 */
public class MainViewController implements Initializable {

    //Create instance
    public static MainViewController instance = null;

    public MainViewController() {
    }

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXButton englishButton, dutchButton;

    @FXML
    private ImageView logoView;

    @FXML
    private HBox topHBox;

    public static String previousView;

    @FXML
    private Label title;

    @FXML
    private JFXButton settingsButton, logoutButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image logo = new Image("Images/Logo.png");
        logoView.setImage(logo);
        topHBox.toFront();
        settingsButton.setGraphic(new ImageView("Images/settings.png"));

        instance = this;

        englishButton.setOnAction(e -> {
            MainApp.language = "english";
            System.out.println("Language changed to " + MainApp.language);
            try {
                backButton.setText("< Back");
                settingsButton.setText("Settings");
                MainApp.switchView(MainApp.currentView);
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        dutchButton.setOnAction(e -> {
            MainApp.language = "dutch";
            System.out.println("Language changed to " + MainApp.language);
            try {
                backButton.setText("< Terug");
                settingsButton.setText("Instellingen");
                MainApp.switchView(MainApp.currentView);
            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    //Get instance
    public static MainViewController getInstance() {
        return instance;
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {

        MainApp.currentUser = null;

        MainApp.switchView("/Views/Admin/LogInView.fxml");
    }

    /**
     * Get the static string header and use instance to pass header to setTitle
     *
     * @param header
     */
    public static void getTitle(String header) throws IOException {
        //header = MainViewController.header;
        getInstance().setTitle(header);
    }

    //Set non-static header as Title
    private void setTitle(String header) {
        title.setText(header);
    }

    @FXML
    private void showSettingsView(ActionEvent event) throws IOException {

        if (MainApp.currentUser != null) {

            MainApp.switchView("/Views/Admin/SettingsView.fxml");

        }

    }

    @FXML
    private void goBackToPreviousScene(ActionEvent event) throws IOException {

        if (previousView != null) {
            System.out.println("-Back: Previous view= " + previousView);

            MainApp.switchView(previousView);

        } else {
            System.out.println("-Back: No previous view/scene");
        }

        //backButton.getScene().h
        //((Node)(event.getSource())).getScene().getWindow().hide();                      
    }

}
