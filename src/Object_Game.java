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
	public static final Font FONT = new Font("Arial", Font.PLAIN, 20);
	public static final boolean FULLSCREEN = false;
	
	private boolean switching;
	private Abstract_Scene scene;
	private Abstract_Scene next_scene;
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
		this.next_scene = null;
		this.switching = false;
		//this.scene = new Scene_AnimationManagerTest(this);
		
		this.screen.setTitle(GAME_TITLE);
		this.screen.addKeyListener(keyhandler);
		this.screen.setVisible(true);
	}

	//Die Ausf�hrung dieser Methode entspricht genau einem Frame
	public void update() {
		
		if (this.switching) {
			
			if (!this.animationmanager.isFading()) {
				System.out.println("\t\t\tTAUSCHEN");
				//Fadeout abgeschlossen, tausche die scenes
				this.scene.onExit();
				this.scene = this.next_scene;
				if (this.scene == null) {
					return;
				}
				this.scene.onStart();
				this.switching = false;
				this.next_scene = null;
				this.animationmanager.fadeIn(1, 10);
				return;
			}
			
			this.screen.clear();
			this.scene.updateDataOnSwitching();
			this.animationmanager.updateData();
			this.keyhandler.freezeUpdate();
			this.scene.updateScreenOnSwitching();
			this.animationmanager.updateScreen();
			
		}
		else {
			this.screen.clear();

			this.scene.updateData();
			this.animationmanager.updateData();
			this.keyhandler.freezeUpdate();
			
			this.scene.updateScreen();
			this.animationmanager.updateScreen();
		}
	}
	
	//Aktualisiert den tats�chlichen Bildschirm (muss nicht notwendigerweise mit update() synchron laufen!)
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
	
	public void switchScene(Abstract_Scene next, boolean fading) {
		if (fading) {
			this.animationmanager.fadeOut(1,10);
			this.switching = true;
			this.next_scene = next;
		}
		else {
			this.scene.onExit();
			this.scene = next;
			this.scene.onStart();
		}
	}
	
	public void switchScene(Abstract_Scene scene) {
		switchScene(scene, false);
	}
	
	public void exitOnError(String msg) {
		System.out.println("Programmabbruch: "+msg);
		this.quit();
	}
	
	public void quit() {
		this.switchScene(null);
	}

}
