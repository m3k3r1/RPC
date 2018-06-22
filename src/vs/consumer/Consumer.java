package vs.consumer;

import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import vs.broker.Broker;
import vs.connection.ReceiverConnection;
import vs.connection.SenderConnection;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Consumer extends ReceiverConnection implements PropertyChangeListener {
	private String brokerIP;
    private Stub Hstub;
    private Stub Vstub;
    private Stub Gstub;

    private GUIController guiController;
    private CaDSRobotGUISwing gui;
    private ArrayList<String> robots;
    private ArrayList<String> oldRobots;

    private ArrayList<JSONObject> messages;

    private ArrayList<JSONObject> Hmessages;
    private ArrayList<JSONObject> Vmessages;
    private ArrayList<JSONObject> Gmessages;

    private String guiName;
    
    public Consumer(String guiName, String brokerHost) {
    	this.guiName = guiName;
        robots = new ArrayList<String>();
        oldRobots = new ArrayList<String>();
        messages = new ArrayList<>();
        Hmessages = new ArrayList<>();
        Vmessages = new ArrayList<>();
        Gmessages = new ArrayList<>();

        brokerIP = brokerHost;
        //Timer timer = new Timer(true);
        //GetServices task = new GetServices();
        //timer.scheduleAtFixedRate(task, 0, 5000);

    }

    public void registerGUI(){
    	JSONObject obj = new JSONObject();
    	JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	head.put("id", "");
    	head.put("origin", guiName);
        head.put("destination", "broker");
        head.put("destinationIP", brokerIP);
        header.add(head);
        body.put("method", "registerGUI");
        values.put("value", "");
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        obj.put("header", header);
        obj.put("remoteCall", remoteCall);
        
        messages.add(obj);

        new Thread(new ServicesAvailableListener()).start();
    }
    private void getAvailableServices() {
    	JSONObject obj = new JSONObject();
    	JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	head.put("id", "");
    	head.put("origin", guiName);
        head.put("destination", "broker");
        head.put("destinationIP", brokerIP);
        header.add(head);
        body.put("method", "getAvailableServices");
        values.put("ip", "");
        values.put("port", "");
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        obj.put("header", header);
        obj.put("remoteCall", remoteCall);
        
        messages.add(obj);
    }
    public void start()  {
        Hstub = new Stub(guiName);
        Vstub = new Stub(guiName);
        Gstub = new Stub(guiName);
        
        Hstub.addPropertyChangeListener(this);
        Vstub.addPropertyChangeListener(this);
        Gstub.addPropertyChangeListener(this);
        
        guiController = new GUIController(Hstub,Vstub,Gstub);
        gui = new CaDSRobotGUISwing(guiController,guiController,guiController,guiController,guiController);
        getAvailableServices();
        new Thread(new MessageSenderThread(brokerIP, 10001)).start();
        new Thread(new RobotFeedbackListener()).start();
    }
    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        String propertyName = arg0.getPropertyName();
        
        switch(propertyName) {
        	case "horizontal":Hmessages.add(Hstub.getJSON());    break;
        	case "vertical":Vmessages.add(Vstub.getJSON());break;
        	case "grabber":Gmessages.add(Gstub.getJSON());break;
        }
        
    }
    public void horizontalFeedback(String value) {
    	gui.setHorizontalProgressbar(Integer.parseInt(value));
    }
    public void verticalFeedback(String value) {
    	gui.setVerticalProgressbar(Integer.parseInt(value));

    }
    public void grabberFeedback(String value) {
    	
    }
    public void refreshList() {
    	for(String service: oldRobots) {
        	//gui.removeService(service);
    	}
    	for(String service: robots) {
        	gui.addService(service);

    	}
    }
    private class MessageSenderThread    implements Runnable  {
        int brokerPort;
        String brokerHost;
        
        public MessageSenderThread( String brokerHost, int brokerPort) {
                this.brokerPort = brokerPort;
                this.brokerHost = brokerHost;
        }

        @Override
        public void run() {
        	  while(true) {
                  try {
                      if(messages.size() > 0) {
                          System.out.println(messages.get(0));
                    	  Hstub.sendMessage(brokerHost, messages.get(0), brokerPort);
                          Thread.sleep(1000);

                    	  //System.out.println(Hstub.getConfirmation());
                          messages.remove(0);
                      }
                      if(Hmessages.size() > 0) {
                          System.out.println(Hmessages.get(0));
                    	  Hstub.sendMessage(brokerHost, Hmessages.get(0), brokerPort);
                          Thread.sleep(1000);

                    	  //System.out.println(Hstub.getConfirmation());
                          Hmessages.remove(0);
                      }
                      if(Vmessages.size() > 0) {
                          System.out.println(Vmessages.get(0));
                    	  Hstub.sendMessage(brokerHost, Vmessages.get(0), brokerPort);
                          Thread.sleep(1000);

                    	  //System.out.println(Vstub.getConfirmation());
                          Vmessages.remove(0);
                      }
                      if(Gmessages.size() > 0) {
                          System.out.println(Gmessages.get(0));
                    	  Hstub.sendMessage(brokerHost, Gmessages.get(0), brokerPort);
                          Thread.sleep(1000);

                    	  //System.out.println(Gstub.getConfirmation());
                          Gmessages.remove(0);
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
                this.doReceiverConnection(10003);
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
                    JSONArray robots = (JSONArray) robotsMsg.get("robots");
                    for(int i = 0 ; i < robots.size(); i++)
                    	gui.addService(robots.get(i).toString());
                    System.out.println(robotsMsg);
                    System.out.println(robotsMsg);
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
                this.doReceiverConnection(10010);
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
					JSONArray header = (JSONArray) feedback.get("header");
					JSONArray remoteCall = (JSONArray) feedback.get("remoteCall");
					JSONObject head = (JSONObject) header.get(0);
					JSONObject body = (JSONObject) remoteCall.get(0);
					JSONArray params = (JSONArray) body.get("params");
					JSONObject value = (JSONObject) params.get(0);
                    
					Method m;
					try {
						m = Consumer.this.getClass().getMethod(body.get("method").toString(),String.class);
						m.invoke(Consumer.this, value.get("value").toString());

					} catch (NoSuchMethodException | SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
        }
    }
  }
}
