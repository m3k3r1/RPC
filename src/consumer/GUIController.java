package consumer;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

public class GUIController implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer {

    private Stub stub;
    private String robotID;

    public GUIController(Stub stub) {
        this.stub = stub;
    }

    @Override
    public void update(String roboID) {
        this.robotID = roboID;
    }
    @Override
    public int moveHorizontalToPercent(int arg0, int percent) throws Exception {
        stub.moveHorizontal(percent, robotID);
        return 0;
    }
    @Override
    public int moveVerticalToPercent(int arg0, int percent) throws Exception {
        stub.moveVertical(percent, robotID);
        return 0;
    }
    @Override
    public int closeGripper(int arg0) throws Exception {
        stub.moveGrabber(0, robotID);
        return 0;
    }
    @Override
    public int openGripper(int arg0) throws Exception {
        stub.moveGrabber(100, robotID);
        return 0;
    }


    //NOT USED
    @Override
    public void register(ICaDSRobotGUIUpdater arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public int isUltraSonicOccupied() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getCurrentVerticalPercent() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getCurrentHorizontalPercent() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int stop(int arg0) throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int isGripperClosed() throws Exception {
        // TODO Auto-generated method stub
        return 0;
    }
}

