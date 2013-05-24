/*
 * Component.java
 * 
 * Hierin befinden sich fast alle Komponenten.
 */

/*
 * Die Klasse, von der alle Komponenten abgeleitet werden.
 */
abstract class Component {
	
	protected System_Component system;
	protected String type;
	protected Object_Entity entity;
	
	public Component(String type, Object_Entity entity, System_Component system) {
		this.type = type;
		this.system = system;
		this.entity = entity;
		entity.addComponent(this);
	}
	
	public Object_Entity getEntity() { return this.entity; }
	
	public void init() {
		this.system.register(this);
	}
	
	public void deinit() {
		this.system.deregister(this);
	}
}