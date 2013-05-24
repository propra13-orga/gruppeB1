import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/*
 * ComponentSystem.java
 * 
 * Abstrakte Klasse ComponentSystem, von der alle Komponentensysteme erben.
 * Ist nur für die Spielmechanik innerhalb der Scene_Level brauchbar.
 */


abstract class System_Component {
	protected Scene scene;
	protected String[] types;
	protected List<Component> components;
	protected Hashtable<String,List<Object_Entity>> entitiesByType;
	
	public System_Component(Scene scene, String ...types) {
		this.scene = scene;
		this.components = new LinkedList<Component>();
		this.types = types;
		this.entitiesByType = new Hashtable<String,List<Object_Entity>>();
		for (String type : this.types){
			this.entitiesByType.put(type,new LinkedList<Object_Entity>());
		}
	}
	
	abstract public void update();
	
	public void addEvent(Object_Event event) {
		((Scene_Level) this.scene).addEvent(event);
	}
	
	public void addEvents(List<Object_Event> events) {
		for (Object_Event event : events) this.addEvent(event);
	}
	
	public void register(Component component) {
		this.components.add(component);
		this.entitiesByType.get(component.type).add(component.entity);
	}
	
	public void deregister(Component component) {
		this.components.remove(component);
		this.entitiesByType.get(component.type).remove(component.entity);
	}
	
	public List<Object_Entity> getEntitiesByType(String type) {
		return this.entitiesByType.get(type);
	}
	
	public List<Object_Event> getEvents(EventType type) {
		return ((Scene_Level) this.scene).getEvents(type); 
	}
	
	public Scene_Level getScene() {
		return (Scene_Level) this.scene;
	}
}