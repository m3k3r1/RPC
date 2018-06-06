package nameserver;

import connection.ReceiverConnection;
import connection.SenderConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NameServer extends SenderConnection{
    HashMap<String ,String> routingTable;
    int i = 0;

    public NameServer() {
        this.routingTable = new HashMap<>();
        new Thread(new BrokerListener()).start();
        new Thread(new BrokerListenerAndSendIP()).start();
    }

    private class BrokerListener extends ReceiverConnection implements Runnable{
        public BrokerListener() {
            try {
                this.doReceiverConnection(7791);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    addService(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class BrokerListenerAndSendIP extends ReceiverConnection implements Runnable{
        public BrokerListenerAndSendIP() {
            try {
                this.doReceiverConnection(7796);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    sendIP(getHostname(received));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addService(String hostLocation){
        String[] part1 = hostLocation.split("\\^");
        if(routingTable.containsKey(part1[1])){

            routingTable.put(part1[1], "Robot"+i);
            i++;
            System.out.print("[ADDED] " + part1[1] + " as " + routingTable.get(part1[1]));
            String send = "^" + routingTable.get(part1[1]) + "^";
            try {
                this.doSenderConnection();
                this.sendMessage(send, 7790);
            } catch (SocketException e) {
                    e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else System.out.println("Key already exists!");

    }

    private void removeService(String hostLocation){
        routingTable.remove(hostLocation);
        i--;
    }

    private String getHostname(String name){

        String[] part = name.split("\\^");
        for (Map.Entry<String, String> entry : routingTable.entrySet()) {
            if (Objects.equals(part[1], entry.getValue())) {
                return "^" + entry.getKey() + "^";
            }
        }
        return null;
    }

    private void sendIP(String ip) throws IOException {
        this.doSenderConnection();
        this.sendMessage(ip,7795);
    }

    public static void main(String[] args){ NameServer dns = new NameServer(); }
}
