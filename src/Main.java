/*
 * 
 * Main.java
 * 
 */

public class Main {
	
	static int SLEEP_TIME = 6;
	
	public static void main(String[] args) {
		
		Game game = new Game();
		
		while (game.scene != null) {
			try {
				game.update();
			}
			catch (Exception e) {
				//Irgendeine unerwartete Exception
				e.printStackTrace();
			}
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Programm per System.exit(0) beenden, damit auch die
		//Swing-Anwendungen geschlossen werden!
		System.exit(0);
	}
}