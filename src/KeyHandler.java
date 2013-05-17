import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * KeyHandler.java
 * Bietet eine Schnittstelle zur Tastatur.
 * Sobald ein KeyHandler Objekt mittels addKeyListener an den Screen gebunden wird,
 * lassen sich alle Methoden des KeyHandlers aufrufen.
 * 
 */

public class KeyHandler implements KeyListener {
	
	final static int NUMBER_OF_KEYS = 100;
	
	final static int NO_KEY = 0;
	final static int KEY_UP = 1;
	final static int KEY_DOWN = 2;
	final static int KEY_LEFT = 3;
	final static int KEY_RIGHT = 4;
	final static int KEY_ESCAPE = 5;
	
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
			if (frozen[KEY_UP] > 0) return;
			keys[KEY_UP] = true;
			if (first != KEY_UP && second != KEY_UP) {
				second = first;
				first = KEY_UP;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (frozen[KEY_DOWN] > 0) return;
			keys[KEY_DOWN] = true;
			if (first != KEY_DOWN && second != KEY_DOWN) {
				second = first;
				first = KEY_DOWN;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (frozen[KEY_DOWN] > 0) return;
			keys[KEY_LEFT] = true;
			if (first != KEY_LEFT && second != KEY_LEFT) {
				second = first;
				first = KEY_LEFT;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (frozen[KEY_RIGHT] > 0) return;
			keys[KEY_RIGHT] = true;
			if (first != KEY_RIGHT && second != KEY_RIGHT) {
				second = first;
				first = KEY_RIGHT;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (frozen[KEY_ESCAPE] > 0) return;
			keys[KEY_ESCAPE] = true;
			if (first != KEY_ESCAPE && second != KEY_ESCAPE) {
				second = first;
				first = KEY_ESCAPE;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (frozen[KEY_UP] > 0) return;
			keys[KEY_UP] = false;
			if (second == KEY_UP) second = NO_KEY;
			if (first == KEY_UP) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (frozen[KEY_DOWN] > 0) return;
			keys[KEY_DOWN] = false;
			if (second == KEY_DOWN) second = NO_KEY;
			if (first == KEY_DOWN) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (frozen[KEY_LEFT] > 0) return;
			keys[KEY_LEFT] = false;
			if (second == KEY_LEFT) second = NO_KEY;
			if (first == KEY_LEFT) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (frozen[KEY_RIGHT] > 0) return;
			keys[KEY_RIGHT] = false;
			if (second == KEY_RIGHT) second = NO_KEY;
			if (first == KEY_RIGHT) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (frozen[KEY_ESCAPE] > 0) return;
			keys[KEY_ESCAPE] = true;
		}
	}
	
	public void clear() {
		first = NO_KEY;
		second = NO_KEY;
		keys[KEY_UP] = false;
		keys[KEY_DOWN] = false;
		keys[KEY_LEFT] = false;
		keys[KEY_RIGHT] = false;
		keys[KEY_ESCAPE] = false;
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
	
	public boolean get_up() { return keys[KEY_UP]; }
	public boolean get_down() { return keys[KEY_DOWN]; }
	public boolean get_left() { return keys[KEY_LEFT]; }
	public boolean get_right() { return keys[KEY_RIGHT]; }
	public boolean get_escape() {return keys[KEY_ESCAPE]; }

}
