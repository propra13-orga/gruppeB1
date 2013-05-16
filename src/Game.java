/*
 * Game.java
 * 
 * Hier werden alle wichtigen Initialisierungen durchgeführt und die update Funktion
 * definiert. Darin läuft das gesamte Spiel ab. Tasten werden abgefragt, Gegner werden
 * bewegt und der Bildschirm wird aktualisiert.
 * 
 * Bis jetzt:
 * Ein Game Objekt besitzt ein Screen Objekt, um etwas auf dem Bildschirm anzeigen zu können
 * Ein Game Objekt besitzt ein KeyHandler Objekt, um möglichst einfach Tasten abzufragen
 * Ein Game Objekt besitzt ein Player Objekt. Dies ist der Hauptspieler, welcher vom Benutzer
 * gesteuert wird
 * Ein Game Objekt besitzt einen boolean 'exit' um der Hauptschleife zu signalisieren, dass sie
 * beendet werden soll.
 */

public class Game {

	static String GAME_TITLE = "Spiel Test!";
	
	Scene scene;
	
	private Screen screen;
	private KeyHandler keyhandler;
	
	Game() {
		keyhandler = new KeyHandler();
		
		screen = new Screen();
		screen.setTitle(GAME_TITLE);
		screen.addKeyListener(keyhandler);
		screen.setVisible(true);
		
		scene = new Scene_Map(this);
	}

	public void update() {
		//player_move();
		//player_animation();
		scene.update();
		keyhandler.freeze_update();
		screen.update();
	}

	public Screen getScreen() {
		return screen;
	}

	public KeyHandler getKeyHandler() {
		return keyhandler;
	}

}
