package vs.consumer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.json.simple.JSONObject;

public class Stub {
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);
    JSONObject obj = new JSONObject();

    public JSONObject getJSON() {
        return obj;
    }
    public void moveVertical(int percent, String robotId) {
        marshall(percent, robotId, "vertical");
    }
    public void moveHorizontal(int percent, String robotId) {
        marshall(percent, robotId, "horizontal");
    }
    public void moveGrabber(int percent, String robotId) {
        marshall(percent, robotId, "grabber");
    }
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
    private void marshall(int percent, String robotId, String movement) {
        obj.put("type", "action");
        obj.put("robot", robotId);
        obj.put("movement", movement);
        obj.put("value", percent);

        changes.firePropertyChange("stubJSON", null, obj);

    }
}
