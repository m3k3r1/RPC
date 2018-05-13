package connection;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ReceiverConnection implements IConnection {
    DatagramSocket socket;

    @Override
    public void doSenderConnection() throws SocketException, UnknownHostException {
        System.out.print("[ERROR] - Method not allowed");
    }

    @Override
    public void sendMessage(Message msg, int port) throws IOException {

    }

    @Override
    public void sendMessage(JSONObject msg, int port) throws IOException {

    }

    @Override
    public void doReceiverConnection(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    @Override
    public void close() {

    }
}
