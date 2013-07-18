import java.util.Hashtable;
import java.util.Map;

/**
 * 
 * Trigger-Komponenten reagieren auf Events und lösen einen neuen Event aus.
 * Damit können zum Beispiel Schalter im Spiel realisiert werden.
 * 
 * @author Victor Persien
 *
 */
public class Component_Trigger extends Abstract_Component {
	
	private static final long serialVersionUID = -889156983476532236L;
	private EventType eventType;
	private EventType newEvent;
	private Hashtable<String,String> properties;
	private boolean ready;
	
	/**
	 * Konstruktor. Alle diese Komponenten sind vom Typ "trigger_event".
	 * 
	 * @param entity		Entität, der diese Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 * @param eventType		Typ des auslösenden Events.
	 * @param newEvent		Typ des auszulösenden Events.
	 * @param properties	Weitere Eigenschaften.
	 */
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
	public Map<String,String> getProperties() { return this.properties; }
	
	public void setReady() { this.ready = true; }
	public void unsetReady() { this.ready = false; }
	
	
}
