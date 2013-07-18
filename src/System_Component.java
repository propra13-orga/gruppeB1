import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstrakte Klasse System_Component, von der alle Komponentensysteme erben.
 * Ist nur für die Spielmechanik innerhalb der Scene_Level brauchbar.
 */


abstract class System_Component implements IEventListener {
	protected Abstract_Scene scene;
	protected String[] types;
	protected List<Abstract_Component> components;
	protected Hashtable<String,List<Entity>> entitiesByType;
	protected Map<EventType,List<Event>> events;
	
	public System_Component(Abstract_Scene scene, String ...types) {
		this.events = new HashMap<EventType,List<Event>>();
		this.scene = scene;
		this.components = new LinkedList<Abstract_Component>();
		this.types = types;
		this.entitiesByType = new Hashtable<String,List<Entity>>();
		for (String type : this.types){
			this.entitiesByType.put(type,new LinkedList<Entity>());
		}
	}
	
	abstract public void update();
	
	@Override
	public void addEvent(Event event) {
		((Scene_Level) this.scene).addEvent(event);
	}
	
	protected void addEvents(List<Event> events) {
		for (Event event : events) this.addEvent(event);
	}
	
	@Override
	public void broadcastEvent(Event event) {
		EventType type = event.getType();
		if (!this.events.containsKey(type)) return;
		this.events.get(type).add(event);
	}
	
	public void register(Abstract_Component component) {
		this.components.add(component);
		this.entitiesByType.get(component.type).add(component.entity);
	}
	
	public void deregister(Abstract_Component component) {
		this.components.remove(component);
		this.entitiesByType.get(component.type).remove(component.entity);
	}
	
	public List<Entity> getEntitiesByType(String type) {
		return this.entitiesByType.get(type);
	}
	
	@Override
	public List<Event> getEvents(EventType... types) {
		List<Event> events = new LinkedList<Event>();
		for (EventType type : types) {
			events.addAll(this.events.get(type));
			this.events.get(type).clear();
		}
		return events;
	}
	
	public Scene_Level getScene() { return (Scene_Level) this.scene; }
	public String[] getTypes() { return this.types; }
	
	public boolean hasType(String type) {
		for (String t : this.types) {
			if (t.equals(type)) return true;
		}
		return false;
	}
	
	@Override
	public void listenTo(EventType...eventTypes) {
		for (EventType eventType : eventTypes) {
			this.events.put(eventType, new LinkedList<Event>());
		}
		((Scene_Level) this.scene).listenTo(this, eventTypes);
	}
}