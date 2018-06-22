package vs.connection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import org.json.simple.JSONObject;

public class SenderConnection implements IConnection {
    protected DatagramSocket socket;
    protected InetAddress address;
    protected JSONObject confirmation;
    protected byte[] sendData = new byte[1024];
    protected byte[] receiveData = new byte[1024];

    
    @Override
    public void doSenderConnection() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    @Override
    public void doSenderConnection(String nameHost) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName(nameHost);
    }

    @Override
    public void sendMessage(Object msg, int port) throws IOException, SocketException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        //oos.close();
        sendData = baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
        socket.send(packet); 
    }
    
    public JSONObject getConfirmation() throws ClassNotFoundException, IOException {
    	DatagramPacket Rpacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(Rpacket);
        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receiveData));
        this.confirmation = new JSONObject();
        return  (JSONObject) iStream.readObject();
    }
    
    public void setConfirmation(JSONObject o) {
    	confirmation = o;
    }


    @Override
    public void doReceiverConnection(int port) throws SocketException {
        System.err.print("[ERROR] - Method not allowed");

    }

    @Override
    public void close() {

    }
}
