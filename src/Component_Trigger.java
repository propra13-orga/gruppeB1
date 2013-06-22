import java.util.Hashtable;


public class Component_Trigger extends Abstract_Component {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -889156983476532236L;
	private EventType eventType;
	private EventType newEvent;
	private Hashtable<String,String> properties;
	private boolean ready;

	public Component_Trigger(Entity entity, System_Component system,
			EventType eventType, EventType newEvent, Hashtable<String, String> properties) {
		super("trigger_event",entity,system);
		this.eventType = eventType;
		this.properties = properties;
		this.ready = true;
		this.newEvent = newEvent;
	}

	public Component_Trigger(Entity entity, System_Component system,
			EventType eventType, EventType newEvent) {
		this(entity, system, eventType, newEvent, null);
	}
	
	public EventType getEventType() { return this.eventType; }
	public EventType getTriggeredEvent() { return this.newEvent; }
	public boolean isReady() { return this.ready; }
	public String getProperty(String property) { return this.properties.get(property); }
	
	public void setReady() { this.ready = true; }
	public void unsetReady() { this.ready = false; }
	
	
}
