import java.util.Arrays;
/**
 * Class Which Stores Information Of Sender And Its Receivers List In Object.
 * @author rajmistry
 */
public class CallsToBeMade {
	private String sender;
    private String[] receivers;

    public CallsToBeMade(String sender, String[] receivers) {
        this.sender = sender;
        this.receivers = receivers;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }
    
    public String toString() {
        return sender +" : " + Arrays.toString(receivers);
    }
}