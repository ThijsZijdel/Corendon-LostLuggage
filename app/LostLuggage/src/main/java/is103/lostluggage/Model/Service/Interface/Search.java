package is103.lostluggage.Model.Service.Interface;

import java.sql.SQLException;

/**
 *
 * @author Thijs Zijdel - 500782165
 */
public interface Search {
    
    /**  
     * This method is for ensuring that the class has a search functionality
     * 
     * @throws java.sql.SQLException        connection to the database
     * @void - No direct output             searching
     */
    public void search() throws SQLException;

}

