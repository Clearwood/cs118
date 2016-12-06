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
        public ArrayList<Integer> junctions = new ArrayList<Integer>();

        public void resetJunctionCounter() {
            junctionCounter = 0;
        }

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

    private RobotData robotData;

    public void controlRobot(IRobot robot) {
        if ((robot.getRuns() == 0) && (pollRun == 0)) {
            robotData = new RobotData();
            explorerMode = 1;
            explore = 1;
        } else if (robot.getRuns() != 0 && pollRun == 0) {
            explore = 0;
        }
        if (explore == 0 && pollRun == 0) {
            printJunction();
        }
        robot.setHeading(exploreControl(robot));
        pollRun++;
    }

    public int exploreControl(IRobot robot) {
        if (explore == 0 && pollRun == 0) return FirstMove();
        ArrayList<Integer> exits = nonWallExits(robot);
        int exit = exits.size();
        int direction = 0;
        switch (exit) {
            case 1:
                direction = deadEnd(robot, exits);
                break;
            case 2:
                direction = corridor(robot, exits);
                break;
            case 3:
            case 4:
                direction = crossRoad(robot, exits);
                break;
        }
        return direction;
    }

    public void reset() {
        pollRun = 0;
        robotData.resetJunctionCounter();
    }

    private int FirstMove() {
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

    private void print_nice(String print) {
        println("---------------------------");
        println(print);
        println("---------------------------");
    }

    private void printJunction() {
        printHash();
        for (int i = 0; i < robotData.junctions.size(); i++) {
            System.out.println("i: " + i + " direction: " + direction(robotData.junctions.get(i)));
        }
        printHash();
    }

    private void printHash() {
        println("##################################");
    }

    private void println(String str) {
        System.out.println(str);
    }

    private int deadEnd(IRobot robot, ArrayList<Integer> exits) {
        if (pollRun != 0) {
            explorerMode = 0;
        }
        return exits.get(0);
    }

    private int crossRoad(IRobot robot, ArrayList<Integer> exits) {
        if (explore == 1) {
            int heading = robot.getHeading();
            ArrayList<Integer> passage = passageExits(robot);
            int passageSize = passage.size();
            String err2;
            if (explorerMode == 1 && passageSize >= 1 && (pollRun != 0)) {
                print_nice("Explorer Mode: 1 | passage Size: " + passageSize + " exit size: " + exits.size());
                neverBefore(robot, heading);
            }
            if (passageSize != 0) {
                explorerMode = 1;
                return passage.get(randomizer(passageSize));
            } else {
                if (explorerMode == 1) {
                    print_nice("Junction | Explorer Mode: 1 | passage Size: " + passageSize + " exit size: " + exits.size());
                    explorerMode = 0;
                    return IRobot.NORTH + ((((heading - IRobot.NORTH) + 2) % 4 + 4) % 4);
                }
                int junctionSize = robotData.junctions.size() - 1;
                int dir2 = robotData.junctions.get(junctionSize);
                int dir = IRobot.NORTH + (((dir2 - IRobot.NORTH) + 2) % 4 + 4) % 4;
                robotData.junctions.remove(junctionSize);
                err2 = "pulled of " + direction(dir2) + " | new direction: " + direction(dir) + " | new size: " + robotData.junctions.size();
                wall(dir, robot, junctionSize);
                print_nice(MessageFormat.format("{3} | Explore Mode: {0} | case: {1} | heading: {2}", explorerMode, passageSize, direction(heading), err2));
                return dir;
            }

        } else {
            return getIntelligentDir(robot);
        }
    }

    private int getIntelligentDir(IRobot robot) {
        println("The Junctioncounter has a value of: " + junctionCounter);
        if (junctionCounter < robotData.junctions.size()) {
            println("This is not the last junction.");
            int dir = robotData.junctions.get(junctionCounter);
            print_nice("direction: " + print(dir));
            junctionCounter++;
            return dir;
        } else {
            println("This is the last junction.");
            return lastDir(robot);
        }
    }

    private void neverBefore(IRobot robot, int heading) {
        robotData.add(heading);
        junctionCounter++;
    }

    private int randomizer(int n) {
        return (int) (Math.random() * n);
    }

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

    private int corridor(IRobot robot, ArrayList<Integer> exits) {
        int heading = robot.getHeading();
        ArrayList<Integer> passage = passageExits(robot);
        int coming = IRobot.NORTH + (((robot.getHeading() - IRobot.NORTH) + 2) % 4 + 4) % 4;
        int indexGo = exits.indexOf(coming);
        int going = exits.indexOf(robot.getHeading());
        if (explore == 1) {
            int indexTo = exits.indexOf(heading);
            int passageSize = passage.size();
            String err2;
            if (indexTo != -1) {
                if (passage.size() >= 1 || explorerMode == 0) {
                    if (indexGo != -1) {
                        exits.remove(indexGo);
                        return exits.get(0);
                    } else {
                        return exits.get(randomizer(2));
                    }
                } else {
                    explorerMode = 0;
                    return coming;
                }
            }
            if (explorerMode == 1 && passageSize >= 1 && (pollRun != 0)) {
                System.out.println("Corridor | Explorer Mode: 1 | passage Size: " + passageSize + " | exit size: " + exits.size());
                neverBefore(robot, heading);
            }

            if (passage.size() >= 1) {
                explorerMode = 1;
                exits.remove(indexGo);
                return exits.get(0);
            } else {
                if (explorerMode == 1) {
                    explorerMode = 0;
                    return coming;
                } else {
                    print_nice("CORNER| Explore Mode: " + explorerMode + " | case: " + passageSize + " | heading: " + direction(heading));
                    int junctionSize = robotData.junctions.size() - 1;
                    int dir2 = robotData.junctions.get(junctionSize);
                    int dir = IRobot.NORTH + (((dir2 - IRobot.NORTH) + 2) % 4 + 4) % 4;
                    robotData.junctions.remove(junctionSize);
                    err2 = "pulled of " + direction(dir2) + " | new direction: " + direction(dir) + " | new size: " + robotData.junctions.size();
                    wall(dir, robot, junctionSize);
                    print_nice(MessageFormat.format("{3} | Explore Mode: {0} | case: {1} | heading: {2}", explorerMode, passageSize, direction(heading), err2));

                    return dir;
                }
            }
        } else if (going != -1) {
            if (indexGo != -1) {
                exits.remove(indexGo);
            }
            return exits.get(0);
        } else {
            return getIntelligentDir(robot);
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

    //checks if there is a wall in the direction to the robot which was parsed in
    private int noWallAhead(int direction, IRobot robot) {
        if (lookHeading(direction, robot) != IRobot.WALL) {
            return direction;
        } else {
            return 0;

        }
    }

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

