import java.util.Date;

/*
 * 
 * Main.java
 * Die Klasse Main ist die Klasse, die das gesamte Programm startet. Dafür kann sie eigentlich
 * nicht viel.
 * Sie erstellt ein Game Objekt und führt dann den gameloop solange aus, wie
 * game.scene nicht auf null zeigt.
 * Dadurch kann das Spiel sich selbst ganz bequem beenden indem man in einer Scene einfach
 * 
 * 				game.scene = null;
 * 
 * schreibt. Eventuelle Aufräumarbeiten (Dateien schließen, etc.) können dann noch hier
 * ausgeführt werden, bevor das Programm sich komplett beendet.
 * 
 */

public class Main {
	
	static int SLEEP_TIME = 6;
	
	public static void main(String[] args) {
		
		Game game = new Game();
		long time;
		long timeDiff;
		Date date = new Date();
		
		//Gameloop
		while (game.scene != null) {
			
			time = date.getTime();
			
			try {
				game.update();
			}
			catch (Exception e) {
				//Irgendeine unerwartete Exception
				e.printStackTrace();
			}
			
			timeDiff = date.getTime() - time;
			
			if (SLEEP_TIME-timeDiff > 0) {
				try {
					Thread.sleep(SLEEP_TIME-timeDiff);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		//Programm per System.exit(0) beenden, damit auch die
		//Swing-Anwendungen geschlossen werden!
		System.exit(0);
	}
}