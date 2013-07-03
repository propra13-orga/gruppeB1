import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/*
 * Diese Klasse enthält diverse Methoden, die vor allem für die Implementierung
 * der KI gebraucht werden.
 */


public class Static_AITools {
	
	/*
	 * Rotiert einen 2D-Vektor um 90 Grad.
	 */
	static int[] rotateVector90Degrees(int[] v, boolean clockwise) {
		int[] new_v = {v[1],v[0]};
		if (clockwise) new_v[0] *= -1;
		else new_v[1] *= -1;
		return new_v;
	}
	
	/*
	 * Signumfunktion.
	 */
	static int sign(int x) {
		if (x < 0) return -1;
		if (x > 0) return 1;
		return 0;
	}
	
	/*
	 * Setzt eine Richtungsangabe in einen Vektor um.
	 */
	static int[] orientationToVector(int d) {
		int[] v = {0,0};
		switch(d) {
		case 1:
			v[1] = -1;
			break;
		case 2:
			v[1] = 1;
			break;
		case 3:
			v[0] = -1;
			break;
		case 4:
			v[0] = 1;
			break;
		}
		return v;
	}
	
	/*
	 * Stutzt einen Vektor so, dass seine Einträge nicht kleiner als 0 und nicht
	 * größer als max_x bzw. max_y werden.
	 */
	static int[] delimitVector(int[] v, int max_x, int max_y) {
		if (v[0] < 0) v[0] = 0;
		else if (v[0] >= max_x) v[0] = max_x;
		if (v[1] < 0) v[1] = 0;
		else if (v[1] >= max_y) v[1] = max_y;
		
		return v;		
	}
	
	/*
	 * Rundet bis .5 ab, darüber auf.
	 */
	static int round(float x) {
		if (x%1 < 0.5) return (int) x;
		return (int) x + 1;
	}
	
	/*
	 * 
	 */
	static int[][] castRay(int array[][], int x, int y, int ex, int ey) {
		float dx = ex-x;
		float dy = ey-y;
		float len = (float) Math.sqrt(dx*dx + dy*dy);
		dx = dx/len;
		dy = dy/len;
		
		float sx = (float) x;
		float sy = (float) y;
		
		int pen = 0;
		while (sx <= ex && sy <= sy) {
			int rsx = round(sx);
			int rsy = round(sy);
			if (array[rsy][rsx] != 0) pen = 1;
			array[rsy][rsx] = pen;
			sx += dx;
			sy += dy;
		}
		
		return array;
	}
	
	/*
	 * 
	 */
	static int[][] floodfill4(int[][] array, int x, int y, int v_old, int v_new) {
		if (x < 0 || y < 0 || x >= array[0].length || y >= array.length) {
			return array;
		}
		if (array[y][x] == v_old) {
			array[y][x] = v_new;
			array = floodfill4(array,x-1,y,v_old,v_new);
			array = floodfill4(array,x+1,y,v_old,v_new);
			array = floodfill4(array,x,y-1,v_old,v_new);
			array = floodfill4(array,x,y+1,v_old,v_new);
		}
		return array;
	}
	
	/*
	 * Gibt ein Array mit Radius r um (x,y) herum zurück.
	 */
	static int[][] getRange(int[][] array, int x, int y, int r) {
		int ul_x = Math.max(x-r,0);
		int ul_y = Math.max(y-r,0);
		int lr_x = Math.min(x+r,array[0].length);
		int lr_y = Math.min(y+r,array.length);
		
		int[][] new_arr = new int[lr_y-ul_y][Math.abs(lr_x-ul_x)];
		
		for (int j=ul_y; j<lr_y; j++) {
			for (int i=ul_x; i<lr_x; i++) {
				new_arr[j-ul_y][i-ul_x] = array[j][i];
			}
		}
		
		return new_arr;
	}
	
	/*
	 * Gibt das Sichtfeld (Winkel 90 Grad) von Punkt (x,y) in Richtung 
	 * "orientation" zurück, wobei alles Sichtbare den Wert 0 bekommt, sonst 1. 
	 */
	static int[][] getFOVRaw(int[][] array, int x, int y, int orientation) {
		int[][] new_arr = new int[array.length][array[0].length];
		new_arr = fillArray(new_arr,1);
		
		int[] rightv = {1,1};
		int[] leftv = {1,1};
		switch (orientation) {
		case 1:
			rightv[1] = -1;
			leftv[0] = -1;
			leftv[1] = -1;
			break;
		case 2:
			rightv[0] = -1;
			break;
		case 3:
			rightv[0] = -1;
			rightv[1] = -1;
			leftv[0] = -1;
			break;
		case 4:
			leftv[1] = -1;
			break;
		}
		
		int maxsteps = Math.min(array.length, array[0].length);
		
		int[] leftp = {x,y};
		int[] rightp = {x,y};
		
		while (leftp[0] < maxsteps && leftp[1] < maxsteps
				&& leftp[0] >= 0 && leftp[1] >= 0) {
			new_arr[leftp[1]][leftp[0]] = 0;
			leftp[0] += leftv[0];
			leftp[1] += leftv[1];
		}
		while (rightp[0] < maxsteps && rightp[1] < maxsteps
				&& rightp[0] >= 0 && rightp[1] >= 0) {
			new_arr[rightp[1]][rightp[0]] = 0;
			rightp[0] += rightv[0];
			rightp[1] += rightv[1];
		}
		
		new_arr = floodfill4(new_arr, x+rightv[0]+leftv[0], y+rightv[1]+leftv[1], 1, 0);
		
		return new_arr;
	}
	
	/*
	 * Druckt ein 2D-Array.
	 */
	static void printArray(int[][] array) {
		for (int j=0;j<array.length;j++) {
			for (int i=0;i<array[0].length;i++) {
				System.out.print(array[j][i]);
			}
			System.out.print("\n");
		}
	}
	
	/*
	 * Füllt ein 2D-Array mit einem Wert c.
	 */
	static int[][] fillArray(int[][] array, int c) {
		int[][] new_arr = new int[array.length][array[0].length];
		for (int j=0;j<array.length;j++) {
			for (int i=0;i<array[0].length;i++) {
				new_arr[j][i] = c;
			}
		}
		return new_arr;
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
		//System.out.printf("(%d,%d)\t", s.x,s.y);
		while (!s.equals(e)) {
			s = s.from;
			//System.out.printf("(%d,%d)\t", s.x,s.y);
		}
		//System.out.println("");
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