package is103.lostluggage.Model.Service.Model;

/**
 * This class is for structuring the way a FoundLuggage objects is.
 * The fields are for a equal to the data on the database.
 * 
 * @author Thijs Zijdel - 500782165
 */
public class FoundLuggage {
    private String  
            registrationNr, 
            dateFound, 
            timeFound, 
            luggageTag;
    
    private int 
            luggageType;
    
    private String
            brand,
            mainColor,
            secondColor,     
            weight;
    
    private String 
            size,
            otherCharaccteristics;
    
    private int 
            passengerId;
            
    private String 
            arrivedWithFlight,
            locationFound,
            employeeId;
    
    private int
            matchedId;

    public FoundLuggage(
            String registrationNr, 
            String dateFound, 
            String timeFound, 
            String luggageTag, 
            int luggageType, 
            String brand, 
            String mainColor, 
            String secondColor, 
            String size, 
            String weight, 
            String otherCharaccteristics, 
            int passengerId, 
            String arrivedWithFlight, 
            String locationFound, 
            String employeeId, 
            int matchedId) {
        this.registrationNr = registrationNr;
        this.dateFound = dateFound;
        this.timeFound = timeFound;
        this.luggageTag = luggageTag;
        this.luggageType = luggageType;
        this.brand = brand;
        this.mainColor = mainColor;
        this.secondColor = secondColor;
        this.size = size;
        this.weight = weight;
        this.otherCharaccteristics = otherCharaccteristics;
        this.passengerId = passengerId;
        this.arrivedWithFlight = arrivedWithFlight;
        this.locationFound = locationFound;
        this.employeeId = employeeId;
        this.matchedId = matchedId;
    }


    

    public FoundLuggage() {
        
    }

    /**
     * @return the registrationNr
     */
    public String getRegistrationNr() {
        return registrationNr;
    }

    /**
     * @return the dateFound
     */
    public String getDateFound() {
        return dateFound;
    }

    /**
     * @return the timeFound
     */
    public String getTimeFound() {
        return timeFound;
    }

    /**
     * @return the luggageTag
     */
    public String getLuggageTag() {
        return luggageTag;
    }

    /**
     * @return the luggageType
     */
    public int getLuggageType() {
        return luggageType;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return the mainColor
     */
    public String getMainColor() {
        return mainColor;
    }

    /**
     * @return the secondColor
     */
    public String getSecondColor() {
        return secondColor;
    }

    /**
     * @return the weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @return the otherCharaccteristics
     */
    public String getOtherCharaccteristics() {
        return otherCharaccteristics;
    }

    /**
     * @return the passengerId
     */
    public int getPassengerId() {
        return passengerId;
    }

    /**
     * @return the arrivedWithFlight
     */
    public String getFlight() {
        return arrivedWithFlight;
    }

    /**
     * @return the locationFound
     */
    public String getLocationFound() {
        return locationFound;
    }

    /**
     * @return the employeeId
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * @return the matchedId
     */
    public int getMatchedId() {
        return matchedId;
    }

    /**
     * This method is used by the instance classes.
     * For passing the right luggage id (registrationNr) trough classes
     * 
     * @param registrationNr the registrationNr to set
     */
    public void setRegistrationNr(String registrationNr) {
        this.registrationNr = registrationNr;
    }



}