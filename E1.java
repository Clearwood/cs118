import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.*;
public class E1 {
	private int pollRun = 0;
	private static int maxJunctions = 10000;
	private static int junctionCounter;
	class RobotData {
		//defining an arraylist of typ junctionrecorder
		public ArrayList<junctionRecorder> junctions = new ArrayList<junctionRecorder>();
		//method to reset the junctioncounter
		public void resetJunctionCounter(){
			junctionCounter = 0;
		}
		//function to add coordinates and direction to the Junctionrecorder
		public void add(int x, int y, int arrived){
			junctionRecorder arrive = new junctionRecorder(x,y,arrived);
			this.junctions.add(arrive);
		}
		//function to print out newly encountered junctions
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
	//defining a class to record the junctions in
	class junctionRecorder{
		//defining three private ints to store the x and y location and the direction into
		private int x;
		private int y;
		private int arrived;
		//defining a constructor method
		public junctionRecorder(int x, int y, int arrived){
			this.x = x;
			this.y = y;
			this.arrived = arrived;
		}
		//defining methods to retrieve the data from the object
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
	//main control method which is being colled by the Polled Controller Wrapper
	public void controlRobot(IRobot robot) {
		//initiates a new store object at the first move of the first run
		if((robot.getRuns() == 0) && (pollRun == 0)){
			robotData = new RobotData();
		}
		//increments the pollRun counter to count the quantity of moves
		pollRun++;
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
		robot.setHeading(direction);
		pollRun++;
	}
	public void reset(){
		robotData.resetJunctionCounter();
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
			return passage.get(randomizer(passageSize));
		} else{
			return exits.get(randomizer(4));
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
			case 1:
				return passage.get(0);
			case 2:
				neverBefore(robot, heading);
				return passage.get(randomizer(2));	
			default: 
				return exits.get(randomizer(3));

		}
	}
	private int randomizer (int n){
		int randno = (int) (Math.random()*n);
		return randno;

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

