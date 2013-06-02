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
	protected boolean initiated;
	
	public Abstract_Component(String type, Entity entity, System_Component system) {
		this.type = type;
		this.system = system;
		this.entity = entity;
		this.initiated = false;
		entity.addComponent(this);
	}
	
	public Entity getEntity() { return this.entity; }
	
	public boolean isInitiated() { return this.initiated; }
	
	public void init() {
		this.system.register(this);
		this.initiated = true;
	}
	
	public void deinit() {
		this.system.deregister(this);
		this.initiated = false;
	}
}