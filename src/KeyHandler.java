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
	
	final static int NO_KEY = 0;
	final static int UP = 1;
	final static int DOWN = 2;
	final static int LEFT = 3;
	final static int RIGHT = 4;
	final static int ESCAPE = 5;
	
	private boolean key_up = false;
	private boolean key_down = false;
	private boolean key_left = false;
	private boolean key_right = false;
	private boolean key_escape = false;
	private int first;
	private int second;
	
	KeyHandler() {
		first = NO_KEY;
		second = NO_KEY;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			key_up = true;
			if (first != UP && second != UP) {
				second = first;
				first = UP;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			key_down = true;
			if (first != DOWN && second != DOWN) {
				second = first;
				first = DOWN;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			key_left = true;
			if (first != LEFT && second != LEFT) {
				second = first;
				first = LEFT;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			key_right = true;
			if (first != RIGHT && second != RIGHT) {
				second = first;
				first = RIGHT;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			key_escape = true;
			if (first != ESCAPE && second != ESCAPE) {
				second = first;
				first = ESCAPE;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			key_up = false;
			if (second == UP) second = NO_KEY;
			if (first == UP) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			key_down = false;
			if (second == DOWN) second = NO_KEY;
			if (first == DOWN) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			key_left = false;
			if (second == LEFT) second = NO_KEY;
			if (first == LEFT) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			key_right = false;
			if (second == RIGHT) second = NO_KEY;
			if (first == RIGHT) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			key_escape = true;
		}
	}
	
	public void clear() {
		first = NO_KEY;
		second = NO_KEY;
		key_up = false;
		key_down = false;
		key_left = false;
		key_right = false;
		key_escape = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	public int get_last() {
		return first;
	}
	
	public boolean get_up() { return key_up; }
	public boolean get_down() { return key_down; }
	public boolean get_left() { return key_left; }
	public boolean get_right() { return key_right; }
	public boolean get_escape() {return key_escape; }

}
