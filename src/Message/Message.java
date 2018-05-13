package message;

import java.io.Serializable;

public class Message implements Serializable{
    private String message;
    private int slide;

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    int transactionID;

    public Message() {
    }

    public Message(String message,int transactionID,  int slide){
        this.message = message;
        this.slide = slide;
        this.transactionID = transactionID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSlide() {
        return slide;
    }

    public void setSlide(int slide) {
        this.slide = slide;
    }
}