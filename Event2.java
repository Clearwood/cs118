import uk.ac.warwick.dcs.maze.logic.IEvent;
import uk.ac.warwick.dcs.maze.logic.Event;
import uk.ac.warwick.dcs.maze.logic.EventBus;
import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.awt.*;
import java.util.ArrayList;

public class Event2 {
    private int pollRun = 0;
    public void reset() {
        pollRun = 0;
    }
    class RobotData {
        public ArrayList<Integer> junctions = new ArrayList<Integer>();
        public void add(int arrived) {
            this.junctions.add(arrived);
            this.printJunction();
        }
        public void printJunction() {
            int dir = junctions.get(junctions.size() - 1);
            int index = junctions.lastIndexOf(dir);
            String str = direction(dir);
            System.out.println("Junction " + index + " heading " + str);
        }

    }
    private String direction(int dir) {
        switch (dir) {
            case IRobot.NORTH:
                return "NORTH";
            case IRobot.SOUTH:
                return "SOUTH";
            case IRobot.WEST:
                return "WEST";
            case IRobot.EAST:
                return "EAST";
        }
        return "";
    }

    private RobotData robotData;
    public void controlRobot(IRobot robot) {
        if (pollRun == 0) {
            pollRun++;
            Point target = new Point();
            target.x = robot.getTargetLocation().x;
            target.y = robot.getTargetLocation().y-1;
            EventBus.broadcast(new
                    Event(IEvent.ROBOT_RELOCATE, target));
            if(robot.getLocation().y < robot.getTargetLocation().y){
                robotData.add(IRobot.SOUTH);
                robot.setHeading(IRobot.SOUTH);
            }
        }


    }
}