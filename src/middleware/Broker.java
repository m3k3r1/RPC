package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;

public class Broker extends SenderConnection
{
    public Broker() {
        new Thread(new HorizontalSkeletonListener()).start();
        new Thread(new HorizontalStubListener()).start();
        new Thread(new VerticalStubListener()).start();
        new Thread(new VerticalSkeletonListener()).start();
        new Thread(new GrabberSkeletonListener()).start();
        new Thread(new GrabberStubListener()).start();
        new Thread(new NameServerLisntener()).start();
    }

    private class HorizontalStubListener extends ReceiverConnection {

    }
    private class VerticalStubListener extends ReceiverConnection {

    }
    private class GrabberStubListener extends ReceiverConnection {

    }
    private class HorizontalSkeletonListener extends ReceiverConnection {

    }
    private class VerticalSkeletonListener extends ReceiverConnection {

    }
    private class GrabberSkeletonListener extends ReceiverConnection {

    }
    private class NameServerLisntener extends ReceiverConnection {

    }

    public void saveMessageToFile(){ }
    public void checkFile(){}
    
    public static void main(String[] args){
        new Broker();
    }
}
