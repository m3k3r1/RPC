package vs.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import java.net.SocketException;

public class Provider {
    public static void main(String[] args) {
        Robot robot = new Robot();
        CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, robot, robot);
         
        try {
            //Horizontal Movement Service
            Skeleton skeletonH = new Skeleton(robot);
            Service serviceH = new Service(7798,"horizontal", "teste1" ,"localhost", 1000 ,skeletonH);
            serviceH.doRegistry();
            serviceH.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            //Vertical Movement Service
            Skeleton skeletonV = new Skeleton(robot);
            Service serviceV = new Service(7799,"vertical", "teste1" ,"localhost", 1000 ,skeletonV);
            serviceV.doRegistry();
            serviceV.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            //Grabber Movement Service
            Skeleton skeletonG = new Skeleton(robot);
            Service serviceG = new Service(8000,"grabber", "teste1" ,"localhost", 1000 ,skeletonG);
            serviceG.doRegistry();
            serviceG.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
        try {
            //Feedback Service
            Service serviceG = new Feedback(8001,"feedback", "teste1" ,"localhost", 1000 ,null, robot);
            serviceG.doRegistry();
            serviceG.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        




    }
}
