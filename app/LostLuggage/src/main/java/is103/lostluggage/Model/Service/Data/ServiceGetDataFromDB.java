package is103.lostluggage.Model.Service.Data;

import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Thijs Zijdel - 500782165
 */
public class ServiceGetDataFromDB {
    private final String TABLE;
    private final String FIELD;
    private final String CONDITION;
    private ResultSet resultSet;
    
    private ObservableList<String> results = FXCollections.observableArrayList();
    
    //conection to the db
    private final MyJDBC db = MainApp.getDatabase();  
    
    //index used for string array
    private int index = 0;
    
    //constructor for the sql query
    public ServiceGetDataFromDB(String table, String field, String condition){
        this.TABLE = table;
        this.FIELD = field;
        this.CONDITION = condition;
    }

    /**  
     * Getting the resultSet based on the objects attributes 
     * 
     * @throws SQLException        getting the resultSet from the db
     * @return ResultSet           the right resultSet   
     */
    public ResultSet getServiceDetailsResultSet() throws SQLException{    
        if (CONDITION == null){
            //if there is no condition set:
                //get the objects fields from the objects table
            resultSet = db.executeResultSetQuery("SELECT "+FIELD+" FROM "+TABLE+";");
        } else if ( CONDITION.toUpperCase().contains("WHERE".toUpperCase()) ){
            //if the condition is a where statement:
                //execute this query
            resultSet = db.executeResultSetQuery("SELECT "+FIELD+" FROM "+TABLE+" "+CONDITION+";");
        } else if ("all".equals(CONDITION) && CONDITION != null|| "*".equals(CONDITION) && CONDITION != null){
            //if condition is set to all (different ways):
                //get all the data from the table
                resultSet = db.executeResultSetQuery("SELECT * FROM "+TABLE+";");
        } else {
            //else:
                //execute the normal query
            resultSet = db.executeResultSetQuery("SELECT "+FIELD+" FROM "+TABLE+" "+CONDITION+";");
        }
        //return the resultset
        return resultSet;
    }
    
    /**  
     * Getting a ObservableStringList based on the objects resultSet  
     * 
     * @throws SQLException             getting the resultSet from the object
     * @return ObservableList< String>  objects data in a observable string list   
     */
    public ObservableList<String> getStringList() throws SQLException{ 
        //getting the resultset
        ResultSet resultGetSetString = getServiceDetailsResultSet();
        
        //loop trough al the items in the resultSet
        while (resultGetSetString.next()){
            //get the data from the objects fields
            String result =     resultSet.getString(FIELD);
            //add this result to the string list
            results.add(result);
        }
        
        return results;
    }
    
    /**  
     * Getting a StringArray based on the objects resultSet  
     * 
     * @throws SQLException       getting the resultSet from the object
     * @return String[]           objects data in a string array     
     */
    public String[] getStringArrayList() throws SQLException{
        //getting the resultset
        results = getStringList();
        
        //make a new string[] with an index of the hits
        //> methode that count the fields
        String[] resultArray = new String[countHits()];
        
        //loop trough all the items of the resultSet
        results.forEach((String) -> {
            //get the right index of the array
            this.index++;
            
            //try to fill this index with the right data from the resultset
            try {
                resultArray[this.index]= resultSet.getString(FIELD);
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDataFromDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        return resultArray;         
    }
    
    /**  
     * Getting total amount of hits from the objects attributes
     * 
     * @throws SQLException       getting the resultSet from the object
     * @return hits               the amount of hits (integer)      
     */
    public int countHits() throws SQLException{
        //get the count resultSet
        resultSet = db.executeResultSetQuery("SELECT COUNT("+FIELD+") as 'hits' FROM "+TABLE+";");
        
        //loop trough all the items of the resultSet
        while (resultSet.next()){
            //get the amounth of hits
             int hits = resultSet.getInt("hits");
             return hits;
        }
        return 0;
    }
    
    /**  
     * Getting the id of the objects field
     * (used for converting   example:  red --> ralCode)
     * 
     * @throws SQLException       getting the resultSet from the object
     * @return gotten             the id of the wanted field     
     */
    public int getIdValue() throws SQLException{
        //get the right resultSetThisField resultSet
        ResultSet resultSetThisField = getServiceDetailsResultSet();
        int gotten = 0;
        
        //loop trough the item
        while (resultSetThisField.next()){
            //get the id 
            gotten = resultSetThisField.getInt(this.FIELD);
        }
        return gotten;
    }
    
    
}
