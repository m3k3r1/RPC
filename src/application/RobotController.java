package application;

import connection.ReceiverConnection;
import connection.SenderConnection;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class RobotController{

    private DatagramSocket socketToStub;
    private InetAddress stubAddress;
    private byte[] buf;

    private class Sender extends SenderConnection{

        @Override
        public void doSenderConnection(String nameHost) throws SocketException, UnknownHostException{

            socketToStub = new DatagramSocket();
            stubAddress = InetAddress.getByName("localhost");

            buf = nameHost.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
            try {
                socket.send(packet);
            } catch (IOException ex) {
                Logger.getLogger(RobotController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }


    private class Receiver extends ReceiverConnection{
        @Override
        public void doReceiverConnection(int port) throws SocketException{
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received= new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            } catch (IOException ex) {
                Logger.getLogger(RobotController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}