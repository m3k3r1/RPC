package middleware;

import connection.ReceiverConnection;
import message.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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

        }

    }
}
