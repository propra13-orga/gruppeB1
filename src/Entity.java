import java.util.Collection;
import java.util.Hashtable;

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

class Entity implements java.io.Serializable, Comparable<Entity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2840301197733965373L;
	
	protected String entityType;
	protected String name;
	transient protected Object_EntityManager manager;
	protected boolean active = false;
	protected int ID;
	
	protected Hashtable<String,Abstract_Component> components;
	
	public Entity(String name, String entityType, Object_EntityManager manager) {
		this.entityType = entityType;
		this.name = name;
		this.ID = -1;
		this.manager = manager;
		this.components = new Hashtable<String,Abstract_Component>();
	}
	
	public Entity(Entity entity) {
		this.entityType = entity.entityType;
		this.name = entity.name;
		this.manager = entity.manager;
		this.active = entity.active;
		this.ID = entity.ID;
		this.components = entity.components;
	}
	
	public Entity(Entity entity, Object_EntityManager manager) {
		this.entityType = entity.entityType;
		this.name = entity.name;
		this.manager = manager;
		this.active = entity.active;
		this.ID = entity.ID;
		this.components = entity.components;
	}
	
	public void setManager(Object_EntityManager manager) { this.manager = manager; }
	
	public void init() {
		for (Abstract_Component component : this.components.values()) {
			component.init();
		}
		if (this.ID == -1) this.ID = manager.receiveID(this);
		this.manager.register(this);
		this.active = true;
	}
	
	public void deinit() {
		for (Abstract_Component component : this.components.values()) {
			component.deinit();
		}
		this.active = false;
	}
	
	public void addComponent(Abstract_Component component) {
		this.components.put(component.type, component);
	}
	
	public boolean hasComponent(String type) {
		if (this.components.containsKey(type)) return true;
		return false;
	}
	
	public Abstract_Component getComponent(String type) { return this.components.get(type); }
	public Collection<Abstract_Component> getComponents() { return this.components.values(); }
	public Object_EntityManager getManager() {	return this.manager; }
	public int getID() { return this.ID; }
	public String getName() { return this.name; }
	public String getType() { return this.entityType; }
	
	public boolean isPlayer() {	return this.manager.isPlayer(this); }
	

	@Override
	public int compareTo(Entity entity) {
		if (this.getType().equals(entity.getType())) return 0;
		return -1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (((Entity) o).compareTo(this) == 0) return true;
		return false;
	}
}