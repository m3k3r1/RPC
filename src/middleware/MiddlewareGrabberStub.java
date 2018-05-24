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

public class MiddlewareGrabberStub extends SenderConnection{
    public MiddlewareGrabberStub() {
        new Thread(new GUIListener()).start();
    }

    private class GUIListener extends ReceiverConnection implements Runnable{
        public GUIListener(){
            try {
                this.doReceiverConnection(5598);
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

    private JSONObject marshelling(Message m){
        JSONObject obj = new JSONObject();
        obj.put("id", m.getTransactionID());
        obj.put("move", m.getMessage());

        System.out.println("[RECEIVED] " + obj);
        return obj;
    }
    private void sendMarshelledMessage(JSONObject obj){
        try {
            this.doSenderConnection();
            this.sendMessage(obj,5599);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        MiddlewareGrabberStub stub = new MiddlewareGrabberStub();
    }

}
