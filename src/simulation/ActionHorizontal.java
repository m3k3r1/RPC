package simulation;

import lejos.utility.Delay;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class ActionHorizontal  {
    protected static CaDSEV3RobotHAL caller;

    private class ActionThread implements Runnable, ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
        @Override
        public void giveFeedbackByJSonTo(JSONObject arg0) {
            // TODO Auto-generated method stub

        }
        @Override
        public void onStatusMessage(JSONObject arg0) {
            // horizontalValue = arg0.get("percent").toString();

        }
        @Override
        public void run() {
            try {
                caller = CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, this, this);
                boolean on = true;
                while (!Thread.currentThread().isInterrupted()) {
                    if (on) {
                        caller.stop_h();
                        caller.moveLeft();
                    } else {
                        caller.stop_h();
                        caller.moveRight();
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
