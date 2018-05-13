package connection;

import Message.Message;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class SenderConnection implements IConnection {
    @Override
    public void doSenderConnection() throws SocketException, UnknownHostException {

    }

    @Override
    public void sendMessage(Message msg, int port) throws IOException {

    }

    @Override
    public void sendMessageJSON(JSONObject msg, int port) throws IOException {

    }

    @Override
    public void doReceiverConnection(int port) throws SocketException {

    }

    @Override
    public void close() {

    }
}
