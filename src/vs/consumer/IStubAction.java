package vs.consumer;

public interface IStubAction {
    public void moveVertical(int percent, String robotId, int id);
    public void moveHorizontal(int percent, String robotId, int id);
    public void moveGrabber(int percent, String robotId, int id);
}
