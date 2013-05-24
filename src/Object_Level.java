import java.util.LinkedList;
import java.util.List;


public class Object_Level extends Object_Map {
	List<Object_Entity> entities;
	int ID;

	public Object_Level(String mapname, int ID) {
		super(mapname);
		this.entities = new LinkedList<Object_Entity>();
		this.ID = ID;
	}
	
	// Getters
	
	public List<Object_Entity> getEntities() { return this.entities; }
	public int getID() { return this.ID; }
	
	// Setters
	
	public void addEntity(Object_Entity entity) { this.entities.add(entity); }
	public void addEntities(List<Object_Entity> entities) { this.entities.addAll(entities); }
	
	public void init() {
		for (Object_Entity entity : this.entities) {
			entity.init();
		}
	}
	
	public void deinit() {
		for (Object_Entity entity : this.entities) {
			entity.getManager().deregister(entity);
		}
	}

}
