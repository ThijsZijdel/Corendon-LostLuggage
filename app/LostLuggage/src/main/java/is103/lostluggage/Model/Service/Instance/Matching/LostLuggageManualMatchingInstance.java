package is103.lostluggage.Model.Service.Instance.Matching;

import is103.lostluggage.Model.Service.Model.LostLuggage;

/**
 * This class is for passing the Lost Luggage manual matching instance
 * Note: this is used by the manual matching lost on the matching view
 * 
 * @author Thijs Zijdel - 500782165
 */
public class LostLuggageManualMatchingInstance {
    /**
     * Create one instance of this class
     */
    private final static LostLuggageManualMatchingInstance instance = new LostLuggageManualMatchingInstance();
    
    /**
     * Way of getting the private instance of this class and:
     * The lost luggage that is callable by the next method.
     * 
     * @return the one and only instance of this class.
     */
    public static LostLuggageManualMatchingInstance getInstance() {
        return instance;
    }
    
    /**
     * Create one lost luggage object
     * Note: the details are being passed trough this instance.
     */
    private LostLuggage luggage = new LostLuggage();

    /**
     * Method for getting the current (lost) luggage of:
     * the one and only instance of this class.
     * 
     * @return the current lost luggage
     */
    public LostLuggage currentLuggage() {
        return luggage;
    }
}
