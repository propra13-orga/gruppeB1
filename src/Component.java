/*
 * Component.java
 * 
 * Hierin befinden sich fast alle Komponenten.
 */

/*
 * Die Klasse, von der alle Komponenten abgeleitet werden.
 */
abstract class Component {
	protected ComponentSystem system;
	protected String type;
	protected Entity entity;
	
	public Component(String type, Entity entity, ComponentSystem system) {
		this.type = type;
		this.system = system;
		this.entity = entity;
		entity.addComponent(this);
	}
	
	public Entity getEntity() { return this.entity; }
	
	public void init() {
		this.system.register(this);
	}
	
	public void deinit() {
		this.system.deregister(this);
	}
}

/*
 * Die Bewegungskomponente enthält alle zur Bewegung (und Kollision) wichtigen
 * Daten. Jede Entität, die auf der Karte erscheinen soll, braucht diese.
 */
class CompMovement extends Component {
	public int x, y, dx, dy;
	public int orientation;
	public int delay, tick;
	public boolean moving, walkable, collidable, moveable;
	
	public CompMovement(Entity entity, ComponentSystem system,
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
	
	public CompMovement(Entity entity, ComponentSystem system,
			int x, int y) {
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

/*
 * Das RenderSystem richtet sich beim Zeichnen der Karte nach der Entität mit 
 * der Kamerakomponente. Muss genau einer Entität gegeben werden (sinnvollster-
 * weise dem Spieler).
 */
class CompCamera extends Component {
	public CompCamera(Entity entity, ComponentSystem system) {
		super("camera",entity,system);
	}
}

class CompHealth extends Component {
	private int hp;
	public CompHealth(Entity entity, ComponentSystem system, int hp) {
		super("health",entity,system);
		this.hp = hp;
	}
	
	// Getters
	
	public int getHP() { return this.hp; }
	
	// Setters
	
	public void setHP(int hp) { this.hp = hp; }
	public void addHP(int hp) { this.hp += hp; }
	public void discountHP(int hp) { 
		this.hp -= hp;
		if (this.hp < 0) this.hp = 0;
	}
}

/*
 * Entitäten mit dieser Komponente können gesteuert werden. Der Spieler über die
 * Tastatur, NPCs über die KI (kommt noch).
 */
class CompControls extends Component {
	public CompControls(Entity entity, ComponentSystem system) {
		super("controls",entity,system);
	}
}

/*
 * Trigger sind Komponenten, die auf bestimmte Ereignisse, wie Kollisionen, 
 * reagieren. Das InteractionSystem behandelt die verschiedenen Trigger und
 * was nach ihrer Auslösung passiert. 
 */
abstract class CompTrigger extends Component {
	private EventType eventType;
	private boolean ready;
	public CompTrigger(String type, Entity entity, ComponentSystem system, 
			EventType eventType) {
		super(type,entity,system);
		this.eventType = eventType;
		this.ready = true;
	}
	
	public EventType getEventType() { return this.eventType; }
	public boolean isReady() { return this.ready; }
	
	public void setReady() { this.ready = true; }
	public void unsetReady() { this.ready = false; }
}

class CompTriggerLevelChange extends CompTrigger {
	private int levelID;
	private int x, y;
	
	public CompTriggerLevelChange(Entity entity, ComponentSystem system, 
			EventType eventType,
			int levelID, int x, int y) {
		super("trigger_levelchange",entity,system,eventType);
		this.levelID = levelID;
		this.x = x;
		this.y = y;
	}
	
	// Getters
	
	public int getLevelID() {
		return this.levelID;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int[] getXY() {
		int[] XY = {this.getX(),this.getY()};
		return XY;
	}
}

class CompTriggerAttack extends CompTrigger {
	private int ap;		// Angriffspunkte
	
	public CompTriggerAttack(Entity entity, ComponentSystem system,
			EventType eventType, int ap) {
		super("trigger_attack",entity,system,eventType);
		this.ap = ap;
	}
	
	public int getAP() { return this.ap; }
}