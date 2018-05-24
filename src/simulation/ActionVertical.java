package simulation;

import lejos.utility.Delay;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class ActionVertical {
    protected static CaDSEV3RobotHAL caller;
    ActionThread a;

    public ActionVertical() {
        a = new ActionThread();
    }

    public void moveAround(String orientation, String percent){
        a.moveAround(orientation,percent);
    }

    private class ActionThread implements Runnable, ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
        boolean pause = false;
        String lim;
        String orientation;

        public ActionThread() {
            caller = CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, this, this);
        }

        @Override
        public void giveFeedbackByJSonTo(JSONObject arg0) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onStatusMessage(JSONObject arg0) {

            if(arg0 != null){
                if(arg0.get("percent").toString().equals(lim)) {
                    caller.stop_v();
                    pause = true;
                }
            }
        }

        public void moveAround(String orientation, String percent){
            this.orientation = orientation;
            this.lim = percent;
            new Thread(this).start();
        }

        @Override
        public void run() {
            if(orientation.equals("down")) {
                caller.stop_v();
                caller.moveDown();
            }else {
                caller.stop_v();
                caller.moveUp();
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
}
