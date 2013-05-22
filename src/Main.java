<<<<<<< HEAD
=======

>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
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

	static int SLEEP_TIME = 10;

	public static void main(String[] args) {

		Game game = new Game();
		long time;
		long timeDiff;
<<<<<<< HEAD

		// Gameloop
		while (game.scene != null) {

			time = System.currentTimeMillis();
			game.update();

=======
		
		//Gameloop
		while (game.scene != null) {
			
			time = System.currentTimeMillis();
			game.update();
			
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
<<<<<<< HEAD

=======
			
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
			timeDiff = System.currentTimeMillis() - time;
			if (timeDiff < SLEEP_TIME) {
				try {
					Thread.sleep(SLEEP_TIME - timeDiff);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
<<<<<<< HEAD
			System.out.println(Math.round(1000.0 / timeDiff));
=======
			System.out.println(Math.round(1000.0/timeDiff));
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
		}

		// Programm per System.exit(0) beenden, damit auch die
		// Swing-Anwendungen geschlossen werden!
		System.exit(0);
	}
}