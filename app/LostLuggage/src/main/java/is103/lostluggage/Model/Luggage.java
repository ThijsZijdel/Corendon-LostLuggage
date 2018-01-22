
package is103.lostluggage.Model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Doel:
 * @author gebruiker
 */
public class Luggage {
private SimpleStringProperty 
            idmissedLuggage, 
            timeField, 
            airportField, 
            dateDatepicker;
    
    private SimpleStringProperty 
            nameField, 
            addressField, 
            residenceField, 
            postalcodeField, 
            countryField, 
            emailField;
    
    private SimpleStringProperty 
            labelnumberField, 
            flightnumberField, 
            destinationField;
    

    private SimpleStringProperty 
            typeField,
            brandField,
            colorField,
            signaturesField;

    /**
     *
     * @param idmissedLuggage
     * @param timeField
     * @param airportField
     * @param dateDatepicker
     * @param nameField
     * @param addressField
     * @param residenceField
     * @param postalcodeField
     * @param countryField
     * @param emailField
     * @param labelnumberField
     * @param flightnumberField
     * @param destinationField
     * @param typeField
     * @param brandField
     * @param colorField
     * @param signaturesField
     */
    public Luggage(
            String idmissedLuggage, 
            String timeField, 
            String airportField, 
            String dateDatepicker, 
            String nameField, 
            String addressField, 
            String residenceField, 
            String postalcodeField, 
            String countryField, 
            String emailField, 
            String labelnumberField, 
            String flightnumberField, 
            String destinationField, 
            String typeField, 
            String brandField, 
            String colorField, 
            String signaturesField) {
        this.idmissedLuggage =  new SimpleStringProperty(idmissedLuggage);
        this.timeField =        new SimpleStringProperty(timeField);
        this.airportField =     new SimpleStringProperty(airportField);
        this.dateDatepicker =   new SimpleStringProperty(dateDatepicker);
        this.nameField =        new SimpleStringProperty(nameField);
        this.addressField =     new SimpleStringProperty(addressField);
        this.residenceField =   new SimpleStringProperty(residenceField);
        this.postalcodeField =  new SimpleStringProperty(postalcodeField);
        this.countryField =     new SimpleStringProperty(countryField);
        this.emailField =       new SimpleStringProperty(emailField);
        this.labelnumberField = new SimpleStringProperty(labelnumberField);
        this.flightnumberField = new SimpleStringProperty(flightnumberField);
        this.destinationField = new SimpleStringProperty(destinationField);
        this.typeField =        new SimpleStringProperty(typeField);
        this.brandField =       new SimpleStringProperty(brandField);
        this.colorField =       new SimpleStringProperty(colorField);
        this.signaturesField =  new SimpleStringProperty(signaturesField);
    }

    public Luggage(int i, String a392D4K, String tomos, String trolley, String d383D, String amsterdam_Airport, String rode_sticker, String reiziger) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the idmissedLuggage
     */
    public SimpleStringProperty getIdmissedLuggage() {
        return idmissedLuggage;
    }

    /**
     * @param idmissedLuggage the idmissedLuggage to set
     */
    public void setIdmissedLuggage(SimpleStringProperty idmissedLuggage) {
        this.idmissedLuggage = idmissedLuggage;
    }

    /**
     * @return the timeField
     */
    public SimpleStringProperty getTimeField() {
        return timeField;
    }

    /**
     * @param timeField the timeField to set
     */
    public void setTimeField(SimpleStringProperty timeField) {
        this.timeField = timeField;
    }

    /**
     * @return the airportField
     */
    public SimpleStringProperty getAirportField() {
        return airportField;
    }

    /**
     * @param airportField the airportField to set
     */
    public void setAirportField(SimpleStringProperty airportField) {
        this.airportField = airportField;
    }

    /**
     * @return the dateDatepicker
     */
    public SimpleStringProperty getDateDatepicker() {
        return dateDatepicker;
    }

    /**
     * @param dateDatepicker the dateDatepicker to set
     */
    public void setDateDatepicker(SimpleStringProperty dateDatepicker) {
        this.dateDatepicker = dateDatepicker;
    }

    /**
     * @return the nameField
     */
    public SimpleStringProperty getNameField() {
        return nameField;
    }

    /**
     * @param nameField the nameField to set
     */
    public void setNameField(SimpleStringProperty nameField) {
        this.nameField = nameField;
    }

    /**
     * @return the addressField
     */
    public SimpleStringProperty getAddressField() {
        return addressField;
    }

    /**
     * @param addressField the addressField to set
     */
    public void setAddressField(SimpleStringProperty addressField) {
        this.addressField = addressField;
    }

    /**
     * @return the residenceField
     */
    public SimpleStringProperty getResidenceField() {
        return residenceField;
    }

    /**
     * @param residenceField the residenceField to set
     */
    public void setResidenceField(SimpleStringProperty residenceField) {
        this.residenceField = residenceField;
    }

    /**
     * @return the postalcodeField
     */
    public SimpleStringProperty getPostalcodeField() {
        return postalcodeField;
    }

    /**
     * @param postalcodeField the postalcodeField to set
     */
    public void setPostalcodeField(SimpleStringProperty postalcodeField) {
        this.postalcodeField = postalcodeField;
    }

    /**
     * @return the countryField
     */
    public SimpleStringProperty getCountryField() {
        return countryField;
    }

    /**
     * @param countryField the countryField to set
     */
    public void setCountryField(SimpleStringProperty countryField) {
        this.countryField = countryField;
    }

    /**
     * @return the emailField
     */
    public SimpleStringProperty getEmailField() {
        return emailField;
    }

    /**
     * @param emailField the emailField to set
     */
    public void setEmailField(SimpleStringProperty emailField) {
        this.emailField = emailField;
    }

    /**
     * @return the labelnumberField
     */
    public SimpleStringProperty getLabelnumberField() {
        return labelnumberField;
    }

    /**
     * @param labelnumberField the labelnumberField to set
     */
    public void setLabelnumberField(SimpleStringProperty labelnumberField) {
        this.labelnumberField = labelnumberField;
    }

    /**
     * @return the flightnumberField
     */
    public SimpleStringProperty getFlightnumberField() {
        return flightnumberField;
    }

    /**
     * @param flightnumberField the flightnumberField to set
     */
    public void setFlightnumberField(SimpleStringProperty flightnumberField) {
        this.flightnumberField = flightnumberField;
    }

    /**
     * @return the destinationField
     */
    public SimpleStringProperty getDestinationField() {
        return destinationField;
    }

    /**
     * @param destinationField the destinationField to set
     */
    public void setDestinationField(SimpleStringProperty destinationField) {
        this.destinationField = destinationField;
    }

    /**
     * @return the typeField
     */
    public SimpleStringProperty getTypeField() {
        return typeField;
    }

    /**
     * @param typeField the typeField to set
     */
    public void setTypeField(SimpleStringProperty typeField) {
        this.typeField = typeField;
    }

    /**
     * @return the brandField
     */
    public SimpleStringProperty getBrandField() {
        return brandField;
    }

    /**
     * @param brandField the brandField to set
     */
    public void setBrandField(SimpleStringProperty brandField) {
        this.brandField = brandField;
    }

    /**
     * @return the colorField
     */
    public SimpleStringProperty getColorField() {
        return colorField;
    }

    /**
     * @param colorField the colorField to set
     */
    public void setColorField(SimpleStringProperty colorField) {
        this.colorField = colorField;
    }

    /**
     * @return the signaturesField
     */
    public SimpleStringProperty getSignaturesField() {
        return signaturesField;
    }

    /**
     * @param signaturesField the signaturesField to set
     */
    public void setSignaturesField(SimpleStringProperty signaturesField) {
        this.signaturesField = signaturesField;
    }



    
    

    
}
