import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Der EntityManager dient der verwaltung der Entitäten. Das schließt das
 * Hinzufügen und Entfernen dieser von der Karte oder dem gesamten Spiel mit ein.
 * 
 * @author Victor Persien
 */


class Object_EntityManager implements IEventListener {
	protected Scene_Level scene;
	protected List<Entity> entities;
	protected HashMap<Integer,Entity> entitiesByID;
	protected Entity player;
	protected int lastID;
	protected Map<EventType,List<Event>> events;
	
	public Object_EntityManager(Scene_Level scene) {
		this.events = new HashMap<EventType,List<Event>>();
		this.scene = scene;
		this.entities = new LinkedList<Entity>();
		this.entitiesByID = new HashMap<Integer,Entity>();
		this.lastID = 0;
		this.listenTo(EventType.DEATH);
	}
	
	/**
	 * Update.
	 */
	public void update() {
		/**
		 * Behandelt den Tod von Entitäten. Ist der Spieler betroffen, wird dies
		 * der zugehörigen Scene_Level mitgeteilt.
		 */
		for (Event event : this.getEvents(EventType.DEATH)) {
			Entity entity = event.getUndergoer();
			if (this.isPlayer(entity)) {
				this.getScene().setPlayerDead();
			}
			else {
				this.deregister(entity);
			}
		}
	}
	
	

	
	/**
	 * Füge eine Entität der Entitätenliste hinzu.
	 */
	public void register(Entity entity) {
		if (!this.entities.contains(entity)) {
			this.entities.add(entity);
			int id = entity.getID();
			this.entitiesByID.put(id, entity);
			if (id > this.lastID) this.lastID = id;
		}
	}
	
	/**
	 * Deinitialisiere und dann entferne eine Entität aus der Entitätenliste.
	 */
	public void deregister(Entity entity) {
		try {
			entity.deinit();
			this.entitiesByID.remove(entity.getID());
			this.entities.remove(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Lege eine Entität als Spieler fest.
	 */
	public void setPlayer(Entity entity) {
		this.player = entity;
	}
	
	public void serializeEntities() {
		for (int i=0;i<this.entities.size();i++) {
			try {
				Entity entity = this.entities.get(i);
				String fpath = "";
				if (this.isPlayer(entity)) {
					fpath = "res/save/player.ser";
				}
				else fpath = String.format("res/save/entity%d.ser",entity.getID());
				FileOutputStream fileOut = new FileOutputStream(fpath);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(entity);
				out.close();
				fileOut.close();
			}
			catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	/**
	 * Deserialisiert alle Entitäten im aktuellen Raum.
	 * @param room
	 * @return
	 */
	public List<Entity> deserializeEntities(Object_Room room) {
		File folder = new File("res/save");
		File[] listOfFiles = folder.listFiles();
		
		List<Entity> entities = new LinkedList<Entity>();
		boolean isplayer = false;
		
		for (File file : listOfFiles) {
			String fname = file.toString();
			if (fname.contains("player.ser")) {
				isplayer = true;
			}
			try {
				FileInputStream fileIn = new FileInputStream(fname);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				Entity entity = (Entity) in.readObject();
				in.close();
				fileIn.close();
				entity.setManager(this);
				for (Abstract_Component comp : entity.components.values()) {
					comp.setSystem(this.getScene().getSystemByType(comp.getType()));
				}
				if (isplayer) this.player = entity;
				else entities.add(entity);
				isplayer = false;
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return entities;
	}
	
	/**
	 * Ist die angegebene Entität der Spieler?
	 */
	public boolean isPlayer(Entity entity) {
		if (this.player.equals(entity)) return true;
		return false;
	}
	
	/**
	 * Gebe die Spielerentität zurück.
	 */
	public Entity getPlayer() { return this.player; }
	
	/**
	 * Gibt die Entität anhand ihrer ID zurück (veraltet).
	 * @param id
	 * @return
	 */
	public Entity getEntityByID(int id) {
		if (this.entitiesByID.containsKey(id)) return this.entitiesByID.get(id);
		return null;
	}
	
	/**
	 * Entitäten können sich hier bei ihrer Initialisierung eine noch nicht
	 * vergebene ID zur eindeutigen Identifizierung abholen.
	 */
	public int receiveID(Entity entity) {
		int id = this.lastID;
		this.lastID += 1;
		return id;
	}
	
	/*
	 * Privates
	 */
	
	/**
	 * Gibt die Levelscene zurück.
	 * @return
	 */
	private Scene_Level getScene() {
		return this.scene;
	}

	@Override
	public void addEvent(Event event) {
		this.scene.addEvent(event);		
	}

	@Override
	public void broadcastEvent(Event event) {
		EventType type = event.getType();
		if (!this.events.containsKey(type)) return;
		this.events.get(type).add(event);		
	}

	@Override
	public List<Event> getEvents(EventType... eventTypes) {
		List<Event> events = new LinkedList<Event>();
		for (EventType type : eventTypes) {
			events.addAll(this.events.get(type));
			this.events.get(type).clear();
		}
		return events;
	}

	@Override
	public void listenTo(EventType... eventTypes) {
		for (EventType eventType : eventTypes) {
			this.events.put(eventType, new LinkedList<Event>());
		}
		this.scene.listenTo(this, eventTypes);
	}

}