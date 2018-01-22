package is103.lostluggage.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * This class is for different types of validation for multiple types of data
 * 
 * @author Thijs Zijdel - 500782165
 */
public class Validate {
    /** 
     * In this method will Integers be checked for preventing wrong input's
     * 
     * @param value the String that needs to be checked
     * @return a valid Integer, note this can be zero if the value was not valid
     **/
    public static Integer isValidInt(String value) {
        try {
          return Integer.parseInt(value);
        } catch (NumberFormatException e) {
          return 0;
        }
    }
    /** 
     * In this method will Dates be checked for preventing wrong input's
     * Note: Also a check for wrong formats --> value 08/13/2018 --> return null
     * 
     * @param value the String that needs to be checked
     * @return a valid String date, note null will be returned when value was not valid
     **/
    public static String isValidDate(String value) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            df.parse(value);
            return value;
        } catch (ParseException e) {
            return null;
        }
    }
    /** 
     * In this method will the Time be checked for preventing wrong input's
     * Note: Also a check for wrong formats --> value = 10:61  --> return = null
     * 
     * @param value the String that needs to be checked
     * @return a valid String time, note null will be returned when value was not valid
     **/
    public static String isValidTime(String value){
        try {
            LocalTime.parse(value);
            return value;
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }    
}
