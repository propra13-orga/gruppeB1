import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
	protected Abstract_Scene scene;
	protected String[] types;
	protected List<Abstract_Component> components;
	protected Hashtable<String,List<Entity>> entitiesByType;
	
	public System_Component(Abstract_Scene scene, String ...types) {
		this.scene = scene;
		this.components = new LinkedList<Abstract_Component>();
		this.types = types;
		this.entitiesByType = new Hashtable<String,List<Entity>>();
		for (String type : this.types){
			this.entitiesByType.put(type,new LinkedList<Entity>());
		}
	}
	
	abstract public void update();
	
	public void addEvent(Event event) {
		((Scene_Level) this.scene).addEvent(event);
	}
	
	public void addEvents(List<Event> events) {
		for (Event event : events) this.addEvent(event);
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
	
	public List<Event> getEvents(EventType type) { 
		return ((Scene_Level) this.scene).getEvents(type); 
	}
	
	public Scene_Level getScene() { return (Scene_Level) this.scene; }
	public String[] getTypes() { return this.types; }
	
	public boolean hasType(String type) {
		for (String t : this.types) {
			if (t.equals(type)) return true;
		}
		return false;
	}
}