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
                marshelling((Message) iStream.readObject());

                iStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void marshelling(Message m){
        //TODO - Generate JSON file
        System.out.println("[STATUS] - Received " +" [ID:" + m.getTransactionID()+"] " + m.getMessage() + " - " + m.getSlide() + "%");
    }


    public static void main(String[] args){
        MiddlewareStub stub = new MiddlewareStub();
        stub.doConnect();
        new Thread(stub).start();
    }
}


