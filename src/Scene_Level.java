import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Scene_Level extends Abstract_Scene {
	
	
	/*
	 * LEVELVERWALTUNG.
	 */
	private String levelname = "level01";
	
	private Object_Level currentLevel;
	private Object_Level nextLevel;
	private int[] nextLevelSpawn = new int[2];
	private Hashtable<Integer,Object_Level> levels;
	
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
	}
	
	public Scene_Level(Object_Game g, String levelname) {
		this(g);
		this.levelname = levelname;
	}
	
	public Scene_Level(Object_Game g, boolean load) {
		this(g);
		this.load = load;
	}
	
	public void serialize() {
		Object_SaveLoadData.serialize(
				new Object_SaveLoadData(
						this.levels,
						this.currentLevel.getID(),
						this.getPlayer()
				)
		);
	}
	
	public void deserialize() {
		Object_SaveLoadData sld = Object_SaveLoadData.deserialize(this);
		this.levels = sld.getRooms();
		this.currentLevel = levels.get(sld.getCurrentRoom());
		this.eManager.setPlayer(sld.getPlayer());
	}


	@Override
	public void onStart() {
		if (this.already_running) {
			return;
		}
		this.nextScene = null;
		this.levels = new Hashtable<Integer,Object_Level>();
		this.nextScene = null;
		this.currentLevel = null;
		this.nextLevel = null;
		this.playerDead = false;
		this.gameBeaten = false;
		
		this.eventManager = new Object_EventManager();
		
		this.eManager = new Object_EntityManager(this);
		this.aiSystem = new System_AI(this);
		this.movementSystem = new System_Movement(this,game.getKeyHandler());
		this.interactionSystem = new System_Interaction(this);
		this.renderSystem = new System_Render(this,game.getScreen());
		this.questSystem = new System_Quest(this);
		
		if (this.load) {
			this.deserialize();
			this.getPlayer().init();
		}
		else {
			this.initLevel();
		}
		
		this.currentLevel.init();
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
		if (this.nextLevel != null) {
			this.changeLevel();
		}
		else if (this.nextScene != null) {
			this.changeScene();
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
	
	public void addEvent(Event event) {
		this.eventManager.addEvent(event);
	}
	
	public void listenTo(IEventListener listener, EventType... eventTypes) {
		this.eventManager.listenTo(listener, eventTypes);
	}
	
	public void beatGame() {
		this.gameBeaten = true;
	}
	
	public void demandLevelChange(int ID, int x, int y) {
		this.nextLevel = this.levels.get(ID);
		this.nextLevelSpawn[0] = x;
		this.nextLevelSpawn[1] = y;
	}
	
	public void demandSceneChange(Abstract_Scene scene) {
		this.nextScene = scene;
	}
	
	
	public void setPlayerDead() {
		this.playerDead = true;
	}
	
	public Object_Level getCurrentLevel() {
		return this.currentLevel;
	}
	
	/*
	 * Gibt alle Entitäten zurück, die sich an Position xy befinden.
	 */
	public List<Entity> getEntitiesAt(int x, int y) {
		return this.movementSystem.getEntitiesAt(x,y);
	}
	
	/*
	 * Gibt ein Array in den Maßen der aktuellen Map zurück mit einer 1 an den
	 * Stellen, wo sich mindestens eine Entität befindet, und einer 0 sonst.
	 */
	public int[][] getEntityPositions() {
		return this.movementSystem.getEntityPositions();
	}
	
	
	/*
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
	
	public System_Component getSystemByType(String type) {
		if (aiSystem.hasType(type)) return aiSystem;
		if (movementSystem.hasType(type)) return movementSystem;
		if (interactionSystem.hasType(type)) return interactionSystem;
		if (renderSystem.hasType(type)) return renderSystem;
		return null;
	}
	
	/*
	 * Überprüft, ob das Spiel als beendet erklärt wurde.
	 */
	private void check_gameBeaten() {
		if (this.gameBeaten) {
			this.game.getKeyHandler().clear();
			this.game.switchScene(new Scene_GameBeaten(game));
		}
	}
	
	/*
	 * Prüft, ob ESC gedrückt wurde und ruft in diesem Fall das Spielmenü auf.
	 */
	private void check_menu() {
		if (game.getKeyHandler().getKey(Object_KeyHandler.KEY_ESCAPE)) {
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_ESCAPE, 20);
			//Men� aufrufen
			game.switchScene(new Scene_GameMenu(game, this), true);
			//return true;
		}
		//return false;
	}
	
	/*
	 * Prüft, ob der Spieler gestorben ist und in diesem Fall das Game-Over-Menü
	 * auf.
	 */
	private void check_playerDeath() {
		if (this.playerDead) {
			this.game.getKeyHandler().clear();
			this.game.switchScene(new Scene_GameOver(game));
		}
	}
	
	
	/*
	 * Nimmt die nötigen Änderungen vor, um das Level zu wechseln.
	 */
	private void changeLevel() {
		this.currentLevel.deinit();
		this.currentLevel = this.nextLevel;
		this.nextLevel = null;
		int x = this.nextLevelSpawn[0];
		int y = this.nextLevelSpawn[1];
		Component_Movement compMovement = (Component_Movement) this.eManager.getPlayer().getComponent("movement");
		Component_Sprite compSprite = (Component_Sprite) this.getPlayer().getComponent("sprite");
		compMovement.warp(x, y);
		compSprite.setX(compMovement.getX());
		compSprite.setY(compMovement.getY());
		this.currentLevel.init();
	}
	
	/*
	 * 
	 */
	private void changeScene() {
		this.game.getKeyHandler().clear();
		this.game.switchScene(this.nextScene);
	}
	
	
	/*
	 * Basisdaten für Spieler.
	 */
	private Map<String,String> basicPlayerData() {
		Map<String,String> data = new HashMap<String,String>();
		data.put("entityType", "player_raw");
		data.put("name", "Hannes");
		return data;
	}
	
	/*
	 * Hier werden Level und Entitäten initialisiert.
	 */
	private void initLevel() {
		Map<String,String> playerData = this.basicPlayerData();
		/*
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
				Object_Level level = new Object_Level(this.game, this.levelname+"/"+roomname, number);
				this.levels.put(level.getID(), level);
				/*
				 * Frage ggf. Startposition ab. Sobald mindestens eine TMX-Datei
				 * die Properties "startX" und "startY" hat, wird die erste
				 * gelesene davon als Anfangsmap gesetzt.
				 */
				if (this.currentLevel == null && level.hasProperty("startX")) {
					this.currentLevel = level;
					playerData.put("x", level.getProperties().get("startX"));
					playerData.put("y", level.getProperties().get("startY"));
				}
			}
		}
		
		Factory factory = new Factory(this);
		
		for (Object_Level level : this.levels.values()) {
			for (Map<String,String> entityData : level.getEntityData()) {
				level.addEntity(factory.build(entityData));
			}
		}
		
		
		Entity player = factory.build(playerData);
		new Component_Camera(player,renderSystem);
		player.init();
		eManager.setPlayer(player);
		
	}
}