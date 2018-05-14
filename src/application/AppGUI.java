package application;

import connection.SenderConnection;
import message.Message;
import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

import java.io.*;
import java.net.*;

public class AppGUI  implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer, Runnable {
    CaDSRobotGUISwing gui;
     DatagramSocket socketToStub;
     DatagramSocket socketListener;
     InetAddress stubAddress;

    protected byte[] buf = new byte[256];

    public AppGUI() {

        CaDSRobotGUISwing gui = new CaDSRobotGUISwing(this,this,this,this,this);

        try {
            socketListener = new DatagramSocket(7793);
            new Thread(this).start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(Message m){
        try {
            socketToStub = new DatagramSocket();
            stubAddress = InetAddress.getByName("localhost");

            ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(m);
            oos.close();
            byte[] buf= baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, stubAddress, 7798);
            socketToStub.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(ICaDSRobotGUIUpdater iCaDSRobotGUIUpdater) {

    }

    @Override
    public void update(String s) {

    }

    @Override
    public int closeGripper(int i) throws Exception {
        return 0;
    }

    @Override
    public int openGripper(int i) throws Exception {
        return 0;
    }

    @Override
    public int isGripperClosed() throws Exception {
        return 0;
    }

    @Override
    public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
        System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
        sendMessage(new Message("horizontal",transactionID,percent));
        return 0;
    }

    @Override
    public int moveVerticalToPercent(int transactionID, int percent) throws Exception {
        System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);

        return 0;
    }

    @Override
    public int stop(int transactionID) throws Exception {
        return 0;
    }

    @Override
    public int getCurrentVerticalPercent() throws Exception {
        return 0;
    }

    @Override
    public int getCurrentHorizontalPercent() throws Exception {
        return 0;
    }

    @Override
    public int isUltraSonicOccupied() throws Exception {
        return 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socketListener.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), 0, packet.getLength());
                gui.addService(received);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        AppGUI g = new AppGUI();

    }
}
