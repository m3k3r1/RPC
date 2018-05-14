package application;

import connection.ReceiverConnection;
import connection.SenderConnection;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class RobotController extends SenderConnection{

    public RobotController() {
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