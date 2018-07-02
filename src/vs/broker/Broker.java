package vs.broker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import vs.connection.ReceiverConnection;
import vs.connection.SenderConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Broker  extends SenderConnection {
    HashMap<String, String> registredConsumers;
    HashMap<String, String> registredProvider;
    HashMap<String, String> HQueue;
    HashMap<String, String> VQueue;
    boolean consecH = false;
    boolean consecV = false;

    public Broker(int providerPort, int consumerPort) {
    	registredConsumers = new HashMap<>();
    	registredProvider = new HashMap<>();
    	HQueue = new HashMap<>();
    	VQueue = new HashMap<>();
    	Timer cleaner = new Timer();
    	cleaner.scheduleAtFixedRate(new CacheCleaner(), 0, 5000);
    	
        new Thread(new MessagesListener(consumerPort)).start();
        new Thread(new MessagesListener(providerPort)).start();
        new Thread(new HeartBeatServer()).start();

     }
    public void registerGUI(String ip, String name) {
    	registredConsumers.put(ip, name);
    	System.out.println("[REGISTRY] - " + name + " registred as a consumer in " + ip);
    	
    }
    public void registerRobot(String ip, String name) {
    	if(registredProvider.containsKey(name)) {
        	System.out.println("[REGISTRY] - Provider service already registred");
    	}else {
        	registredProvider.put(name, ip);
        	System.out.println("[REGISTRY] - " + name + " registred as a provider");
    	}    	
    }
    public void getAvailableServices(String ip, String port) {
    	JSONObject msg = new JSONObject();
        JSONObject r = new JSONObject();
    	JSONArray robots = new JSONArray();
    	String ant = " ";
    	
    	for (Map.Entry<String, String> entry : registredProvider.entrySet()) {
            String s = entry.getKey();
            String robot = s.split("/")[0];
            if(!ant.equals(robot)) {
            	robots.add(robot);
            	ant = robot;
            }
            
    	}
    	
    	msg.put("robots", robots);
    	try {
			this.doSenderConnection(ip);
			this.sendMessage(msg, 10003);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    public void forwardToProvider(JSONObject actionMsg, String method, String robot, String value) {
    	switch(method) {
			case "moveHorizontal" : 
				for (Map.Entry<String, String> entry : registredProvider.entrySet()) {
		            String service = entry.getKey();
		            String ip = entry.getValue();
		            String r = service.split("/")[0];
		            String move = service.split("/")[1];
		            move = move.split(":")[0];

		            if(r.equals(robot)&& move.equals(method)) {
			            int port = Integer.parseInt(service.split(":")[1]); 
			            try {
							this.doSenderConnection(ip);
							this.sendMessage(actionMsg, port);

							for (Map.Entry<String, String> e : HQueue.entrySet()) {
					            String rbt = e.getKey();
					            String v = e.getValue();
					            
					            int last_v = Integer.parseInt(v);
					            int current_v = Integer.parseInt(value);
					            System.out.println(last_v + " - " + current_v);
					           if(last_v > current_v) {
						            System.out.println("PARA!!!");

					            	JSONObject msg = new JSONObject();
					            	msg.put("para", "para");
									this.doSenderConnection(ip);
									this.sendMessage(msg, 9999);
									this.sendMessage(actionMsg, port);
					            }
							}
							
							
						} catch (IOException e) {
							e.printStackTrace();
						}  
		            }
		        }
	            HQueue.put(robot ,value);
				
		    	
				break;
			case "moveVertical"   : 
				for (Map.Entry<String, String> entry : registredProvider.entrySet()) {
		            String service = entry.getKey();
		            String ip = entry.getValue();
		            String r = service.split("/")[0];
		            String move = service.split("/")[1];
		            move = move.split(":")[0];
		            
		            if(r.equals(robot) && move.equals(method)) {
			            int port = Integer.parseInt(service.split(":")[1]); 
			            try {
							this.doSenderConnection(ip);
							this.sendMessage(actionMsg, port);
							
							for (Map.Entry<String, String> e : VQueue.entrySet()) {
					            String rbt = e.getKey();
					            String v = e.getValue();
					            
					            int last_v = Integer.parseInt(v);
					            int current_v = Integer.parseInt(value);
					            System.out.println(last_v + " - " + current_v);

					           if(last_v > current_v) {
					            	JSONObject msg = new JSONObject();
					            	msg.put("para", "para");
									this.doSenderConnection(ip);
									this.sendMessage(msg, 9999);
									this.sendMessage(actionMsg, port);
					            }
							}
						} catch (IOException e) {
							e.printStackTrace();
						}  
		            } 
		        }
				
	            VQueue.put(robot ,value);
								
				break;
			case "moveGrabber"    :
		    	for (Map.Entry<String, String> entry : registredProvider.entrySet()) {
		            String service = entry.getKey();
		            String ip = entry.getValue();
		            String r = service.split("/")[0];
		            String move = service.split("/")[1];
		            move = move.split(":")[0];

		            if(r.equals(robot) && move.equals(method)) {
			            int port = Integer.parseInt(service.split(":")[1]); 
			            try {
							this.doSenderConnection(ip);
							this.sendMessage(actionMsg, port);
						} catch (IOException e) {
							e.printStackTrace();
						}     
		            } 
		        }
		    	HQueue.clear();
		    	VQueue.clear();
				break;
    	}
    }
    public void forwardToGUI(JSONObject msg) throws SocketException, IOException {
    	for (Map.Entry<String, String> entry : registredConsumers.entrySet()) {
            String ip = entry.getKey();
			this.doSenderConnection(ip);
			this.sendMessage(msg, 10010);    
    	}
    }
    public void emergencyBrake() {
    	
    	if(registredProvider.size() > 0 ) {
    		for (Map.Entry<String, String> entry : registredProvider.entrySet()) {
                String ip = entry.getValue();
    	            	JSONObject msg = new JSONObject();
    	            	msg.put("para", "para");
    					try {
    						this.doSenderConnection(ip);
    						this.sendMessage(msg, 9999);
    					} catch (SocketException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					} catch (UnknownHostException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					} catch (IOException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
    			           
    			
            }
    	} else {
            //System.out.println("No Robots to stop yet!!!");
    	}
    }
    private class MessagesListener extends ReceiverConnection {
        public MessagesListener(int port) {
            try {
                this.doReceiverConnection(port);
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
                    int port = packet.getPort();
                    String ip = packet.getAddress().toString().substring(1);
                    InetAddress IPAddress = packet.getAddress();
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    JSONObject consumerMsg = new JSONObject();
                    consumerMsg = (JSONObject) iStream.readObject() ;
					JSONArray header = (JSONArray) consumerMsg.get("header");
					JSONArray remoteCall = (JSONArray) consumerMsg.get("remoteCall");
					JSONObject head = (JSONObject) header.get(0);
					JSONObject body = (JSONObject) remoteCall.get(0);
					JSONArray params = (JSONArray) body.get("params");
					JSONObject value = (JSONObject) params.get(0);
					
			        byte[] sendData = new byte[1024];
			        JSONObject id = new JSONObject();
			        id.put("id", head.get("id"));
			        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			        ObjectOutputStream oos = new ObjectOutputStream(baos);
			        oos.writeObject(id);
			        oos.close();
			        sendData = baos.toByteArray();
		            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		            socket.send(sendPacket);
		            
					
					switch(head.get("destination").toString()) {
						case"broker": 
							Method m = Broker.this.getClass().getMethod(body.get("method").toString(),String.class, String.class);
							m.invoke(Broker.this, ip, head.get("origin").toString());
							break;
						case "GUI":
							forwardToGUI(consumerMsg);
						default:
							forwardToProvider(consumerMsg, body.get("method").toString(), head.get("destination").toString(), value.get("value").toString());
							break;
					}
                } catch (IOException |ClassNotFoundException | SecurityException|NoSuchMethodException|IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();

                }
            }
        }
    }
    private class HeartBeatServer extends ReceiverConnection {
    	Timer timer;
    	
    	public HeartBeatServer() {
    		try {
    			timer = new Timer();
				this.doReceiverConnection(5500);
				timer.scheduleAtFixedRate(new TimerTask() {
					  @Override
					  public void run() {
						  emergencyBrake();
					  }
					}, 505, 500);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	@Override
    	public void run() {
    		while(true) {
                try {
           		 DatagramPacket packet = new DatagramPacket(buf, buf.length);
           		 socket.receive(packet);
           		 timer.cancel();
           		 timer = new Timer();
           		 timer.scheduleAtFixedRate(new TimerTask() {
					  @Override
					  public void run() {
						  emergencyBrake();
					  }
					}, 505, 500);
   		         ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
   	             JSONObject heartBeatMsg = new JSONObject();
   	             heartBeatMsg = (JSONObject) iStream.readObject();
   			} catch (IOException | ClassNotFoundException  e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
    		}
    
    	}
    }
    private class CacheCleaner extends TimerTask{
		@Override
		public void run() {
			HQueue.clear();
			VQueue.clear();
			System.out.println("Clearing cache");
		}
    }
    
    public static void main(String[] args){
    	if(args.length <= 0) {
    		System.out.println("Usage: java -jar Broker.jar <provider port> <consumer port>");
    	}
    	
    	new Broker(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
