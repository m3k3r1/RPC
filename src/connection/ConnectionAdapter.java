package connection;

import org.json.simple.JSONObject;
//import simulation.ActionLeftRight;
import java.io.*;
import java.net.*;

public class ConnectionAdapter implements IConnection {
    DatagramSocket socket;
    InetAddress address;
    private byte[] buf;
    private byte[] recvBuffer = new byte[256];

    @Override
    public void doSenderConnection() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    @Override
    public void sendMessage(String msg, int port) throws IOException {
        buf = msg.getBytes("UTF-8");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    @Override
    public void sendMessage(JSONObject msg, int port) throws IOException{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(stream);
        oo.writeObject(msg);
        oo.close();
        buf = stream.toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    @Override
    public void doReceiverConnection(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    @Override
    public void close() {

    }

    /*@Override
    public void doConnect() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    @Override
    public void doConnect(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    @Override
    public void sendMessage(JSONObject msg, int port) throws IOException{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(stream);
        oo.writeObject(msg);
        oo.close();
        buf = stream.toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }



    public void receiveMessage(String filename) {
        DatagramPacket packet = new DatagramPacket(recvBuffer, recvBuffer.length);

        try {
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            //TODO decopulate this part to stub/skeleton
            ObjectInputStream iStream;
            iStream = new ObjectInputStream(new ByteArrayInputStream(recvBuffer));
            JSONObject message = (JSONObject) iStream.readObject();

            message.put("id", "ID");

            FileWriter fileWriter = new FileWriter(filename,true);
            fileWriter.write(message.toJSONString());
            fileWriter.flush();

            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        socket.close();
    }*/
}
