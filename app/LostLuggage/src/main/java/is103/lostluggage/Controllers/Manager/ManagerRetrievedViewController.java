package is103.lostluggage.Controllers.Manager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.Admin.OverviewUserController;

import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.PdfDocument;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import static java.sql.JDBCType.NULL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Node;

import javafx.scene.control.TableColumn;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author daron
 */
public class ManagerRetrievedViewController implements Initializable {

    //luggage list
    @FXML
    private TableView<RetrievedLuggage> retrievedTable;

    @FXML
    private TableColumn<RetrievedLuggage, Integer> FormID;
    @FXML
    private TableColumn<RetrievedLuggage, String> Date;
    @FXML
    private TableColumn<RetrievedLuggage, String> Customer;
    @FXML
    private TableColumn<RetrievedLuggage, String> Employee;
    @FXML
    private TableColumn<RetrievedLuggage, String> Deliverer;
    @FXML
    private TableColumn<RetrievedLuggage, String> koffernr;

    @FXML
    private JFXTextField formtextid;
    @FXML
    private JFXTextField customerid;
    @FXML
    private JFXTextField deivererid;
    @FXML
    private JFXTextField employeeservice;
    @FXML
    private JFXTextField emailid;
    @FXML
    private JFXTextField adresid;
    @FXML
    private JFXTextField dateid;
    @FXML
    private JFXTextField lostluggageid;

    @FXML
    private StackPane stackPane;

    private final JFXDialogLayout DIALOG_LAYOUT = new JFXDialogLayout();

    private final TextFlow MESSAGE_FLOW = new TextFlow();

    private String alert, alertHeader, headerColor, buttonText;

    //Hashmap containing all the text fields
    private Map<String, String> formValues = new LinkedHashMap<>();

    private String header;
    private String headerDutch;
    //conection to the db
    public final MyJDBC DB = MainApp.getDatabase();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        header = "Retrieved lugagge";
        headerDutch = "Teruggebrachte bagage";
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

        //Set which view was previous 
        MainApp.currentView = "/Views/ManagerRetrievedView.fxml";

        //To Previous Scene
        MainViewController.previousView = "/Views/ManagerHomeView.fxml";

        FormID.setCellValueFactory(new PropertyValueFactory<>("FormID"));
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        koffernr.setCellValueFactory(new PropertyValueFactory<>("Koffer"));
        Customer.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        Employee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        Deliverer.setCellValueFactory(new PropertyValueFactory<>("Deliverer"));
        retrievedTable.setItems(getRetrievedLuggage());

        formtextid.setEditable(false);

        lostluggageid.setEditable(false);
        customerid.setEditable(false);
        deivererid.setEditable(false);
        emailid.setEditable(false);
        adresid.setEditable(false);

        employeeservice.setEditable(false);

        dateid.setEditable(false);

        retrievedTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {

                    Node node = ((Node) event.getTarget()).getParent();

                    TableRow row;

                    if (node instanceof TableRow) {
                        row = (TableRow) node;
                    } else {
                        // clicking on text part
                        row = (TableRow) node.getParent();
                    }

                    //get value of selected row in tableview
                    int lostluggage = retrievedTable.getSelectionModel().getSelectedItem().getKoffer();
                    int formid = retrievedTable.getSelectionModel().getSelectedItem().getFormID();
                    String customer = retrievedTable.getSelectionModel().getSelectedItem().getCustomer();
                    String deliver = retrievedTable.getSelectionModel().getSelectedItem().getDeliverer();
                    String employee = retrievedTable.getSelectionModel().getSelectedItem().getEmployee();
                    String date = retrievedTable.getSelectionModel().getSelectedItem().getDate();

                    //convert int to string
                    String formidString = Integer.toString(formid);
                    String lostluggagenr = Integer.toString(lostluggage);

                    // set the values of tableview in textfield
                    formtextid.setText(formidString);

                    lostluggageid.setText(lostluggagenr);

                    customerid.setText(customer);
                    customerid.setEditable(true);
                    deivererid.setText(deliver);
                    deivererid.setEditable(true);

                    employeeservice.setText(employee);

                    dateid.setText(date);

                    try {
                        //search mail and adress where registrationnr is the same
                        ResultSet resultSetPassenger = DB.executeResultSetQuery("SELECT * FROM passenger "
                                + "LEFT JOIN lostluggage ON lostluggage.passengerId = passenger.passengerId "
                                + "WHERE lostluggage.registrationNr = '" + lostluggagenr + "'; ");

                        while (resultSetPassenger.next()) {

                            String passadres = resultSetPassenger.getString("passenger.address");

                            //check if customer has a email
                            if (!NULL.equals("passenger.email") || !"".equals("passenger.email")) {
                                String passmail = resultSetPassenger.getString("passenger.email");
                                emailid.setText(passmail);
                                emailid.setEditable(true);
                            } else {
                                emailid.clear();
                                emailid.setEditable(true);
                            }

                            adresid.setText(passadres);
                            adresid.setEditable(true);
                        }

                    } catch (SQLException ex) {
                        Logger.getLogger(ManagerRetrievedViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

        });

    }

    @FXML
    public void updateFormInfo(ActionEvent event) throws SQLException {

        String customer = customerid.getText();
        String lostkoffer = lostluggageid.getText();
        String deliverer = deivererid.getText();
        String email = emailid.getText();
        String adres = adresid.getText();
        String id = this.formtextid.getText();

        if (!id.isEmpty()) {
            int updateInfo = DB.executeUpdateQuery("UPDATE passenger "
                    + "                                   JOIN lostluggage ON lostluggage.passengerId = passenger.passengerId  "
                    + "                                             JOIN matched on lostluggage.registrationNr = matched.lostluggage  "
                    + "                                                     SET passenger.name = '" + customer + "', passenger.email = '" + email + "', passenger.address = '" + adres + "', matched.delivery = '" + deliverer + "' "
                    + "                                                     WHERE lostluggage.registrationNr = '" + lostkoffer + "'");

            alertHeader = "Updated!";
            headerColor = "#f03e3e";
            alert = "The delivered luggage is updated";
            buttonText = "Ok";
            showAlertMessage();

        } else {
            alertHeader = "Something went wrong!";
            headerColor = "#f03e3e";
            alert = "Please select a row before updating details";
            buttonText = "Try again";
            showAlertMessage();
        }
    }

    @FXML
    public void refreshTable(ActionEvent event) throws SQLException, IOException {
        String id = this.formtextid.getText();
        if (!id.isEmpty()) {

            getRetrievedLuggage().removeAll(getRetrievedLuggage());
            while (retrievedTable.getRowFactory() != null) {
                getRetrievedLuggage().addAll();

            }
            retrievedTable.setItems(getRetrievedLuggage());
        } else {
            alertHeader = "There is nothing to update";
            headerColor = "#f03e3e";
            alert = "Please update retrieved luggage before refreshing";
            buttonText = "Close";
            showAlertMessage();
        }
    }

    @FXML
    public void exportPdf(ActionEvent event) throws SQLException, IOException {
        String id = this.formtextid.getText();
        if (!id.isEmpty()) {
            //Fileobject
            File file = MainApp.selectFileToSave("*.pdf");

            //If fileobject has been initialized
            if (file != null) {
                String customer = customerid.getText();
                String lostkoffer = lostluggageid.getText();
                String deliverer = deivererid.getText();

                String email = "";
                String adres = "";

                if (emailid.getText() != null && !emailid.getText().isEmpty()) {
                    email = emailid.getText();
                } else {

                    alertHeader = "Email is empty";
                    headerColor = "#f03e3e";
                    alert = "please put and email in";
                    buttonText = "Oke";
                    showAlertMessage();
                }

                if (adresid.getText() != null && !adresid.getText().isEmpty()) {
                    email = adresid.getText();
                } else {
                    alertHeader = "Address is empty";
                    headerColor = "#f03e3e";
                    alert = "please put and address in";
                    buttonText = "Oke";
                    showAlertMessage();
                }

                String treated = employeeservice.getText();
                String date = dateid.getText();
                String formid = formtextid.getText();

                formValues.put("Registration ID: ", formid);
                formValues.put("Registration date: ", date);
                formValues.put("Employee name: ", treated);
                formValues.put("Customer name: ", customer);
                formValues.put("Lost luggage registration ID: ", lostkoffer);
                formValues.put("Customer address: ", adres);
                formValues.put("Customer email: ", email);
                formValues.put("Deliverer: ", deliverer);
                //get the location to store the file
                String fileName = file.getAbsolutePath();
                //New pdf document with filebath in constructor
                PdfDocument Pdf = new PdfDocument(fileName);

                //set the values for the pdf
                Pdf.setPdfValues(formValues);

                //Save the pdf
                Pdf.savePDF();
            }
        } else {
            alertHeader = "Something went wrong!";
            headerColor = "#f03e3e";
            alert = "Please select a row before exporting details";
            buttonText = "Try again";
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

    public ObservableList<RetrievedLuggage> getRetrievedLuggage() {

        ObservableList<RetrievedLuggage> retrievedList = FXCollections.observableArrayList();

        try {

            ResultSet resultSet = DB.executeResultSetQuery("SELECT delivery, dateMatched, employee.firstname, matched.matchedId, matched.lostluggage, passenger.name  FROM matched "
                    + "                                 INNER JOIN employee ON matched.employeeId = employee.employeeId "
                    + "                                     INNER JOIN foundluggage ON matched.foundluggage = foundluggage.registrationNr "
                    + "                                             INNER JOIN lostluggage on matched.lostluggage = lostluggage.registrationNr"
                    + "                                         INNER JOIN passenger ON lostluggage.passengerId = passenger.passengerId WHERE matched.matchedId");

            while (resultSet.next()) {
                String delivercheck = resultSet.getString("matched.delivery");
                if (!"".equals(delivercheck)) {
                    int registrationnr = resultSet.getInt("matched.matchedId");
                    String date = resultSet.getString("matched.dateMatched");
                    String passengername = resultSet.getString("passenger.name");
                    String employeename = resultSet.getString("employee.firstname");
                    String delivered = resultSet.getString("matched.delivery");
                    int koffer = resultSet.getInt("matched.lostluggage");

                    retrievedList.add(new RetrievedLuggage(registrationnr, koffer, date, passengername, employeename, delivered));

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retrievedList;
    }

}
