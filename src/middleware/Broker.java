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
    private String skeletonIP = "localhost";
    private String stubIP = "localhost";
    private String nameserverIP = "localhost";

    public void setSkeletonIP(String skeletonIP) {
        this.skeletonIP = skeletonIP;
    }

    public void setStubIP(String stubIP) {
        this.stubIP = stubIP;
    }

    public void setNameserverIP(String nameserverIP) {
        this.nameserverIP = nameserverIP;
    }

    public Broker() {
        new Thread(new HorizontalSkeletonListener()).start();
        new Thread(new VerticalSkeletonListener()).start();
        new Thread(new GrabberSkeletonListener()).start();
        new Thread(new HorizontalStubListener()).start();
        new Thread(new VerticalStubListener()).start();
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
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    nameServiceGetName(getIPFromNameServer((JSONObject) iStream.readObject()));

                    iStream.close();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    nameServiceGetName(getIPFromNameServer((JSONObject) iStream.readObject()));

                    iStream.close();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    nameServiceGetName(getIPFromNameServer((JSONObject) iStream.readObject()));

                    iStream.close();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    String received = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    nameServiceRegister(received);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    String received = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    nameServiceRegister(received);

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
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);

                    String received = new String(packet.getData(), packet.getOffset(), packet.getLength());
                    nameServiceRegister(received);

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
                    sendNameToStub(received);
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

    private void nameServiceRegister (String ip) throws IOException {
        this.doSenderConnection(nameserverIP);
        this.sendMessage(ip, 7791);
    }

    private void nameServiceGetName(String name) throws IOException {
        this.doSenderConnection(nameserverIP);
        this.sendMessage(name, 7796);

    }

    private String getIPFromNameServer(JSONObject obj){
        temp.add(obj);
        System.out.println(temp.get(temp.size()-1).get("move"));
        String name = (String) obj.get("name");
        name = "^" + name + "^";
        return name;
    }


    private JSONObject addIPToJSON(String ip){
        System.out.println("Ip " + ip);
        String[] part = ip.split("\\^");
        JSONObject obj = temp.get(temp.size()-1);
        obj.put("ip", part[1]);
        System.out.println(temp.get(temp.size()-1).get("ip"));
        temp.remove(0);
        return obj;
    }

    private void sendJson(JSONObject obj){
        String type = (String) obj.get("move");
        if(type.equals("horizontal")) {
            try {
                this.doSenderConnection(skeletonIP);
                this.sendMessage(obj, 7789);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(type.equals("vertical")){
            try {
                this.doSenderConnection(skeletonIP);
                this.sendMessage(obj, 6689);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(type.equals("open") || type.equals("close")){
            try {
                this.doSenderConnection(skeletonIP);
                this.sendMessage(obj, 5589);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNameToStub(String name){
        try {
            this.doSenderConnection(stubIP);
            this.sendMessage(name,7792);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.doSenderConnection(stubIP);
            this.sendMessage(name,6692);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.doSenderConnection(stubIP);
            this.sendMessage(name,5592);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        if(args.length < 3)
        {
            System.out.println("Usage: java Broker <skeleton ip> <stub ip> <nameserver ip");
            System.exit(0);
        }

        Broker b = new Broker();
        b.setSkeletonIP(args[0]);
        b.setStubIP(args[1]);
        b.setNameserverIP(args[2]);
    }
}
