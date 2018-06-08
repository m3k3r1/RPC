package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import message.Message;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;

public class MiddlewareVerticalStub  extends SenderConnection {

    private String brokerIP = "localhost";
    private String appIP = "localhost";

    public void setAppIP(String appIP) { this.appIP = appIP; }

    public void setBrokerIP(String brokerPort) {
        this.brokerIP = brokerPort;
    }

    public MiddlewareVerticalStub() {
        new Thread(new GUIListener()).start();
        new Thread(new BrokerListener()).start();
    }

    private class GUIListener extends ReceiverConnection implements Runnable{
        public GUIListener(){
            try {
                this.doReceiverConnection(6698);
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
                this.doReceiverConnection(6692);
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
            this.doSenderConnection(appIP);
            this.sendMessage(robot,6693);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject marshelling(Message m){
        JSONObject obj = new JSONObject();
        obj.put("id", m.getTransactionID());
        obj.put("name", m.getRobot());
        obj.put("move", "vertical");
        obj.put("orientation", m.getOrientation());
        obj.put("value", m.getSlide());

        System.out.println("[RECEIVED] " + obj);
        return obj;
    }

    private void sendMarshelledMessage(JSONObject obj){
        try {
            this.doSenderConnection(brokerIP);
            this.sendMessage(obj, 6699);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        if(args.length < 2)
        {
            System.out.println("Usage: java MiddlewareVerticalStub <broker ip> <gui ip>");
            System.exit(0);
        }
        MiddlewareVerticalStub stub = new MiddlewareVerticalStub();
        stub.setBrokerIP(args[0]);
        stub.setAppIP(args[1]);
    }

}
