package connection;

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

    public void close(){
        socket.close();
    }

}