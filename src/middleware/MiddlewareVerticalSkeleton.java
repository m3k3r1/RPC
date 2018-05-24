package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.json.simple.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;


public class MiddlewareVerticalSkeleton extends SenderConnection{

    public MiddlewareVerticalSkeleton(){
        new Thread (new listenerStubAndActionPerformer()).start();
        new Thread (new listenerRobotAndRegisterNameService()).start();
    }

    private class listenerStubAndActionPerformer extends ReceiverConnection implements Runnable{
        public listenerStubAndActionPerformer(){
            try {
                 this.doReceiverConnection(6699);
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
                 this.doReceiverConnection(6694);
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

                    String ip = new String(packet.getData());
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
        sendMessage (msg, ip);

    }

    private void sendMessage (String msg, String ip) throws IOException {
        this.doSenderConnection(ip);
        this.sendMessage(msg, 6697);
    }

    private void nameServiceRegister (String ip) throws IOException {
        ip = ip.substring(7, 20);

        this.doSenderConnection();
        this.sendMessage(ip, 6696);
    }

    public static void main(String[] args) {
         MiddlewareVerticalSkeleton vertical = new MiddlewareVerticalSkeleton();
    }


}
