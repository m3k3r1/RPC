package application;

import connection.ReceiverConnection;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

public class AppGUI extends SenderConnection implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer {
    CaDSRobotGUISwing gui;
    String robot;
    int orientation_h = 0;
    int orientation_v = 0;
    List<String> robots = new ArrayList<>();

     public AppGUI() {
         gui = new CaDSRobotGUISwing(this,this,this,this,this);
        new Thread(new HorizontalStubListener()).start();
        new Thread(new VerticalStubListener()).start();
        new Thread(new GrabberStubListener()).start();

         Timer timer = new Timer();
         timer.scheduleAtFixedRate(new ClearRobots(), 0, 30000);
    }

    private class ClearRobots extends TimerTask {

        @Override
        public void run() {
            for (int j = 0; j < robots.size(); j++) {
                gui.removeService(robots.get(j));
            }
            robots.clear();
        }
    }

    private String stubIP = "localhost";

    public void setStubIP(String stubIP) { this.stubIP = stubIP; }




    private class HorizontalStubListener extends ReceiverConnection implements  Runnable {
         public HorizontalStubListener() {
             try {
                this.doReceiverConnection(7793);
             } catch (SocketException e) {
                 e.printStackTrace();
             }
         }

         @Override
         public void run() {
             while (true) {
                 try {
                     DatagramPacket packet = new DatagramPacket(buf, buf.length);
                     socket.receive(packet);
                     String received= new String(packet.getData(), 0, packet.getLength());
                     addService(received);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
     }

    private class VerticalStubListener extends ReceiverConnection implements  Runnable {
        public VerticalStubListener() {
            try {
                this.doReceiverConnection(6693);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    addService(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GrabberStubListener extends ReceiverConnection implements  Runnable {
        public GrabberStubListener() {
            try {
                this.doReceiverConnection(5593);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    String received= new String(packet.getData(), 0, packet.getLength());
                    addService(received);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

     synchronized void addService(String s){
         String[] part1 = s.split("\\^");
         System.out.println(part1[1]);
         if(robots.size() == 0) {
             robots.add(part1[1]);
             gui.addService(part1[1]);
         }
         else {
             if (robots.contains(part1[1])) {
                 System.out.println("Robot already registered");
             } else {
                 robots.add(part1[1]);
                 gui.addService(part1[1]);
             }
         }
     }

    @Override
    public void register(ICaDSRobotGUIUpdater observer) {
        System.out.println("New Observer");
        observer.addService("Service 1");
        observer.addService("Service 2");
        observer.setChoosenService("Service 2", -1, -1, false);
    }
    @Override
    public void update(String s) {
        this.robot = s;
        System.out.println(s);
    }
    @Override
    public int closeGripper(int transactionID) throws Exception {
        System.out.println("Close.... TID: " + transactionID);
        this.doSenderConnection(stubIP);
        sendMessage(new Message(robot,"close",transactionID, 100, "close"),5598);
        return 0;
    }
    @Override
    public int openGripper(int transactionID) throws Exception {
        System.out.println("open.... TID: " + transactionID);
        this.doSenderConnection(stubIP);
        sendMessage(new Message(robot,"open",transactionID, 100, "open"),5598);
        return 0;
    }
    @Override
    public int isGripperClosed() throws Exception {
        return 0;
    }
    @Override
    public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
        System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
        this.doSenderConnection(stubIP);
        if (percent > orientation_h)
            sendMessage(new Message(robot,"horizontal",transactionID,percent, "left"),7798);
        else
            sendMessage(new Message(robot,"horizontal",transactionID,percent, "right"),7798);

        orientation_h = percent;
        return 0;
    }
    @Override
    public int moveVerticalToPercent(int transactionID, int percent) throws Exception {
        System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
        this.doSenderConnection(stubIP);

        if (percent > orientation_v)
            sendMessage(new Message(robot,"vertical",transactionID,percent, "up"), 6698);
        else
            sendMessage(new Message(robot,"vertical",transactionID,percent, "down"), 6698);

        orientation_v = percent;
        return 0;
    }
    @Override
    public int stop(int transactionID) throws Exception {
        System.out.println("Stop movement.... TID: " + transactionID);
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

    public static void main(String[] args){
        if(args.length == 0)
        {
            System.out.println("Usage: java AppGui <stub ip>");
            System.exit(0);
        }
         AppGUI g = new AppGUI();
        g.setStubIP(args[0]);
    }
}
