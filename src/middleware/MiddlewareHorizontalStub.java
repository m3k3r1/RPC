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
    JSONArray jsonArray;
    ArrayList<String> hosts;

    public MiddlewareHorizontalStub( ) {
        this.jsonArray = new JSONArray();
        hosts = new ArrayList<>();
        new Thread(new GUIListener()).start();
        new Thread(new NameServerListener()).start();
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
                    marshelling((Message) iStream.readObject());
                    sendMarshelledMessage();
                    iStream.close();
                } catch (IOException | ClassNotFoundException | ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class NameServerListener extends ReceiverConnection implements Runnable{
        public NameServerListener (){
            try {
                this.doReceiverConnection(7793);
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
                    ArrayList<String> received = (ArrayList<String>) iStream.readObject();
                    iStream.close();
                    setHosts(received);
                    sendHosts();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendHosts(){
        try {
            this.doSenderConnection();
            this.sendMessage(hosts,7793);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setHosts(ArrayList<String> hosts) {
        this.hosts=hosts;
    }
    private void marshelling(Message m){
        JSONObject obj = new JSONObject();
        obj.put("id", m.getTransactionID());
        obj.put("move", "horizontal");
        obj.put("value", m.getSlide());

        jsonArray.add(obj);
        System.out.println("[RECEIVED] " + obj);
    }
    private void sendMarshelledMessage(){
        try {
            this.doSenderConnection();

            while(!jsonArray.isEmpty()){
                this.sendMessage(jsonArray.get(0),7799);
                jsonArray.remove(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        MiddlewareHorizontalStub stub = new MiddlewareHorizontalStub();
    }
}
