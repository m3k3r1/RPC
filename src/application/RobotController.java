package application;

import connection.ReceiverConnection;
import connection.SenderConnection;
import simulation.*;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class RobotController extends SenderConnection{
    protected static  ActionHorizontal h;
    String skeletonIP;
    String ip;

    public RobotController(String hostname) {
        skeletonIP = hostname;

        try {
            this.doSenderConnection(skeletonIP);
            ip = "^" + getIp()  + "^";
            this.sendMessage(ip, 7794);
        } catch (IOException o) {
            System.out.println("Deu merda");
        }
        try {
            this.doSenderConnection(skeletonIP);
            ip = "^" + getIp() + "^";
            this.sendMessage(ip, 6694);
        } catch (IOException o) {
            System.out.println("Deu merda");
        }
        try {
            this.doSenderConnection(skeletonIP);
            ip = "^" + getIp() + "^";
            this.sendMessage(ip, 5594);
        } catch (IOException o) {
            System.out.println("Deu merda");
        }

        new Thread(new HorizontalSkeletonListener()).start();
        new Thread(new VerticalSkeletonListener()).start();
        new Thread(new GrabberSkeletonListener()).start();
        h = new ActionHorizontal();
    }

    private class  HorizontalSkeletonListener extends ReceiverConnection {
        public HorizontalSkeletonListener() {
            try {
                this.doReceiverConnection(7797);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    received = received.substring(7,received.length());
                    System.out.println(received);

                    String[] parts = received.split(",");
                    String move = parts[0];
                    String percent = parts[1];
                    String orientation = parts[2];

                    execute(move,percent,orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String getIp(){
        Enumeration e = null;
        boolean save = false;
        String ip = null;

        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();

                if(save){
                    ip = i.getHostAddress();
                    save = false;
                }

                String[] part;
                part = i.getHostAddress().split("%");

                if(part.length > 1){
                    if(part[1].equals("wlan0")){
                        save = true;
                    }
                }
            }
        }

        return ip;
    }
    private class  VerticalSkeletonListener extends ReceiverConnection {
        public VerticalSkeletonListener() {
            try {
                this.doReceiverConnection(6697);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    received = received.substring(7,received.length());
                    System.out.println(received);

                    String[] parts = received.split(",");
                    String move = parts[0];
                    String percent = parts[1];
                    String orientation = parts[2];

                    execute(move,percent,orientation);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class  GrabberSkeletonListener extends ReceiverConnection {
        public GrabberSkeletonListener() {
            try {
                this.doReceiverConnection(5597);
            } catch (SocketException e) {
                System.err.print("[ERROR] - Couldn't create socket");
            }
        }

        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    received = received.substring(7,received.length());
                    System.out.println(received);
                    String[] parts = received.split(",");
                    String move = parts[0];
                    String percent = parts[1];
                    String orientation = parts[2];

                    execute(move,percent,orientation);
                    //execute("null","100",received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void execute(String move, String percent ,String orientation ){
        System.out.println(move + " " + percent + " " + orientation);
            h.moveAround(move,orientation,percent);
    }

    public static void main(String[] args) throws IOException {
        RobotController robot = new RobotController("localhost");
    }
}