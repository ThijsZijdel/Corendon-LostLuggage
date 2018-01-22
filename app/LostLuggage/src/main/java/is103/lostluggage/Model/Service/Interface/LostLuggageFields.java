package is103.lostluggage.Model.Service.Interface;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Thijs Zijdel - 500782165
 */
public interface LostLuggageFields {
    
    /**  
     * Method to get a lost luggage resultSet 
     * Open for extensions
     * 
     * @return resultSet                resultSet for the wanted luggage
     * @throws java.sql.SQLException    getting the data from an database
     */ 
    public ResultSet getManualLostLuggageResultSet() throws SQLException;
    
    /**  
     * Method to set lost luggage fields with a given resultSet
     * Open for extensions
     * 
     * @param  resultSet                will be set on the fields
     * @void   no direct output         can be on different ways  
     * @throws java.sql.SQLException    getting the data from an database
     */
    public void setLostFields(ResultSet resultSet) throws SQLException;
}
