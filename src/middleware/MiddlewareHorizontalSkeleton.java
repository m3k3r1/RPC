package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.json.simple.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;


public class MiddlewareHorizontalSkeleton extends SenderConnection{

    public MiddlewareHorizontalSkeleton(){
        new Thread (new listenerBrokerAndActionPerformer()).start();
        new Thread (new listenerRobotAndRegisterNameService()).start();
    }

    private class listenerBrokerAndActionPerformer extends ReceiverConnection implements Runnable{
        public listenerBrokerAndActionPerformer(){
            try {
                 this.doReceiverConnection(7789);
             } catch (SocketException e) {
                 System.err.print("[ERROR] - Couldn't create socket");
             }
        }

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
        public listenerRobotAndRegisterNameService(){
            try {
                 this.doReceiverConnection(7794);
             } catch (SocketException e) {
                 System.err.print("[ERROR] - Couldn't create socket");
             }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    String ip = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    nameServiceRegister(ip);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void unmarshalling (JSONObject object) throws IOException {
        String move = (String) object.get("move");
        String orientation = (String) object.get("orientation");
        int value = (int) object.get("value");
        String ip = (String) object.get("ip");

        String msg = new StringBuilder(move).append(",").append(value).append(",").append(orientation).append(",").toString();
        System.out.println(msg);
        System.out.println(ip);
        sendMessage (msg, ip);

    }

    private void sendMessage (String msg, String ip) throws IOException {
        this.doSenderConnection(ip);
        this.sendMessage(msg, 7797);
    }

    private void nameServiceRegister (String ip) throws IOException {
        //System.out.println(ip);
        System.out.println("Skeleton - " + ip);
        this.doSenderConnection();
        this.sendMessage(ip, 7788);
    }

    public static void main(String[] args) {
         MiddlewareHorizontalSkeleton horizontal = new MiddlewareHorizontalSkeleton();
    }


}
