package provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import java.net.SocketException;

public class Provider {
    public static void main(String[] args) {
        Robot robot = new Robot();
        CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.SIMULATION, robot, robot);

        try {
            //Horizontal Movement
            Skeleton skeletonH = new Skeleton(robot);
            Service serviceH = new Service("127.0.0.1","horizontal", "teste1" ,"localhost", 1000 ,skeletonH);
            serviceH.doRegistry();
            serviceH.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            //Vertical Movement
            Skeleton skeletonV = new Skeleton(robot);
            Service serviceV = new Service("127.0.0.2","vertical", "teste1" ,"localhost", 1000 ,skeletonV);
            serviceV.doRegistry();
            serviceV.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            //Grabber Movement
            Skeleton skeletonG = new Skeleton(robot);
            Service serviceG = new Service("127.0.0.3","grabber", "teste1" ,"localhost", 1000 ,skeletonG);
            serviceG.doRegistry();
            serviceG.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }



    }
}
