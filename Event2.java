import uk.ac.warwick.dcs.maze.logic.IEvent;
import uk.ac.warwick.dcs.maze.logic.Event;
import uk.ac.warwick.dcs.maze.logic.IEventClient;
import uk.ac.warwick.dcs.maze.logic.EventBus;
import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.awt.*;
import java.util.ArrayList;

public class Event2 implements IEventClient{
    /*
    public void reset() {
        pollRun = 0;
    }
    */
    public void notify(IEvent var1){
    }
    class RobotData {
        public Point target = new Point();
    }
    public Event2(){

    }
    private RobotData robotData;
    public void controlRobot(IRobot robot) {
        if (robot.getRuns() == 0) {
            System.out.println("first Run");
            Point target = new Point();
            target.x = robot.getTargetLocation().x;
            target.y = robot.getTargetLocation().y;
            EventBus.broadcast(new
                    Event(IEvent.ROBOT_RELOCATE, target));
        }
        Point target = robotData.target;
        System.out.println(target.x);
        System.out.println(target.y);
        EventBus.broadcast(new
                Event(IEvent.ROBOT_RELOCATE, target));

    }
}