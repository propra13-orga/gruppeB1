/*
 * Component.java
 * 
 * Hierin befinden sich fast alle Komponenten.
 */

/*
 * Die Klasse, von der alle Komponenten abgeleitet werden.
 */
abstract class Abstract_Component {
	
	protected System_Component system;
	protected String type;
	protected Entity entity;
	
	public Abstract_Component(String type, Entity entity, System_Component system) {
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