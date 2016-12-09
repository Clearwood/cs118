import uk.ac.warwick.dcs.maze.logic.IEvent;
import uk.ac.warwick.dcs.maze.logic.Event;
import uk.ac.warwick.dcs.maze.logic.EventBus;
import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.awt.*;
import java.util.ArrayList;

public class Event3 {
    public class RobotData {
        public Point target = new Point();
	public void add(Point nP){
	this.target = nP;
	}

    }
    public RobotData robotData;
    public void controlRobot(IRobot robot) {
        if (robot.getRuns() == 0) {
	    System.out.println("first Run");
            Point target = robot.getTargetLocation();
            target.x = robot.getTargetLocation().x;
            target.y = robot.getTargetLocation().y;
	    robotData.add(robot.getTargetLocation());
            EventBus.broadcast(new
                    Event(IEvent.ROBOT_RELOCATE, target));
        } else{
  	System.out.println("second run");	
        EventBus.broadcast(new
                Event(IEvent.ROBOT_RELOCATE, robotData.target));

    }
}
}
