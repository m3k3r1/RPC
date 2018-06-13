package provider;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class Service extends SenderConnection {
    DatagramSocket socket;
    private JSONParser parser;
    private Skeleton skeleton;
    private String serviceName;
    private String robotName;
    private String brokerHost;
    private String ip;
    private int brokerPort;


    public Service(String ip, String name, String robotName ,String brokerHost, int brokerPort, Skeleton skeleton) throws SocketException {
        this.ip = ip;
        this.serviceName = name;
        this.robotName = robotName;
        this.brokerHost = brokerHost;
        this.brokerPort = brokerPort;
        this.skeleton = skeleton;
        socket = new DatagramSocket(new InetSocketAddress(ip, 1005));
        System.out.println("[SERVICE] - "+ name + " service started on " + ip + ":1005");
    }

    public void doRegistry(){
        JSONObject registryMsg = new JSONObject();

        registryMsg.put("type", "registry");
        registryMsg.put("service", serviceName);
        registryMsg.put("robot", robotName);
        registryMsg.put("ip", ip);

        try {
            this.doSenderConnection(brokerHost);
            this.sendMessage(registryMsg, brokerPort);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        new Thread(new ServiceListener()).start();
    }
    private void handleCall(JSONObject msg){
        System.out.println("[ACTION] - performing " + msg.get("movement") + " with value " + msg.get("value"));
        if(msg.get("type").equals("action") & msg.get("movement").equals(serviceName)){
            String type = msg.get("movement").toString();
            switch (type){
                case "horizontal": skeleton.moveHorizontal(Integer.parseInt(msg.get("value").toString())); break;
                case "vertical": skeleton.moveVertical(Integer.parseInt(msg.get("value").toString())); break;
                case "grabber": skeleton.moveGrabber(Integer.parseInt(msg.get("value").toString())); break;
            }
        }
    }

    private class ServiceListener implements Runnable {
        protected byte[] buf = new byte[4096];

        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    JSONObject robotsMsg = (JSONObject) iStream.readObject();
                    handleCall(robotsMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
