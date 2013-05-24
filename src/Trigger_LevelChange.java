class Trigger_LevelChange extends Trigger {
	private int levelID;
	private int x, y;
	
	public Trigger_LevelChange(Object_Entity entity, System_Component system, 
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