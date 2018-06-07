package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import message.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;

public class MiddlewareHorizontalStub extends SenderConnection {

    public MiddlewareHorizontalStub( ) {
        new Thread(new GUIListener()).start();
        new Thread(new BrokerListener()).start();
    }

    private class GUIListener extends ReceiverConnection implements Runnable{
        public GUIListener(){
            try {
                this.doReceiverConnection(7798);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        @Override
        public void run(){
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));

                    sendMarshelledMessage(marshelling((Message) iStream.readObject()));
                    iStream.close();
                } catch (IOException | ClassNotFoundException | ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class BrokerListener extends ReceiverConnection implements Runnable{
        public BrokerListener (){
            try {
                this.doReceiverConnection(7792);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }
        @Override
        public void run(){
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    System.out.print("New Host - " +received.substring(7,received.length()));
                    sendHosts(received.substring(7,received.length()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendHosts(String robot){
        try {
            this.doSenderConnection();
            this.sendMessage(robot,7793);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject marshelling(Message m){
        JSONObject obj = new JSONObject();
        obj.put("name", m.getRobot());
        obj.put("id", m.getTransactionID());
        obj.put("move", "horizontal");
        obj.put("orientation", m.getOrientation());
        obj.put("value", m.getSlide());

        System.out.println("[RECEIVED] " + obj);
        return obj;
    }
    private void sendMarshelledMessage(JSONObject obj){
        try {
            this.doSenderConnection();
            this.sendMessage(obj,7799);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        MiddlewareHorizontalStub stub = new MiddlewareHorizontalStub();
    }
}
