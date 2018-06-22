package vs.provider;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Robot implements ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	JSONObject feedback;
	int oldH = 0;
	int oldV = 0;
	int oldG = 0;
	String name;
	boolean sendHFeedback = false;
	boolean sendVFeedback = false;
	String limH = "";
	String limV = "";
	
    public Robot(String name) {
    	this.name = name;
    }
    
    public void stop() {
        CaDSEV3RobotHAL.getInstance().stop_v();
        CaDSEV3RobotHAL.getInstance().stop_h();
    }

    public void moveVertical(String percent) {
    	sendVFeedback = true;
        CaDSEV3RobotHAL.getInstance().stop_v();
    	int p = Integer.parseInt(percent);
    	limV = percent;
    	if(p > oldV){
           CaDSEV3RobotHAL.getInstance().stop_v();
           CaDSEV3RobotHAL.getInstance().moveUp();
        } else {
           CaDSEV3RobotHAL.getInstance().stop_v();
           CaDSEV3RobotHAL.getInstance().moveDown();
        }

        oldV = p;
    }
    public void moveHorizontal(String percent) {
    	sendHFeedback = true;
        CaDSEV3RobotHAL.getInstance().stop_h();
    	int p = Integer.parseInt(percent);

    	limH = percent;
        if(p > oldH){ 
            CaDSEV3RobotHAL.getInstance().stop_h();
            CaDSEV3RobotHAL.getInstance().moveLeft();
        } else {
            CaDSEV3RobotHAL.getInstance().stop_h();
            CaDSEV3RobotHAL.getInstance().moveRight();
        }

        oldH = p;
    }
    public void moveGrabber(String percent) {
    	int p = Integer.parseInt(percent);
        if(p > oldG){
            CaDSEV3RobotHAL.getInstance().doOpen();
        } else {
            CaDSEV3RobotHAL.getInstance().doClose();
        }

        oldG = p;
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
    public JSONObject getFeedback() {
    	return feedback;
    }
    private void setFeedback(String type, String value) {
		feedback = new JSONObject();


		JSONObject head = new JSONObject();
    	JSONObject body = new JSONObject();
    	JSONObject values = new JSONObject();
    	JSONArray params = new JSONArray();
    	JSONArray  header = new JSONArray();
    	JSONArray  remoteCall = new JSONArray();

    	head.put("id", "");
    	head.put("origin", name);
        head.put("destination", "GUI");
        head.put("destinationIP", "");
        header.add(head);
        
        body.put("method",type +"Feedback");
        values.put("value", value);
        params.add(values);
        body.put("params", params);
        body.put("return", "int");
        remoteCall.add(body);
        
        feedback.put("header", header);
        feedback.put("remoteCall", remoteCall);
	
		
		changes.firePropertyChange("feedbackJSON", null, feedback);	
    }
    @Override
    public void giveFeedbackByJSonTo(JSONObject arg1) {
    }
    @Override
    public void onStatusMessage(JSONObject feedback) {	
    	String type = feedback.get("state").toString();
    	
    	switch(type) {
	    	case "horizontal":  
				if(limH.equals(feedback.get("percent").toString())) { 
			    	CaDSEV3RobotHAL.getInstance().stop_h();
			    	if(sendHFeedback) {
			    		setFeedback("horizontal", feedback.get("percent").toString());
			    		sendHFeedback = false;
			    	}
				}
			    break;
	    	case "vertical": 
		    	if(limV.equals(feedback.get("percent").toString())) {
		    		CaDSEV3RobotHAL.getInstance().stop_v();
			    	if(sendVFeedback) {
			    		setFeedback("vertical", feedback.get("percent").toString());
			    		sendVFeedback = false;
			    	}
		    	}
		    	break;
		    	
    	}
    }
}
