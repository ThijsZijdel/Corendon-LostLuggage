package is103.lostluggage.Model.Service.Instance.Details;

import is103.lostluggage.Model.Service.Model.FoundLuggage;


/**
 * This class is for passing the Found Luggage details
 * Note: this is used by the detailed pop up (found luggage) view
 * 
 * @author Thijs Zijdel - 500782165
 */
public class FoundLuggageDetailsInstance {
 
    /**
     * Create one instance of this class
     */
    private final static FoundLuggageDetailsInstance instance = new FoundLuggageDetailsInstance();

    /**
     * Way of getting the private instance of this class and:
     * The found luggage that is callable by the next method.
     * 
     * @return the one and only instance of this class.
     */
    public static FoundLuggageDetailsInstance getInstance() {
        return instance;
    }

    /**
     * Create one found luggage object
     * Note: the details are being passed trough this instance.
     */
    private FoundLuggage luggage = new FoundLuggage();

    /**
     * Method for getting the current (found) luggage of:
     * the one and only instance of this class.
     * 
     * @return the current found luggage
     */
    public FoundLuggage currentLuggage() {
        return luggage;
    }
    
    

}
