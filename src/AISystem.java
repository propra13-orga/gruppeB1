import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/*
 * AISystem.java
 * 
 * Kümmert sich um die KI. Bitte nicht großartig beachten, da kaum 
 * funktionstüchtig!
 */

class AISystem extends ComponentSystem {

	public AISystem(Scene scene) {
		super(scene, "ai");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		for (Entity entity : this.getEntitiesByType("ai")) {
			//CompMovement compMovement = (CompMovement) entity.getComponent("movement");
			CompAI compAI = (CompAI) entity.getComponent("ai");
			if (compAI.nothingToDo()) {
				compAI.addRandomCommands();
			}
		}
	}
	
	/*
	 * Privates
	 */
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return ((Scene_Level) this.scene).getCurrentLevel().isPassable(x, y);
	}
	
	/*
	 * Pfadsuche.
	 */
	
	private void findPath(Entity entity, int toX, int toY) {
		
		
	}
	
	

}


/*
 * UNFERTIGE implementierung von A*.
 */
class AStar {
	private Level level;
	private int[] start;
	private int[] goal;
	private List<int[]> openList;
	private List<int[]> closedList;
	public AStar(Level level, int fromX, int fromY, int toX, int toY) {
		this.level = level;
		this.start = new int[2];
		this.start[0] = fromX;
		this.start[1] = fromY;
		this.goal = new int[2];
		this.goal[0] = toX;
		this.goal[1] = toY; 
		this.openList = new LinkedList<int[]>();
		this.closedList = new LinkedList<int[]>();
		this.closedList.add(start);
	}
	
	public int getH(int x, int y) {
		if (x == this.start[0] && y == this.start[1]) return 0;
		return Math.abs(this.goal[0]-x)+Math.abs(this.goal[1]-y);
	}
	
	public int getH(int[] xy) {
		return this.getH(xy[0], xy[1]);
	}
	
	public boolean dismissable(int[] xy) {
		return this.closedList.contains(xy);
	}
	
	public void tryToAdd(int[] xy) {
		for (int[] candidate : this.openList) {
			//
		}
	}
	
	public void dismiss(int[] xy) {
		this.closedList.add(xy);
	}
	
	public void dismiss(int x, int y) {
		int xy[] = {x,y};
		this.dismiss(xy);
	}
	
	public void findPath() {
		
	}
	
	
	public void addNeighbours(List<int[]> neighbours) {
		for (int[] xy : neighbours) {
			if (!this.closedList.contains(xy)) {
				//
			}
		}
	}
	
	private List<int[]> getNeigbours(int x, int y) {
		List<int[]> neighbours = new ArrayList<int[]>();
		if (this.walkable(x+1,y)) {
			int[] newXY = {x+1,y};
			neighbours.add(newXY);
		}
		if (this.walkable(x-1,y)) {
			int[] newXY = {x-1,y};
			neighbours.add(newXY);
		}
		if (this.walkable(x,y+1)) {
			int[] newXY = {x,y+1};
			neighbours.add(newXY);
		}
		if (this.walkable(x,y-1)) {
			int[] newXY = {x,y-1};
			neighbours.add(newXY);
		}		
		return neighbours;
	}
	
	private boolean walkable(int x, int y) {
		return this.level.isPassable(x, y);
	}
}

class CompAI extends Component {
	private List<Integer> commands;
	private Random generator;
	private AStar aStarData;
	public CompAI(Entity entity, ComponentSystem system) {
		super("ai",entity,system);
		this.commands = new LinkedList<Integer>();
		this.commands.add(0);
		this.generator = new Random();
	}
	
	public void setAStarData(AStar aStarData) { this.aStarData = aStarData; }
	
	public int getKey() {
		return this.commands.remove(0);
	}
	
	public boolean nothingToDo() {
		if (this.commands.isEmpty()) return true;
		return false;
	}
	
	public void addRandomCommands() {
		List<Integer> commands = new LinkedList<Integer>();
		int n = this.generator.nextInt();
		for (int i=0;i<8;i++) {
			commands.add(this.getRandomKey(n,i));
		}
		
		this.commands.addAll(commands);
	}
	
	private int getRandomKey(int n, int i) {
		int out = ((n >> 2*i) & 0b11)+1;
		return out;
	}
}