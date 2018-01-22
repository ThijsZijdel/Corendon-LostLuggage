package is103.lostluggage.Model.Service.Interface;

import is103.lostluggage.Model.Service.Model.LostLuggage;
import javafx.collections.ObservableList;

/**
 *
 * @author Thijs Zijdel - 500782165
 */
public interface LostLuggageTable {   
    
    /**  
     * Here will be insured that lost luggage table be set with the right data
     * 
     * @param  list of the type found luggage
     * @void - No direct output 
     * @call - set lostLuggageTable             
     */
    public void setLostLuggageTable(ObservableList<LostLuggage> list);
    
    /**  
     * Here is lost luggage table overview initialized with the right values
     * 
     * @void - No direct output 
     */
    public void initializeLostLuggageTable();
}