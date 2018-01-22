/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is103.lostluggage.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Arthur Krom & Daron ozdemir
 * This class resembles a PDF document
 */

public class PdfDocument {

    //The PDF Document that is going to be filled and exported
    private final PDDocument document = new PDDocument();

    //Document information for the pdf document
    private final PDDocumentInformation docInfo = document.getDocumentInformation();

    //The first page in the pdf document
    private final PDPage page1 = new PDPage();

    //The stream used to write to the pages
    private PDPageContentStream contentStream;
    
    //HashMap containing the form information
    private Map<String, String> pdfValues = new LinkedHashMap<>();

    //Name of the PDF
    private String filename = "";

    //Object constructor
    public PdfDocument(String filename) throws IOException {

        //Set object properties
        this.filename = filename;
    }

    public Map<String, String> getPdfValues() {
        return pdfValues;
    }

    public void setPdfValues(Map<String, String> pdfValues) {
        this.pdfValues = pdfValues;
    }

    //adds a new line to the document
    public void savePDF() throws IOException {

        //Initialize a contentstream to write to the first page of the document
        this.contentStream = new PDPageContentStream(document, page1);

        //Begin the text writing
        this.contentStream.beginText();

        //Set the font
        this.contentStream.setFont(PDType1Font.HELVETICA, 12);

        //set the leading of the page
        this.contentStream.setLeading(14.5f);

        //new lines at this offset
        // 25 from left (higher number means more to right, 750 from bottom(higher number means more to top)
        this.contentStream.newLineAtOffset(25, 750);

        //Show the text on the page
        for (Map.Entry<String, String> entry: this.getPdfValues().entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();
            
            //if key equals new line we have to set an additional new line
            if(value.equals("Passenger Information") || value.equals("Luggage Information")){
                contentStream.newLine();
                contentStream.showText(value);
                contentStream.newLine();
                
            }else{

                contentStream.showText(key + value);

                contentStream.newLine();
            }
        }

        //End the text
        contentStream.endText();

        //End the stream
        contentStream.close();

        //Add the first page to the pdf document
        this.document.addPage(this.page1);

        try {
            this.document.save(filename + ".pdf");
            this.document.close();
        } catch (IOException ex) {
            Logger.getLogger(PdfDocument.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
}