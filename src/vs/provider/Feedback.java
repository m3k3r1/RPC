package vs.provider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import org.json.simple.JSONObject;

import vs.broker.Broker;

public class Feedback extends Service implements PropertyChangeListener{
	Robot robot;
	
	public Feedback(int port, String name, String robotName, String brokerHost, int brokerPort, Skeleton skeleton, Robot robot) throws SocketException {
		super(port, name, robotName, brokerHost, brokerPort, skeleton);
		this.robot = robot;
		robot.addPropertyChangeListener(this);
	}
	
    
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
        JSONObject obj = robot.getFeedback();
        
        System.out.println("[ACTION] - Action completed" + obj);
		
		try {
            this.doSenderConnection(brokerHost);
            this.sendMessage(obj, brokerPort);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
