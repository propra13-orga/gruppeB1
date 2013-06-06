/*
 * Factory.java
 * 
 * Mit der Factory soll es möglich sein, Spielobjekte und Level zu erzeugen.
 * 
 */


public class Factory {
	Scene_Level scene;
	Object_DBBrowser db;
	
	public Factory(Scene_Level scene, String dbpath) {
		this.scene = scene;
		this.db = new Object_DBBrowser(dbpath);
		}
	
	
	public Object_DBBrowser getDB() { return this.db; }
	
	public Entity buildEntity(String entityType, String name, int x, int y) {
		return this.getEntity(new EntityData(db,entityType,name,x,y));
	}
	
	/*
	 * Wandelt Instanzen von Klassen mit Interface IEntityData in Entitäten um.
	 */
	public Entity getEntity(IEntityData data) {
		Entity entity = new Entity(data.getName(),this.scene.getEntityManager());
		
		if (data.getSprite() != null) {
			new Component_Sprite(entity,scene.getSystemRender(),data.getSprite());
		}
		
		if (data.hasAI()) {
			new Component_AI(entity,scene.getSystemAI(),"basicenemy",3,8,10,0.8);
		}
		
		if (data.isControllable()) {
			new Component_Controls(entity,scene.getSystemMovement());
		}
		
		if (data.getHP() > -1 || data.getMP() > -1 || data.getATK() > -1) {
			int hp = data.getHP();
			int mp = data.getMP();
			int atk = data.getATK();
			int def = data.getDEF();
			int dex = data.getDEX();
			int spd = data.getSPD();
			if (hp < 0) hp = 1;
			if (mp < 0) mp = 0;
			if (atk < 0) atk = 0;
			if (def < 0) def = 0;
			if (dex < 0) dex = 0;
			if (spd < 0) spd = 1;
			new Component_Battle(entity,scene.getSystemInteraction(),hp,mp,atk);//,def,spd,dex);
		}
		
		if ((data.getX() > -1 && data.getY() > -1)) {
			int x = data.getX();
			int y = data.getY();
			int orientation = data.getOrientation();
			int delay;
			
			if (data.getSpeed() > -1) {
				delay = this.calculateDelay(data.getSpeed());
			}
			else { 
				delay = 128;
			}
			
			if (orientation < 0) orientation = 0;
			
			new Component_Movement(entity,scene.getSystemMovement(),
					x,y,0,0,
					delay,
					data.isWalkable(),
					data.isCollidable(),
					true);
		}
		
		EventType[] eventTypes = {EventType.COLLISION, EventType.ACTION};
		for (EventType type : eventTypes) {
			if (data.onEvent(type) != null) {
				String triggerType = data.onEvent(type);
				switch (triggerType) {
				case "trigger_levelchange":
					new Trigger_LevelChange(entity,scene.getSystemInteraction(),
							type,
							data.toLevel(),
							data.toX(),
							data.toY());
					break;
				case "trigger_attack":
					new Trigger_Attack(entity,scene.getSystemInteraction(),type,data.getATK());
					break;
				case "trigger_endgame":
					new Trigger_EndGame(entity,scene.getSystemInteraction(),type);
					break;
				case "trigger_buymenu":
					new Trigger_BuyMenu(entity,scene.getSystemInteraction(),type);
					break;
				}
			}
		}
		
		return entity;
	}
	
	
	/*
	 * Privates
	 */
	
	private int calculateDelay(int speed) { return (int) Math.pow(2,6-speed); }
}


/*
 * EntityData liest Daten aus der Spieldatenbank und baut daraus Entitäten. Es
 * ist vorerst eine testweise Implementierung des Interfaces IEntityData, 
 * welches dazu dienen soll, der Factory Daten von Entitäten anzubieten, welche
 * aus der Spieldatenbank oder einer Speicherdatei (XML) geladen wurden.
 */
class EntityData implements IEntityData {
	String name, entityType;
	int x, y;
	int toLevel, toX, toY;
	Object_DBBrowser db;
	public EntityData(Object_DBBrowser db, String entityType, String name, int x, int y) {
		this.db = db;
		this.x = x;
		this.y = y;
		this.name = name;
		this.entityType = entityType;
		this.toLevel = -1;
		this.toX = -1;
		this.toY = -1;
	}
	
	/*
	 * Getters
	 */
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getSprite() {
		String entry = db.getEntry(entityType, "Sprite");
		if (entry != null) return entry; 
		return null;
	}

	@Override
	public int getHP() {
		String entry = db.getEntry(entityType, "HP");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getCurrentHP() {
		String entry = db.getEntry(entityType, "CurrentHP");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getMP() {
		String entry = db.getEntry(entityType, "MP");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getCurrentMP() {
		String entry = db.getEntry(entityType, "CurrentHP");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getATK() {
		String entry = db.getEntry(entityType, "ATK");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}
	
	@Override
	public int getDEF() {
		String entry = db.getEntry(entityType, "DEF");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getDEX() {
		String entry = db.getEntry(entityType, "DEX");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getSPD() {
		String entry = db.getEntry(entityType, "SPD");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getCurrentSPD() {
		String entry = db.getEntry(entityType, "CurrentSPD");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

	@Override
	public int getOrientation() {
		String entry = db.getEntry(entityType, "Orientation");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public int getSpeed() {
		String entry = db.getEntry(entityType, "Speed");
		if (entry != null) return Integer.parseInt(entry);
		return -1;
	}

	@Override
	public boolean isControllable() {
		String entry = db.getEntry(entityType, "Controllable");
		if (entry != null) return true;
		return false;
	}

	@Override
	public boolean isWalkable() {
		String entry = db.getEntry(entityType, "Walkable");
		if (entry != null) return true;
		return false;
	}

	@Override
	public boolean isCollidable() {
		String entry = db.getEntry(entityType, "Collidable");
		if (entry != null) return true;
		return false;
	}
	
	@Override
	public boolean hasAI() {
		String entry = db.getEntry(entityType, "AI");
		if (entry != null) return true;
		return false;
	}

	@Override
	public String onEvent(EventType type) {
		String trigger = null;
		switch (type) {
		case COLLISION:
			trigger = db.getEntry(entityType, "OnCollision");
			break;
		case ATTACK:
			trigger = db.getEntry(entityType, "OnAttack");
			break;
		case ACTION:
			trigger = db.getEntry(entityType, "OnAction");
			break;
		default:
			break;
		}
		return trigger;
	}

	@Override
	public int toLevel() {
		return this.toLevel;
	}

	@Override
	public int toX() {
		return this.toX;
	}

	@Override
	public int toY() {
		return this.toY;
	}
	
	/*
	 * Setters
	 */
	
	public void setToLevel(int ID) { this.toLevel = ID; }
	public void setToX(int x) { this.toX = x; }
	public void setToY(int y) { this.toY = y; }
	public void initTriggerLevelChange(int ID, int x, int y) {
		this.toLevel = ID;
		this.toX = x;
		this.toY = y;
	}

	@Override
	public int getAP() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}