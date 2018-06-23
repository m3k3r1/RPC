package vs.provider;

import java.net.SocketException;

import org.json.simple.JSONObject;

public class Brake extends Service {

	public Brake(int port, String name, String robotName, String brokerHost, int brokerPort, Skeleton skeleton)
			throws SocketException {
		super(port, name, robotName, brokerHost, brokerPort, skeleton);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void handleCall(JSONObject msg){
    	try {
			if(msg.get("para").toString().equals("para")) 
	    		skeleton.stop();
			System.out.println("PARA");

    	}  	catch(NullPointerException e) {
    		//TODO
    	}
    }

}
