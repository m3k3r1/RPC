package vs.provider;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class Robot implements ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	JSONObject feedback;
	int oldValue = 0;
	int oldH = 0;
	int oldV = 0;
	int oldG = 0;
	
    public Robot() {
    }

    public void moveVertical(int percent) {
        if(percent > oldValue){
           CaDSEV3RobotHAL.getInstance().moveUp();
        } else {
           CaDSEV3RobotHAL.getInstance().moveDown();
        }

        oldValue = percent;
    }
    public void moveHorizontal(int percent) {
        if(percent > oldValue){ 
            CaDSEV3RobotHAL.getInstance().moveLeft();
        } else {
            CaDSEV3RobotHAL.getInstance().moveRight();
        }

        oldValue = percent;
    }
    public void moveGrabber(int percent) {
        if(percent > oldValue){
            CaDSEV3RobotHAL.getInstance().doOpen();
        } else {
            CaDSEV3RobotHAL.getInstance().doClose();
        }

        oldValue = percent;
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
    private void setFeedback(int h, int v, int g) {
    	if(h!=oldH || v!=oldV || g!=oldG) {
    		feedback = new JSONObject();
    		feedback.put("type", "feedback");
    		feedback.put("horizontal", h);
    		feedback.put("vertical", v);
    		feedback.put("grabber", g);
    		
    		changes.firePropertyChange("feedbackJSON", null, feedback);
    		
    		oldH = h;
    		oldV = v;
    		oldG = g;
    	}
    }
    @Override
    public void giveFeedbackByJSonTo(JSONObject arg1) {
    	
    }

    @Override
    public void onStatusMessage(JSONObject feedback) {	
    	String type = feedback.get("state").toString();
    	int h = 0,v = 0,g = 0;
    	
    	switch(type) {
	    	case "horizontal": h = Integer.parseInt(feedback.get("percent").toString()); break;
	    	case "vertical": v = Integer.parseInt(feedback.get("percent").toString());break;
	    	case "grabber": g = Integer.parseInt(feedback.get("percent").toString());break;
    	}
    	    	
    	setFeedback(h,v,g);
    	
    
    }
}
