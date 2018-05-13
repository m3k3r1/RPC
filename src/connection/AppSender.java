package connection;

import Message.Message;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class AppSender implements IConnection{
    ConnectionAdapter socket;

    public AppSender(){
        socket = new ConnectionAdapter();
    }

    public void doConnect(){
        try {
            socket.doSenderConnection();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageClass(Message msg)  {
        try {
            socket.sendMessage(msg, 7798);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public void close(){
        socket.close();
    }

}