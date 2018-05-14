package application;

import connection.ReceiverConnection;
import connection.SenderConnection;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;

public class RobotController extends SenderConnection{

    public RobotController() {
        try {
            this.doSenderConnection();
            this.sendMessage((String)Inet4Address.getLocalHost().getHostAddress(),7797);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new SkeletonListener()).start();
    }

    private class  SkeletonListener extends ReceiverConnection {
        public SkeletonListener() {
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
                    System.out.println(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        RobotController robot = new RobotController();
    }
}