 package vs.provider;

import org.cads.ev3.middleware.CaDSEV3RobotHAL;
import org.cads.ev3.middleware.CaDSEV3RobotType;

import java.net.SocketException;

public class Provider {
    public static void main(String[] args) {
    	if(args.length <= 0) {
    		System.out.println("Usage: java -cp Provider.jar vs.provider.Provider <broker ip> <robot name>");
    	} else {
	        Robot robot = new Robot(args[1]);
	        CaDSEV3RobotHAL.createInstance(CaDSEV3RobotType.REAL, robot, robot);
	        
	        try {
	            //Horizontal Movement Service
	            Skeleton skeletonH = new Skeleton(robot);
	            Service serviceH = new Service(7798,"moveHorizontal", args[1] ,args[0], 10000 ,skeletonH);
	            serviceH.doRegistry();
	            serviceH.start();
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
	
	        try {
	            //Vertical Movement Service
	            Skeleton skeletonV = new Skeleton(robot);
	            Service serviceV = new Service(7799,"moveVertical", args[1] ,args[0], 10000 ,skeletonV);
	            serviceV.doRegistry();
	            serviceV.start();
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
	
	        try {
	            //Grabber Movement Service
	            Skeleton skeletonG = new Skeleton(robot);
	            Service serviceG = new Service(8000,"moveGrabber", args[1] ,args[0], 10000 ,skeletonG);
	            serviceG.doRegistry();
	            serviceG.start();
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
	        
	        try {
	            //Emergency stop
	            Skeleton skeletonStop = new Skeleton(robot);
	            Service serviceStop = new Brake(9999,"stop", args[1] ,args[0], 10000 ,skeletonStop);
	            serviceStop.doRegistry();
	            serviceStop.start();
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
	        	        
	        try {
	            //Feedback Service
	            Service serviceG = new Feedback(8001,"feedback", args[1] ,args[0], 10000 ,null, robot);
	            serviceG.doRegistry();
	            serviceG.start();
	        } catch (SocketException e) {
	            e.printStackTrace();
	        }
    	}
    }
}
