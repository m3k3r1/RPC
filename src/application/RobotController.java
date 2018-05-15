package application;

import connection.ReceiverConnection;
import connection.SenderConnection;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import simulation.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.net.UnknownHostException;

//test

public class RobotController extends SenderConnection{
    protected static CaDSEV3RobotHAL caller;

    public RobotController() {
        try {
            this.doSenderConnection();
            this.sendMessage((String)Inet4Address.getLocalHost().getHostAddress(),7794);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new HorizontalSkeletonListener()).start();
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
                    System.out.println(received);
                    execute(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void execute(String s){
        ActionHorizontal h = new ActionHorizontal(s.substring(11, s.length()));
        h.startThread();
    }

    public static void main(String[] args){
        RobotController robot = new RobotController();
    }
}