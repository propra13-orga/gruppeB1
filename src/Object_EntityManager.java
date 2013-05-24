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
		for (Object_Event event : this.getEvents(EventType.DEATH)) {
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
	
	private List<Object_Event> getEvents(EventType type) {
		return ((Scene_Level) this.scene).getEvents(type); 
	}
	
	private Scene_Level getScene() {
		return (Scene_Level) this.scene;
	}
}

/*
 * Instanzen der Klasse Entity (Entitäten) sind sog. Flyweight-Objekte, die
 * außer einem Namen und einem Verweis auf ihren EntityManager keine wirklichen
 * Daten enthalten, sondern nur Container für Komponenten.
 * 
 * Die Komponenten wiederum enthalten je nach Typ unterschiedliche Daten. Die
 * Eigenschaften, die eine Entität hat, werden durch die vorhandenen Komponenten
 * festgelegt. Besitzt eine Entität zum Beispiel eine Komponente vom Typ -
 * "position", so verfügt sie über Positionsdaten.
 */

class Entity {
	protected String name;
	protected Object_EntityManager manager;
	private Hashtable<String,Component> components;;
	protected boolean active = false;
	
	public Entity(String name, Object_EntityManager manager) {
		this.name = name;
		this.manager = manager;
		this.components = new Hashtable<String,Component>();
	}
	
	public void init() {
		for (Component component : this.components.values()) {
			component.init();
		}
		this.manager.register(this);
		this.active = true;
	}
	
	public void deinit() {
		for (Component component : this.components.values()) {
			component.deinit();
		}
		this.active = false;
	}
	
	public void addComponent(Component component) {
		this.components.put(component.type, component);
	}
	
	public boolean hasComponent(String type) {
		if (this.components.containsKey(type)) return true;
		return false;
	}
	
	public Component getComponent(String type) { return this.components.get(type); }
	public Object_EntityManager getManager() {	return this.manager; }
	
	public boolean isPlayer() {	return this.manager.isPlayer(this); }
	
}