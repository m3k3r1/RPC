package connection;

import org.json.simple.JSONObject;
//import simulation.ActionLeftRight;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public interface IConnection {
    void doSenderConnection() throws SocketException, UnknownHostException;
    void doSenderConnection(String nameHost) throws SocketException, UnknownHostException;
    void sendMessage(Object msg, int port) throws IOException;
    void doReceiverConnection(int port) throws SocketException;
    void close();
}
