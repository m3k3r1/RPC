package middleware;

import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MiddlewareHorizontalSkeleton {

    public MiddlewareHorizontalSkeleton(){
        new Thread(new listenerStubAndActionPerformer()).start();
        //new Thread(new listenerRobotAndRegisterNameService()).start();
    }

    public class listenerStubAndActionPerformer implements Runnable{

        protected DatagramSocket socket;
        protected InetAddress address;
        protected byte[] buf = new byte[256];

        //Constructor
        public listenerStubAndActionPerformer(){
            this.doConnect();
        }

        //start connectin with stub
        public void doConnect(){
            try {
                socket = new DatagramSocket(7799);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        //unmarshall of the message
        private void unmarshalling(JSONObject object) throws IOException {
            int id = (int) object.get("id");
            String move = (String) object.get("move");
            int value = (int) object.get("value");
            String ip = (String) object.get("ip");
            //String ip = " ";

            System.out.println("[RECEIVED] " + object);
            actionPerformer(move, value, ip);
        }

        //open connection and prepare to send message
        private void actionPerformer(String move, int value, String ip) throws IOException {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ip);
            String msg = new StringBuilder(move).append(",").append(value).toString();
            System.out.println(msg);
            sendMessage(msg);

        }

        //send message to Robot
        private void sendMessage(String msg) throws IOException {
            byte[] buffer= msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7797);
            socket.send(packet);
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

    /*public class listenerRobotAndRegisterNameService implements Runnable{

        protected DatagramSocket socket;
        protected InetAddress address;
        protected byte[] buf = new byte[256];

        //Constructor
        public listenerRobotAndRegisterNameService(){
            this.doConnect();
        }

        //Connection to the robot
        public void doConnect(){
            try {
                socket = new DatagramSocket(7797);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        //Open connection to nameserver
        private void nameServiceRegister(String ip) throws IOException {
            socket = new DatagramSocket();
            address = InetAddress.getByName("localhost");
            sendMessage(ip);

        }

        //send message to name server
        private void sendMessage(String ip) throws IOException {
            byte[] buffer= ip.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7796);
            socket.send(packet);
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
    */
    public static void main(String[] args){
        MiddlewareHorizontalSkeleton skeleton = new MiddlewareHorizontalSkeleton();
    }
}
