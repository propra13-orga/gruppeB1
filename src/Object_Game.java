import java.awt.Font;

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

	public static final String GAME_TITLE = "ProPra - 2. Meilenstein";
	//Fonts:
	//Gentium Book Basic <--- Gut!
	//Arial Black
	public static final Font FONT = new Font("Arial", Font.PLAIN, 20);
	
	private Abstract_Scene scene;
	private Object_Screen screen;
	private Object_KeyHandler keyhandler;
	private Object_SoundManager soundmanager;
	private Object_AnimationManager animationmanager;
	
	Object_Game() {
		//Screen und KeyHandler initialisieren
		this.keyhandler = new Object_KeyHandler();
		this.screen = new Object_Screen();
		this.screen.getBuffer().getGraphics().setFont(new Font("Arial", Font.PLAIN, 130));
		this.soundmanager = new Object_SoundManager();
		this.animationmanager = new Object_AnimationManager(this);
		
		//this.scene = new Scene_BattleSystem(c1, null, this);
		this.scene = new Scene_StartMenu(this);
		//this.scene = new Scene_AnimationManagerTest(this);
		
		this.screen.setTitle(GAME_TITLE);
		this.screen.addKeyListener(keyhandler);
		this.screen.setVisible(true);
	}

	//Die Ausf�hrung dieser Methode entspricht genau einem Frame
	public void update() {
		this.scene.update();
		this.keyhandler.freezeUpdate();		//Der Counter von eingefrorenen Tasten wird
											//in jedem Frame dekrementiert
	}
	
	//Aktualisiert den tats�chlichen Bildschirm
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
	
	public Object_SoundManager getSoundManager() {
		return this.soundmanager;
	}
	
	public Object_AnimationManager getAnimationManager() {
		return this.animationmanager;
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
