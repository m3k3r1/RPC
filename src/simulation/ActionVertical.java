package simulation;

import lejos.utility.Delay;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class ActionVertical {
    protected static CaDSEV3RobotHAL caller;
    String lim;

    public ActionVertical(String  lim) {
        this.lim = lim;
    }

    public void startThread(){
        new Thread(new ActionThread()).start();
    }

    private class ActionThread implements Runnable, ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
        @Override
        public void giveFeedbackByJSonTo(JSONObject arg0) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onStatusMessage(JSONObject arg0) {
            // horizontalValue = ;
            if(arg0.get("percent").toString().equals(lim))
                caller.stop_v();
        }
        @Override
        public void run() {
            try {
                caller = CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, this, this);
                boolean on = true;
                while (!Thread.currentThread().isInterrupted()) {
                    if (on) {
                        caller.stop_v();
                        caller.moveDown();
                    } else {
                        caller.stop_v();
                        caller.moveUp();
                    }
                    Delay.msDelay(5100);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
            System.exit(0);
        }
    }
}
