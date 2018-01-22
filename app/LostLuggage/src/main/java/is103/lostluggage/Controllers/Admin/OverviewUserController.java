package is103.lostluggage.Controllers.Admin;

import com.jfoenix.controls.JFXTextField;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Controllers.Admin.AdminAddUserViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.User;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

//import javax.swing.text.TableView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableRow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * UserScene Controller class
 *
 * @author Michael de Boer
 */
public class OverviewUserController implements Initializable {

    private String header = "Overview User";
    private String headerDutch = "Overzicht Gebruikers";

    private final MyJDBC DB = MainApp.getDatabase();

    @FXML
    private JFXTextField searchTextField;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> locationColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TableColumn<User, String> statusColumn;

    @FXML
    private void goToAddView(ActionEvent event) {
        try {
            AdminAddUserViewController.edit = false;

            MainApp.switchView("/Views/Admin/AdminAddUserView.fxml");

        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Error in creating database User
        //MyJDBC.createLostLuggageDatabase("LostLuggage");
        try {
            if (MainApp.language.equals("dutch")) {
                MainViewController.getInstance().getTitle(headerDutch);
            } else {
                MainViewController.getInstance().getTitle(header);

            }
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("Id"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<User, String>("Location"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<User, String>("Role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<User, String>("Status"));

        tableView.setItems(getUsers());

        mouseClickedOnRow();

        //To Previous Scene
        MainViewController.previousView = "/Views/Admin/HomeUserView.fxml";
        MainApp.currentView = "/Views/Admin/UserScene.fxml";

    }

    public ObservableList<User> getUsers() {

        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            ResultSet resultSet;

            resultSet = DB.executeResultSetQuery("SELECT employeeId, firstname, lastname, location, status, role FROM employee");

            while (resultSet.next()) {
                String id = resultSet.getString("employeeId");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String location = resultSet.getString("location");
                String status = resultSet.getString("status");
                String role = resultSet.getString("role");

                System.out.println("ID: " + id + "  Firstname: " + firstName + " Lastname: " + lastName + " Location: " + location + " Status: " + status + " Role: " + role);
                users.add(new User(id, lastName, firstName, location, role, status));

            }

        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    @FXML
    private void searchUsers(KeyEvent event) throws SQLException {

        ObservableList<User> users = FXCollections.observableArrayList();
        String searchInput = searchTextField.getText();

        try {

            ResultSet resultSet = DB.executeSearchQuery(searchInput);

            while (resultSet.next()) {
                String id = resultSet.getString("employeeId");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                String location = resultSet.getString("location");
                String status = resultSet.getString("status");
                String role = resultSet.getString("role");

                System.out.println("ID: " + id + "  Firstname: " + firstName + " Lastname: " + lastName + " Location: " + location + " Status: " + status + " Role: " + role);
                users.add(new User(id, lastName, firstName, location, role, status));
            }

            for (int i = 0; i < users.size(); i++) {
                System.out.println(users.get(i));
            }
            tableView.setItems(users);

        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void mouseClickedOnRow() {
        tableView.setOnMousePressed((MouseEvent event) -> {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                User user = (User) row.getItem();
                AdminAddUserViewController.selectedUser = user;
                AdminAddUserViewController.edit = true;
                try {
                    MainApp.switchView("/Views/Admin/AdminAddUserView.fxml");
                } catch (IOException ex) {
                    Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

}
