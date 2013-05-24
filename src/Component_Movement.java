/*
 * Die Bewegungskomponente enthält alle zur Bewegung (und Kollision) wichtigen
 * Daten. Jede Entität, die auf der Karte erscheinen soll, braucht diese.
 */

class Component_Movement extends Component {
	public int x, y, dx, dy;
	public int orientation;
	public int delay, tick;
	public boolean moving, walkable, collidable, moveable;
	
	public Component_Movement(Entity entity, System_Component system,
			int x, int y, int dx, int dy, int delay,
			boolean walkable, boolean collidable) {
		super("movement",entity,system);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.orientation = 2;
		this.walkable = walkable;
		this.collidable = collidable;
		this.moveable = true;
		// Das Delay bestimmt die Bewegungsgeschwindigkeit. Je höher, desto
		// langsamer.
		this.delay = delay;
		// "tick" wird benutzt, um das Delay herunterzuzählen. Ist tick == 0, 
		// dann kann die Entität bewegt werden.
		this.tick = 0;
	}
	
	public Component_Movement(Entity entity, System_Component system, int x, int y) {
		this(entity,system,x,y,0,0,0,true,false);
	}
	
	// Getters
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getdX() { return this.dx; }
	public int getdY() { return this.dy; }
	public int getOldX() { return this.x-this.dx; }
	public int getOldY() { return this.y-this.dy; }
	public int getOrientation() { return this.orientation; }
	public int getDelay() { return this.delay; }
	public int getTick() { return this.tick; }
	
	public boolean isMoving() { return this.moving; }
	public boolean isWalkable() { return this.walkable; }
	public boolean isCollidable() { return this.collidable; }
	public boolean isMoveable() { return this.moveable; }
	
	// Setters
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setdX(int dx) { this.dx = dx; }
	public void setdY(int dy) { this.dy = dy; }
	public void addToX(int dx) { this.x += dx; }
	public void addToY(int dy) { this.y += dy; }
	public void setOrientation(int d) { this.orientation = d; }
	public void setDelay(int delay) { this.delay = delay; }
	
	public void setMoving() { this.moving = true; }
	public void setMoveable() { this.moveable = true; }
	
	public void unsetMoving() { this.moving = false; }
	public void unsetMoveable() { this.moveable = false; }
	
	public void warp(int x, int y) {		
		this.dx = 0;
		this.dy = 0;
		this.x = x;
		this.y = y;
	}
	
	public void tick() {
		if (this.tick > 0) this.tick--;
	}
	public void resetTick() { this.tick = this.delay; }
	public void nullifyTick() { this.tick = 0; }
}