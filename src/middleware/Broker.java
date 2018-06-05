package middleware;

import connection.ReceiverConnection;
import connection.SenderConnection;

import java.net.SocketException;

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
        public HorizontalStubListener() {
            try {
                this.doReceiverConnection(7799);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class VerticalStubListener extends ReceiverConnection {
        public VerticalStubListener() {
            try {
                this.doReceiverConnection(6699);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class GrabberStubListener extends ReceiverConnection {
        public GrabberStubListener() {
            try {
                this.doReceiverConnection(5599);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class HorizontalSkeletonListener extends ReceiverConnection {
        public HorizontalSkeletonListener() {
            try {
                this.doReceiverConnection(7788);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class VerticalSkeletonListener extends ReceiverConnection {
        public VerticalSkeletonListener() {
            try {
                this.doReceiverConnection(6688);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class GrabberSkeletonListener extends ReceiverConnection {
        public GrabberSkeletonListener() {
            try {
                this.doReceiverConnection(5588);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }
    private class NameServerLisntener extends ReceiverConnection {
        public NameServerLisntener() {
            try {
                this.doReceiverConnection(7790);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){

        }
    }

    public void saveMessageToFile(){ }
    public void checkFile(){}

    public static void main(String[] args){
        new Broker();
    }
}
