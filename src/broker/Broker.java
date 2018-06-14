package vs.broker;


import org.json.simple.JSONObject;
import vs.connection.ReceiverConnection;
import vs.connection.SenderConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Broker  extends SenderConnection{
    HashMap<String, String > services;
    ArrayList<String> robots;
    ArrayList<String> consumers;
    ArrayList<String> feedbackConsumers;
    ArrayList<String> consumerMessages;
    ArrayList<String> providerMessages;
    ArrayList<JSONObject> feedback;


    public Broker(int providerPort, int consumerPort) {
        services = new HashMap<>();
        robots = new ArrayList<>();
        consumers = new ArrayList<>();
        feedback = new ArrayList<>();
        feedbackConsumers = new ArrayList<>();
        consumerMessages = new ArrayList<>();
        providerMessages = new ArrayList<>();

        new Thread(new ProviderListener(providerPort)).start();
        new Thread(new ConsumerListener(consumerPort)).start();
        new Thread(new MessagesSenderConsumer()).start();

     }

    private void handleMessage(JSONObject msg, String senderIP){
    	if(msg!=null) {
            if(msg.get("type").equals("registry")){
                registerService(senderIP, msg.get("port").toString(),    msg.get("robot") + "/" + msg.get("service"), msg.get("robot").toString());
            } else if (msg.get("type").equals("subscribe")) {
                subscribeConsumer(senderIP);
            } else if (msg.get("type").equals("action")){
                callRemoteService(msg.get("robot").toString() + "/" + msg.get("movement") , msg.get("value").toString());
            } else if (msg.get("type").equals("feedback")) {
            	sendFeedback(msg);
            }
    	}
    }
    private void registerService(String ip, String port, String service, String robot){
        ip=ip.split("/")[1];
    	if(!robots.contains(robot)){
            robots.add(robot);
        }

        if(services.containsKey(service)){
            System.out.println("[SERVICE] - " + service + " already added");

        } else {
            System.out.println("[NEW SERVICE] - " + service + " on " + ip +":"+port);
            services.put(service, ip + ":"+ port);
        }
    }
    private void subscribeConsumer(String senderIP){
        consumers.add(senderIP.split("/")[1]);
        feedbackConsumers.add(senderIP.split("/")[1]);
        System.out.println("[SUBSCRIPTION] - " +senderIP.split("/")[1] + "/Consumer subscribed to services");
    }
    private void callRemoteService(String service, String value) {
        System.out.println("[SERVICE] - Consumer is trying to call remote service " + service + "?value=" + value);

        JSONObject obj = new JSONObject();
        obj.put("type", "action");
        obj.put("movement", service.split("/")[1]);
        obj.put("value", value);

        for (Map.Entry<String, String> entry : services.entrySet()) {
            String s = entry.getKey();
            String ip = entry.getValue().split(":")[0];
            String port = entry.getValue().split(":")[1];
            if (s.equals(service)) {
                System.out.println("[REMOTE] - Execute "  + service + "?value=" + value + " on " + ip+":"+port);
                try {
                    this.doSenderConnection(ip);
                    this.sendMessage(obj, Integer.parseInt(port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendFeedback(JSONObject msg) {
      	 try {
	    	for(String hostName : feedbackConsumers) {
				this.doSenderConnection(hostName);
				System.out.println("[SERVICE] - Sending feedback service to " + hostName + "/Consumer");
	            this.sendMessage(msg, 1010);
                this.close();
	        }
	    }catch (SocketException | UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private class ConsumerListener extends ReceiverConnection {
        public ConsumerListener(int port) {
            try {
                this.doReceiverConnection(port);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String ip = packet.getAddress().toString();
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    JSONObject consumerMsg = (JSONObject) iStream.readObject() ;
                    handleMessage(consumerMsg, ip);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class ProviderListener extends ReceiverConnection{
        public ProviderListener(int port){
            try {
                this.doReceiverConnection(port);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String ip = packet.getAddress().toString();
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    JSONObject providerMsg = (JSONObject) iStream.readObject();
                    handleMessage(providerMsg, ip);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class MessagesSenderConsumer extends SenderConnection implements Runnable{
        @Override
        public void run() {
            while(true){
                try {
                    if(consumers.size() > 0 && robots.size() > 0) {
                    	for(String hostname : consumers) {
                            this.doSenderConnection(hostname);
                            for(String s: robots){
                                System.out.println("[SERVICE] - Sending "+ s+" service to " + hostname + "/Consumer");
                                JSONObject obj = new JSONObject();
                                obj.put("robot", s);
                                this.sendMessage(obj, 1003);
                                this.close();
                            }
                    	}
                    	consumers.clear();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException | IOException  e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args){
        new Broker(1000, 1001);
    }
}
