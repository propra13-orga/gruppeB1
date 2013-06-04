import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class Scene_Level extends Abstract_Scene {
	
	/*
	 * LEVELVERWALTUNG.
	 */
	private Object_Level currentLevel;
	private Object_Level nextLevel;
	private int[] nextLevelSpawn = new int[2];
	private Hashtable<Integer,Object_Level> levels;
	
	/*
	 * EVENTVERWALTUNG.
	 */
	private Hashtable<EventType,List<Event>> events;
	private Abstract_Scene nextScene;
	
	/*
	 * ENTITYMANAGER UND KOMPONENTENSYSTEME.
	 */
	private Object_EntityManager eManager;
	private System_AI aiSystem;
	private System_Movement movementSystem;
	private System_Interaction interactionSystem;
	private System_Render renderSystem;
	
	/*
	 * FLAGS.
	 */
	private boolean playerDead;
	private boolean gameBeaten;
	
	public Scene_Level(Object_Game g) {
		super(g);
		this.levels = new Hashtable<Integer,Object_Level>();
		this.events = this.initEventTable();
		this.nextScene = null;
		this.currentLevel = null;
		this.nextLevel = null;
		this.playerDead = false;
		this.gameBeaten = false;
		
		this.eManager = new Object_EntityManager(this);
		this.aiSystem = new System_AI(this);
		this.movementSystem = new System_Movement(this,game.getKeyHandler());
		this.interactionSystem = new System_Interaction(this);
		this.renderSystem = new System_Render(this,game.getScreen());
		
		
		this.BeispielInit();
		
		this.currentLevel = levels.get(1);
		this.currentLevel.init();
	}
	


	@Override
	public void onStart() {
		this.nextScene = null;
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
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
		this.clearEvents();
		this.check_menu();
		this.aiSystem.update();
		this.movementSystem.update();
		this.interactionSystem.update();
		this.eManager.update();
	}

	@Override
	public void updateScreen() {
		this.renderSystem.update();
	}
	
	public void addEvent(Event event) {
		this.events.get(event.getType()).add(event);
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
	
	public List<Event> getEvents(EventType type) {
		return this.events.get(type);
	}
	
	public Entity getPlayer() {
		return this.eManager.getPlayer();
	}
	
	public System_Component getSystemMovement() { return this.movementSystem; }
	public System_Component getSystemInteraction() { return this.interactionSystem; }
	public System_Component getSystemAI() { return this.aiSystem; }
	public System_Component getSystemRender() { return this.renderSystem; }
	public Object_EntityManager getEntityManager() { return this.eManager; }
	
	
	/*
	 * Privates
	 */
	
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
			game.switchScene(new Scene_GameMenu(game, this));
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
	 * Löscht alle Events.
	 */
	private void clearEvents() {
		for (EventType type : this.events.keySet()) {
			this.events.get(type).clear();
		}
	}
	
	/*
	 * Initialisiert die Hashtabelle, in der die Events gespeichert werden.
	 */
	private Hashtable<EventType,List<Event>> initEventTable() {
		Hashtable<EventType,List<Event>> events = new Hashtable<EventType,List<Event>>();
		for (EventType type : EventType.values()) {
			events.put(type, new LinkedList<Event>());
		}
		return events;
	}
	
	/*
	 * Hier werden Level und Entitäten initialisiert. Wird später ausgelagert.
	 */
	private void BeispielInit() {
		Object_Level level1 = new Object_Level("map2", 1);
		Object_Level level2 = new Object_Level("map3", 2);
		Object_Level level3 = new Object_Level("map4", 3);
		this.levels.put(level1.getID(), level1);
		this.levels.put(level2.getID(), level2);
		this.levels.put(level3.getID(), level3);
		
		Factory factory = new Factory(this,"res/db.csv");
		
		Entity player = factory.buildEntity("PlayerRaw", "Held", 2, 5);
		new Component_Camera(player,renderSystem);
		new Component_Inventory(player, interactionSystem);
		player.init();
		eManager.setPlayer(player);
		
		String bla = "Sie hatten uns mit Zwang und Lügen in ihre Stöcke eingeschraubt. Sie hatten gnädig uns erlaubt, in ihrem Joch ihr Land zu pflügen. Sie saßen da in Prunk und Pracht mit vollgestopftem Magen und zwangen uns, für ihre Macht einander totzuschlagen. Doch wir, noch stolz auf unsere Fesseln, verbeugten uns vor ihren Sesseln.";
		Entity enemy = factory.buildEntity("NPC1", "Hannes", 4, 5);
		new Trigger_Dialog(enemy,interactionSystem,EventType.ACTION,bla);
		
		Entity salesperson = factory.buildEntity("Salesperson","Ladenhueter",1,4);
		
		
		Entity instadeath = factory.buildEntity("Instadeath","Toeter",14,3);
		
		EntityData triggerData = new EntityData(factory.getDB(),"Teleport","Portal",19,12);
		triggerData.initTriggerLevelChange(2, 0, 4);
		Entity trigger = factory.getEntity(triggerData);
		
		triggerData = new EntityData(factory.getDB(),"Teleport","Portal",24,7);
		triggerData.initTriggerLevelChange(3, 0, 4);
		Entity trigger2 = factory.getEntity(triggerData);
		
		Entity trigger3 = factory.buildEntity("Success1","Spiel-Ende", 22, 2);
		
		Entity trap1 = factory.buildEntity("Trap1", "Falle", 8, 10);
		Entity trap2 = factory.buildEntity("Trap1", "Falle", 9, 11);
		Entity trap3 = factory.buildEntity("Trap1", "Falle", 10, 12);
		
		
		// Fehler: Sprite verschwindet nicht.
//		Entity item = new Entity("testitem",this.eManager);
//		new Component_Movement(item,movementSystem,5,8);
//		new Component_Item(item, interactionSystem, "item", 0, 0, 0, 1, true, true, true);
//		new Component_Sprite(item, renderSystem, "player");
//		new Trigger_PickUp(item, interactionSystem, EventType.ACTION);
		
		
		level1.addEntity(enemy);
		level1.addEntity(trigger);
		level1.addEntity(instadeath);
		level1.addEntity(salesperson);
//		level1.addEntity(item);
		
		level2.addEntity(trigger2);
		
		level2.addEntity(trap1);
		level2.addEntity(trap2);
		level2.addEntity(trap3);
		
		level3.addEntity(trigger3);
	}
}