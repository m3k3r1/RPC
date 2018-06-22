package vs.provider;

import vs.connection.SenderConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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


    public Service(int port, String name, String robotName ,String brokerHost, int brokerPort, Skeleton skeleton) throws SocketException  {
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
    	JSONObject obj = new JSONObject();
    	JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	head.put("id", "");
    	head.put("origin", robotName+"/"+serviceName+":"+port);
        head.put("destination", "broker");
        head.put("destinationIP", brokerHost);
        header.add(head);
        body.put("method", "registerRobot");
        values.put("value", robotName+"/"+serviceName+":"+port);
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        obj.put("header", header);
        obj.put("remoteCall", remoteCall);

        try {
            this.doSenderConnection(brokerHost);
            this.sendMessage(obj, brokerPort);
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
    	skeleton.unmarshall(msg, serviceName);
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
                }catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
    }
}
