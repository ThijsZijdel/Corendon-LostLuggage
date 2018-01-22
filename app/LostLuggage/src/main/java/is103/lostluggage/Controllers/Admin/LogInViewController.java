package is103.lostluggage.Controllers.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 * LogInView Controller class
 *
 * @author Michael de Boer
 */
public class LogInViewController implements Initializable {

    private final String header = "Log In";
    private final String headerDutch = "Inloggen";

    @FXML
    private JFXButton logInButton;

    @FXML
    private JFXButton clearAllButton;

    @FXML
    private JFXTextField idTextField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private StackPane stackPane;

    private final JFXDialogLayout DIALOG_LAYOUT = new JFXDialogLayout();

    private final TextFlow MESSAGE_FLOW = new TextFlow();

    private String alert, alertHeader, headerColor, buttonText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackPane.setVisible(false);

        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(headerDutch);
            } else {
                MainViewController.getInstance().getTitle(header);

            }
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        logInButton.setOnAction(e -> {
            try {
                loggingIn();
            } catch (IOException ex) {
                Logger.getLogger(LogInViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(LogInViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        clearAllButton.setOnAction(e -> {
            idTextField.clear();
            passwordField.clear();
        });
        MainApp.currentView ="/Views/Admin/LogInView.fxml";

    }

    public void loggingIn() throws SQLException, IOException {

        MyJDBC db = MainApp.getDatabase();
        String id = idTextField.getText();
        String password = passwordField.getText();
        User currentUser = null;
        try {

            currentUser = db.executeLogInQuery(id, password);
            //MainApp.currentUser = currentUser;
        } catch (SQLException ex) {
            Logger.getLogger(LogInViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //PreparedStatement preparedStatement = db.executeResultSetQuery(sql);
        if (currentUser != null) {
            if (currentUser.getStatus().equals("Active")) {
                MainApp.checkLoggedInStatus(currentUser);
            } else {
                alertHeader = "Access Denied";
                headerColor = "#f03e3e";
                alert = "\nThis account has been set on Inactive."
                        + "\nPlease contact your Administrator or Manager\n";
                buttonText = "Close";
                showAlertMessage();
            }
        } else {
            alertHeader = "Failed to Login";
            headerColor = "#495057";
            alert = "\nThe username and password you entered did not match our records."
                    + " Please double-check and try again.\n";
            buttonText = "Try Again";
            showAlertMessage();
            //System.out.println("The username and password you entered did not match our records. Please double-check and try again.");
        }
    }

    private void showAlertMessage() {
        stackPane.setVisible(true);
        MESSAGE_FLOW.getChildren().clear();

        //Customize header
        Text header = new Text(alertHeader);
        header.setFont(new Font("System", 18));
        header.setFill(Paint.valueOf(headerColor));

        JFXButton hideMessageButton = new JFXButton(buttonText);
        //Customize button
        hideMessageButton.setStyle("-fx-background-color: #4dadf7");
        hideMessageButton.setTextFill(Paint.valueOf("#FFFFFF"));
        hideMessageButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        hideMessageButton.setButtonType(JFXButton.ButtonType.RAISED);

        MESSAGE_FLOW.getChildren().add(new Text(alert));
        DIALOG_LAYOUT.setHeading(header);
        DIALOG_LAYOUT.setBody(MESSAGE_FLOW);
        DIALOG_LAYOUT.setActions(hideMessageButton);
        JFXDialog alertView = new JFXDialog(stackPane, DIALOG_LAYOUT, JFXDialog.DialogTransition.CENTER);
        alertView.setOverlayClose(false);
        hideMessageButton.setOnAction(e -> {
            alertView.close();
            stackPane.setVisible(false);
        });

        alertView.show();
    }

}
