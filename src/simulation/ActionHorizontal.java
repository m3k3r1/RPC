package simulation;

import lejos.utility.Delay;
import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class ActionHorizontal  {
    protected static CaDSEV3RobotHAL caller;
    ActionThread a;

    public ActionHorizontal() {
         a = new ActionThread();
    }

    public void moveAround(String move, String orientation, String percent){
        a.moveAround(move, orientation,percent);
    }

    private class ActionThread implements Runnable, ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
        boolean pause = false;
        String lim;
        String orientation;
        String move;

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
                    if(move.equals("horizontal")) {
                        caller.stop_h();
                        caller.stop_v();
                    }else{
                        caller.stop_h();
                        caller.stop_v();

                    }
                    pause = true;
                }
            }
        }

        public void moveAround(String move, String orientation, String percent){
            this.move = move;
            this.orientation = orientation;
            this.lim = percent;
            new Thread(this).start();
        }

        @Override
        public void run() {

            switch (orientation) {
                case "open":
                    caller.stop_v();
                    caller.stop_h();
                    caller.doOpen();
                    break;
                case "close":
                    caller.stop_v();
                    caller.stop_h();
                    caller.doClose();
                    break;
                case "left":
                    caller.stop_v();
                    caller.stop_h();
                    caller.moveLeft();
                    break;
                case "right":
                    caller.stop_v();
                    caller.stop_h();
                    caller.moveRight();
                    break;
                case "up":
                    caller.stop_v();
                    caller.stop_h();
                    caller.moveUp();
                    break;
                case "down":
                    caller.stop_v();
                    caller.stop_h();
                    caller.moveDown();
                    break;
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
        }
}
