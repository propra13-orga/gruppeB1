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
		
		
		/*
		 * Im Folgenden werden ein paar Level und Entitäten erstellt. Das sollte
		 * später ausgelagert und aus Dateien eingelesen werden.
		 */
		Level level1 = new Level("map2", 1);
		Level level2 = new Level("map3", 2);
		
		Entity player = new Entity("Tollkühner Held",eManager);
		new CompMovement(player,movementSystem,2,5,0,0,16,false,true);
		new CompSprite(player,renderSystem,"player_2");
		new CompControls(player,movementSystem);
		new CompCamera(player,renderSystem);
		player.init();
		eManager.setPlayer(player);
		
		Entity enemy = new Entity("Gegner",eManager);
		new CompMovement(enemy,movementSystem,4,5,0,0,16,false,true);
		new CompSprite(enemy,renderSystem,"character_1");
		
		Entity trigger = new Entity("Portal",eManager);
		new CompMovement(trigger,movementSystem,19,12,0,0,0,true,true);
		new CompTriggerLevelChange(trigger,interactionSystem,EventType.COLLISION,2,0,4);
		
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
		if (check_menu()) return;
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
	
	public Level getCurrentLevel() {
		return this.currentLevel;
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
	 * Prüft, ob ESC gedrückt wurde und ruft in diesem Fall das Spielmenü auf.
	 */
	private boolean check_menu() {
		if (game.getKeyHandler().getKey(KeyHandler.KEY_ESCAPE)) {
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_ESCAPE, 20);
			//Men� aufrufen
			game.scene = new Scene_GameMenu(game, this);
			return true;
		}
		return false;
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
		CompMovement compMovement = (CompMovement) this.eManager.getPlayer().getComponent("movement");
		CompSprite compSprite = (CompSprite) this.getPlayer().getComponent("sprite");
		compMovement.warp(x, y);
		compSprite.setX(compMovement.getX());
		compSprite.setY(compMovement.getY());
		this.currentLevel.init();
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
	 * Löscht alle Events.
	 */
	private void clearEvents() {
		for (EventType type : this.events.keySet()) {
			this.events.get(type).clear();
		}
	}
}