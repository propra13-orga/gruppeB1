import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class Scene_Level extends Scene {
	private Level currentLevel;
	private Level nextLevel;
	private int[] nextLevelSpawn = new int[2];
	private Hashtable<Integer,Level> levels;
	private Hashtable<EventType,List<Event>> events;
	private EntityManager eManager;
	private MovementSystem movementSystem;
	private InteractionSystem interactionSystem;
	private RenderSystem renderSystem;

	public Scene_Level(Game g) {
		super(g);
		this.levels = new Hashtable<Integer,Level>();
		this.events = this.initEventTable();
		this.currentLevel = null;
		this.nextLevel = null;
		
		this.eManager = new EntityManager(this);
		this.movementSystem = new MovementSystem(this,game.getKeyHandler());
		this.interactionSystem = new InteractionSystem(this);
		this.renderSystem = new RenderSystem(this,game.getScreen());
		
		Level level1 = new Level("map1", this, 1);
		Level level2 = new Level("map2", this, 2);
		
		Entity player = new Entity("Tollkühner Held",eManager);
		new CompMovement(player,movementSystem,2,5,0,0,false,true);
		new CompSprite(player,renderSystem,"character_2");
		new CompControls(player,movementSystem);
		new CompCamera(player,renderSystem);
		player.init();
		eManager.setPlayer(player);
		
		Entity enemy = new Entity("Gegner",eManager);
		new CompMovement(enemy,movementSystem,4,5,0,0,false,true);
		new CompSprite(enemy,renderSystem,"character_1");
		
		Entity trigger = new Entity("Portal",eManager);
		new CompMovement(trigger,movementSystem,2,7,0,0,true,true);
		new CompTriggerLevelChange(trigger,interactionSystem,EventType.COLLISION,2,5,5);
		
		level1.addEntity(enemy);
		level1.addEntity(trigger);
		
		this.levels.put(level1.getID(), level1);
		this.levels.put(level2.getID(), level2);
		
		this.currentLevel = levels.get(1);
		this.currentLevel.init();
	}
	
	@Override
	public void update() {
		if (this.nextLevel != null) {
			this.changeLevel();
		}
		this.clearEvents();
		this.eManager.update();
		this.movementSystem.update();
		this.interactionSystem.update();
		this.renderSystem.update();
	}
	
	public void addEvent(Event event) {
		this.events.get(event.getType()).add(event);
	}
	
	public void demandLevelChange(int ID, int x, int y) {
		this.nextLevel = this.levels.get(ID);
		this.nextLevelSpawn[0] = x;
		this.nextLevelSpawn[1] = y;
	}
	
	public Map getCurrentLevel() {
		return this.currentLevel;
	}
	
	public List<Event> getEvents(EventType type) {
		return this.events.get(type);
	}
	
	
	
	/*
	 * Privates
	 */
	
	private void changeLevel() {
		this.currentLevel.deinit();
		this.currentLevel = this.nextLevel;
		this.nextLevel = null;
		int x = this.nextLevelSpawn[0];
		int y = this.nextLevelSpawn[1];
		CompMovement compMovement = (CompMovement) this.eManager.getPlayer().getComponent("movement");
		compMovement.warp(x, y);
	}
	
	private Hashtable<EventType,List<Event>> initEventTable() {
		Hashtable<EventType,List<Event>> events = new Hashtable<EventType,List<Event>>();
		for (EventType type : EventType.values()) {
			events.put(type, new LinkedList<Event>());
		}
		return events;
	}
	
	private void clearEvents() {
		for (EventType type : this.events.keySet()) {
			this.events.get(type).clear();
		}
	}

}

/*
 * Besondere Klassen.
 */

enum EventType { COLLISION, ILLEGALCOLLISION, ATTACK }

abstract class ComponentSystem {
	protected Scene scene;
	protected String[] types;
	protected List<Component> components;
	protected Hashtable<String,List<Entity>> entitiesByType;
	
	public ComponentSystem(Scene scene, String ...types) {
		this.scene = scene;
		this.components = new LinkedList<Component>();
		this.types = types;
		this.entitiesByType = new Hashtable<String,List<Entity>>();
		for (String type : this.types){
			this.entitiesByType.put(type,new LinkedList<Entity>());
		}
	}
	
	abstract public void update();
	
	private void addEvent(Event event) {
		((Scene_Level) this.scene).addEvent(event);
	}
	
	public void addEvents(List<Event> events) {
		for (Event event : events) this.addEvent(event);
	}
	
	public void register(Component component) {
		this.components.add(component);
		this.entitiesByType.get(component.type).add(component.entity);
	}
	
	public void deregister(Component component) {
		this.components.remove(component);
		this.entitiesByType.get(component.type).remove(component.entity);
	}
	
	public List<Entity> getEntitiesByType(String type) {
		return this.entitiesByType.get(type);
	}
	
	public List<Event> getEvents(EventType type) {
		return ((Scene_Level) this.scene).getEvents(type); 
	}
	
	public Scene_Level getScene() {
		return (Scene_Level) this.scene;
	}
}

class EntityManager {
	protected Scene scene;
	protected List<Entity> entities;
	protected Entity player;
	
	public EntityManager(Scene scene) {
		this.scene = scene;
		this.entities = new LinkedList<Entity>();
	}
	
	public void update() {
		
	}
	
	public void register(Entity entity) {
		if (this.entities.contains(entity)) this.entities.add(entity);
	}
	
	public void deregister(Entity entity) {
		try {
			entity.deinit();
			this.entities.remove(entity);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void setPlayer(Entity entity) {
		this.player = entity;
	}
	
	public boolean isPlayer(Entity entity) {
		if (this.player.equals(entity)) return true;
		return false;
	}
	
	public Entity getPlayer() { return this.player; }
}

class Entity {
	protected String name;
	protected EntityManager manager;
	private Hashtable<String,Component> components;;
	protected boolean active = false;
	
	public Entity(String name, EntityManager manager) {
		this.name = name;
		this.manager = manager;
		this.components = new Hashtable<String,Component>();
	}
	
	public void init() {
		for (Component component : this.components.values()) {
			component.init();
		}
		this.manager.register(this);
		this.active = true;
	}
	
	public void deinit() {
		for (Component component : this.components.values()) {
			component.deinit();
		}
		this.active = false;
	}
	
	public void addComponent(Component component) {
		this.components.put(component.type, component);
	}
	
	public boolean hasComponent(String type) {
		if (this.components.containsKey(type)) return true;
		return false;
	}
	
	public Component getComponent(String type) { return this.components.get(type); }
	public EntityManager getManager() {	return this.manager; }
	
	public boolean isPlayer() {	return this.manager.isPlayer(this); }
	
}

class Event {
	protected EventType type;
	protected Entity actor;
	protected Entity undergoer;
	
	public Event(EventType type, Entity actor, Entity undergoer) {
		this.type = type;
		this.actor = actor;
		this.undergoer = undergoer;
	}
	
	public EventType getType() { return this.type; }
	public Entity getActor() { return this.actor; }
	public Entity getUndergoer() { return this.undergoer; }
}