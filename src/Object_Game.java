/*
 * Game.java
 * Dies ist die Hauptklasse, welche den Screen und den KeyHandler (sp�ter auch evtl.
 * noch weitere global zug�ngliche Objekte) bereitstellt.
 * Hier wird die aktuelle auszuf�hrende Szene gespeichert und geupdatet.
 * 
 * Die Methode game.update(), deren Ausf�hrung genau einem Frame entspricht wird dann
 * letztendlich im Gameloop der Klasse Main aufgerufen.
 */

public class Object_Game {

	static String GAME_TITLE = "ProPra - 1. Meilenstein";
	
	Abstract_Scene scene;
	
	private Object_Screen screen;
	private Object_KeyHandler keyhandler;
	
	Object_Game() {
		
		//Screen und KeyHandler initialisieren
		keyhandler = new Object_KeyHandler();
		screen = new Object_Screen();
		scene = new Scene_StartMenu(this);//Level(this);
		
		screen.setTitle(GAME_TITLE);
		screen.addKeyListener(keyhandler);
		screen.setVisible(true);
	}

	//Die Ausf�hrung dieser Methode entspricht genau einem Frame
	public void update() {
		scene.update();
		keyhandler.freezeUpdate();		//Der Counter von eingefrorenen Tasten wird
										//in jedem Frame dekrementiert
		screen.update();
	}

	//Getter / Setter
	
	public Object_Screen getScreen() {
		return screen;
	}

	public Object_KeyHandler getKeyHandler() {
		return keyhandler;
	}

}
