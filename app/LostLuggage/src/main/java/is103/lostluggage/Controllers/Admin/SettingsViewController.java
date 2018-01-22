/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Controllers.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Settings Controller class
 *
 * @author Michael de Boer
 */
public class SettingsViewController implements Initializable {

    @FXML
    private JFXPasswordField oldPasswordField, newPasswordField, newPasswordField2;

    @FXML
    private JFXButton cancelButton, changePasswordButton;

    @FXML
    private Label userLabel;
    @FXML
    private StackPane stackPane;

    private final JFXDialogLayout DIALOG_LAYOUT = new JFXDialogLayout();

    private final TextFlow MESSAGE_FLOW = new TextFlow();

    private String alert, alertHeader, headerColor, buttonText;

    private final String header = "Settings";
    private final String headerDutch = "Instellingen";

    private final MyJDBC DB = MainApp.getDatabase();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        stackPane.setVisible(false);
        //Set Header
        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(headerDutch);
            } else {
                MainViewController.getInstance().getTitle(header);

            }
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        changePasswordButton.setOnAction(e -> {
            try {
                changePassword();
            } catch (SQLException ex) {
                Logger.getLogger(SettingsViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        if (MainApp.currentUser.getRole().equals("Administrator")) {
            MainViewController.previousView = "/Views/Admin/HomeUserView.fxml";

        }
        if (MainApp.currentUser.getRole().equals("Service")) {
            MainViewController.previousView = "/Views/Service/ServiceHomeView.fxml";

        }
        if (MainApp.currentUser.getRole().equals("Manager")) {
            MainViewController.previousView = "/Views/ManagerHomeView.fxml";

        }
        MainApp.currentView = "/Views/Admin/SettingsView.fxml";

    }

    private void changePassword() throws SQLException {

        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String passwordCheck = newPasswordField2.getText();
        String employeeId = MainApp.currentUser.getId();
        String currentPassword = "";

        if (!oldPassword.isEmpty() || !newPassword.isEmpty() || !passwordCheck.isEmpty()) {
            if (newPassword.equals(passwordCheck)) {

                ResultSet resultSet = null;
                String selectQuery = "SELECT password FROM employee WHERE employeeId = 'id'";
                selectQuery = selectQuery.replace("id", employeeId);
                System.out.println(selectQuery);

                resultSet = DB.executeResultSetQuery(selectQuery);

                while (resultSet.next()) {
                    currentPassword = resultSet.getString("password");

                }

                if (oldPassword.equals(currentPassword)) {

                    int returnValue = 0;

                    returnValue = DB.executePasswordUpdateQuery(employeeId, newPassword);
                    if (returnValue >= 1) {
                        alertHeader = "Succeful!";
                        headerColor = "#495057";
                        alert = "\nYour password has been updated!";
                        buttonText = "Ok";
                        showAlertMessage();
                    } else {
                        alertHeader = "Something went wrong!";
                        headerColor = "#f03e3e";
                        alert = "\nPlease try again later. Contact your administrator if this problem persists";
                        buttonText = "Ok";
                        showAlertMessage();
                    }

                } else {
                    alertHeader = "Something went wrong!";
                    headerColor = "#f03e3e";
                    alert = "\nThe password you entered doesn't match your current password.\nPlease enter the correct password";
                    buttonText = "Try again";
                    showAlertMessage();
                }

            } else {
                alertHeader = "Something went wrong!";
                headerColor = "#f03e3e";
                alert = "\nThe passwords you entered don't match. Please try again! \n\nContact your administrator if this problem persists";
                buttonText = "Try Again";
                showAlertMessage();
            }
        } else {
            alertHeader = "Something went wrong!";
            headerColor = "#f03e3e";
            alert = "\nIt seems you forget to enter one or more fields";
            buttonText = "Ok";
            showAlertMessage();

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
