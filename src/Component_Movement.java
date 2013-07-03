/*
 * Die Bewegungskomponente enthält alle zur Bewegung (und Kollision) wichtigen
 * Daten. Jede Entität, die auf der Karte erscheinen soll, braucht diese.
 */

class Component_Movement extends Abstract_Component {
	public int x, y, dx, dy;
	public int orientation;
	public int delay, tick;
	public boolean moving, walkable, collidable, moveable, visible;
	
	public Component_Movement(Entity entity, System_Component system,
			int x, int y, int dx, int dy, int orientation, int delay,
			boolean walkable, boolean collidable, boolean visible) {
		super("movement",entity,system);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.orientation = orientation;
		this.walkable = walkable;
		this.collidable = collidable;
		this.moveable = true;
		this.visible = visible;
		// Das Delay bestimmt die Bewegungsgeschwindigkeit. Je höher, desto
		// langsamer.
		this.delay = delay;
		// "tick" wird benutzt, um das Delay herunterzuzählen. Ist tick == 0, 
		// dann kann die Entität bewegt werden.
		this.tick = 0;
	}
	
	public Component_Movement(Entity entity, System_Component system, int x, int y) {
		this(entity,system,x,y,0,0,0,0,true,false,true);
	}
	
	public Component_Movement(Component_Movement compMovement) {
		super(compMovement);
		this.x = compMovement.x;
		this.y = compMovement.y;
		this.dx = compMovement.dx;
		this.dy = compMovement.dy;
		this.orientation = compMovement.orientation;
		this.delay = compMovement.delay;
		this.tick = compMovement.tick;
		this.moving = compMovement.moving;
		this.moveable = compMovement.moveable;
		this.walkable = compMovement.walkable;
		this.collidable = compMovement.collidable;
		this.visible = compMovement.visible;
	}
	
	public Component_Movement(Abstract_Component comp, Entity entity, System_Component system) {
		super(comp.getType(),entity,system);
		Component_Movement compMovement = (Component_Movement) comp;
		this.x = compMovement.x;
		this.y = compMovement.y;
		this.dx = compMovement.dx;
		this.dy = compMovement.dy;
		this.orientation = compMovement.orientation;
		this.delay = compMovement.delay;
		this.tick = compMovement.tick;
		this.moving = compMovement.moving;
		this.moveable = compMovement.moveable;
		this.walkable = compMovement.walkable;
		this.collidable = compMovement.collidable;
		this.visible = compMovement.visible;
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
	public boolean isVisible() { return this.visible; }
	
	public int[] orientationToVectior() { return this.orientationToVector(this.orientation); }
	public int[] orientationToVector(int d) {
		int[] dxdy = {0,0};
		switch(d) {
		case 1:
			dxdy[1] = -1;
			break;
		case 2:
			dxdy[1] = 1;
			break;
		case 3:
			dxdy[0] = -1;
			break;
		case 4:
			dxdy[0] = 1;
			break;
		}
		return dxdy;
	}
	
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
	public void setVisible() { this.visible = true; }
	
	public void unsetMoving() { this.moving = false; }
	public void unsetMoveable() { this.moveable = false; }
	public void unsetVisible() { this.visible = false; }
	
	public void warp(int x, int y) {		
		this.dx = 0;
		this.dy = 0;
		this.x = x;
		this.y = y;
		this.nullifyTick();
		this.unsetMoving();
	}
	
	public void tick() {
		if (this.tick > 0) this.tick--;
	}
	public void resetTick() { this.tick = this.delay; }
	public void nullifyTick() { this.tick = 0; }
	
	public void drawFromMap() {
		this.unsetVisible();
		this.x = -1;
		this.y = -1;
	}
	
	public void putOnMap(int x, int y) {
		this.setVisible();
		this.x = x;
		this.y = y;
	}
}