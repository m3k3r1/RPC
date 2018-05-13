package Message;

public class Message{
    private String message;
    private int slide;

    public Message(String message, int slide){
        this.message = message;
        this.slide = slide;
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