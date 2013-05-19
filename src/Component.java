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

class CompMovement extends Component {
	public int x, y, dx, dy;
	public int orientation;
	public boolean moving, walkable, collidable, moveable;
	
	public CompMovement(Entity entity, ComponentSystem system,
			int x, int y, int dx, int dy,
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
	}
	
	public CompMovement(Entity entity, ComponentSystem system,
			int x, int y) {
		this(entity,system,x,y,0,0,true,false);
	}
	
	// Getters
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getdX() { return this.dx; }
	public int getdY() { return this.dy; }
	public int getOldX() { return this.x-this.dx; }
	public int getOldY() { return this.y-this.dy; }
	public int getOrientation() { return this.orientation; }
	
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
}

class CompCamera extends Component {
	public CompCamera(Entity entity, ComponentSystem system) {
		super("camera",entity,system);
	}
}

class CompControls extends Component {
	public CompControls(Entity entity, ComponentSystem system) {
		super("controls",entity,system);
	}
}

class CompRenderable extends Component {
	public Sprite sprite;
	public CompRenderable(Entity entity, ComponentSystem system,
			String spritePath) {
		super("renderable",entity,system);
		sprite = new Sprite(spritePath);
	}
	
	public int getMovecounter() {
		return this.sprite.movecounter;
	}
	
	public boolean isVisible() {
		return this.sprite.visible;
	}
	
	public void setVisible() {
		this.sprite.visible = true;
	}
	
	public void setInvisible() {
		this.sprite.visible = false;
	}
	
	public void setMovecounter(int i) {
		this.sprite.movecounter = i;
	}
	
	public void addToMovecounter(int i) {
		sprite.movecounter += i;
	}
}

abstract class CompTrigger extends Component {
	private EventType eventType;
	public CompTrigger(String type, Entity entity, ComponentSystem system, 
			EventType eventType) {
		super(type,entity,system);
		this.eventType = eventType;
	}
	
	public EventType getEventType() { return this.eventType; }
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