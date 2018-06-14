package vs.consumer;

public class GUI {
    public static void main(String[] args) {
        Consumer consumer = new Consumer("localhost");
        consumer.subscribe();
        consumer.run();
    }
}
