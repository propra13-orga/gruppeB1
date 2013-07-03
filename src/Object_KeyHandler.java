import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * KeyHandler.java
 * Bietet eine Schnittstelle zur Tastatur.
 * Sobald ein KeyHandler Objekt mittels addKeyListener an den Screen gebunden wird,
 * lassen sich alle Methoden des KeyHandlers aufrufen.
 * 
 * Die Konstanten KEY_XXX symbolisieren die verschiedenen Tasten und k�nnen intern
 * �ber keys[KEY_XXX] (true/false) abgefragt werden. Zus�tzlich ist noch ein (momentan
 * nur zwei Pl�tze gro�er) Puffer implementiert, f�r den Fall, dass mehrere Tasten gedr�ckt
 * werden. Ein gr��erer Puffer ist nicht sinnvoll, da es im Spiel keine Situation gibt, in
 * der auf mehr als 2 Tasten gleichzeitig reagiert werden muss. Falls doch eine dritte Taste
 * gedr�ckt wird, so wird die zuerst gedr�ckte aus dem Puffer gel�scht und neueste r�ckt an den
 * vordersten Platz im Puffer.
 * 
 * Nach au�en werden folgende Funktionen geboten:
 * 
 * int get_last()				Gibt die Taste zur�ck, welche sich an erster Stelle im
 * 								Tastaturpuffer befindet
 * 
 * boolean get_key(key)			Gibt keys[key] zur�ck. Damit l�sst sich also pr�fen, ob die
 * 								Taste 'key' gedr�ckt wird.
 * 
 * clear()						L�scht den Tastaturpuffer und setzt alle Werte im key Array
 * 								auf false. Wichtig beim Wechsel zwischen zwei Szenen, da nicht sofort
 * 								in der neuen Szene auf Eingaben aus der alten Szene reagiert
 * 								werden soll.
 * 
 * freeze(int key, int time)	Bietet die M�glichkeit, eine Taste f�r 'time' Frames zu sperren.
 */

public class Object_KeyHandler implements KeyListener {
	
	final static int NUMBER_OF_KEYS = 100;
	
	final static int NO_KEY = 0;
	final static int KEY_UP = 1;
	final static int KEY_DOWN = 2;
	final static int KEY_LEFT = 3;
	final static int KEY_RIGHT = 4;
	final static int KEY_ESCAPE = 5;
	final static int KEY_ENTER = 6;
	
	private boolean[] keys;
	private int[] frozen;
	private int first;
	private int second;
	
	Object_KeyHandler() {
		first = NO_KEY;
		second = NO_KEY;
		//Arrays sind von selbst mit 'false' und 0 initialisiert
		keys = new boolean[NUMBER_OF_KEYS];
		//'frozen' speichert f�r jede Taste einen Wert >= 0, der (falls er nicht 0 ist) in
		//jeden Frame um 1 dekrementiert wird. Falls in dieser Zeit die Taste gedr�ckt wird,
		//wird die Eingabe ganz einfach ignoriert.
		//Auf diese Weise l�sst sich die Taste f�r X Frames sperren.
		frozen = new int[NUMBER_OF_KEYS];
		first = NO_KEY;
		second = NO_KEY;
	}

	//�berschreibe Methoden von KeyListener
	
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
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (frozen[KEY_ENTER] > 0) return;
			keys[KEY_ENTER] = true;
			if (first != KEY_ENTER && second != KEY_ENTER) {
				second = first;
				first = KEY_ENTER;
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
			if (first == KEY_ESCAPE) {
				first = second;
				second = NO_KEY;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (frozen[KEY_ENTER] > 0) return;
			keys[KEY_ENTER] = true;
			if (first == KEY_ENTER) {
				first = second;
				second = NO_KEY;
			}
		}
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	//�ffentliche Methoden
	
	public void clear() {
		first = NO_KEY;
		second = NO_KEY;
		keys[KEY_UP] = false;
		keys[KEY_DOWN] = false;
		keys[KEY_LEFT] = false;
		keys[KEY_RIGHT] = false;
		keys[KEY_ESCAPE] = false;
	}

	public void freeze(int key, int time) {
		frozen[key] = time;
	}
	
	public void freezeUpdate() {
		for (int i=0; i<NUMBER_OF_KEYS; i++) {
			if (frozen[i] > 0) frozen[i]--;
		}
	}
	
	public int getLast() {
		return first;
	}
	
	public boolean getKey(int key) {
		return keys[key];
	}

}
