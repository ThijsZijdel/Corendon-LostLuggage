package is103.lostluggage.Controllers.Manager;

/**
 *
 * @author daron
 */
public class customerdetails {

    public String emailid, adresid;

    public customerdetails(String emailid, String adresid) {
        this.emailid = emailid;
        this.adresid = adresid;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAdresid() {
        return adresid;
    }

    public void setAdresid(String adresid) {
        this.adresid = adresid;
    }
    
    
    
}