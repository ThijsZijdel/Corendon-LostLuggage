package is103.lostluggage.Model;

/**
 * @author made for: Poek Lighthart
 */
public class Data {
    private String id;
    private String dutch,english;

    public Data(String id, String dutch, String english) {
        this.id = id;
        this.dutch = dutch;
        this.english = english;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the dutch
     */
    public String getDutch() {
        return dutch;
    }

    /**
     * @param dutch the dutch to set
     */
    public void setDutch(String dutch) {
        this.dutch = dutch;
    }

    /**
     * @return the english
     */
    public String getEnglish() {
        return english;
    }

    /**
     * @param english the english to set
     */
    public void setEnglish(String english) {
        this.english = english;
    }
    
    
}
