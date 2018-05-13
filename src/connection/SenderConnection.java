package connection;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SenderConnection implements IConnection {
    @Override
    public void doSenderConnection() throws SocketException, UnknownHostException {

    }

    @Override
    public void sendMessage(String msg, int port) throws IOException {

    }


    @Override
    public void doReceiverConnection(int port) throws SocketException {
        System.err.print("[ERROR] - Method not allowed");

    }

    @Override
    public void close() {

    }
}
