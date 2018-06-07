package message;

import java.io.Serializable;

public class Message implements Serializable{
    private String message;
    private int slide;
    int transactionID;
    String orientation;

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    String robot;

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }



    public Message() {
    }

    public Message(String robot, String message,int transactionID,  int slide, String orientation){
        this.robot = robot;
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