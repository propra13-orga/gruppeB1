import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * Diese Klasse definiert das Hauptspiel, das heißt alles, was sich auf der Karte
 * abspielt.
 * <p>
 * Ein Level besteht zunächst einmal aus mehreren Räumen (Object_Room), in denen
 * sich Entitäten befinden. Dem Spieler ist es möglich, zwischen diesen Räumen
 * zu wechseln.
 * <p>
 * Die Verarbeitung der Spieldaten findet in den Komponentensystemen statt,
 * wobei jedes unterschiedliche Typen von Komponenten manipuliert. Komponenten
 * sind spezielle Datencontainer, die das Verhalten von Spielobjekten (Entitäten)
 * definieren. Die Masse der Manipulationen bilden zusammen die Spielmechanik.
 * <p>
 * Die Kommunikation zwischen den Komponentensystem erfolgt über das verschicken
 * und empfangen von Events, die Paaren von Entitäten einen Event-Typ zuweisen.
 * Je nach Event-Typ sind die Komponentensysteme veranlasst, unterschiedliche 
 * Manipulationen an den Komponenten der jeweiligen Entitäten vorzunehmen.
 * <p>
 * Eine solche Architektur wird auch als "Entity-Component System" bezeichnet
 * und stellt eine Form des Data-Oriented Development dar, welches Spielobjekte
 * nicht als hierarchisch geordnet ansieht, sondern als reine passive
 * Datencontainer, um so eine größere Dynamik gewährleisten zu können.
 * 
 * @author Victor Persien
 *
 */

public class Scene_Level extends Abstract_Scene {
	
	
	/*
	 * LEVELVERWALTUNG.
	 */
	private String levelname = "level01";
	private String nextLevelName;
	
	private Object_Room currentRoom;
	private Object_Room nextRoom;
	private int[] nextRoomSpawn = new int[2];
	private Hashtable<Integer,Object_Room> rooms;
	
	/*
	 * EVENTVERWALTUNG.
	 */
	private Abstract_Scene nextScene;
	private Object_EventManager eventManager;
	
	/*
	 * ENTITYMANAGER UND KOMPONENTENSYSTEME.
	 */
	private Object_EntityManager eManager;
	private System_AI aiSystem;
	private System_Movement movementSystem;
	private System_Interaction interactionSystem;
	private System_Render renderSystem;
	private System_Quest questSystem;
	
	private Factory factory;
	
	/*
	 * FLAGS.
	 */
	private boolean playerDead;
	private boolean gameBeaten;
	private boolean load;
	private boolean already_running;
	
	public Scene_Level(Object_Game g) {
		super(g);
		this.load = false;
		this.already_running = false;
		
		this.eventManager = new Object_EventManager();
		
		this.eManager = new Object_EntityManager(this);
		this.aiSystem = new System_AI(this);
		this.movementSystem = new System_Movement(this,game.getKeyHandler());
		this.interactionSystem = new System_Interaction(this);
		this.renderSystem = new System_Render(this,game.getScreen());
		this.questSystem = new System_Quest(this);
		
		this.factory = new Factory(this);
	}
	
	public Scene_Level(Object_Game g, String levelname, Entity player) {
		this(g);
		this.levelname = levelname;
		if (player != null) {
			this.factory.updateSystems(player);
			player.init();
			this.eManager.setPlayer(player);
		}
	}
	
	public Scene_Level(Object_Game g, boolean load) {
		this(g);
		this.load = load;
	}
	
	public void serialize() {
		Object_SaveLoadData.serialize(
				new Object_SaveLoadData(
						this.rooms,
						this.currentRoom.getID(),
						this.getPlayer()
				)
		);
	}
	
	public void deserialize() {
		Object_SaveLoadData sld = Object_SaveLoadData.deserialize(this);
		this.rooms = sld.getRooms();
		this.currentRoom = rooms.get(sld.getCurrentRoom());
		this.eManager.setPlayer(sld.getPlayer());
	}


	@Override
	public void onStart() {
		if (this.already_running) {
			return;
		}
		this.nextScene = null;
		this.rooms = new Hashtable<Integer,Object_Room>();
		this.nextScene = null;
		this.currentRoom = null;
		this.nextRoom = null;
		this.nextLevelName = null;
		this.playerDead = false;
		this.gameBeaten = false;
		
		if (this.load) {
			this.deserialize();
			this.getPlayer().init();
		}
		else {
			this.initLevel();
		}
		
		this.currentRoom.init();
		this.already_running = true;
	}

	@Override
	public void onExit() {
		//
	}

	@Override
	public void updateData() {
		this.check_playerDeath();
		this.check_gameBeaten();
		if (this.nextRoom != null) {
			this.changeRoom();
			this.nextRoom = null;
		}
		else if (this.nextLevelName != null) {
			this.changeLevel();
			this.nextLevelName = null;
			return;
		}
		else if (this.nextScene != null) {
			this.changeScene();
			this.nextScene = null;
		}
		this.check_menu();
		this.aiSystem.update();
		this.movementSystem.update();
		this.interactionSystem.update();
		this.questSystem.update();
		this.eManager.update();
	}

	@Override
	public void updateScreen() {
		this.renderSystem.update();
	}
	
	/**
	 * Fügt dem Eventmanager ein Event hinzu.
	 * @param event
	 */
	public void addEvent(Event event) {
		this.eventManager.addEvent(event);
	}
	
	/**
	 * Registiert einen EventListener beim Eventmanager.
	 * @param listener
	 * @param eventTypes
	 */
	public void listenTo(IEventListener listener, EventType... eventTypes) {
		this.eventManager.listenTo(listener, eventTypes);
	}
	
	/**
	 * Setzt das Spiel auf erfolgreich bestanden.
	 */
	public void beatGame() {
		this.gameBeaten = true;
	}
	
	/**
	 * Setzt das nächste Level auf levelname. Der Wechsel erfolgt dann im nächsten
	 * Zyklus.
	 * @param levelname
	 */
	public void demandLevelChange(String levelname) {
		this.nextLevelName = levelname;
	}
	
	/**
	 * Setzt den nächsten Raum auf denjenigen mit der angegebenen ID und die 
	 * dortigen Startkoordinaten auf (x,y).
	 * @param ID	ID des nächsten Raums.
	 * @param x		X-Koordinate nach Betreten.
	 * @param y		Y-Koordinate nach Betreten.
	 */
	public void demandRoomChange(int ID, int x, int y) {
		this.nextRoom = this.rooms.get(ID);
		this.nextRoomSpawn[0] = x;
		this.nextRoomSpawn[1] = y;
	}
	
	/**
	 * Setzt die nächste Scene auf scene.
	 * @param scene
	 */
	public void demandSceneChange(Abstract_Scene scene) {
		this.nextScene = scene;
	}
	
	
	public void setPlayerDead() {
		this.playerDead = true;
	}
	
	public Object_Room getCurrentRoom() {
		return this.currentRoom;
	}
	
	/**
	 * Gibt alle Entitäten zurück, die sich an Position xy befinden.
	 */
	public List<Entity> getEntitiesAt(int x, int y) {
		return this.movementSystem.getEntitiesAt(x,y);
	}
	
	/**
	 * Gibt ein Array in den Maßen der aktuellen Map zurück mit einer 1 an den
	 * Stellen, wo sich mindestens eine Entität befindet, und einer 0 sonst.
	 */
	public int[][] getEntityPositions() {
		return this.movementSystem.getEntityPositions();
	}
	
	/**
	 * Gibt die Factory (Fabrikfunktion für Entitäten) zurück.
	 */
	
	public Factory getFactory() { return this.factory; }
	
	
	/**
	 * Gibt die Spielerentität zurück.
	 */
	public Entity getPlayer() {
		return this.eManager.getPlayer();
	}
	
	public System_Component getSystemMovement() { return this.movementSystem; }
	public System_Component getSystemInteraction() { return this.interactionSystem; }
	public System_Component getSystemAI() { return this.aiSystem; }
	public System_Component getSystemRender() { return this.renderSystem; }
	public System_Component getSystemQuest() { return this.questSystem; }
	public Object_EntityManager getEntityManager() { return this.eManager; }
	
	
	/*
	 * Privates
	 */
	
	/**
	 * Gibt ein Komponentensystem zurück, welches Komponenten des Typs type
	 * verwaltet.
	 * @param type		Komponententyp.
	 * @return			Entsprechendes Komponentensystem.
	 */
	public System_Component getSystemByType(String type) {
		if (aiSystem.hasType(type)) return aiSystem;
		if (movementSystem.hasType(type)) return movementSystem;
		if (interactionSystem.hasType(type)) return interactionSystem;
		if (renderSystem.hasType(type)) return renderSystem;
		if (questSystem.hasType(type)) return questSystem;
		return null;
	}
	
	/**
	 * Überprüft, ob das Spiel als beendet erklärt wurde.
	 */
	private void check_gameBeaten() {
		if (this.gameBeaten) {
			this.game.getKeyHandler().clear();
			this.game.switchScene(new Scene_GameBeaten(game));
		}
	}
	
	/**
	 * Prüft, ob ESC gedrückt wurde und ruft in diesem Fall das Spielmenü auf.
	 */
	private void check_menu() {
		if (game.getKeyHandler().getKey(Object_KeyHandler.KEY_ESCAPE)) {
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_ESCAPE, 20);

			game.switchScene(new Scene_GameMenu(game, this), true);

		}
	}
	
	/**
	 * Prüft, ob der Spieler gestorben ist und in diesem Fall das Game-Over-Menü
	 * auf.
	 */
	private void check_playerDeath() {
		if (this.playerDead) {
			this.game.getKeyHandler().clear();
			this.game.switchScene(new Scene_GameOver(game));
		}
	}
	
	/**
	 * Wechselt das Level.
	 */
	private void changeLevel() {
		this.game.switchScene(new Scene_Level(this.game,this.nextLevelName,this.getPlayer()), true);
	}
	
	
	/**
	 * Nimmt die nötigen Änderungen vor, um das Level zu wechseln.
	 */
	private void changeRoom() {
		this.currentRoom.deinit();
		this.currentRoom = this.nextRoom;
		this.nextRoom = null;
		int x = this.nextRoomSpawn[0];
		int y = this.nextRoomSpawn[1];
		Component_Movement compMovement = (Component_Movement) this.eManager.getPlayer().getComponent("movement");
		Component_Sprite compSprite = (Component_Sprite) this.getPlayer().getComponent("sprite");
		compMovement.warp(x, y);
		compSprite.setX(compMovement.getX());
		compSprite.setY(compMovement.getY());
		this.currentRoom.init();
	}
	
	/**
	 * Wechselt die Scene zu der in this.nextScene angegebenen.
	 */
	private void changeScene() {
		this.game.getKeyHandler().clear();
		this.game.switchScene(this.nextScene);
	}
	
	
	/**
	 * Basisdaten für Spieler.
	 */
	private Map<String,String> basicPlayerData() {
		Map<String,String> data = new HashMap<String,String>();
		data.put("entityType", "player_raw");
		data.put("name", "Hannes");
		return data;
	}
	
	/**
	 * Hier werden Level und Entitäten initialisiert.
	 */
	private void initLevel() {

		/**
		 * Lese alle TMX-Dateien im Ordner des entsprechenden Levels.
		 */
		File folder = new File("res/maps/"+this.levelname);
		File[] files = folder.listFiles();
		for (File file : files) {
			String name = file.getName();
			Pattern p = Pattern.compile("(\\w+-(\\d)).tmx");
			Matcher m = p.matcher(name);
			if (m.matches()) {
				String roomname = m.group(1);
				int number = Integer.parseInt(m.group(2));
				Object_Room level = new Object_Room(this.game, this.levelname+"/"+roomname, number);
				this.rooms.put(level.getID(), level);
				/*
				 * Frage ggf. Startposition ab. Sobald mindestens eine TMX-Datei
				 * die Properties "startX" und "startY" hat, wird die erste
				 * gelesene davon als Anfangsmap gesetzt.
				 */
				if (this.currentRoom == null && level.hasProperty("startX")) {
					this.currentRoom = level;
				}
			}
		}
				
		this.initEntities();
	}
	
	/**
	 * Initialisiert alle Entitäten, die sich im aktuellen Raum befinden.
	 */
	private void initEntities() {
		// Mapeigene Entitäten bauen.
		for (Object_Room level : this.rooms.values()) {
			for (Map<String,String> entityData : level.getEntityData()) {
				level.addEntity(factory.build(entityData));
			}
		}
		
		// Spieler bauen oder verschieben.
		int x = Integer.parseInt(this.currentRoom.getProperties().get("startX"));
		int y = Integer.parseInt(this.currentRoom.getProperties().get("startY"));
		
		if (this.getPlayer() == null) {
			System.out.println("player == null");
			Map<String,String> playerData = this.basicPlayerData();
			playerData.put("x", Integer.toString(x));
			playerData.put("y", Integer.toString(y));
			
			Entity player = factory.build(playerData);
			player.init();
			eManager.setPlayer(player);
		}
		else {
			Component_Movement compMovement = (Component_Movement) this.eManager.getPlayer().getComponent("movement");
			Component_Sprite compSprite = (Component_Sprite) this.getPlayer().getComponent("sprite");
			compMovement.warp(x, y);
			compSprite.setX(compMovement.getX());
			compSprite.setY(compMovement.getY());
		}
	}
}









