package message;

import java.io.Serializable;

public class Message implements Serializable{
    private String message;
    private int slide;
    int transactionID;
    String orientation;

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }



    public Message() {
    }

    public Message(String message,int transactionID,  int slide, String orientation){
        this.orientation = orientation;
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

    public String getOrientation() {
        return orientation;
    }
}