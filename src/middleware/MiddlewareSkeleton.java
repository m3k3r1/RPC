package middleware;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;

public class MiddlewareSkeleton {

    public class listenerStubAndActionPerformer implements Runnable{

        protected DatagramSocket socket;
        protected InetAddress address;
        protected byte[] buf = new byte[256];

        public void doConnect(){
            try {
                socket = new DatagramSocket(7799);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        private void unmarshalling(JSONObject object) throws IOException {
            String id = (String) object.get("id");
            String move = (String) object.get("move");
            String value = (String) object.get("value");
            String ip = (String) object.get("ip");

            System.out.println("[RECEIVED] " + object);
            actionPerformer(move, value, ip);
        }

        private void actionPerformer(String move, String value, String ip) throws IOException {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ip);
            String msg = new StringBuilder(move).append(",").append(value).toString();
            sendMessage(msg);

        }

        private void sendMessage(String msg) throws IOException {
            byte[] buffer= msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 7797);
            socket.send(packet);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buf, buf.length, address, port);

                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    unmarshalling((JSONObject) iStream.readObject());

                    iStream.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class listenerRobotAndRegisterNameService implements Runnable{

        protected DatagramSocket socket;
        protected InetAddress address;
        protected byte[] buf = new byte[256];

        public void doConnect(){
            try {
                socket = new DatagramSocket(7797);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        @Override
        public void run() {

        }
    }
}