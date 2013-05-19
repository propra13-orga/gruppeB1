import java.awt.Image;

/*
 * Sprite.java
 * Diese Klasse speicher alle wichtigen Daten, um einen (animierten) Sprite
 * darzustellen.
 */

class CompSprite extends Component {
	
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
	boolean visible;
	
	private int width;
	private int height;
	private int old_x;
	private int old_y;
	private SpriteSet spriteset;
	
	public CompSprite(Entity entity, ComponentSystem system,
			String filename, int x, int y) {
		super("sprite",entity,system);
		spriteset = new SpriteSet(filename);
		pos_x = x;
		pos_y = y;
		old_x = pos_x;
		old_y = pos_y;
		direction = KeyHandler.KEY_DOWN;
		animation = ANIMATION_MIDDLE;
		move_distance = 1;
		//Momentan noch konstant, evtl sp�ter Variabel. Dann w�ren Charsets
		//verschiedener Gr��e m�glich.
		width = 32;
		height = 64;
		this.visible = true;
	}
	
	public CompSprite(Entity entity, ComponentSystem system, String filename) {
		this(entity,system,filename,0,0);
		this.visible = false;
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
	public Image getImage(int direction) {
		//Gibt die Spritegrafik entsprechend der Blickrichtung und
		//Animationsstufe zur�ck
		return spriteset.getSprite(direction, animation);
	}
}
