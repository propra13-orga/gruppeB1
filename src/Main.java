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

	public static void main(String[] args) {

		Object_Game game = new Object_Game();
		
		int FRAMES_PER_SECOND = 100;
		int SKIP_TIME = 1000 / FRAMES_PER_SECOND;
		long start_time;
		long time_diff;
		
		start_time = System.currentTimeMillis();
		
		while (game.getScene() != null) {
			time_diff = System.currentTimeMillis() - start_time;
			if (time_diff >= SKIP_TIME) {
				game.update();
				start_time = System.currentTimeMillis();
			}
			game.display();
		}
		
		System.exit(0);
	}
}
