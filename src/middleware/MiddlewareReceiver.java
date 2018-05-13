package middleware;

import connection.ReceiverConnection;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class MiddlewareReceiver extends ReceiverConnection{

    public void doConnect(){
        try {
            this.doReceiverConnection(7799);
        } catch (SocketException e) {
            System.err.print("[ERROR] - Couldn't create socket");
        }
    }

    @Override
    public void run(){
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                File file = (File) iStream.readObject();
                iStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MiddlewareReceiver receiver = new MiddlewareReceiver();
        receiver.doConnect();
        new Thread(receiver).start();
    }
}
