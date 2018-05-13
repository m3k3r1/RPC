package middleware;

import connection.ReceiverConnection;
import message.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class MiddlewareStub extends ReceiverConnection {
    public void doConnect(){
        try {
            this.doReceiverConnection(7798);
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
                Message messageClass = (Message) iStream.readObject();
                iStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}


