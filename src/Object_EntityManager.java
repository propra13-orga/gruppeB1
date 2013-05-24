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
	protected List<Object_Entity> entities;
	protected Object_Entity player;
	
	public Object_EntityManager(Scene scene) {
		this.scene = scene;
		this.entities = new LinkedList<Object_Entity>();
	}
	
	/*
	 * Update.
	 */
	public void update() {
		/*
		 * Sind Entitäten gestorben? Wenn ja, entferne diese. Der Spielertod
		 * wird gesondert behandelt.
		 */
		for (Object_Event event : this.getEvents(EventType.DEATH)) {
			Object_Entity entity = event.getUndergoer();
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
	public void register(Object_Entity entity) {
		if (this.entities.contains(entity)) this.entities.add(entity);
	}
	
	/*
	 * Deinitialisiere und dann entferne eine Entität aus der Entitätenliste.
	 */
	public void deregister(Object_Entity entity) {
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
	public void setPlayer(Object_Entity entity) {
		this.player = entity;
	}
	
	/*
	 * Ist die angegebene Entität der Spieler?
	 */
	public boolean isPlayer(Object_Entity entity) {
		if (this.player.equals(entity)) return true;
		return false;
	}
	
	/*
	 * Gebe die Spielerentität zurück.
	 */
	public Object_Entity getPlayer() { return this.player; }
	
	/*
	 * Privates
	 */
	
	private List<Object_Event> getEvents(EventType type) {
		return ((Scene_Level) this.scene).getEvents(type); 
	}
	
	private Scene_Level getScene() {
		return (Scene_Level) this.scene;
	}
}