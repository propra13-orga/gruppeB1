import java.awt.Image;
import java.lang.Comparable;

/*
 * Sprite.java
 * Diese Klasse speicher alle wichtigen Daten, um einen (animierten) Sprite
 * darzustellen.
 */

public class Sprite implements Comparable<Sprite> {
	
	static int ANIMATION_LEFT = 0;
	static int ANIMATION_MIDDLE = 1;
	static int ANIMATION_RIGHT = 2;
	
	int pos_x;
	int pos_y;
	int direction;
	int animation;
	int old_animation;
	int movecounter;
	int move_distance; //Sollte zwischen 1 und 2 liegen
	boolean moving;
	
	private int width;
	private int height;
	private int old_x;
	private int old_y;
	private SpriteSet spriteset;
	
	Sprite (String filename, int x, int y) {
		spriteset = new SpriteSet(filename);
		pos_x = x;
		pos_y = y;
		old_x = pos_x;
		old_y = pos_y;
		direction = KeyHandler.KEY_DOWN;
		animation = ANIMATION_MIDDLE;
		move_distance = 1;
		//Momentan noch konstant, evtl später Variabel. Dann wären Charsets
		//verschiedener Größe möglich.
		width = 32;
		height = 64;
	}
	
	public void save_position() {
		old_x = pos_x;
		old_y = pos_y;
	}
	
	public void restore_position() {
		pos_x = old_x;
		pos_y = old_y;
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int get_old_x() {
		return old_x;
	}
	public int get_old_y() {
		return old_y;
	}
	public SpriteSet getSpriteSet() {
		return spriteset;
	}
	public Image getImage() {
		//Gibt die Spritegrafik entsprechend der Blickrichtung und
		//Animationsstufe zurück
		return spriteset.getSprite(direction, animation);
	}

	@Override
	public int compareTo(Sprite other) {
		if (pos_y > other.pos_y) {
			return 1;
		}
		if (pos_y < other.pos_y) {
			return -1;
		}
		return 0;
	}
}
