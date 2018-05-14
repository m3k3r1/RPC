package connection;

import message.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

public class SenderConnection implements IConnection {
    protected DatagramSocket socket;
    protected InetAddress address;

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
    public void sendMessage(Object msg, int port) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(msg);
        oos.close();
        byte[] buf= baos.toByteArray();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 7798);
        socket.send(packet);
    }


    @Override
    public void doReceiverConnection(int port) throws SocketException {
        System.err.print("[ERROR] - Method not allowed");

    }

    @Override
    public void close() {

    }
}
