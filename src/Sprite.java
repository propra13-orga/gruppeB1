import java.awt.Image;

/*
 * Sprite.java
 * Diese Klasse stellt eine darstellbare Spielfigur dar. Dies können Gegner, 
 * Objekte und auch der Character selbst sein.
 * Jeder Character hat für jede der Richtungen in die er schauen kann 3 Animationen.
 * Eine Standanimation und zwei Laufanimationen.
 * Seine Position wird nicht in Pixeln gespeichert sondern in dem Spielfeld, auf dem er
 * gerade steht. Die Größe eines Tiles beträgt 32 Pixel also würde einer Position von
 * (32, 32) der Position (1, 1) entsprechen.
 * Movecounter, move_distance und moving sind nur für die Laufanimation wichtig
 * 'old_x' und 'old_y' speichern die Koordinaten des characters vor einer eventuellen
 * bewegung, um zu prüfen ob er sich bewegt hat und animiert werden muss.
 */

public class Sprite {
	
	static int ANI_LEFT = 0;
	static int ANI_MIDDLE = 1;
	static int ANI_RIGHT = 2;
	
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
		direction = KeyHandler.DOWN;
		animation = ANI_MIDDLE;
		move_distance = 2;
		//Es wird davon ausgegangen, dass alle Bilder im SpriteSet die selben
		//Maße haben!
		width = spriteset.getSprite(0).getWidth(null);
		height = spriteset.getSprite(0).getHeight(null);
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
	public void save_position() {
		old_x = pos_x;
		old_y = pos_y;
	}
	public void restore_position() {
		pos_x = old_x;
		pos_y = old_y;
	}
	public SpriteSet getSpriteSet() {
		return spriteset;
	}
	public Image getImage() {
		//Rechne die Richtung und Animationsstufe in den entsprechenden
		//Index im SpriteSet Array um
		int idx = (direction-1)*3 + animation;
		return spriteset.getSprite(idx);
	}
}
