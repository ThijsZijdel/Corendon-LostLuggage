/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Model.Service.Model;

import is103.lostluggage.MainApp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arthur
 * Descripes a form that will be filled in by an employee.
 * Classes Foundform and Lostform will inherit these methods / properties
 */
public class Form {
    
    //Type of form, found or lost luggage
    private String type = "";
    
    
    //Method to retrieve all the options for the comboboxes from the database.
    //Method could be static.
    public Map<String, ArrayList<String>> getFormOptions() throws SQLException{
        
        //Map that contains all the options for the comboboxes in the forms
        Map<String, ArrayList<String>> formOptions = new HashMap(); 
        
        //Make an arraylist that contains the two form types
        ArrayList<String> formtypes = new ArrayList<>();
        
        formtypes.add("Found"); 
        formtypes.add("Lost");

        
        //Add a new item to the map, key is color as the value will contain an ArrayList of strings that contain the names of the colors
        //Select queries so prepared statement not necessary
        formOptions.put("foundorlost", formtypes);
        formOptions.put("colors", resultSetToArrayList(MainApp.getDatabase().executeResultSetQuery("SELECT * FROM color"), MainApp.getLanguage()));
        formOptions.put("airports", resultSetToArrayList(MainApp.getDatabase().executeResultSetQuery("SELECT * FROM destination"), "airport"));
        formOptions.put("flights", resultSetToArrayList(MainApp.getDatabase().executeResultSetQuery("SELECT * FROM flight"), "flightNr"));
        formOptions.put("luggagetypes", resultSetToArrayList(MainApp.getDatabase().executeResultSetQuery("SELECT * FROM luggagetype"), MainApp.getLanguage()));
        formOptions.put("locations", resultSetToArrayList(MainApp.getDatabase().executeResultSetQuery("SELECT * FROM location"), MainApp.getLanguage()));
        
        //Return the form options
        return formOptions;
    }
    
    
    //get the last registrationNr
    public String getLastId() throws SQLException{
        
        String table;
        
        //determine the table
        if(this.getType().equals("Lost")){
            table = "lostluggage";
        }else{
            table = "foundluggage";
        }
        
        ResultSet result = MainApp.getDatabase().executeResultSetQuery("SELECT registrationNr FROM " + table + " ORDER BY registrationNr DESC LIMIT 1");
      
        //Nog aanpassen.. int is overbodig
        int lastId =  Integer.parseInt(resultSetToArrayList(result, "registrationNr").get(0));
        
        return Integer.toString(lastId);
    }
    
    //Method to convert resultset to arraylist
    private ArrayList<String> resultSetToArrayList(ResultSet result, String column) throws SQLException{

        //The ArrayList that will contain all the values of the resultSet
        ArrayList<String> resultArrayList = new ArrayList<>();

        //Iterate through the resultSet
        while(result.next()){
            resultArrayList.add(result.getString(column));
        }
        
        //Bring the pointer back to the first row
        result.beforeFirst();
        
        //Return the ArrayList
        return resultArrayList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
