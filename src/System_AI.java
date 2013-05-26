import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/*
 * AISystem.java
 * 
 * Kümmert sich um die KI. Bitte nicht großartig beachten, da kaum 
 * funktionstüchtig!
 */

class System_AI extends System_Component {
	int w;
	int h;
	int[][] walkability;
	int[][] entityPositions;

	public System_AI(Abstract_Scene scene) {
		super(scene, "ai");
	}

	@Override
	public void update() {
		this.w = this.getScene().getCurrentLevel().getWidth();
		this.h = this.getScene().getCurrentLevel().getHeight();
		this.retrieveEntityPositions();
		this.retrieveWalkability();
		
		for (Entity entity : this.getEntitiesByType("ai")) {
			//CompMovement compMovement = (CompMovement) entity.getComponent("movement");
			Component_AI compAI = (Component_AI) entity.getComponent("ai");
			if (compAI.nothingToDo()) {
				compAI.addRandomCommands();
			}
			this.findPath(entity, 5, 5);
		}
	}
	
	/*
	 * Privates
	 */
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return this.getScene().getCurrentLevel().isPassable(x, y);
	}
	
	
	
	/*
	 * Pfadsuche.
	 */
	
	private void findPath(Entity entity, int toX, int toY) {
		Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");
		AStar astar = new AStar(compMovement.getX(),compMovement.getY(),toX,toY,this.walkability);
		astar.findPath();
		astar.reconstructPath();
	}
	
	private void retrieveEntityPositions() {
		this.entityPositions = this.getScene().getEntityPositions();
	}
	
	private void retrieveWalkability() {
		int[][] walkability = new int[this.h][this.w];
		for (int i=0;i<walkability[0].length;i++) {
			for (int j=0;j<walkability.length;j++) {
				if (!this.walkable(i, j)) walkability[j][i] = 1;
			}
		}
		this.walkability = walkability;
	}
	

}


/*
 * UNFERTIGE Implementierung von A*.
 */
class AStar {
	
	int fromX, fromY, toX, toY;
	int distance;
	private List<Node> closedList;
	private PriorityQueue<Node> openList;
	private int[][] walkability;
	boolean found;
	public AStar(int fromX, int fromY, int toX, int toY, int[][] walkability) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		this.closedList = new LinkedList<Node>();		
		this.openList = new PriorityQueue<Node>();
		this.walkability = walkability;
		this.found = false;
		this.distance = Integer.MAX_VALUE;
		
		Node start = new Node(fromX,fromY,0);
		this.openList.add(start);
	}	
	
	private class Node implements Comparable<Node> {
		int x;
		int y;
		int h;
		int g;
		Node from;
		public Node(int x, int y, int g) {
			this.x = x;
			this.y = y;
			this.g = g;
			this.h = Math.abs(toX-x)+Math.abs(toY-y);
			this.from = this;
		}
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void setG(int g) { this.g = g; }
		
		private int f() {
			return this.g+this.h;
		}
		
		public void setFrom(Node s) { this.from = s; }
		
		@Override
		public int compareTo(Node n) {
			return this.f() - n.f();
		}
	}
	
	public Node getNode(Node s) {
		for (Node n : this.closedList) {
			if (n.x == s.x && n.y == s.y) return n;
		}
		return null;
	}
	
	public void reconstructPath() {
		Node s = this.getNode(new Node(this.toX,this.toY));
		Node e = this.getNode(new Node(this.fromX, this.fromY));
		System.out.printf("(%d,%d)\t", s.x,s.y);
		while (!s.equals(e)) {
			s = s.from;
			System.out.printf("(%d,%d)\t", s.x,s.y);
		}
		System.out.println("");
	}
	
	public void findPath() {
		while (true) {
			Node s = this.openList.remove();
			this.closedList.add(s);
			if (s.x == this.toX && s.y == this.toY) {
				this.found = true;
				this.distance = s.g;
				break;
			}
			Node[] neighbours = this.getNeighbours(s);
			for (int i=0;i<4;i++) {
				if (neighbours[i] != null) {
					Node t = neighbours[i];
					if (!this.inList(this.openList, t)) {
						t.setG(s.g+d(s,t));
						t.setFrom(s);
						this.openList.add(t);
					}
					else {
						t = this.removeFromList(this.openList, t);
						int newf = s.g+t.h+d(s,t);
						if (newf < t.f()) {
							t.setG(s.g+d(s,t));
							t.setFrom(s);
							this.openList.add(t);
						}
					}
				}
			}
			if (openList.isEmpty()) break;
		}
	}
	
	private boolean inList(PriorityQueue<Node> L, Node t) {
		for (Node n : L) {
			if (n.x == t.x && n.y == t.y) return true;
		}
		return false;
	}
	
	private Node removeFromList(PriorityQueue<Node> L, Node t) {
		for (Node n : L) {
			if (n.x == t.x && n.y == t.y) {
				L.remove(n);
				return n;
			}
		}
		return null;
	}
	
	public int d(Node node1, Node node2) {
		return 1;
	}
	
	public int getDistance() {
		return this.distance;
	}
	
	public int[][] getPathMap() {
		int w = this.walkability[0].length;
		int h = this.walkability.length;
		int[][] pathMap = new int[h][w];
		for (Node node : this.closedList) {
			pathMap[node.y][node.x] = node.g;
		}
		return pathMap;
	}
	
	private Node[] getNeighbours(Node node) {
		int x = node.x;
		int y = node.y;
		Node[] neighbours = new Node[4];
		if (x-1 > -1 && this.walkability[y][x-1] == 0){
			neighbours[0] = new Node(x-1,y,node.g+1);
		}
		if (x+1 < this.walkability[0].length && this.walkability[y][x+1] == 0) {
			neighbours[1] = new Node(x+1,y,node.g+1);
		}
		if (y-1 > -1 && this.walkability[y-1][x] == 0) {
			neighbours[2] = new Node(x,y-1,node.g+1);
		}
		if (y+1 < this.walkability.length && this.walkability[y+1][x] == 0) {
			neighbours[3] = new Node(x,y+1,node.g+1);
		}
		return neighbours;
	}
}

class Component_AI extends Abstract_Component {
	private List<Integer> commands;
	private Random generator;
	private AStar aStar;
	public Component_AI(Entity entity, System_Component system) {
		super("ai",entity,system);
		this.commands = new LinkedList<Integer>();
		this.commands.add(0);
		this.generator = new Random();
	}
	
	public void setAStarData(AStar aStar) { this.aStar = aStar; }
	
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