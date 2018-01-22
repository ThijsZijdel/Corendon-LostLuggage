package is103.lostluggage.Controllers.Manager;

import com.jfoenix.controls.JFXButton;
import is103.lostluggage.Controllers.Admin.OverviewUserController;
import is103.lostluggage.Controllers.Admin.HomeUserViewController;
import is103.lostluggage.Controllers.MainViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author daron
 */
public class ManagerReportViewController implements Initializable {

    public String mon;
    public MyJDBC db = MainApp.getDatabase();

    private String header = "Manager reports";
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //To Previous Scene

        MainViewController.previousView = "/Views/ManagerHomeView.fxml";
try {
            MainViewController.getInstance().getTitle(header);
        } catch (IOException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    LineChart<String, Number> lineChart;

    @FXML
    public void sixteen(ActionEvent event) throws IOException {
        XYChart.Series<String, Number> seriesfound = new XYChart.Series<>();
        XYChart.Series<String, Number> serieslost = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesmatched = new XYChart.Series<>();
        lineChart.getData().clear();
        try {

            ResultSet resultSet;
            ResultSet lostSet;
            ResultSet retrievedSet;

            resultSet = db.executeResultSetQuery("SELECT count(dateFound) as quantityfound, extract(month FROM dateFound) as months from foundluggage   WHERE extract(year from dateFound) = '2016' GROUP BY extract(month from dateFound)");
            lostSet = db.executeResultSetQuery("SELECT count(dateLost) as quantitylost, extract(month FROM dateLost) as months from lostluggage WHERE extract(year from dateLost) = '2016'  GROUP BY extract(month from dateLost)");
            retrievedSet = db.executeResultSetQuery("SELECT count(delivery) as quantityretrieved, extract(month FROM dateMatched) as months from matched   WHERE extract(year from dateMatched) = '2016' GROUP BY extract(month from dateMatched)");

            while (resultSet.next()) {
                int countfound = resultSet.getInt("quantityfound");
                int month = resultSet.getInt("months");

                cases(month);
                seriesfound.getData().add(new XYChart.Data<>(mon, countfound));

            }

            while (lostSet.next()) {

                int month = lostSet.getInt("months");
                int countlost = lostSet.getInt("quantitylost");

                cases(month);

                serieslost.getData().add(new XYChart.Data<>(mon, countlost));
            }

            while (retrievedSet.next()) {
                String delivercheck = resultSet.getString("matched.delivery");
                if (!"".equals(delivercheck)) {
                    int month = retrievedSet.getInt("months");
                    int countmatched = retrievedSet.getInt("quantityretrieved");

                    cases(month);

                    seriesmatched.getData().add(new XYChart.Data<>(mon, countmatched));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        seriesfound.setName("Found Luggage 2016");
        serieslost.setName("Lost Luggage 2016");
        seriesmatched.setName("Retrieved Luggage 2016");
        lineChart.getData().add(seriesfound);
        lineChart.getData().add(serieslost);
        lineChart.getData().add(seriesmatched);

    }

    @FXML
    public void fifteen(ActionEvent event) throws IOException {
        XYChart.Series<String, Number> seriesfound = new XYChart.Series<>();
        XYChart.Series<String, Number> serieslost = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesmatched = new XYChart.Series<>();
        lineChart.getData().clear();
        try {

            ResultSet resultSet;
            ResultSet lostSet;
            ResultSet retrievedSet;

            resultSet = db.executeResultSetQuery("SELECT count(dateFound) as quantityfound, extract(month FROM dateFound) as months from foundluggage   WHERE extract(year from dateFound) = '2015' GROUP BY extract(month from dateFound)");
            lostSet = db.executeResultSetQuery("SELECT count(dateLost) as quantitylost, extract(month FROM dateLost) as months from lostluggage WHERE extract(year from dateLost) = '2015'  GROUP BY extract(month from dateLost)");
            retrievedSet = db.executeResultSetQuery("SELECT count(delivery) as quantityretrieved, extract(month FROM dateMatched) as months from matched   WHERE extract(year from dateMatched) = '2015' GROUP BY extract(month from dateMatched)");

            while (resultSet.next()) {
                int countfound = resultSet.getInt("quantityfound");
                int month = resultSet.getInt("months");

                cases(month);
                seriesfound.getData().add(new XYChart.Data<>(mon, countfound));

            }

            while (lostSet.next()) {

                int month = lostSet.getInt("months");
                int countlost = lostSet.getInt("quantitylost");

                cases(month);

                serieslost.getData().add(new XYChart.Data<>(mon, countlost));
            }

            while (retrievedSet.next()) {
                    int month = retrievedSet.getInt("months");
                    int countmatched = retrievedSet.getInt("quantityretrieved");

                    cases(month);

                    seriesmatched.getData().add(new XYChart.Data<>(mon, countmatched));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        seriesfound.setName("Found Luggage 2015");
        serieslost.setName("Lost Luggage 2015");
        seriesmatched.setName("Retrieved Luggage 2015");
        lineChart.getData().add(seriesfound);
        lineChart.getData().add(serieslost);
        lineChart.getData().add(seriesmatched);

    }

    @FXML
    public void sevteen(ActionEvent event) throws IOException {
        XYChart.Series<String, Number> seriesfound = new XYChart.Series<>();
        XYChart.Series<String, Number> serieslost = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesmatched = new XYChart.Series<>();
        lineChart.getData().clear();
        try {

            ResultSet resultSet;
            ResultSet lostSet;
            ResultSet retrievedSet;

            resultSet = db.executeResultSetQuery("SELECT count(dateFound) as quantityfound, extract(month FROM dateFound) as months from foundluggage   WHERE extract(year from dateFound) = '2017' GROUP BY extract(month from dateFound)");
            lostSet = db.executeResultSetQuery("SELECT count(dateLost) as quantitylost, extract(month FROM dateLost) as months from lostluggage WHERE extract(year from dateLost) = '2017'  GROUP BY extract(month from dateLost)");
            retrievedSet = db.executeResultSetQuery("SELECT count(delivery) as quantityretrieved, extract(month FROM dateMatched) as months from matched   WHERE extract(year from dateMatched) = '2017' GROUP BY extract(month from dateMatched)");

            while (resultSet.next()) {
                int countfound = resultSet.getInt("quantityfound");
                int month = resultSet.getInt("months");

                cases(month);
                seriesfound.getData().add(new XYChart.Data<>(mon, countfound));

            }

            while (lostSet.next()) {

                int month = lostSet.getInt("months");
                int countlost = lostSet.getInt("quantitylost");

                cases(month);

                serieslost.getData().add(new XYChart.Data<>(mon, countlost));
            }

            while (retrievedSet.next()) {
                    int month = retrievedSet.getInt("months");
                    int countmatched = retrievedSet.getInt("quantityretrieved");

                    cases(month);

                    seriesmatched.getData().add(new XYChart.Data<>(mon, countmatched));
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        seriesfound.setName("Found Luggage 2017");
        serieslost.setName("Lost Luggage 2017");
        seriesmatched.setName("Retrieved Luggage 2017");
        lineChart.getData().add(seriesfound);
        lineChart.getData().add(serieslost);
        lineChart.getData().add(seriesmatched);

    }

    @FXML
    public void eighteen(ActionEvent event) throws IOException {
        XYChart.Series<String, Number> seriesfound = new XYChart.Series<>();
        XYChart.Series<String, Number> serieslost = new XYChart.Series<>();
        XYChart.Series<String, Number> seriesmatched = new XYChart.Series<>();
        lineChart.getData().clear();
        try {

            ResultSet resultSet;
            ResultSet lostSet;
            ResultSet retrievedSet;

            resultSet = db.executeResultSetQuery("SELECT count(dateFound) as quantityfound, extract(month FROM dateFound) as months from foundluggage   WHERE extract(year from dateFound) = '2018' GROUP BY extract(month from dateFound)");
            lostSet = db.executeResultSetQuery("SELECT count(dateLost) as quantitylost, extract(month FROM dateLost) as months from lostluggage WHERE extract(year from dateLost) = '2018'  GROUP BY extract(month from dateLost)");
            retrievedSet = db.executeResultSetQuery("SELECT count(delivery) as quantityretrieved, matched.delivery as delivery, extract(month FROM dateMatched) as months from matched   WHERE extract(year from dateMatched) = '2018' GROUP BY extract(month from dateMatched)");

            while (resultSet.next()) {
                int countfound = resultSet.getInt("quantityfound");
                int month = resultSet.getInt("months");

                cases(month);
                seriesfound.getData().add(new XYChart.Data<>(mon, countfound));

            }

            while (lostSet.next()) {

                int month = lostSet.getInt("months");
                int countlost = lostSet.getInt("quantitylost");

                cases(month);

                serieslost.getData().add(new XYChart.Data<>(mon, countlost));
            }

            while (retrievedSet.next())  {
                    int month = retrievedSet.getInt("months");
                    int countmatched = retrievedSet.getInt("quantityretrieved");

                    cases(month);

                    seriesmatched.getData().add(new XYChart.Data<>(mon, countmatched));
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(OverviewUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        seriesfound.setName("Found Luggage 2018");
        serieslost.setName("Lost Luggage 2018");
        seriesmatched.setName("Retrieved Luggage 2018");
        lineChart.getData().add(seriesfound);
        lineChart.getData().add(serieslost);
        lineChart.getData().add(seriesmatched);

    }

    public String cases(int month) {

        switch (month) {
            case 1:
                mon = "Jan";
                break;
            case 2:
                mon = "Feb";
                break;
            case 3:
                mon = "Mar";
                break;
            case 4:
                mon = "Apr";
                break;
            case 5:
                mon = "Mei";
                break;
            case 6:
                mon = "Jun";
                break;
            case 7:
                mon = "Jul";
                break;
            case 8:
                mon = "Aug";
                break;
            case 9:
                mon = "Sep";
                break;
            case 10:
                mon = "Okt";
                break;
            case 11:
                mon = "Nov";
                break;
            case 12:
                mon = "Dec";

        }
        return mon;
    }

    @FXML
    protected void naarVermisteOverzichtScherm(ActionEvent event) throws IOException {
        MainApp.switchView("/Views/ManagerHomeView.fxml");
    }

}
