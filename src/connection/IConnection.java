package connection;

import Message.Message;
import org.json.simple.JSONObject;
//import simulation.ActionLeftRight;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public interface IConnection {
    void doSenderConnection() throws SocketException, UnknownHostException;
    void sendMessage(Message msg, int port) throws IOException;
    void sendMessageJSON(JSONObject msg, int port) throws IOException;
    void doReceiverConnection(int port) throws SocketException;
    void close();
}
