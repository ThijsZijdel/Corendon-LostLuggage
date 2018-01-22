package is103.lostluggage.Model.Service.Interface;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thijs Zijdel - 500782165
 */
public interface FoundLuggageFields {
    
    /**  
     * Method to get a found luggage resultSet 
     * Open for extensions
     * 
     * @return resultSet                resultSet for the wanted luggage
     * @throws java.sql.SQLException    getting the data from an database
     */ 
    public ResultSet getManualFoundLuggageResultSet() throws SQLException;
    
    /**  
     * Method to set found luggage fields with a given resultSet
     * Open for extensions
     * 
     * @param  resultSet                will be set on the fields
     * @void   no direct output         can be on different ways  
     * @throws java.sql.SQLException    getting the data from an database
     */ 
    public void setFoundFields(ResultSet resultSet) throws SQLException;
    
}
