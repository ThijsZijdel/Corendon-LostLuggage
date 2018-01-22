package is103.lostluggage.Model.Service.Data;

import is103.lostluggage.Controllers.Service.ServiceOverviewFoundViewController;
import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Model.FoundLuggage;
import is103.lostluggage.Model.Service.Model.LostLuggage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Thijs Zijdel - 500782165
 */
public class ServiceSearchData {
    //create a new resultset
    ResultSet searchResultSet;
    
    //connection to database
    private static final MyJDBC DB = MainApp.getDatabase();      
    
    private final int ZERO_CARACTERS = 0;
    
    /**  
     * This method is for converting/ reading the passed resultSet to a list 
     * 
     * @param resultSet     that will be read                 
     * @return List         list of the items from the resultSet (type: found)
     */
    public ObservableList<FoundLuggage> getFoundLuggageSearchList(ResultSet resultSet){
        ServiceDataFound dataList;
        try { 
            dataList = new ServiceDataFound();
            return dataList.getObservableList(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOverviewFoundViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /** 
     * This method is for converting/ reading the passed resultSet to a list 
     * 
     * @param resultSet     that will be read                 
     * @return List         list of the items from the resultSet (type: lost)
     */
    public ObservableList<LostLuggage> getLostLuggageSearchList(ResultSet resultSet){
        ServiceDataLost dataList;
        try { 
            dataList = new ServiceDataLost();
            return dataList.getObservableList(resultSet);
        } catch (SQLException ex) {
            Logger.getLogger(ServiceOverviewFoundViewController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /** 
     * This method is for getting the right search query, based on the user input
     * 
     * @param search        a string that will be searched and converted in the query
     * @param value         this is a potential filter that the user can select      
     * @param luggageType   is also the table that needs to be searched
     * @return Query        the right query based on the input that can be executed 
     * @throws java.sql.SQLException  for making the final query it already needs data
     */
    public String getSearchQuery(String value, String search, String luggageType) throws SQLException{
        //final query that will be set
        String finalQuery;
        
        //check if there isn't a value set
        value = value.toLowerCase();
        if ("".equals(value) || value == null){
            //if this is the case, filter for 'all fields'
            value = "all fields";
        }
        
        //create the first part for the query 
        //Note: tablename will be replaced
        String query = "SELECT * FROM tablename WHERE (";
        
        //check the type for maybe using a specific query over the normal one
        if ("foundluggage".equals(luggageType)){
            //if this is true than use de detailed query from ServiceDataFound
            query = ServiceDataFound.DETAILED_QUERY+" WHERE (";
        } else if ("lostluggage".equals(luggageType)){
            //else if this is true than use de detailed query from ServiceDataLost
            query = ServiceDataLost.DETAILED_QUERY+" WHERE (";
        }
        
        //check if the search value doesn't contain harmfull caracters.
        //Since using a full prepared statement here is way to complex..
        if (!search.contains(";") && !search.contains("`") 
            && !search.contains("'") && !search.contains(")")
            && !search.contains("]") && !search.contains("(")
            && !search.contains("%") && !search.contains("&")
            && !search.contains("*") && !search.contains("!") 
            && search.trim().length() > 0) {
            //no harmfull caracters in the search so:
            
            //switch over the filter (value) for getting the query
            query = switchOverFilter(query, value, search, luggageType);
            
                //if there isn't a statement (OR) set
            if (!query.contains("OR")){
                //query = query.substring(0,(query.indexOf("WHERE")));
                //add a random statement
                query += "mainColor LIKE '%99999%'";
            } else {
                //get te substring to the last OR
                query = query.substring(0,query.lastIndexOf("OR"));
            }
            //close the query
            query += ") ;";

            //set the right tablename on the query
            query = query.replaceAll("tablename", luggageType.toLowerCase());

            //replace' -> with the right search input
            finalQuery = query.replaceAll("replace", search); 
        
        } else { //the users inputted search value can be harmfull
            
            //check if it was just empty
            if (search.trim().length() <= ZERO_CARACTERS){
                //search was empty 
                System.out.println("Nothing searched for.");
            } else {
                //harmfull characters used in the search
                System.out.println("Harmfull caracters in the search used");
            }
            
            //backup for when there isn't a luggage type given.
            if (null == luggageType){
                luggageType = "foundluggage";
                System.out.println("No luggage type defined.");
            } 
            
            //switch over the right luggage type to define the full query
            //because of a wrong user search input
            switch (luggageType) {
                case "foundluggage":
                    //if this is true than use de detailed query from ServiceDataFound
                    finalQuery = ServiceDataFound.DETAILED_QUERY+" ;";
                    break;
                case "lostluggage":
                    //else if this is true than use de detailed query from ServiceDataLost
                    finalQuery = ServiceDataLost.DETAILED_QUERY+" ;";
                    break;
                default:
                    //else get data from <type> tabel
                    finalQuery = "SELECT * FROM "+luggageType;
                    break;
            }
        }
        //return the final created query
        return finalQuery;
    }

    /** 
     * This method is for generating the right color search query, based on the user input
     * 
     * @param search        a string that will be searched and converted in the query    
     * @return statement    the right query statement (part) for the colors
     * @throws java.sql.SQLException  checking in the db
     */
    private String generateColorStatement(String search) throws SQLException{
        //create a temporary query (String) that will be returned
        String generatedStatement = "";
        
        //create an other temporary string that will be checked in the db
        //for searching trough all the ralCodes and language's possibilities
        String colorQuery = "SELECT ralCode FROM color WHERE "
                + " english LIKE '%replace%' OR "
                + " dutch LIKE '%replace%' OR "
                + " ralCode LIKE '%replace%';";

        //replace the parts based on the user input
        colorQuery = colorQuery.replaceAll("replace", search);

        //get the resultSet of the input
        ResultSet resultSetColor = DB.executeResultSetQuery(colorQuery);
        
        //create a temporary list where the results (ralCode) will be put in
        ObservableList<String> stringList =  FXCollections.observableArrayList(); 

        //loop trough the resultSet
        while (resultSetColor.next()){
            //if there is an result get the ralCode 
            String colorId = resultSetColor.getString("ralCode");
            //add this to the string List
            stringList.add(colorId);
        }
        
        //loop trough evry item in the colorList and add the result to the query 
        for (String colorListItem : stringList) {
            generatedStatement += " mainColor LIKE '%"+colorListItem+"%' OR "
                   + " secondColor LIKE '%"+colorListItem+"%' OR ";          
        }
        
        return generatedStatement;
    }
    
    /** 
     * This method is for generating the right passenger search query, based on the user input
     * 
     * @param search        a string that will be searched and converted in the query    
     * @return statement    the right query statement (part) for the passenger
     * @throws java.sql.SQLException  checking in the db
     */
    private String generatePassengerStatement(String search) throws SQLException{
        //create a temporary query (String) that will be returned
        String generatedStatement = "";
        
        //create an other temporary string that will be checked 
        //for searching trough all the passenger detail possibilities in the db
        String passengerQuery = "SELECT passengerId FROM passenger WHERE "
                + " name LIKE '%replace%' OR "
                + " address LIKE '%replace%' OR "
                + " place LIKE '%replace%' OR "
                + " address LIKE '%replace%' OR "
                + " place LIKE '%replace%' OR "
                + " postalcode LIKE '%replace%' OR "
                + " country LIKE '%replace%' OR "
                + " email LIKE '%replace%' OR "
                + " phone LIKE '%replace%';";

        //replace the parts based on the user input
        passengerQuery = passengerQuery.replaceAll("replace", search);

        //get the resultSet of the input
        ResultSet resultSetPassenger = DB.executeResultSetQuery(passengerQuery);
        
        //create a temporary list where the results (passengerId) will be put in
        ObservableList<String> stringListPassenger =  FXCollections.observableArrayList(); 

        //loop trough the resultSet
        while (resultSetPassenger.next()){
            //if there is an result get the passengerId 
            String colorId = resultSetPassenger.getString("passengerId");
            //add this to the string List
            stringListPassenger.add(colorId);
        }
        
        //loop trough evry item in the colorList and add the result to the query 
        for (String passengerListItem : stringListPassenger) {
            generatedStatement += " passengerId LIKE '"+passengerListItem+"' OR ";          
        }

        return generatedStatement;
    }
    
    /** 
     * This method is for generating the right location search query, based on the user input
     * 
     * @param search        a string that will be searched and converted in the query    
     * @return statement    the right query statement (part) for the location
     * @throws java.sql.SQLException  checking in the db
     */
    private String generateLocationStatement(String search) throws SQLException{
        //create a temporary query (String) that will be returned
        String generatedStatement = "";
        
        //create an other temporary string that will be checked 
        //for searching trough all the location detail possibilities in the db
        String locationQuery = "SELECT locationId FROM location WHERE "
                + " english LIKE '%replace%' OR "
                + " dutch LIKE '%replace%' OR "
                + " locationId LIKE '%replace%';";
        
        //replace the parts based on the user input
        locationQuery = locationQuery.replaceAll("replace", search);

        //get the resultSet of the input
        ResultSet resultSetLocation = DB.executeResultSetQuery(locationQuery);
        
        //create a temporary list where the results (passengerId) will be put in
        ObservableList<String> stringListLocation =  FXCollections.observableArrayList(); 

        //loop trough the resultSet
        while (resultSetLocation.next()){
            //if there is an result get the locationId 
            String colorId = resultSetLocation.getString("locationId");
            //add this to the string List
            stringListLocation.add(colorId);
        }

        //loop trough evry item in the colorList and add the result to the query 
        for (String locationListItem : stringListLocation) {
            generatedStatement += " locationId LIKE '"+locationListItem+"' OR ";          
        }

        return generatedStatement;
    }

    /**
     * This method is for splitting the getSearchQuery apart.
     * This is done by moving the switch part of the function to here.
     * Note: for this reason are all the already given parameters needed.
     * 
     * @param query        were the first part is already defined
     * @param value        of the column filter value.
     * @param search       (users s.) for creating the color and passenger query
     * @param luggageType  found luggage or lost luggage
     * @return created Q:  the query that is created in this method
     * @throws SQLException for getting the color, passenger and location query
     */
    private String switchOverFilter(String query, String value, String search, String luggageType) throws SQLException{
        //switch over the given filter value
        switch (value) {
            case "all fields":
                //needs to be searched on all fields so:
                //add to the query the normal fields (/columns)
                query += "registrationNr LIKE '%replace%' OR "
                    + " luggageTag LIKE '%replace%' OR "
                    + " brand LIKE '%replace%' OR "
                    + " mainColor LIKE '%replace%' OR "
                    + " otherCharacteristics LIKE '%replace%' OR ";
                //add the color search part            
                query += generateColorStatement(search);
                //add the passenger search part
                query += generatePassengerStatement(search);

                if ("foundluggage".equals(luggageType)){
                    query += generateLocationStatement(search);
                }
                break;
            case "registrationnr":
                query += "registrationNr LIKE '%replace%' OR ";
                break;
            case "luggagetag":
                query += "luggageTag LIKE '%replace%' OR ";
                break;    
            case "brand":
                query += "brand LIKE '%replace%' OR";
                break;
            case "color":
                //generate the color query
                query += generateColorStatement(search);

                break;
            case "characteristics":
                query += "otherCharacteristics LIKE '%replace%' OR ";
                break;
            case "weight":
                query += "weight LIKE '%replace%' OR ";
                break;
            case "date":
                    //if there wasnt a type set (null) use normal
                    if (null == luggageType.toLowerCase()){
                        query = "date LIKE '%replace%' OR "
                                + " time LIKE '%replace%' OR ";
                    //else switch    
                    } else switch (luggageType.toLowerCase()) {
                        case "foundluggage":
                            query += "dateFound LIKE '%replace%' OR "
                                    + " timeFound LIKE '%replace%' OR ";
                            break;
                        case "lostluggage":
                            query += "dateLost LIKE '%replace%' OR "
                                    + " timeLost LIKE '%replace%' OR ";
                            break;
                        default: //if there was a type set, but not right/ specific
                            query = "date LIKE '%replace%' OR "
                                    + " time LIKE '%replace%' OR ";
                            break;
                    }
                break;
            case "passenger":
                //generate the passenger query
                query += generatePassengerStatement(search);

                break;
            case "location":
                //generate the location query
                query += generateLocationStatement(search);

                break;
            default:
                //default basic where statement
                query += "registrationNr LIKE '%replace%' OR "
                    + " luggageTag LIKE '%replace%' OR "
                    + " brand LIKE '%replace%' OR "
                    + " mainColor LIKE '%replace%' OR "
                    + " otherCharacteristics LIKE '%replace%' OR ";
        }
        //return the created query
        return query;
    }
}
