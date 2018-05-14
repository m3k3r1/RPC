package nameserver;

import connection.ReceiverConnection;

import java.util.HashMap;

public class NameServer {
    HashMap<String ,String> routingTable;

    public NameServer() {
        this.routingTable = new HashMap<>();
        new Thread(new SkeletonListener()).start();
        new Thread(new StubListener()).start();
    }

    private class SkeletonListener extends ReceiverConnection implements Runnable{
        public SkeletonListener() {
        }

        @Override
        public void run(){

        }
    }
    private class StubListener extends ReceiverConnection implements Runnable{
        public StubListener() {
        }

        @Override
        public void run(){

        }
    }

    private void addService(String hostLocation){}
    private void removeService(String hostLocation){}

}
