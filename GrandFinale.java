import uk.ac.warwick.dcs.maze.logic.IRobot;

import java.text.MessageFormat;
import java.util.*;

public class GrandFinale {
    private int pollRun = 0;
    private static int maxJunctions = 10000;
    private static int junctionCounter;
    private int explorerMode;
    private int explore = 1;

    class RobotData {
        //defines class to store the dirction which the robot had when entering each junction/corner
        public ArrayList<Integer> junctions = new ArrayList<Integer>();
        //resets JunctionCounter
        public void resetJunctionCounter() {
            junctionCounter = 0;
        }

        //adds direction to the Arraylist and prints out these
        public void add(int arrived) {
            this.junctions.add(arrived);
            this.printJunction();
        }

        public void printJunction() {
            int dir = junctions.get(junctions.size() - 1);
            int index = junctions.lastIndexOf(dir);
            String str = print(dir);
            print_nice("Junction " + index + " heading " + str);
        }
    }
   //logging functions following here:
    public String print(int dir) {
        String str = "";
        switch (dir) {
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
                str = "EAST";
                break;
        }
        return str;
    }
    //wraps input string into a nice output
    private void print_nice(String print) {
        println("---------------------------");
        println(print);
        println("---------------------------");
    }
    //prints out the whole arraylist of directions
    private void printJunction() {
        printHash();
        for (int i = 0; i < robotData.junctions.size(); i++) {
            System.out.println("i: " + i + " direction: " + direction(robotData.junctions.get(i)));
        }
        printHash();
    }
    //prints out hashes for some nice logging
    private void printHash() {
        println("##################################");
    }

    //changes an integer dir into the corresponding string
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
    //for easier usability defines System.out.println for Strings as println
    private void println(String str) {
        System.out.println(str);
    }

    //loggs if there is a wall ahead
    private void wall(int dir, IRobot robot, int junctionSize) {
        if (lookHeading(dir, robot) == IRobot.WALL) {
            System.out.println("WALL AHEAD!");
            System.out.println("size junctions -1: " + junctionSize);
            printJunction();
        } else {
            System.out.println("NO WALL");
            System.out.println("size junctions -1: " + junctionSize);
        }

    }


    //creates class to store the directions
    private RobotData robotData;

    public void controlRobot(IRobot robot) {
        /*sets the mode to Explore and activates the exploration of the maze at
        the first encounter of a new maze*/
        if ((robot.getRuns() == 0) && (pollRun == 0)) {
            robotData = new RobotData();
            explorerMode = 1;
            explore = 1;
        } //activates the intelligent mode of the controller
        else if (robot.getRuns() != 0 && pollRun == 0) {
            explore = 0;
        }
        robot.setHeading(exploreControl(robot));
        pollRun++;
    }

    public int exploreControl(IRobot robot) {
        //fetches an ArrayList of all available exits
        ArrayList<Integer> exits = nonWallExits(robot);
        int exit = exits.size();
        int direction = 0;
        //checks if the exploration mode is activated or junctions are recorded
        if(robotData.junctions.size()!=0 || explore == 1) {
            //returns tbe first junction if the robot is not exploring
            if (explore == 0 && pollRun == 0) return FirstMove();
            //otherwise I am using a switch to determine the position in which the robot is and let different methods determine how to guide the robot
            switch (exit) {
                case 1:
                    direction = deadEnd(robot, exits);
                    break;
                case 2:
                    println("corridor");
                    direction = corridor(robot, exits);
                    break;
                case 3:
                case 4:
                    direction = crossRoad(robot, exits);
                    break;
            }
        } //otherwise the robot has to be in one line with the target
        else{
            direction = lastDir(robot);
        }
        return direction;
    }

    public void reset() {
        //is called when the reset button is presed, resets pollRun and the junctionCounter
        pollRun = 0;
        robotData.resetJunctionCounter();
    }

    private int FirstMove() {
        //increases JunctionCounter and gets first move
        junctionCounter++;
        return robotData.junctions.get(0);
    }

    //method which converts an absolute direction into a relative one
    private int lookHeading(int direction, IRobot robot) {
        int heading = robot.getHeading();
        int relative = ((direction - heading) % 4 + 4) % 4;
        int absolute = IRobot.AHEAD + relative;
        return robot.look(absolute);
    }

    //if it is not the first move it changes the explorerMode  to backtrack and returns the only way out.
    private int deadEnd(IRobot robot, ArrayList<Integer> exits) {
        if (pollRun != 0 && explore==1) {
            explorerMode = 0;
        }
        return exits.get(0);
    }
    private int noPassage(IRobot robot, ArrayList<Integer> exits, int mode, int coming, int passageSize, int heading){
        //if the robot is exploring the robot should backtrack now
        if (explorerMode == 1) {
            explorerMode = 0;
            return coming;
        } else {
            //if it is already backtracking it should delete the last junction
            String mod = (mode == 0)? "CORNER" : "JUNCTION";
            print_nice(mod + " | Explore Mode: " + explorerMode + " | case: " + passageSize + " | heading: " + direction(heading));
            int junctionSize = robotData.junctions.size() - 1;
            int dir2 = robotData.junctions.get(junctionSize);
            //gets direction where to go
            int dir = IRobot.NORTH + (((dir2 - IRobot.NORTH) + 2) % 4 + 4) % 4;
            robotData.junctions.remove(junctionSize);
            String err2 = "pulled of " + direction(dir2) + " | new direction: " + direction(dir) + " | new size: " + robotData.junctions.size();
            wall(dir, robot, junctionSize);
            //nicely encodes logging information
            print_nice(MessageFormat.format("{3} | Explore Mode: {0} | case: {1} | heading: {2}", explorerMode, passageSize, direction(heading), err2));
            return dir;
        }
    }
    private int crossRoad(IRobot robot, ArrayList<Integer> exits) {
        //checks if the exploration mode is active
        if (explore == 1) {
            int heading = robot.getHeading();
            int coming = IRobot.NORTH + (((robot.getHeading() - IRobot.NORTH) + 2) % 4 + 4) % 4;
            //fetches every passageExit into one ArrayList
            ArrayList<Integer> passage = passageExits(robot);
            int passageSize = passage.size();
            //whenever the robot is not backtracking and the passageSize is bigger than one and this is not the starting point the robot should store the new junction
            if (explorerMode == 1 && passageSize >= 1 && (pollRun != 0)) {
                print_nice("Explorer Mode: 1 | passage Size: " + passageSize + " exit size: " + exits.size());
                neverBefore(robot, heading);
            }
            //whenever the passageSize is not equal to zero the robot should explore one of the available passages
            if (passageSize != 0) {
                explorerMode = 1;
                return passage.get(randomizer(passageSize));
            } else {
                return noPassage(robot,exits,1,coming, passageSize, heading);
            }

        } else {
            //if the robot is in intelligent mode the method getIntelligentDir handles the direction of the robot
            return getIntelligentDir(robot);
        }
    }

    private int getIntelligentDir(IRobot robot) {
        println("The Junctioncounter has a value of: " + junctionCounter);
        //if the robot hasn't reached the last junction it should pull the direction where to go from the ArrayList of directions
        if (junctionCounter < robotData.junctions.size()) {
            int dir = robotData.junctions.get(junctionCounter);
            print_nice("direction: " + print(dir));
            junctionCounter++;
            return dir;
        } else {
            //at the last junction the specific method is called
            println("This is the last junction.");
            return lastDir(robot);
        }
    }
//adds a direction to the arraylist
    private void neverBefore(IRobot robot, int heading) {
        robotData.add(heading);
        junctionCounter++;
    }
//chooses randomly between a number of elements n
    private int randomizer(int n) {
        return (int) (Math.random() * n);
    }

//at the last junction returns the direction where the target is
    private int lastDir(IRobot robot) {
        if (robot.getLocation().x < robot.getTargetLocation().x) {
            return IRobot.EAST;
        } else if (robot.getLocation().x > robot.getTargetLocation().x) {
            return IRobot.WEST;
        } else if (robot.getLocation().y < robot.getTargetLocation().y) {
            return IRobot.SOUTH;
        } else {
            return IRobot.NORTH;
        }
    }

    private int corridor(IRobot robot, ArrayList<Integer> exits) {
        //defines heading, fetches ArrayList of passage exits, defines int with the arriving direction and takes the index of this direction and the direction the robot is heading to
        int heading = robot.getHeading();
        ArrayList<Integer> passage = passageExits(robot);
        int coming = IRobot.NORTH + (((robot.getHeading() - IRobot.NORTH) + 2) % 4 + 4) % 4;
        int indexGo = exits.indexOf(coming);
        int going = exits.indexOf(robot.getHeading());
        int indexSouth = exits.indexOf(IRobot.SOUTH);
        int indexNorth = exits.indexOf(IRobot.NORTH);
        int indexEast = exits.indexOf(IRobot.EAST);
        int indexWest = exits.indexOf(IRobot.WEST);
        if (explore == 1) {
            int indexTo = exits.indexOf(heading);
            int passageSize = passage.size();
            //if there are two opposite directions which are both included in the exits ArrayList we have to have a corridor here
            if((indexNorth!=-1 && indexSouth!=-1)||(indexEast!=-1 && indexWest!=-1)) {
                if (indexTo != -1) {
                    //if there is a passage exit or we are already backtracking the coming direction should be removed from the exits arraylist and the remaining direction can be returned
                    if (passage.size() >= 1 || explorerMode == 0) {
                            exits.remove(indexGo);
                            return exits.get(0);
                    } else {
                        //otherwise it should not go into the junction or crossroad but begin to backtrack effectively reducing a loopy maze to a prim maze
                        explorerMode = 0;
                        return coming;
                    }
                } else {
                    //if we are in a corridor but the heading direction is not an exit we want to randomly choose an exit. this case can only be executed if these conditions apply at the first move.
                    return exits.get(randomizer(2));
                }
            }
            if(pollRun == 0){
                return exits.get(randomizer(2));
            }
            //if the robot is exploring, this is not the first run and the passage size is bigger than one meaning we have not been at this corner before the corner should be recorded
            if (explorerMode == 1 && passageSize >= 1 && (pollRun != 0)) {
                System.out.println("CORNER | Explorer Mode: 1 | passage Size: " + passageSize + " | exit size: " + exits.size());
                neverBefore(robot, heading);
            }
            //if we have not been here before the robot should change to or change in explorermode, the coming direction should be removed and the robot should move on
            if (passage.size() >= 1) {
                explorerMode = 1;
                exits.remove(indexGo);
                return exits.get(0);
            } else {
                //otherwise a specific function is handling what should happen if there are no passage exits
                return noPassage(robot,exits,1,coming, passageSize, heading);
            }
        } //if we are in intelligent mode an come to a corridor we just want to move on
        else if (going != -1) {
            if (indexGo != -1) {
                exits.remove(indexGo);
            }
            return exits.get(0);
        } //in a corner case we want our specific function to handle the situation
        else {
            return getIntelligentDir(robot);
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
//returns an ArrayList of type integer for all directions where there is a passage exit
    private ArrayList<Integer> passageExits(IRobot robot) {
        ArrayList<Integer> passage = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            int direction = IRobot.NORTH + i;
            if (lookHeading(direction, robot) == IRobot.PASSAGE) {
                passage.add(direction);
            }

        }
        return passage;
    }

    private ArrayList<Integer> nonWallExits(IRobot robot) {
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

