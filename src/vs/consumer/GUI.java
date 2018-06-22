package vs.consumer;

public class GUI {
    public static void main(String[] args) {
    	if(args.length <= 0) {
    		System.out.println("Usage: java -jar GUI.jar <GUI name> <broker IP>");
    	} else {	
	        Consumer consumer = new Consumer(args[0], args[1]);
	        consumer.registerGUI();
	        consumer.start();
        }
    }
}
