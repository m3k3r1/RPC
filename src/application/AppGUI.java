package application;

import connection.SenderConnection;
import message.Message;
import org.cads.ev3.gui.ICaDSRobotGUIUpdater;
import org.cads.ev3.gui.swing.CaDSRobotGUISwing;
import org.cads.ev3.rmi.consumer.ICaDSRMIConsumer;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveGripper;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveHorizontal;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIMoveVertical;
import org.cads.ev3.rmi.generated.cadSRMIInterface.IIDLCaDSEV3RMIUltraSonic;

import java.io.IOException;

public class AppGUI extends SenderConnection implements IIDLCaDSEV3RMIMoveGripper, IIDLCaDSEV3RMIMoveHorizontal, IIDLCaDSEV3RMIMoveVertical, IIDLCaDSEV3RMIUltraSonic, ICaDSRMIConsumer {

    public void sendMessage(Message m){
        try {
            this.doSenderConnection();
            this.sendMessage(m,7798);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void register(ICaDSRobotGUIUpdater iCaDSRobotGUIUpdater) {

    }

    @Override
    public void update(String s) {

    }

    @Override
    public int closeGripper(int i) throws Exception {
        return 0;
    }

    @Override
    public int openGripper(int i) throws Exception {
        return 0;
    }

    @Override
    public int isGripperClosed() throws Exception {
        return 0;
    }

    @Override
    public int moveHorizontalToPercent(int transactionID, int percent) throws Exception {
        System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);
        sendMessage(new Message("horizontal",transactionID,percent));
        return 0;
    }

    @Override
    public int moveVerticalToPercent(int transactionID, int percent) throws Exception {
        System.out.println("Call to move vertical -  TID: " + transactionID + " degree " + percent);

        return 0;
    }

    @Override
    public int stop(int transactionID) throws Exception {
        return 0;
    }

    @Override
    public int getCurrentVerticalPercent() throws Exception {
        return 0;
    }

    @Override
    public int getCurrentHorizontalPercent() throws Exception {
        return 0;
    }

    @Override
    public int isUltraSonicOccupied() throws Exception {
        return 0;
    }

    public static void main(String[] args){
        AppGUI g = new AppGUI();
        CaDSRobotGUISwing gui = new CaDSRobotGUISwing(g,g,g,g,g);
    }
}
