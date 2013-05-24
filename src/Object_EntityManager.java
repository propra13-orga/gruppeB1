import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/*
 * EntityManager.java
 * 
 * Der EntityManager dient der verwaltung der Entitäten. Für deren Definition,
 * siehe unten.
 */


class Object_EntityManager {
	protected Scene scene;
	protected List<Entity> entities;
	protected Entity player;
	
	public Object_EntityManager(Scene scene) {
		this.scene = scene;
		this.entities = new LinkedList<Entity>();
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
		if (this.entities.contains(entity)) this.entities.add(entity);
	}
	
	/*
	 * Deinitialisiere und dann entferne eine Entität aus der Entitätenliste.
	 */
	public void deregister(Entity entity) {
		try {
			entity.deinit();
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
	
	/*
	 * Privates
	 */
	
	private List<Event> getEvents(EventType type) {
		return ((Scene_Level) this.scene).getEvents(type); 
	}
	
	private Scene_Level getScene() {
		return (Scene_Level) this.scene;
	}
}