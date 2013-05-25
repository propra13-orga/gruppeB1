/*
 *
 * Main.java
 * Die Klasse Main ist die Klasse, die das gesamte Programm startet. Dafür kann sie eigentlich
 * nicht viel.
 * Sie erstellt ein Game Objekt und führt dann den gameloop solange aus, wie
 * game.scene nicht auf null zeigt.
 * Dadurch kann das Spiel sich selbst ganz bequem beenden indem man in einer Scene einfach
 *
 * game.scene = null;
 *
 * schreibt. Eventuelle Aufräumarbeiten (Dateien schließen, etc.) können dann noch hier
 * ausgeführt werden, bevor das Programm sich komplett beendet.
 *
 */

public class Main {

	static int SLEEP_TIME = 6;

	public static void main(String[] args) {

		Object_Game game = new Object_Game();
		long time;
		long timeDiff;

		
		//Gameloop
		while (game.getScene() != null) {
			
			time = System.currentTimeMillis();
			game.update();
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			timeDiff = System.currentTimeMillis() - time;
			if (timeDiff < SLEEP_TIME) {
				try {
					Thread.sleep(SLEEP_TIME - timeDiff);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			//System.out.println(Math.round(1000.0 / timeDiff));

		}

		// Programm per System.exit(0) beenden, damit auch die
		// Swing-Anwendungen geschlossen werden!
		System.exit(0);
	}
}
