package connection;

import message.Message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ReceiverConnection implements IConnection, Runnable {
    protected DatagramSocket socket;
    protected byte[] buf = new byte[256];

    @Override
    public void doSenderConnection() throws SocketException, UnknownHostException {
        System.err.print("[ERROR] - Method not allowed");
    }

    @Override
    public void doSenderConnection(String nameHost) throws SocketException, UnknownHostException {

    }

    @Override
    public void sendMessage(Message msg, int port) throws IOException {
        System.err.print("[ERROR] - Method not allowed");

    }

    @Override
    public void doReceiverConnection(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    @Override
    public void close() {

    }

    @Override
    public void run() {

    }
}
