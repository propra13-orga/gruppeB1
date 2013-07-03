/*
 * Component.java
 * 
 * Hierin befinden sich fast alle Komponenten.
 */

/*
 * Die Klasse, von der alle Komponenten abgeleitet werden.
 */
abstract class Abstract_Component implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 210641426922785825L;
	transient protected System_Component system;
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
	
	public Abstract_Component(Abstract_Component comp) {
		this.system = comp.system;
		this.type = comp.type;
		this.entity = comp.entity;
		this.initiated = comp.initiated;
	}
	
	public Entity getEntity() { return this.entity; }
	public String getType() { return this.type; }
	
	public boolean isInitiated() { return this.initiated; }
	
	public void setSystem(System_Component system) { this.system = system; }
	
	public void init() {
		this.system.register(this);
		this.initiated = true;
	}
	
	public void deinit() {
		this.system.deregister(this);
		this.initiated = false;
	}
}