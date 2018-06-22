package vs.consumer;

import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

public class GUIController implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer {

    private Stub Hstub;
    private Stub Vstub;
    private Stub Gstub;
    private String robotID;

    public GUIController(Stub Hstub, Stub Vstub, Stub Gstub) {
        this.Hstub = Hstub;
        this.Vstub = Vstub;
        this.Gstub = Gstub;
    }

    @Override
    public void update(String roboID) {
        this.robotID = roboID;
    }
    @Override
    public int moveHorizontalToPercent(int id, int percent) throws Exception {
    	Hstub.moveHorizontal(percent, robotID, id);
        return 0;
    }
    @Override
    public int moveVerticalToPercent(int id, int percent) throws Exception {
        Vstub.moveVertical(percent, robotID, id);
        return 0;
    }
    @Override
    public int closeGripper(int id) throws Exception {
        Gstub.moveGrabber(0, robotID, id);
        return 0;
    }
    @Override
    public int openGripper(int id) throws Exception {
        Gstub.moveGrabber(100, robotID, id);
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

