package application;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import simulation.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RobotController extends SenderConnection{
    protected static  ActionHorizontal h;


    public RobotController() {
        try {
            String ip = "^" + (String) Inet4Address.getLocalHost().getHostAddress() + "^";
            this.sendMessage(ip, 7794);
        } catch (IOException e) {
            System.out.println("Deu merda");
        }

        new Thread(new HorizontalSkeletonListener()).start();
        new Thread(new VerticalSkeletonListener()).start();
        new Thread(new GrabberSkeletonListener()).start();
        //h = new ActionHorizontal();
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
        RobotController robot = new RobotController();
    }
}