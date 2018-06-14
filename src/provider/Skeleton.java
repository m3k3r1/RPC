package vs.provider;

public class Skeleton {
    Robot robot;

    public Skeleton(Robot robot) {
        this.robot = robot;
    }

    public void moveVertical(int percent) {
        robot.moveVertical(percent);
    }

    public void moveHorizontal(int percent) {
        robot.moveHorizontal(percent);
    }

    public void moveGrabber(int percent) {
        robot.moveGrabber(percent);
    }
}
