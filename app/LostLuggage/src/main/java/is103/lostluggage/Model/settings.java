package is103.lostluggage.Model;

import com.jfoenix.controls.JFXButton;
import is103.lostluggage.MainApp;
import javafx.fxml.FXML;

/**
 *
 */
public class settings {
    private static int language_index = 0;
        
    public static String getLanguageFor(int index, String[][] languageArray){
        String language = MainApp.getLanguage();
        
        if (language.equals("dutch")){
            language_index = 0;
        } else if (language.equals("english")){
            language_index = 1;
        }
        return languageArray[language_index][index];
    }

    public static String[][] serviceHomeLanguage(){
        //amount of language fields
        int amount_of_fields = 4;
        String[][] languageArray = new String[2][amount_of_fields+1];
        
            //set al the data for the getLanguageFor string array
            languageArray[0][0] = "Service home pagina";
            languageArray[0][1] = "Gevonden bagage overzicht";
            languageArray[0][2] = "Verloren bagage overzicht";
            languageArray[0][3] = "Overeenkomende Bagage >";
            languageArray[0][4] = "Bagage invoeren +";
            
            languageArray[1][0] = "Service Home";
            languageArray[1][1] = "Overview Found Luggage";
            languageArray[1][2] = "Overview Lost Luggage";
            languageArray[1][3] = "Luggage Matching >";
            languageArray[1][4] = "Input Luggage +";
//        String[][] languageArray = {
//            {"Service home pagina","Service Home"},
//            {"Gevonden","found"},
//            {"Verloren","los"},
//            {"Overeenkomen","Match"},
//            {"Invoeren","inpu"},
//        };    
        return languageArray;
    }
    
    public static String[][] serviceOverviewLostLanguage() {
        //amount of language fields
        int amount_of_fields = 4;
        String[][] languageArray = new String[2][amount_of_fields+1];
        
            //set al the data for the getLanguageFor string array
            languageArray[0][0] = "Overzicht verloren bagage";
            languageArray[0][1] = "Gevonden bagage overzicht";
            languageArray[0][2] = "Verloren bagage overzicht";
            languageArray[0][3] = "Bagage Invoeren +";
            languageArray[0][4] = "Vind matches >";
            
            languageArray[1][0] = "Overview Lost Luggage"; 
            languageArray[1][1] = "Search for luggage";
            languageArray[1][2] = "Matched Luggage";
            languageArray[1][3] = "Input Luggage +";
            languageArray[1][4] = "Find Matches >";
        
        return languageArray;
    }           
}
