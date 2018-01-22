
package is103.lostluggage.Model.Service.Model;


/**
 * This class is for structuring the way a LostLuggage objects is.
 * The fields are for a equal to the data on the database.
 * 
 * @author Thijs Zijdel - 500782165
 */
public class LostLuggage {
     private String  
            registrationNr, 
            dateLost, 
            timeLost, 
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
            flight,
         
            employeeId;
    
    private int
            matchedId;

    public LostLuggage(
            String registrationNr, 
            String dateLost, 
            String timeLost, 
            String luggageTag, 
            int luggageType, 
            String brand, 
            String mainColor, 
            String secondColor, 
            String size, 
            String weight, 
            String otherCharaccteristics, 
            int passengerId, 
            String flight, 
            String employeeId, 
            int matchedId) {
        this.registrationNr = registrationNr;
        this.dateLost = dateLost;
        this.timeLost = timeLost;
        this.luggageTag = luggageTag;
        this.luggageType = luggageType;
        this.brand = brand;
        this.mainColor = mainColor;
        this.secondColor = secondColor;
        this.size = size;
        this.weight = weight;
        this.otherCharaccteristics = otherCharaccteristics;
        this.passengerId = passengerId;
        this.flight = flight;
        this.employeeId = employeeId;
        this.matchedId = matchedId;
    }
    


    

    public LostLuggage() {
        
    }

    /**
     * @return the registrationNr
     */
    public String getRegistrationNr() {
        return registrationNr;
    }

    /**
     * @return the dateLost
     */
    public String getDateLost() {
        return dateLost;
    }

    /**
     * @return the timeLost
     */
    public String getTimeLost() {
        return timeLost;
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
     * @return the flight
     */
    public String getFlight() {
        return flight;
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