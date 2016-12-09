import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;
public class Ex2 {
	private int pollRun = 0;
	private static int maxJunctions = 10000;
	private static int junctionCounter;
	private int explorerMode;
	class RobotData {
		//defining an arraylist of type integer
		public ArrayList<Integer> junctions = new ArrayList<Integer>();
		//method to reset the junctioncounter
		public void resetJunctionCounter(){
			junctionCounter = 0;
			junctions = new ArrayList<Integer>();
		}
		//function to add direction
		public void add(int arrived){
			this.junctions.add(arrived);
			this.printJunction();
		}
		//function to print out newly encountered junctions
		public void printJunction(){
			int dir = junctions.get(junctions.size() - 1);
			int index = junctions.lastIndexOf(dir);
			String str = "";
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
			System.out.println("Junction " + index + " heading " + str);
		}

	}
	//defining a class to record the junctions in
	private RobotData robotData;

	//main control method which is being colled by the Polled Controller Wrapper
	public void controlRobot(IRobot robot) {
		//initiates a new object to store data at the first move of the first run
		if((robot.getRuns() == 0) && (pollRun == 0)){
			robotData = new RobotData();
			explorerMode = 1;
		}

		//increments the pollRun counter to count the quantity of moves
		pollRun++;
		robot.setHeading(mainControl(robot));
	}
	public int mainControl(IRobot robot){
		ArrayList<Integer> exits = nonWallExits(robot);
		//stores the number of exits
		int exit = exits.size();
		int direction = 0;
		//switches dependent on the number of exits to different methods which control the robot
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
	//is run when the reset button is presed. Resets the explorermode and the junction counter.
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

	// at a deadend this method return the only possible way to get out
	private int deadEnd (IRobot robot, ArrayList<Integer> exits){
		explorerMode = 0;
		return exits.get(0);
	}
	private int crossRoad (IRobot robot, ArrayList<Integer> exits){
		int heading = robot.getHeading();
		ArrayList<Integer> passage = passageExits(robot);
		int passageSize = passage.size();
		// if a crossroad has not been encountered before it stores the junction details including the heading
		if (passageSize == 3){
			neverBefore(robot, heading);
		}
		//if the number of passage exits is not equal to zero it sets the robot to explore a random passage
		if(passageSize!=0){
			explorerMode=1;
			return passage.get(randomizer(passageSize));
		} else{
				int dir = IRobot.NORTH+(((robotData.junctions.get(robotData.junctions.size()-1)+2)%4+4)%4);
				robotData.junctions.remove(robotData.junctions.size()-1);
				return dir;
		}

	}
	//adds heading to junction counter
	private void neverBefore(IRobot robot, int heading){
		robotData.add(heading);
		junctionCounter++;
	}
	private int junction (IRobot robot, ArrayList<Integer> exits){
		int heading = robot.getHeading();
		ArrayList<Integer> passage = passageExits(robot);
		int passageSize = passage.size();
		switch(passageSize){
			//if the robot has not been to the junction before meaning there are two passage exits, the junction will be recorded
			case 2:
				neverBefore(robot, heading);
			case 1:
				//if the number of passage exits is not equal to zero it sets the robot to explore a random passage
				explorerMode=1;
				return passage.get(randomizer(passageSize));	
			default:
				//returns opposite direction on how the robot encountered the junctions the first time
					
				int dir = IRobot.NORTH+(((robotData.junctions.get(robotData.junctions.size()-1)+2)%4+4)%4);
				robotData.junctions.remove(robotData.junctions.size()-1);
				return dir;

		}

	}
	//for an arraylist of size n it gives back a valid index for a item of that arraylist
	private int randomizer (int n){
		return (int) (Math.random()*n);

	}
	//regardless whether it is a corner or a corridor this method returns the only other way out if this method has not been called at the very
	//first move under special circumstances when it can return a random direction.
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
	//returns an ArrayList of type integer for all directions where there is a passage exit
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

