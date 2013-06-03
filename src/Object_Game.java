/*
 * Game.java
 * Dies ist die Hauptklasse, welche den Screen und den KeyHandler (spï¿½ter auch evtl.
 * noch weitere global zugï¿½ngliche Objekte) bereitstellt.
 * Hier wird die aktuelle auszufï¿½hrende Szene gespeichert und geupdatet.
 * 
 * Die Methode game.update(), deren Ausfï¿½hrung genau einem Frame entspricht wird dann
 * letztendlich im Gameloop der Klasse Main aufgerufen.
 */

public class Object_Game {

	static final String GAME_TITLE = "ProPra - 1. Meilenstein";
	
	private Abstract_Scene scene;
	private Object_Screen screen;
	private Object_KeyHandler keyhandler;
	
	Object_Game() {
		//Screen und KeyHandler initialisieren
		this.keyhandler = new Object_KeyHandler();
		this.screen = new Object_Screen();
		//this.scene = new Scene_StartMenu(this);//Level(this);
		
		Object_BattleActor b1 = new Object_BattleActor(this, 460, 130, 9);
		Object_BattleActor b2 = new Object_BattleActor(this, 470, 200, 10);
		Object_BattleContext c1 = new Object_BattleContext(b1, b2);
		
		this.scene = new Scene_BattleSystem(c1, null, this);
		
		this.screen.setTitle(GAME_TITLE);
		this.screen.addKeyListener(keyhandler);
		this.screen.setVisible(true);
	}

	//Die Ausführung dieser Methode entspricht genau einem Frame
	public void update() {
		this.scene.update();
		this.keyhandler.freezeUpdate();		//Der Counter von eingefrorenen Tasten wird
											//in jedem Frame dekrementiert
	}
	
	//Aktualisiert den tatsächlichen Bildschirm
	public void display() {
		this.screen.update();
	}

	//Getter / Setter
	
	public Abstract_Scene getScene() {
		return this.scene;
	}
	
	public Object_Screen getScreen() {
		return this.screen;
	}

	public Object_KeyHandler getKeyHandler() {
		return this.keyhandler;
	}
	
	//Szenen wechseln
	
	public void switchScene(Abstract_Scene next) {
		this.scene.onExit();
		this.scene = next;
		this.scene.onStart();
	}
	
	public void quit() {
		this.scene = null;
	}

}
