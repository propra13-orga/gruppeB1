import java.util.LinkedList;
import java.util.List;

public class Object_Level extends Object_Map implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8032513502291500180L;
	List<Entity> entities;
	int ID;

	public Object_Level(Object_Game game, String mapname, int ID) {
		super(game, mapname);
		this.entities = new LinkedList<Entity>();
		this.ID = ID;
	}
	
	// Getters
	
	public List<Entity> getEntities() { return this.entities; }
	public int getID() { return this.ID; }
	
	// Setters
	
	public void addEntity(Entity entity) { this.entities.add(entity); }
	public void addEntities(List<Entity> entities) { this.entities.addAll(entities); }
	public void removeEntity(Entity entity) {
		if (this.entities.contains(entity)) this.entities.remove(entity);
	}
	
	public void init() {
		for (Entity entity : this.entities) {
			entity.init();
		}
		if (this.properties.containsKey("music")) {
			this.soundmanager.playMidi(this.properties.get("music"));
		}
	}
	
	public void deinit() {
		for (Entity entity : this.entities) {
			entity.getManager().deregister(entity);
		}
	}
}
