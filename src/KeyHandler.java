import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * KeyHandler.java
 * Implementiert einfach einen KeyListener. Diese Klasse wurde allein stehend
 * aufgebaut, um einen möglichst einfachen Zugriff zu erlauben. Da Tastenabfragen
 * und alles was dazu gehört wird hier geregelt, für alle anderen Klassen reicht
 * es, KeyHandler.get_[KEY]() aufzurufen, um zu fragen ob [KEY] gedrückt wird.
 * Um die zuletzt gedrückte Taste zu erhalten, kann man get_last() aufrufen.
 * Siehe dazu z.B. Game.move_player(), dort wird die Methode genutzt, um diagonales
 * Laufen zu verhindern
 */

public class KeyHandler implements KeyListener {
	
	final static int NUMBER_OF_KEYS = 100;
	
	final static int NO_KEY = 0;
	final static int UP = 1;
	final static int DOWN = 2;
	final static int LEFT = 3;
	final static int RIGHT = 4;
	final static int ESCAPE = 5;
	
	private boolean[] keys;
	private int[] frozen;
	private int first;
	private int second;
	
	KeyHandler() {
		first = NO_KEY;
		second = NO_KEY;
		//Arrays sind von selbst mit 'false' und 0 initialisiert
		keys = new boolean[NUMBER_OF_KEYS];
		frozen = new int[NUMBER_OF_KEYS];
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (frozen[UP] > 0) return;
			keys[UP] = true;
			if (first != UP && second != UP) {
				second = first;
				first = UP;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (frozen[DOWN] > 0) return;
			keys[DOWN] = true;
			if (first != DOWN && second != DOWN) {
				second = first;
				first = DOWN;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (frozen[DOWN] > 0) return;
			keys[LEFT] = true;
			if (first != LEFT && second != LEFT) {
				second = first;
				first = LEFT;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (frozen[RIGHT] > 0) return;
			keys[RIGHT] = true;
			if (first != RIGHT && second != RIGHT) {
				second = first;
				first = RIGHT;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (frozen[ESCAPE] > 0) return;
			keys[ESCAPE] = true;
			if (first != ESCAPE && second != ESCAPE) {
				second = first;
				first = ESCAPE;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (frozen[UP] > 0) return;
			keys[UP] = false;
			if (second == UP) second = NO_KEY;
			if (first == UP) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (frozen[DOWN] > 0) return;
			keys[DOWN] = false;
			if (second == DOWN) second = NO_KEY;
			if (first == DOWN) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (frozen[LEFT] > 0) return;
			keys[LEFT] = false;
			if (second == LEFT) second = NO_KEY;
			if (first == LEFT) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (frozen[RIGHT] > 0) return;
			keys[RIGHT] = false;
			if (second == RIGHT) second = NO_KEY;
			if (first == RIGHT) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (frozen[ESCAPE] > 0) return;
			keys[ESCAPE] = true;
		}
	}
	
	public void clear() {
		first = NO_KEY;
		second = NO_KEY;
		keys[UP] = false;
		keys[DOWN] = false;
		keys[LEFT] = false;
		keys[RIGHT] = false;
		keys[ESCAPE] = false;
	}
	
	//freeze bietet die Möglichkeit, eine Taste, für eine bestimmte Anzahl von
	//Frames zu sperren
	public void freeze(int key, int time) {
		frozen[key] = time;
	}
	
	public void freeze_update() {
		for (int i=0; i<NUMBER_OF_KEYS; i++) {
			if (frozen[i] > 0) frozen[i]--;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public int get_last() {
		return first;
	}
	
	public boolean get_up() { return keys[UP]; }
	public boolean get_down() { return keys[DOWN]; }
	public boolean get_left() { return keys[LEFT]; }
	public boolean get_right() { return keys[RIGHT]; }
	public boolean get_escape() {return keys[ESCAPE]; }

}
