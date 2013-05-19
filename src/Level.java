import java.util.LinkedList;
import java.util.List;


public class Level extends Map {
	List<Entity> entities;
	int ID;

	public Level(String mapname, Scene s, int ID) {
		super(mapname, s);
		this.entities = new LinkedList<Entity>();
		this.ID = ID;
	}
	
	// Getters
	
	public List<Entity> getEntities() { return this.entities; }
	public int getID() { return this.ID; }
	
	// Setters
	
	public void addEntity(Entity entity) { this.entities.add(entity); }
	public void addEntities(List<Entity> entities) { this.entities.addAll(entities); }
	
	public void init() {
		for (Entity entity : this.entities) {
			entity.init();
		}
	}
	
	public void deinit() {
		for (Entity entity : this.entities) {
			entity.getManager().deregister(entity);
		}
	}

}
