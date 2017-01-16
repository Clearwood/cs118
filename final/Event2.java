import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;
public class Event2{ public Point target1 = new Point();
    public void controlRobot(IRobot robot){
        target1 = (robot.getRuns()==0) ? robot.getTargetLocation() : target1;
        EventBus.broadcast(new Event(IEvent.ROBOT_RELOCATE, (new Point((target1.x + ((((robot.getHeading() - 1000) % 2) == 0) ? 0 : ((robot.getHeading() == 1001) ? -1 : 1))), (target1.y + ((((robot.getHeading() - 1000) % 2) == 1) ? 0 : ((robot.getHeading() == 1000)? 1 : -1  ))))))); } }