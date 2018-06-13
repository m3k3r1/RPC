package consumer;

public class GUI {
    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.subscribe();
        consumer.run();
    }
}
