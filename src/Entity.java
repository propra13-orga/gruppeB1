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

class Entity {
	
	protected String name;
	protected Object_EntityManager manager;
	protected boolean active = false;
	
	private Hashtable<String,Abstract_Component> components;
	
	public Entity(String name, Object_EntityManager manager) {
		this.name = name;
		this.manager = manager;
		this.components = new Hashtable<String,Abstract_Component>();
	}
	
	public void init() {
		for (Abstract_Component component : this.components.values()) {
			component.init();
		}
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
	public Object_EntityManager getManager() {	return this.manager; }
	
	public boolean isPlayer() {	return this.manager.isPlayer(this); }
	
}