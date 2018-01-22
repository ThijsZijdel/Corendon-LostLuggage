package is103.lostluggage.Model.Service.Model;

/**
 * This class is for structuring the way a MatchedLuggage objects is.
 * The fields are for a part equal to the data on the database.
 * 
 * @author Thijs Zijdel - 500782165
 */
public class MatchLuggage {
    private String  
            registrationNrFound, 
            registrationNrLost,
            luggageTag;
    
    private int 
            matchPercentage;
    
    private String
            luggageType,
            brand,
            mainColor,
            secondColor,
            size,
            weight;
    
    private String
            otherCharaccteristics;
    
    private int
            matchedId;

    public MatchLuggage(
            String registrationNrFound, 
            String registrationNrLost, 
            String luggageTag, 
            int matchPercentage, 
            String luggageType, 
            String brand, 
            String mainColor, 
            String secondColor, 
            String size, 
            String weight, 
            String characcteristics,
            int matchedId) {
        this.registrationNrFound = registrationNrFound;
        this.registrationNrLost = registrationNrLost;
        this.luggageTag = luggageTag;
        this.matchPercentage = matchPercentage;
        this.luggageType = luggageType;
        this.brand = brand;
        this.mainColor = mainColor;
        this.secondColor = secondColor;
        this.size = size;
        this.weight = weight;
        this.otherCharaccteristics = characcteristics;
        this.matchedId = matchedId;
    }

    /**
     * @return the registrationNrFound
     */
    public String getRegistrationNrFound() {
        return registrationNrFound;
    }

    /**
     * @param registrationNrFound the registrationNrFound to set
     */
    public void setRegistrationNrFound(String registrationNrFound) {
        this.registrationNrFound = registrationNrFound;
    }

    /**
     * @return the registrationNrLost
     */
    public String getRegistrationNrLost() {
        return registrationNrLost;
    }

    /**
     * @param registrationNrLost the registrationNrLost to set
     */
    public void setRegistrationNrLost(String registrationNrLost) {
        this.registrationNrLost = registrationNrLost;
    }

    /**
     * @return the luggageTag
     */
    public String getLuggageTag() {
        return luggageTag;
    }

    /**
     * @param luggageTag the luggageTag to set
     */
    public void setLuggageTag(String luggageTag) {
        this.luggageTag = luggageTag;
    }

    /**
     * @return the matchPercentage
     */
    public int getMatchPercentage() {
        return matchPercentage;
    }

    /**
     * @param matchPercentage the matchPercentage to set
     */
    public void setMatchPercentage(int matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    /**
     * @return the luggageType
     */
    public String getLuggageType() {
        return luggageType;
    }

    /**
     * @param luggageType the luggageType to set
     */
    public void setLuggageType(String luggageType) {
        this.luggageType = luggageType;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the mainColor
     */
    public String getMainColor() {
        return mainColor;
    }

    /**
     * @param mainColor the mainColor to set
     */
    public void setMainColor(String mainColor) {
        this.mainColor = mainColor;
    }

    /**
     * @return the secondColor
     */
    public String getSecondColor() {
        return secondColor;
    }

    /**
     * @param secondColor the secondColor to set
     */
    public void setSecondColor(String secondColor) {
        this.secondColor = secondColor;
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return the weight
     */
    public String getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * @return the otherCharaccteristics
     */
    public String getOtherCharaccteristics() {
        return otherCharaccteristics;
    }

    /**
     * @param otherCharaccteristics the otherCharaccteristics to set
     */
    public void setOtherCharaccteristics(String otherCharaccteristics) {
        this.otherCharaccteristics = otherCharaccteristics;
    }


    /**
     * @return the matchedId
     */
    public int getMatchedId() {
        return matchedId;
    }

    /**
     * @param matchedId the matchedId to set
     */
    public void setMatchedId(int matchedId) {
        this.matchedId = matchedId;
    }
    
    
    
    
    
    
}
