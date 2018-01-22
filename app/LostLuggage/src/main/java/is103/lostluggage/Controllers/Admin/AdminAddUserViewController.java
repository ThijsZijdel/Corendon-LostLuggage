/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Controllers.Admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

/**
 * AdminAddUserView Controller class
 *
 * @author Michael de Boer
 *
 */
public class AdminAddUserViewController implements Initializable {

    public static ObservableList<String> roleList;

    public static ObservableList<String> statusList;

    private double navListPrefHeight = 70.0d;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private HBox errorMessageHBox;
    @FXML
    private AnchorPane errorMessageView;
    @FXML
    private AnchorPane whiteAnchorPane;
    @FXML
    private JFXComboBox statusComboBox;
    @FXML
    private JFXComboBox roleComboBox;

    @FXML
    //This label displays a errors
    private Label errorMessageLbl;

    @FXML
    //Button to add the user to the system
    private Button addUserBtn;

    @FXML
    //Field that contains the employeeId
    private JFXTextField employeeIdField;

    @FXML
    //Field that contains the firstname
    private JFXTextField firstnameField;

    @FXML
    //Field that contains the surname
    private JFXTextField lastnameField;

    @FXML
    //Field that contains the phonenumber
    private JFXTextField locationField;

    @FXML
    private JFXButton resetPasswordButton;

    @FXML
    private StackPane stackPane;

    private final JFXDialogLayout DIALOG_LAYOUT = new JFXDialogLayout();

    private final TextFlow MESSAGE_FLOW = new TextFlow();

    private JFXDialog alertView;

    private String userId;

    //Title of the view
    private String header;

    private String headerDutch;

    public static boolean edit = false;

    public static User selectedUser;

    private final MyJDBC DB = MainApp.getDatabase();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackPane.setVisible(false);
        //Add options to List
        roleList = FXCollections.observableArrayList(
                "Administrator",
                "Manager",
                "Service"
        );
        statusList = FXCollections.observableArrayList(
                "Active",
                "Inactive"
        );

        //Add options to Combobox
        roleComboBox.setItems(roleList);
        statusComboBox.setItems(statusList);

        mainBorderPane.setTop(null);

        if (edit != false) {
            header = "Edit User";
            headerDutch = "Wijzig Gebruiker";
            addUserBtn.setText("Edit");
            addUserBtn.setStyle("-fx-background-color: #51cf66; ");
            firstnameField.setText(selectedUser.getFirstName());
            lastnameField.setText(selectedUser.getLastName());
            employeeIdField.setText(selectedUser.getId());
            locationField.setText(selectedUser.getLocation());
            statusComboBox.getSelectionModel().select(selectedUser.getStatus());
            roleComboBox.getSelectionModel().select(selectedUser.getRole());

            employeeIdField.setDisable(true);
            resetPasswordButton.setVisible(true);

        } else {
            resetPasswordButton.setVisible(false);
            header = "Add User";
            headerDutch = "Voeg Gebruiker Toe ";

        }
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

        //Set which view was previous
        MainViewController.previousView = "/Views/Admin/HomeUserView.fxml";
        MainApp.currentView = "/Views/Admin/AdminAddUserView.fxml";
    }

    @FXML
    protected void backToHomeUserView(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/Admin/HomeUserView.fxml");
        edit = false;

    }

    private void resetPassword() throws SQLException {
        String location = locationField.getText();
        userId = employeeIdField.getText();

        int returnValue = DB.executePasswordUpdateQuery(userId, location);

        if (returnValue != -1) {
            String header = "Succesful";
            String message = "The password of user: " + userId + " has been reset";
            String buttonText = "Ok";
            showConfirmedMessage(header, message, buttonText);
        } else {
            String header = "Something went Wrong";
            String message = "The password of user: " + userId + " has not been reset \nPlease try again! ";
            String buttonText = "Ok";
            showConfirmedMessage(header, message, buttonText);
        }

    }

    public void showConfirmedMessage(String headerString, String messageString, String buttonString) {
        Text header = new Text(headerString);
        header.setFont(new Font("System", 18));
        header.setFill(Paint.valueOf("#495057"));
        MESSAGE_FLOW.getChildren().clear();
        MESSAGE_FLOW.getChildren().add(new Text(messageString));
        DIALOG_LAYOUT.getActions().clear();
        DIALOG_LAYOUT.setHeading(header);
        DIALOG_LAYOUT.setBody(MESSAGE_FLOW);

        JFXButton okButton = new JFXButton(buttonString);

        okButton.setStyle("-fx-background-color: #4dadf7");
        okButton.setTextFill(Paint.valueOf("#FFFFFF"));
        okButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        okButton.setButtonType(JFXButton.ButtonType.RAISED);
        DIALOG_LAYOUT.setActions(okButton);
        okButton.setOnAction(e -> {
            alertView.close();
            stackPane.setVisible(false);
        });
    }

    @FXML
    private void showPasswordResetMessage(ActionEvent event) throws IOException {
        stackPane.setVisible(true);
        MESSAGE_FLOW.getChildren().clear();
        userId = employeeIdField.getText();

        //Customize header
        Text header = new Text("Note: This action can't be undone");
        header.setFont(new Font("System", 18));
        header.setFill(Paint.valueOf("#495057"));

        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton resetButton = new JFXButton("Reset");

        //Customize buttons
        cancelButton.setStyle("-fx-background-color: #adb5bd");
        cancelButton.setTextFill(Paint.valueOf("#FFFFFF"));
        cancelButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        cancelButton.setButtonType(JFXButton.ButtonType.RAISED);

        resetButton.setStyle("-fx-background-color: #4dadf7");
        resetButton.setTextFill(Paint.valueOf("#FFFFFF"));
        resetButton.setRipplerFill(Paint.valueOf("#FFFFFF"));
        resetButton.setButtonType(JFXButton.ButtonType.RAISED);

        MESSAGE_FLOW.getChildren().add(new Text("Are you sure you want to reset the password of user: " + userId));
        DIALOG_LAYOUT.setHeading(header);
        DIALOG_LAYOUT.setBody(MESSAGE_FLOW);
        DIALOG_LAYOUT.setActions(cancelButton, resetButton);
        //DIALOG_LAYOUT.setActions(resetButton);

        alertView = new JFXDialog(stackPane, DIALOG_LAYOUT, JFXDialog.DialogTransition.CENTER);
        alertView.setOverlayClose(false);
        cancelButton.setOnAction(e -> {
            alertView.close();
            stackPane.setVisible(false);
        });

        resetButton.setOnAction(e -> {
            System.out.println("The password has been reset");
            //alertView.close();
            try {
                resetPassword();
            } catch (SQLException ex) {
                Logger.getLogger(AdminAddUserViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        alertView.show();
    }

    @FXML
    public void addUser(ActionEvent event) throws IOException, SQLException {

        //default error message is empty
        String errorMessage = "";

        //Get values from TextFields
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String location = locationField.getText();
        Object status = statusComboBox.getValue();
        Object role = roleComboBox.getValue();

        //Counter for empty fields
        int amount = 0;
        //Array of input fields & array of fields that are empty
        String[] fields = {"Firstname", "Lastname", "Aiport / City", "Status", "Role"};
        String[] emptyfields = new String[amount];

        //If one or more fields are empty show errorMessageView, setFocusColor to red 
        if (firstname.isEmpty() || lastname.isEmpty() || location.isEmpty() || statusComboBox.getValue() == null
                || statusComboBox.getValue().toString().isEmpty() || roleComboBox.getValue() == null
                || roleComboBox.getValue().toString().isEmpty()) {

            emptyfields = new String[fields.length];

            if (firstnameField.getText().isEmpty()) {
                emptyfields[amount] = fields[0];
                firstnameField.setUnFocusColor(Paint.valueOf("#f03e3e"));
                amount++;
            } else {
                firstnameField.setUnFocusColor(Paint.valueOf("#4d4d4d"));

            }

            if (lastnameField.getText().isEmpty()) {
                emptyfields[amount] = fields[1];
                amount++;
                lastnameField.setUnFocusColor(Paint.valueOf("#f03e3e"));
            } else {
                lastnameField.setUnFocusColor(Paint.valueOf("#4d4d4d"));
            }

            if (location.isEmpty()) {
                emptyfields[amount] = fields[2];
                amount++;
                locationField.setUnFocusColor(Paint.valueOf("#f03e3e"));

            } else {
                locationField.setUnFocusColor(Paint.valueOf("#4d4d4d"));
            }

            if (statusComboBox.getValue() != null
                    && !statusComboBox.getValue().toString().isEmpty()) {
                String statusChoice = statusComboBox.getValue().toString();
                System.out.println(statusChoice);

            } else {
                emptyfields[amount] = fields[3];
                amount++;
                statusComboBox.setUnFocusColor(Paint.valueOf("#f03e3e"));

            }
            if (roleComboBox.getValue() != null
                    && !roleComboBox.getValue().toString().isEmpty()) {
                String roleChoice = roleComboBox.getValue().toString();
                System.out.println(roleChoice);
            } else {
                emptyfields[amount] = fields[4];
                amount++;
                roleComboBox.setUnFocusColor(Paint.valueOf("#f03e3e"));

            }
            //Main Error message
            errorMessage = "The following fields can't be empty:  ";

            //Add empty fields to Error Message
            for (int i = 0; i <= emptyfields.length - 1; i++) {
                if (emptyfields[i] != null) {
                    errorMessage += emptyfields[i];

                    if (i < (amount - 1)) {
                        errorMessage += ", ";

                    }
                    System.out.print(emptyfields[i]);
                }

            }
            //Put the error message on the label
            errorMessageLbl.setText(errorMessage);

            //Start errorMessageView animation after error message contains all empty fields
            startAnimation();

            //System.out.println(UUID.randomUUID());
        } //All fields are valid, there are no errors
        else {
            String employeeId = employeeIdField.getText();
            if (edit != false) {
                String id = selectedUser.getId();
                String roleString = role.toString();
                String statusString = status.toString();
                User updateUser = new User(id, lastname, firstname, location, roleString, statusString);
                DB.executeUserUpdateQuery(updateUser);
            } else {

                //Temporary id
                //String id = UUID.randomUUID().toString().substring(0, 8);
                String roleString = role.toString();
                String statusString = status.toString();
                String password = location;

                String query = String.format("INSERT INTO employee VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", employeeId, firstname, lastname, password, roleString, location, statusString);

                int result = DB.executeUpdateQuery(query);
                System.out.println(" This is the result:  " + result);
            }
            MainApp.switchView("/Views/Admin/UserScene.fxml");

        }
        edit = false;

    }

    //This method will start de error message animation, at the end of the 4 sec
    // will start de dismiss animation
    public void startAnimation() {
        errorMessageView.prefHeight(0.0d);
        mainBorderPane.setTop(errorMessageView);
        errorMessageHBox.getChildren().add(errorMessageLbl);
        errorMessageLbl.setVisible(true);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(new KeyFrame(Duration.ZERO,
                new KeyValue(errorMessageView.prefHeightProperty(), 0)
        ),
                new KeyFrame(Duration.millis(300.0d),
                        new KeyValue(errorMessageView.prefHeightProperty(), navListPrefHeight)
                )
        );
        addUserBtn.setDisable(true);
        timeline.play();

        PauseTransition wait = new PauseTransition(Duration.seconds(4));
        wait.setOnFinished((e) -> {
            dismissAnimation();
        });
        wait.play();

    }

    //This method will close the animation
    public void dismissAnimation() {
        errorMessageView.prefHeight(0.0d);
        mainBorderPane.setTop(errorMessageView);
        errorMessageHBox.getChildren().remove(errorMessageLbl);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(errorMessageView.prefHeightProperty(), navListPrefHeight)
                ),
                new KeyFrame(Duration.millis(300.0d),
                        new KeyValue(errorMessageView.prefHeightProperty(), 0)
                )
        );
        timeline.play();
        addUserBtn.setDisable(false);

    }

    //This method is still in progress (not yet implemented)
    //function to generate the employeeid by using the first letter of the firstname
    //and the first letter of the surname
    @FXML
    private void generateEmployeeId() throws SQLException {

        if (edit != false) {
        } else {
            //initialy the employee id field is empty
            String employeeId = "";
            int count = 0;

            //array of characters in the employeeId
            char[] employeeIdChars = new char[2];

            //Check whether the input string is empty or not
            if (!firstnameField.getText().isEmpty()) {
                employeeIdChars[0] = firstnameField.getText().charAt(0);
                employeeId = String.valueOf(employeeIdChars).toUpperCase();
                employeeIdField.setText(employeeId);

                if (!firstnameField.getText().isEmpty() && !lastnameField.getText().isEmpty()) {

                    //If its not empty, then the first letter of the string will be the first
                    //letter of the employeeId
                    employeeId = null;
                    employeeIdChars[0] = firstnameField.getText().charAt(0);
                    employeeIdChars[1] = lastnameField.getText().charAt(0);
                    employeeId = String.valueOf(employeeIdChars).toUpperCase();

                    String query = "SELECT COUNT(*) count FROM employee WHERE employeeId LIKE 'input%'";
                    query = query.replace("input", employeeId);

                    ResultSet resultSet = DB.executeResultSetQuery(query);
                    while (resultSet.next()) {
                        count = resultSet.getInt("count");
                        System.out.println("This is the count before: " + count);
                        count++;

                    }

                    System.out.println("This is the count after: " + count);

                    //It is impossible to delete users so it isnt necessary to check 
                    //if the id is already used
                    employeeIdField.setText(employeeId + count);
                }
            }
        }

    }
}
