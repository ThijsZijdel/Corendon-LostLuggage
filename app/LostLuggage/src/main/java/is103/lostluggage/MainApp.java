package is103.lostluggage;

import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.Model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

/**
 * Main class
 *
 * @author Michael de Boer
 *
 */
public class MainApp extends Application {

    private static BorderPane root;

    public static int serviceChangeValue = 99;

    //Database instance
    private static MyJDBC DB;

    public static String language = "english";

    public static String currentView;

    public static User currentUser = null;

    public static Stage mainStage;
    
    @Override
    public void start(Stage stage) throws Exception {

        //Method to set the db property
        setDatabase("corendonlostluggage", "root", "admin");
                
        //set root
        root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"));
        Scene mainScene = new Scene(root);

        checkLoggedInStatus(currentUser);

        mainScene.getStylesheets().add("/styles/Styles.css");

        stage.setTitle("Corendon Lost Luggage");
        stage.setScene(mainScene);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        stage.setMinWidth(1000);
        stage.setMinHeight(700);

        Image logo = new Image("Images/Stage logo.png");
        //Image applicationIcon = new Image(getClass().getResourceAsStream("Images/Logo.png"));
        stage.getIcons().add(logo);

        stage.show();

        //Set the mainstage as a property
        MainApp.mainStage = stage;

    }

    //methode voor het switchen van schermen
    public static void switchView(String view) throws IOException {
        //parent vanuit MainApp laden
        Parent fxmlView;

        if (language.equals("dutch")) {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.Bundle", new Locale("nl"));
            fxmlView = FXMLLoader.load(MainApp.class.getResource(view), bundle);

        } else {
            ResourceBundle bundle = ResourceBundle.getBundle("resources.Bundle");
            fxmlView = FXMLLoader.load(MainApp.class.getResource(view), bundle);

        }
        //scene zetten ( in Center van BorderPane )
        //fxmlView.
        root.setCenter(fxmlView);

    }

    public static File selectFileToSave(String defaultFileName) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Specify filename");

        //todo: provide the file selection dialog to the user
        File file = fileChooser.showSaveDialog(mainStage);

        //File selected? return the file, else return null
        if (file != null) {
            return file;
        } else {
            return null;
        }
    }

    //set the database instance
    public static void setDatabase(String dbname, String user, String password) throws FileNotFoundException {

//        //config file
//        File file = new File("src/main/resources/config");
//        
//        //Scanner object
//        Scanner input = new Scanner(file);
//        
//        String dbname = input.next();
//        String user = input.next();
//        String password = input.next();

        //init db
        MainApp.DB = new MyJDBC(dbname, user, password);
    }

    //method to connect to the database
    public static MyJDBC getDatabase() {
        return MainApp.DB;
    }

    public static String getLanguage() {
        return language;
    }

    public static void checkLoggedInStatus(User user) throws IOException {

        if (user != null) {
            currentUser = user;
            System.out.println(user);
            if (user.getRole().equals("Administrator")) {
                switchView("/Views/Admin/HomeUserView.fxml");
                System.out.println("The correct user role is selected: " + user);

            }
            if (user.getRole().equals("Manager")) {
                switchView("/Views/ManagerHomeView.fxml");

            }
            if (user.getRole().equals("Service")) {
                switchView("/Views/Service/ServiceHomeView.fxml");

            }

        } else {
            switchView("/Views/Admin/LogInView.fxml");

        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
