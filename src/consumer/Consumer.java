package vs.consumer;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import vs.connection.ReceiverConnection;
import vs.connection.SenderConnection;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Consumer implements PropertyChangeListener {
	private String brokerIP;
    private Stub stub;
    private GUIController guiController;
    private CaDSRobotGUISwing gui;
    private ArrayList<String> robots;
    private ArrayList<JSONObject> messages;

    public Consumer(String brokerHost) {
        robots = new ArrayList<String>();
        messages = new ArrayList<>();
        brokerIP = brokerHost;
    }

    public void subscribe(){
        JSONObject  subscriveMsg = new JSONObject();
        subscriveMsg.put("type", "subscribe");
        messages.add(subscriveMsg);

        new Thread(new ServicesAvailableListener()).start();
    }
    public void run() {
        stub = new Stub();
        stub.addPropertyChangeListener(this);
        guiController = new GUIController(stub);
        gui = new CaDSRobotGUISwing(guiController,guiController,guiController,guiController,guiController);

        new Thread(new MessageSenderThread(brokerIP, 1001)).start();
        new Thread(new RobotFeedbackListener()).start();
    }
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        messages.add(stub.getJSON());
    }
    private class MessageSenderThread  extends SenderConnection  implements Runnable  {
        int brokerPort;

        public MessageSenderThread( String brokerHost, int brokerPort) {
            try {
                this.brokerPort = brokerPort;
                this.doSenderConnection(brokerHost);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(true) {
                try {
                    if(messages.size() > 0) {
                        this.sendMessage(messages.get(0), brokerPort);
                        messages.remove(0);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException | IOException  e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class ServicesAvailableListener extends ReceiverConnection {
        public ServicesAvailableListener() {
            try {
                this.doReceiverConnection(1003);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    JSONObject robotsMsg = (JSONObject) iStream.readObject();
                    String newRobot = robotsMsg.get("robot").toString();
                    if(!robots.contains(newRobot)){
                        robots.add(robotsMsg.get("robot").toString());
                        System.out.println("[SERVICE] - Received new Root - " +newRobot);
                        gui.addService(newRobot);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class RobotFeedbackListener  extends ReceiverConnection{
        public RobotFeedbackListener() {
            try {
                this.doReceiverConnection(1010);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            while (true){
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    JSONObject feedback = (JSONObject) iStream.readObject();
                    System.out.println(feedback);
                    
                    int v = Integer.parseInt(feedback.get("vertical").toString());
                    gui.setVerticalProgressbar(v);
                    int h = Integer.parseInt(feedback.get("horizontal").toString());
                    gui.setHorizontalProgressbar(h);
                    
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
        }
    }
  }
}
