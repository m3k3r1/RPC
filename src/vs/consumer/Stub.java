package vs.consumer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Stub implements IStubAction{
    protected DatagramSocket socket;
    protected InetAddress address;
    protected byte[] sendData = new byte[1024];
    protected byte[] receiveData = new byte[1024];
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    JSONObject obj = new JSONObject();
    String stubName = "GUI";

    public Stub(String stubName) {
    	this.stubName = stubName;
    }
    public JSONObject getJSON() {
        return obj;
    }
    public void moveVertical(int percent, String robotId, int id) {
    	JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	head.put("id", id);
    	head.put("origin", stubName);
        head.put("destination", robotId);
        head.put("destinationIP", "");
        header.add(head);
        
        body.put("method","moveVertical");
        values.put("value", percent);
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        
        obj.put("header", header);
        obj.put("remoteCall", remoteCall);
        
        changes.firePropertyChange("vertical", null, obj);
    }
    public void moveHorizontal(int percent, String robotId, int id) {
    	JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	
    	head.put("id", id);
    	head.put("origin", stubName);
        head.put("destination", robotId);
        head.put("destinationIP", "");
        header.add(head);
        
        
        body.put("method","moveHorizontal");
        values.put("value", percent);
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        
        obj.put("header", header);
        obj.put("remoteCall", remoteCall);
        
        changes.firePropertyChange("horizontal", null, obj);
    }
    public void moveGrabber(int percent, String robotId, int id) {
    	JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	
    	head.put("id", id);
    	head.put("origin", stubName);
        head.put("destination", robotId);
        head.put("destinationIP", "");
        header.add(head);
        
        
        body.put("method","moveGrabber");
        values.put("value", percent);
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        
        obj.put("header", header);
        obj.put("remoteCall", remoteCall);
        
        changes.firePropertyChange("grabber", null, obj);
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public void sendMessage(String nameHost, Object msg, int port) throws IOException, SocketException {
        socket = new DatagramSocket();
    	address = InetAddress.getByName(nameHost);
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        sendData = baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(packet); 
    }
    
    public JSONObject getConfirmation() throws ClassNotFoundException, IOException {
    	DatagramPacket Rpacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(Rpacket);
        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receiveData));
        return  (JSONObject) iStream.readObject();
    }

}
