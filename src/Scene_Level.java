import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class Scene_Level extends Abstract_Scene {
	
	private Object_Level currentLevel;
	private Object_Level nextLevel;
	private int[] nextLevelSpawn = new int[2];
	private Hashtable<Integer,Object_Level> levels;
	private Hashtable<EventType,List<Event>> events;
	private Object_EntityManager eManager;
	private System_AI aiSystem;
	private System_Movement movementSystem;
	private System_Interaction interactionSystem;
	private System_Render renderSystem;
	private boolean playerDead;
	private boolean gameBeaten;
	
	public Scene_Level(Object_Game g) {
		super(g);
		this.levels = new Hashtable<Integer,Object_Level>();
		this.events = this.initEventTable();
		this.currentLevel = null;
		this.nextLevel = null;
		this.playerDead = false;
		this.gameBeaten = false;
		
		this.eManager = new Object_EntityManager(this);
		this.aiSystem = new System_AI(this);
		this.movementSystem = new System_Movement(this,game.getKeyHandler());
		this.interactionSystem = new System_Interaction(this);
		this.renderSystem = new System_Render(this,game.getScreen());
		
		
		/*
		 * Im Folgenden werden ein paar Level und Entitaeten erstellt. Das sollte
		 * spaeter ausgelagert und aus Dateien eingelesen werden.
		 */
		Object_Level level1 = new Object_Level("map2", 1);
		Object_Level level2 = new Object_Level("map3", 2);
		Object_Level level3 = new Object_Level("map4", 3);
		
		Entity player = new Entity("Tollkühner Held",eManager);
		new Component_Movement(player,movementSystem,2,5,0,0,16,false,true);
		new Component_Health(player,interactionSystem,10);
		new Component_Sprite(player,renderSystem,"player_2");
		new Component_Controls(player,movementSystem);
		new Component_Camera(player,renderSystem);
		player.init();
		eManager.setPlayer(player);
		
		Entity enemy = new Entity("Gegner",eManager);
		new Component_Movement(enemy,movementSystem,4,5,0,0,16,false,true);
		new Component_AI(enemy,aiSystem);
		new Component_Sprite(enemy,renderSystem,"character_1");
		new Component_Controls(enemy,movementSystem);
		new Component_Health(enemy,interactionSystem,5);
		
		Entity trigger = new Entity("Portal",eManager);
		new Component_Movement(trigger,movementSystem,19,12,0,0,0,true,true);
		new Trigger_LevelChange(trigger,interactionSystem,EventType.COLLISION,2,0,4);
		
		Entity trap1 = new Entity("Falle",eManager);
		new Component_Sprite(trap1,renderSystem,"trap_1");
		new Component_Movement(trap1,movementSystem,5,10,0,0,32,true,true);
		new Trigger_Attack(trap1,interactionSystem,EventType.COLLISION,4);
		new Component_AI(trap1,aiSystem);
		new Component_Controls(trap1,movementSystem);
		
		Entity trap2 = new Entity("Falle",eManager);
		new Component_Sprite(trap2,renderSystem,"trap_1");
		new Component_Movement(trap2,movementSystem,9,11,0,0,32,true,true);
		new Trigger_Attack(trap2,interactionSystem,EventType.COLLISION,4);
		new Component_AI(trap2,aiSystem);
		new Component_Controls(trap2,movementSystem);
		
		Entity trap3 = new Entity("Falle",eManager);
		new Component_Sprite(trap3,renderSystem,"trap_1");
		new Component_Movement(trap3,movementSystem,15,11,0,0,32,true,true);
		new Trigger_Attack(trap3,interactionSystem,EventType.COLLISION,4);
		new Component_AI(trap3,aiSystem);
		new Component_Controls(trap3,movementSystem);
		
		Entity instadeath = new Entity("Toeter!",eManager);
		new Component_Movement(instadeath,movementSystem,14,3,0,0,0,true,true);
		new Trigger_Attack(instadeath,interactionSystem,EventType.COLLISION,1000);
		
		Entity trigger2 = new Entity("Portal",eManager);
		new Component_Movement(trigger2,movementSystem,24,7,0,0,0,true,true);
		new Trigger_LevelChange(trigger2,interactionSystem,EventType.COLLISION,3,0,4);
		
		Entity trigger3 = new Entity("Spielende",eManager);
		new Component_Movement(trigger3,movementSystem,22,2,0,0,0,true,true);
		new Trigger_EndGame(trigger3,interactionSystem,EventType.COLLISION);
		
		
		
		
		/*
		 * Entitaeten den Leveln hinzufuegen nicht vergessen!!!
		 */
		level1.addEntity(enemy);
		level1.addEntity(trigger);
		level1.addEntity(instadeath);
		
		level2.addEntity(trigger2);
		
		level3.addEntity(trigger3);
		level3.addEntity(trap1);
		level3.addEntity(trap2);
		level3.addEntity(trap3);
		
		this.levels.put(level1.getID(), level1);
		this.levels.put(level2.getID(), level2);
		this.levels.put(level3.getID(), level3);
		
		this.currentLevel = levels.get(1);
		this.currentLevel.init();
	}
	


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
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
		this.clearEvents();
		this.check_menu();
		this.movementSystem.update();
		this.interactionSystem.update();
		this.aiSystem.update();
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
	
	
	/*
	 * Privates
	 */
	
	/*
	 * Überprüft, ob das Spiel als beendet erklärt wurde.
	 */
	private void check_gameBeaten() {
		if (this.gameBeaten) {
			game.getKeyHandler().clear();
			game.switchScene(new Scene_GameBeaten(game));
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
			game.getKeyHandler().clear();
			game.switchScene(new Scene_GameOver(game));
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
}