import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/*
 * EntityManager.java
 * 
 * Der EntityManager dient der verwaltung der Entitäten. Für deren Definition,
 * siehe unten.
 */


class Object_EntityManager {
	protected Scene_Level scene;
	protected List<Entity> entities;
	protected HashMap<Integer,Entity> entitiesByID;
	protected Entity player;
	protected int lastID;
	
	public Object_EntityManager(Scene_Level scene) {
		this.scene = scene;
		this.entities = new LinkedList<Entity>();
		this.entitiesByID = new HashMap<Integer,Entity>();
		this.lastID = 0;
	}
	
	/*
	 * Update.
	 */
	public void update() {
		/*
		 * Sind Entitäten gestorben? Wenn ja, entferne diese. Der Spielertod
		 * wird gesondert behandelt.
		 */
		for (Event event : this.getEvents(EventType.DEATH)) {
			Entity entity = event.getUndergoer();
			if (this.isPlayer(entity)) {
				this.getScene().setPlayerDead();
			}
			else {
				this.deregister(entity);
			}
		}
	}
	

	
	/*
	 * Füge eine Entität der Entitätenliste hinzu.
	 */
	public void register(Entity entity) {
		if (!this.entities.contains(entity)) {
			this.entities.add(entity);
			this.entitiesByID.put(entity.getID(), entity);
		}
	}
	
	/*
	 * Deinitialisiere und dann entferne eine Entität aus der Entitätenliste.
	 */
	public void deregister(Entity entity) {
		try {
			entity.deinit();
			this.entitiesByID.remove(entity.getID());
			this.entities.remove(entity);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/*
	 * Lege eine Entität als Spieler fest.
	 */
	public void setPlayer(Entity entity) {
		this.player = entity;
	}
	
	/*
	 * Ist die angegebene Entität der Spieler?
	 */
	public boolean isPlayer(Entity entity) {
		if (this.player.equals(entity)) return true;
		return false;
	}
	
	/*
	 * Gebe die Spielerentität zurück.
	 */
	public Entity getPlayer() { return this.player; }
	
	public Entity getEntityByID(int id) {
		if (this.entitiesByID.containsKey(id)) return this.entitiesByID.get(id);
		return null;
	}
	
	/*
	 * Entitäten können sich hier bei ihrer Initialisierung eine noch nicht
	 * vergebene ID zur eindeutigen Identifizierung abholen.
	 */
	public int receiveID(Entity entity) {
		int id = this.lastID;
		this.lastID += 1;
		return id;
	}
	
	/*
	 * Privates
	 */
	
	private List<Event> getEvents(EventType type) {
		return this.scene.getEvents(type); 
	}
	
	private Scene_Level getScene() {
		return this.scene;
	}

}