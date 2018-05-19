package simulation;

import lejos.utility.Delay;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class ActionHorizontal  {
    protected static CaDSEV3RobotHAL caller;
    String lim;
    String orientation;
    ActionThread a;

    public ActionHorizontal(String  lim, String orientation) {
        this.lim = lim;
        this.orientation = orientation;
         a = new ActionThread();
    }
    public ActionHorizontal() {
         a = new ActionThread();
    }

    public void moveAround(String orientation, String percent){
        this.lim = percent;
        this.orientation = orientation;
        a.moveAround(orientation,percent);
    }

    private class ActionThread implements Runnable, ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
        boolean pause = false;

        public ActionThread() {
            caller = CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, this, this);
        }

        @Override
        public void giveFeedbackByJSonTo(JSONObject arg0) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onStatusMessage(JSONObject arg0) {
            if(arg0.get("percent").toString().equals(lim)) {
                caller.stop_h();
                pause = true;
                this.notify();
            }
        }

        public void moveAround(String orientation, String percent){
            new Thread(this).start();
        }

        @Override
        public void run() {
            if(orientation.equals("left")) {
                caller.stop_h();
                caller.moveLeft();
            }else {
                caller.stop_h();
                caller.moveRight();
            }

            Delay.msDelay(2000);
        }

        synchronized public void waithere() {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ActionHorizontal h = new ActionHorizontal();
        h.moveAround("left","30");
        }
}
