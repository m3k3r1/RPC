package vs.provider;

import vs.connection.SenderConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class Service extends SenderConnection {
	protected DatagramSocket socket;
    protected JSONParser parser;
    protected Skeleton skeleton;
    protected String serviceName;
    protected String robotName;
    protected String brokerHost;
    protected int port;
    protected int brokerPort;


    public Service(int port, String name, String robotName ,String brokerHost, int brokerPort, Skeleton skeleton) throws SocketException {
        this.port = port;
        this.serviceName = name;
        this.robotName = robotName;
        this.brokerHost = brokerHost;
        this.brokerPort = brokerPort;
        this.skeleton = skeleton;
        socket = new DatagramSocket(port);
        System.out.println("[SERVICE] - "+ name + " service started on port " + port);
    }

    public void doRegistry(){
        JSONObject registryMsg = new JSONObject();

        registryMsg.put("type", "registry");
        registryMsg.put("service", serviceName);
        registryMsg.put("robot", robotName);
        registryMsg.put("port", port);

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
        if(msg.get("type").equals("action") & msg.get("movement").equals(serviceName)){
            String type = msg.get("movement").toString();
            System.out.println("[ACTION] - performing " + msg.get("movement") + " with value " + msg.get("value"));
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
