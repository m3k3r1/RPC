package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MiddlewareHorizontalSkeleton extends SenderConnection {

    public MiddlewareHorizontalSkeleton(){
        new Thread(new listenerStubAndActionPerformer()).start();
        new Thread(new listenerRobotAndRegisterNameService()).start();
    }

    private class listenerStubAndActionPerformer extends ReceiverConnection implements Runnable{

        //Constructor
        public listenerStubAndActionPerformer(){
            try {
                this.doReceiverConnection(7799);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        //thread to receive JSON file
        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    unmarshalling((JSONObject) iStream.readObject());

                    iStream.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class listenerRobotAndRegisterNameService extends ReceiverConnection implements Runnable{
        //Constructor
        public listenerRobotAndRegisterNameService(){
            try {
                this.doReceiverConnection(7794);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        // thread to receive robot ip
        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    String ip = new String( packet.getData());
                    nameServiceRegister(ip);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //unmarshall of the message
    private void unmarshalling(JSONObject object) throws IOException {
        int id = (int) object.get("id");
        String move = (String) object.get("move");
        String orientation = (String) object.get("orientation");
        int value = (int) object.get("value");
        String ip = (String) object.get("ip");
        //String ip = " ";

        String msg = new StringBuilder(move).append(",").append(value).toString();
        System.out.println(msg);
        System.out.println("[RECEIVED] " + object);
        sendMessage(msg, ip);
    }

    //send message to Robot
    private void sendMessage(String msg, String ip) throws IOException {
        try {
            this.doSenderConnection(ip);
            this.sendMessage(msg,7797);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Open connection to nameserver
    private void nameServiceRegister(String ip) throws IOException {
        ip = ip.substring(7,20);

        try {
            this.doSenderConnection();
            this.sendMessage(ip,7796);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        MiddlewareHorizontalSkeleton skeleton = new MiddlewareHorizontalSkeleton();
    }
}
