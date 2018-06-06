package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.json.simple.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Broker extends SenderConnection
{
    private List<JSONObject> temp = new ArrayList<>();

    public Broker() {
        new Thread(new HorizontalSkeletonListener()).start();
        new Thread(new HorizontalStubListener()).start();
        new Thread(new VerticalStubListener()).start();
        new Thread(new VerticalSkeletonListener()).start();
        new Thread(new GrabberSkeletonListener()).start();
        new Thread(new GrabberStubListener()).start();
        new Thread(new NameServerListener()).start();
        new Thread(new getHostnameListener()).start();
    }

    private class HorizontalStubListener extends ReceiverConnection {
        public HorizontalStubListener() {
            try {
                this.doReceiverConnection(7799);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                //TODO save to file and send message
                nameServiceGetName(getIPFromNameServer((JSONObject) iStream.readObject()));
                iStream.close();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class VerticalStubListener extends ReceiverConnection {
        public VerticalStubListener() {
            try {
                this.doReceiverConnection(6699);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                //TODO save to file and send message


                iStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class GrabberStubListener extends ReceiverConnection {
        public GrabberStubListener() {
            try {
                this.doReceiverConnection(5599);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                //TODO save to file and send message


                iStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class HorizontalSkeletonListener extends ReceiverConnection {
        public HorizontalSkeletonListener() {
            try {
                this.doReceiverConnection(7788);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class VerticalSkeletonListener extends ReceiverConnection {
        public VerticalSkeletonListener() {
            try {
                this.doReceiverConnection(6688);
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
                    nameServiceRegister(received, 7791);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class GrabberSkeletonListener extends ReceiverConnection {
        public GrabberSkeletonListener() {
            try {
                this.doReceiverConnection(5588);
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
                    nameServiceRegister(received, 7791);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class NameServerListener extends ReceiverConnection {
        public NameServerListener() {
            try {
                this.doReceiverConnection(7790);
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
                    nameServiceRegister(received, 7791);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class getHostnameListener extends ReceiverConnection {
        public getHostnameListener() {
            try {
                this.doReceiverConnection(7795);
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
                    sendJson(addIPToJSON(received));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void saveMessageToFile(){ }
    public void checkFile(){}

    private void nameServiceRegister (String ip, int port) throws IOException {
        ip = ip.substring(7, 20);

        this.doSenderConnection();
        this.sendMessage(ip, port);
    }

    private void nameServiceGetName(String name) throws IOException {
        this.doSenderConnection();
        this.sendMessage(name, 7796);

    }

    private String getIPFromNameServer(JSONObject obj){
        temp.add(obj);
        String name = (String) obj.get("name");
        System.out.println("Broker --- name " + name);
        return name;
    }

    private JSONObject addIPToJSON(String ip){
        JSONObject obj = temp.get(0);
        obj.put("ip", ip);
        temp.remove(0);
        return obj;
    }

    private void sendJson(JSONObject obj){
        try {
            this.doSenderConnection();
            this.sendMessage(obj,7788);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new Broker();
    }
}
