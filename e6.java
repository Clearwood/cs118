import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;
public class e6 {
    private int pollRun = 0;
    private static int maxJunctions = 10000;
    private static int junctionCounter;
    private int explorerMode;
    class RobotData {
        public ArrayList<junctionRecorder> junctions = new ArrayList<junctionRecorder>();
        public void resetJunctionCounter(){
            junctionCounter = 0;
        }
        public void add(int x, int y, int arrived){
            junctionRecorder arrive = new junctionRecorder(x,y,arrived);
            this.junctions.add(arrive);
            this.printJunction();
        }
        public void printJunction(){
            junctionRecorder last = junctions.get(junctions.size() - 1);
            int index = junctions.indexOf(last);
            String str = "";
            int dir = last.getDir();
            switch(dir){
                case IRobot.NORTH:
                    str = "NORTH";
                    break;
                case IRobot.SOUTH:
                    str = "SOUTH";
                    break;
                case IRobot.WEST:
                    str = "WEST";
                    break;
                case IRobot.EAST:
                    str= "EAST";
                    break;
            }
            System.out.println("Junction " + index + " (x="+last.getX()+" ,y="+last.getY()
                    + ") heading " + str);
        }

    }
    private RobotData robotData;
    class junctionRecorder{
        private int x;
        private int y;
        private int arrived;
        public junctionRecorder(int x, int y, int arrived){
            this.x = x;
            this.y = y;
            this.arrived = arrived;
        }
        public int getX(){
            return this.x;
        }
        public int getY(){
            return this.y;
        }
        public int getDir(){
            return this.arrived;
        }
    }
    public void controlRobot(IRobot robot) {
        if((robot.getRuns() == 0) && (pollRun == 0)){
            robotData = new RobotData();
            explorerMode = 1;
        }
        int direction=exploreControl(robot);
        pollRun++;
        robot.setHeading(direction);
    }
    public int searchJunction(int x, int y){
        for (int i = 0, j = robotData.junctions.size(); i<j; i++){
            if(robotData.junctions.get(i).getX() == x && robotData.junctions.get(i).getY() == y){
                return robotData.junctions.get(i).getDir();
            }
        }
        return 0;
    }
    public int exploreControl(IRobot robot){
        ArrayList<Integer> exits = nonWallExits(robot);
        int exit = exits.size();
        int direction = 0;
        switch (exit){
            case 1:
                direction = deadEnd(robot, exits);
                break;
            case 2:
                direction = corridor(robot, exits);
                break;
            case 3:
                direction = junction(robot, exits);
                break;
            case 4:
                direction = crossRoad(robot, exits);
                break;
        }
        return direction;
    }
    public void reset(){
        robotData.resetJunctionCounter();
        explorerMode=1;
    }
    //method which converts an absolute direction into a relative one
    private int lookHeading(int direction, IRobot robot) {
        int heading = robot.getHeading();
        int relative = ((direction - heading) % 4 + 4) % 4;
        int absolute = IRobot.AHEAD + relative;
        return robot.look(absolute);
    }


    private int deadEnd (IRobot robot, ArrayList<Integer> exits){
        explorerMode = 0;
        return exits.get(0);
    }
    private int crossRoad (IRobot robot, ArrayList<Integer> exits){
        int heading = robot.getHeading();
        ArrayList<Integer> passage = passageExits(robot);
        int passageSize = passage.size();
        if (passageSize == 3){
            neverBefore(robot, heading);
        }
        if(passageSize!=0){
            explorerMode=1;
            return passage.get(randomizer(passageSize));
        } else{
            if(explorerMode==1){
                return exits.get(randomizer(4));
            } else {
                return IRobot.NORTH+(((searchJunction(robot.getLocation().x, robot.getLocation().y)+2)%4+4)%4);
            }
        }

    }
    private void neverBefore(IRobot robot, int heading){
        robotData.add(robot.getLocation().x, robot.getLocation().y, heading);
        junctionCounter++;
    }
    private int junction (IRobot robot, ArrayList<Integer> exits){
        int heading = robot.getHeading();
        ArrayList<Integer> passage = passageExits(robot);
        int passageSize = passage.size();
        switch(passageSize){
            case 2:
                neverBefore(robot, heading);
            case 1:
                explorerMode=1;
                return passage.get(randomizer(passageSize));
            default:
                if(explorerMode==1){
                    return exits.get(randomizer(3));
                } else {
                    return IRobot.NORTH+(((searchJunction(robot.getLocation().x, robot.getLocation().y)+2)%4+4)%4);
                }
        }
    }
    private int randomizer (int n){
        return (int) (Math.random()*n);
    }
    private int corridor(IRobot robot, ArrayList<Integer> exits){
        int coming = IRobot.NORTH + (((robot.getHeading()-IRobot.NORTH)+2)%4+4)%4;
        int index = exits.indexOf(coming);
        if(index!=-1){
            exits.remove(index);
            return exits.get(0);
        } else{
            return exits.get(randomizer(2));
        }
    }
    //checks if there is a wall in the direction to the robot which was parsed in
    private int noWallAhead(int direction, IRobot robot) {
        if (lookHeading(direction, robot) != IRobot.WALL) {
            return direction;
        } else {
            return 0;
        }
    }
    private ArrayList<Integer> passageExits(IRobot robot){
        ArrayList<Integer> passage = new ArrayList<Integer>();
        for(int i= 0; i<4; i++){
            int direction= IRobot.NORTH + i;
            if(lookHeading(direction, robot)==IRobot.PASSAGE){
                passage.add(direction);
            }
        }
        return passage;
    }
    private ArrayList<Integer> nonWallExits(IRobot robot){
        //creates an ArrayList for all directions where there is no wall.
        ArrayList<Integer> exits = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            int direction = IRobot.NORTH + i;
            int noWall = noWallAhead(direction, robot);
            if (noWall != 0) {
                exits.add(noWall);
            }
        }
        return exits;
    }

}
