package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

public class MiddlewareGrabberSkeleton extends SenderConnection{
    public MiddlewareGrabberSkeleton() {
        new Thread(new listenerStubAndActionPerformer()).start();
    }

    private class listenerStubAndActionPerformer extends ReceiverConnection implements Runnable{

        //Constructor
        public listenerStubAndActionPerformer(){
            try {
                this.doReceiverConnection(5599);
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

    //unmarshall of the message
    private void unmarshalling(JSONObject object) throws IOException {
        int id = (int) object.get("id");
        String move = (String) object.get("move");
        String msg = move;
        System.out.println(msg);
        System.out.println("[RECEIVED] " + object);
        sendMessage(msg, "localhost");
    }

    //send message to Robot
    private void sendMessage(String msg, String ip) throws IOException {
        try {
            this.doSenderConnection(ip);
            this.sendMessage(msg,5597);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Open connection to nameserver
    private void nameServiceRegister(String ip) throws IOException {
        ip = ip.substring(7,20);

        try {
            this.doSenderConnection();
            this.sendMessage(ip,5596);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        MiddlewareGrabberSkeleton stub = new MiddlewareGrabberSkeleton();
    }

}
