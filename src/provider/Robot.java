package provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotFeedBackListener;
import org.cads.ev3.middleware.hal.ICaDSEV3RobotStatusListener;
import org.json.simple.JSONObject;

public class Robot implements ICaDSEV3RobotStatusListener, ICaDSEV3RobotFeedBackListener {
    int oldValue = 0;

    public Robot() {
    }

    public void moveVertical(int percent) {
        int n = percent;
        if(percent > oldValue){
            CaDSEV3RobotHAL.getInstance().stop_h();
            CaDSEV3RobotHAL.getInstance().moveUp();
        } else {
            CaDSEV3RobotHAL.getInstance().stop_h();
            CaDSEV3RobotHAL.getInstance().moveDown();
        }

        oldValue = percent;
    }
    public void moveHorizontal(int percent) {
        int n = percent;
        if(percent > oldValue){
            CaDSEV3RobotHAL.getInstance().stop_v();
            CaDSEV3RobotHAL.getInstance().moveLeft();
        } else {
            CaDSEV3RobotHAL.getInstance().stop_v();
            CaDSEV3RobotHAL.getInstance().moveRight();
        }

        oldValue = percent;
    }
    public void moveGrabber(int percent) {
        int n = percent;
        if(percent > oldValue){
            CaDSEV3RobotHAL.getInstance().doOpen();
        } else {
            CaDSEV3RobotHAL.getInstance().doClose();
        }

        oldValue = percent;
    }{

    }

    @Override
    public void giveFeedbackByJSonTo(JSONObject jsonObject) {

    }

    @Override
    public void onStatusMessage(JSONObject jsonObject) {

    }
}
