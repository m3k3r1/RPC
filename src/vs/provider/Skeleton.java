package vs.provider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Skeleton implements ISkeletonAction{
    Robot robot;

    public Skeleton(Robot robot) {
        this.robot = robot;
    }

    public void moveVertical(String percent) {
    	System.out.println("move v");
        robot.moveVertical(percent);
    }

    public void moveHorizontal(String percent) {
    	System.out.println("move h");

        robot.moveHorizontal(percent);
    }

    public void moveGrabber(String percent) {
    	System.out.println("move g");

        robot.moveGrabber(percent);
    }
    public void stop() {
    	System.out.println("move g");
    	robot.stop();
    }
    
    public void unmarshall(JSONObject msg, String serviceName) {
    	try {
        	JSONArray remoteCall = (JSONArray) msg.get("remoteCall");
    		JSONObject body = (JSONObject) remoteCall.get(0);
    		JSONArray params = (JSONArray) body.get("params");
    		JSONObject value = (JSONObject) params.get(0);

            if(body.get("method").equals(serviceName)){
    			try {
    	        	Method m = this.getClass().getMethod(body.get("method").toString(),String.class);
    				m.invoke(this, value.get("value").toString());
    			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
    				e.printStackTrace();
    			} catch (NoSuchMethodException e) {
    				e.printStackTrace();
    			} catch (SecurityException e) {
    				e.printStackTrace();
    			}
            }
    	}catch(NullPointerException e) {
        	stop();
        }
    }
}
