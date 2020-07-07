/**
 * Class Which Stores Message That Sender Sent To Its Receiver In Object.
 * @author rajmistry
 */
public class Messages {

    private String sender;
    private String receiver;
    private String message;

    public Messages(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Messages(Messages message) {
        this.sender = message.sender;
        this.receiver = message.receiver;
        this.message = message.message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}