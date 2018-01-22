package is103.lostluggage.Model.Service.Data;

import is103.lostluggage.Database.MyJDBC;
import is103.lostluggage.MainApp;
import is103.lostluggage.Model.Service.Model.LostLuggage;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 
 * @author Thijs Zijdel - 500782165
 */
public final class ServiceDataLost {
    //observable lists
    private static ObservableList<LostLuggage> lostLuggageList = FXCollections.observableArrayList(); 
    private static ObservableList<LostLuggage> resultsetList = FXCollections.observableArrayList(); 
    
    private static ResultSet resultSet;
    
    //connection to database
    private static final MyJDBC DB = MainApp.getDatabase();
    
    
    //detailed query that is used on multiple places
    public static final String DETAILED_QUERY =
        "SELECT " +
            "COALESCE(NULLIF(L.registrationNr,''), 'none') as `registrationNr`," +
            "COALESCE(NULLIF(L.dateLost,''), 'unknown') as `dateLost`, " +
            "COALESCE(NULLIF(L.timeLost,''), 'unknown') as `timeLost`, " +
            "COALESCE(NULLIF(L.luggageTag,''), 'unknown') as `luggageTag`,  " +
            "COALESCE(NULLIF(L.luggageType,''), 'unknown') as `luggageType`, " +
            "COALESCE(NULLIF(L.brand,''), 'unknown') as `brand`," +
            "COALESCE(NULLIF(C1."+MainApp.getLanguage()+",''), 'unknown') as `mainColor`,  " +
            "COALESCE(NULLIF(C2."+MainApp.getLanguage()+",''), 'none') as `secondColor`," +
            "COALESCE(NULLIF(L.size,''), 'unknown')	as `size`,  " +
            "COALESCE(NULLIF(L.weight,''), 'unknown') as `weight`," +
            "COALESCE(NULLIF(L.otherCharacteristics,''), 'none') as `otherCharacteristics`," +
            "COALESCE(NULLIF(L.flight,''), 'unknown') as `flight`," +
            "COALESCE(NULLIF(L.employeeId,''), '') as employeeId, "+
            "COALESCE(NULLIF(L.matchedId,''), '') as matchedId, "+
            "COALESCE(NULLIF(L.passengerId,''), 'none') as `passengerId` " +
                "FROM lostluggage AS L " +
                    "LEFT JOIN color AS C1 " +
                    "	ON L.mainColor = C1.ralCode " +
                    "LEFT JOIN color AS C2 " +
                    "	ON L.secondColor = C2.ralCode ";
    /**  
     * Here will the lost luggage list been set with the data from the db
     * This will be checked in a private method
     * 
     * @throws SQLException      getting data from the db
     */   
    public ServiceDataLost() throws SQLException{
        ServiceDataLost.lostLuggageList = getLostLuggageList();
    }
    
    /**  
     * Way of getting the lost luggage list
     * If the list isn't set, a empty list will be returned 
     * 
     * @return ObservableList      of the type: lost luggage  
     */
    public static ObservableList<LostLuggage> getLostLuggage(){
        return ServiceDataLost.lostLuggageList;
    }
    
    /**  
     * Way of getting the lost luggage list
     * If the list isn't set, a empty list will be returned 
     * 
     * @throws SQLException        getting the resultSet from the db
     * @param  id                  registrationNr of the wanted luggage
     * @return ResultSet           resultSet of the right luggage  
     */
    public ResultSet getLostResultSet(String id) throws SQLException{
        return DB.executeResultSetQuery("SELECT * FROM lostluggage WHERE registrationNr='"+id+"';");
    }
 
    /**  
     * Way of getting the lost luggage resultSet for matched 
     * when matched is is true, only matched luggage will be gotten
     * 
     * @throws SQLException        getting the resultSet from the db
     * @param  matched             boolean for getting the matched items or not
     * @return ResultSet           resultSet of the matched of not matched luggage  
     */
    public ResultSet getLostResultSet(Boolean matched) throws SQLException{
        if (matched == true){
            return DB.executeResultSetQuery(
                    DETAILED_QUERY
                    +" WHERE matchedId IS NOT NULL;");
        } else {
            return DB.executeResultSetQuery(
                    DETAILED_QUERY
                    +" WHERE matchedId IS NULL OR matchedId = '0';");
        }
    }
    
    
    /**  
     * Method to get the full details of lost luggage  
     * Note: innerJoints are used for getting the full data of a luggage
     * 
     * @throws SQLException        data will be get from db
     * @param id                   registrationNr of the wanted lost luggage
     * @return resultSet           for the given id
     */ 
    public ResultSet getAllDetailsLost(String id) throws SQLException{
        String language = MainApp.getLanguage();
        return DB.executeResultSetQuery("SELECT " +
            "COALESCE(NULLIF(F.registrationNr,''), 'none') as `F.registrationNr`," +
            "COALESCE(NULLIF(F.dateLost,''), 'unknown') as `F.dateLost`, " +
            "COALESCE(NULLIF(F.timeLost,''), 'unknown') as `F.timeLost`, " +
            "COALESCE(NULLIF(F.luggageTag,''), 'unknown') as `F.luggageTag`,  " +
            "COALESCE(NULLIF(T."+language+",''), 'unknown') as `T."+language+"`, " +
            "COALESCE(NULLIF(F.brand,''), 'unknown') as `F.brand`," +
            "COALESCE(NULLIF(C1."+language+",''), 'unknown') as `C1."+language+"`,  " +
            "COALESCE(NULLIF(C2."+language+",''), 'none') as `C2."+language+"`," +
            "COALESCE(NULLIF(F.size,''), 'unknown')	as `F.size`,  " +
            "COALESCE(NULLIF(F.weight,''), 'unknown') as `F.weight`," +
            "COALESCE(NULLIF(F.otherCharacteristics,''), 'none') as `F.otherCharacteristics`," +
            "COALESCE(NULLIF(F.flight,''), 'unknown') as `F.flight`," +
            "COALESCE(NULLIF(F.passengerId,''), 'none') as `F.passengerId`," +
            "COALESCE(NULLIF(P.name,''), 'unknown')  as `P.name`," +
            "COALESCE(NULLIF(P.address,''), 'unknown') as `P.address`," +
            "COALESCE(NULLIF(P.place,''), 'unknown') as `P.place`," +
            "COALESCE(NULLIF(P.postalcode,''), 'unknown')  as `P.postalcode`," +
            "COALESCE(NULLIF(P.country,''), 'unknown') as `P.country`," +
            "COALESCE(NULLIF(P.email,''), 'unknown') as `P.email`," +
            "COALESCE(NULLIF(P.phone,''), 'unknown') as `P.phone` " +
                "FROM lostluggage AS F " +
                    "LEFT JOIN luggagetype AS T " +
                    "	ON F.luggageType = T.luggageTypeId " +
                    "LEFT JOIN color AS C1 " +
                    "	ON F.mainColor = C1.ralCode " +
                    "LEFT JOIN color AS C2 " +
                    "	ON F.secondColor = C2.ralCode " +
                    "LEFT JOIN passenger AS P " +
                    " ON (F.passengerId = P.passengerId) "+
                        "WHERE registrationNr='"+id+"';");
     }
     
    /**  
     * Here is the given resultSet converted to a list
     * The return type can be initialized (set) in a table
     * 
     * @throws SQLException        data will be get from db
     * @param  resultSet           set from the db that needs to be converted to
     * @return ObservableList      of the type: lost luggage  
     */ 
    public ObservableList<LostLuggage> getObservableList(ResultSet resultSet) throws SQLException{
        //clear the previous list 
        resultsetList.clear();
        
        return resultsetList = loopTroughResultSet(resultSet, false);
    }
    
    /**  
     * Method to get the full list of lost luggage s in the db   
     * Note: the resultSet will be converted to the right list
     * 
     * @throws SQLException        data will be get from db
     * @return ObservableList      of the type: lost luggage  
     */
    public ObservableList<LostLuggage> getLostLuggageList() throws SQLException{
            //get the resultset of all the lost luggage s
            resultSet = DB.executeResultSetQuery(DETAILED_QUERY);
            
            
        //clear previous list -> so there wont be any duplicate luggage
        //ServiceDataLost.lostLuggageList.clear();

        //the full list
        return lostLuggageList = loopTroughResultSet(resultSet, true);
    }
    
    /**  
     * Method where will be looped trough the given resultSet  
     * 
     * @throws SQLException        a resultSet will be read
     * @param resultSet            given resultSet that will be read
     * @param checkIfMatched       if this is true > get only the not matched 
     * @return ObservableList      of the type: lost luggage  
     */
    public static ObservableList<LostLuggage> loopTroughResultSet(
                                            ResultSet resultSet, 
                                            boolean checkIfMatched) throws SQLException{
        //create a temporary list
        ObservableList<LostLuggage> list = FXCollections.observableArrayList(); 
        //loop trough al the results of the resultSet
        while (resultSet.next()) {
            //Set all the columns to the right variables
                String registrationNr =     resultSet.getString("registrationNr");
                String dateLost =           resultSet.getString("dateLost");
                String timeLost =           resultSet.getString("timeLost");

                String luggageTag =         resultSet.getString("luggageTag");
                int luggageType =           resultSet.getInt("luggageType");
                String brand =              resultSet.getString("brand");
                String mainColor =          resultSet.getString("mainColor");
                String secondColor =        resultSet.getString("secondColor");
                String size =               resultSet.getString("size");
                String weight =                resultSet.getString("weight");   
                String otherCharacteristics=resultSet.getString("otherCharacteristics");
                int passengerId =           resultSet.getInt("passengerId");
                
                String flight =             resultSet.getString("flight"); 
                String employeeId =         resultSet.getString("employeeId");
                int matchedId =             resultSet.getInt("matchedId");
            
            //if check matched is true, filter the result
            if (checkIfMatched == true){
                //if the match id is unasigned put the luggage in the list
                if (matchedId == 0 || "".equals(matchedId)) {
                    //add the data in a found luggage objects and put that in the list 
                    list.add(
                        new LostLuggage(
                                registrationNr, 
                                dateLost, 
                                timeLost, 
                                
                                luggageTag, 
                                luggageType, 
                                brand, 
                                mainColor, 
                                secondColor, 
                                size, 
                                weight, 
                                otherCharacteristics, 
                                passengerId, 
                                
                                flight, 
                                employeeId, 
                                matchedId
                            )); 
                    } else {
                        //Luggage is already matched
                    }
            } else {
                list.add(
                    new LostLuggage(
                        registrationNr, 
                        dateLost, 
                        timeLost, 

                        luggageTag, 
                        luggageType, 
                        brand, 
                        mainColor, 
                        secondColor, 
                        size, 
                        weight, 
                        otherCharacteristics, 
                        passengerId, 

                        flight, 
                        employeeId, 
                        matchedId
                    ));
            }
        }
        return list;
    }
}
