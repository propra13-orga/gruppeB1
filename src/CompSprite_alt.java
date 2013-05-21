import java.awt.Image;
import java.awt.image.BufferedImage;

/*
 * Sprite.java
 * Diese Klasse speicher alle wichtigen Daten, um einen (animierten) Sprite
 * darzustellen.
 */

class CompSprite_alt extends Component {
	
	static final int ANIMATION_LEFT = 0;
	static final int ANIMATION_MIDDLE = 1;
	static final int ANIMATION_RIGHT = 2;
	
	static final int DIRECTION_UP = 1;
	static final int DIRECTION_DOWN = 2;
	static final int DIRECTION_LEFT = 3;
	static final int DIRECTION_RIGHT = 4;
	
	int direction;
	int animation;
	int old_animation;
	int move_distance; //Sollte zwischen 1 und 2 liegen
	boolean moving;
	boolean visible;
	
	private int width;
	private int height;
	private SpriteSet spriteset;
	private int pos_x;
	private int pos_y;
	private int movecounter;
	
	public CompSprite_alt(Entity entity, ComponentSystem system,
			String filename, int x, int y) {
		super("sprite",entity,system);
		spriteset = new SpriteSet(filename);
		pos_x = x*Map.TILESIZE;
		pos_y = y*Map.TILESIZE;
		direction = KeyHandler.KEY_DOWN;
		animation = ANIMATION_MIDDLE;
		old_animation = ANIMATION_LEFT;
		move_distance = 2;
		//Momentan noch konstant, evtl sp�ter Variabel. Dann w�ren Charsets
		//verschiedener Gr��e m�glich.
		width = 32;
		height = 64;
		moving = false;
		this.visible = true;
	}
	
	public CompSprite_alt(Entity entity, ComponentSystem system, String filename) {
		this(entity,system,filename,0,0);
		this.visible = false;
	}
	
	public void setX(int x) { this.pos_x = x*Level.TILESIZE;}
	public void setY(int y) { this.pos_y = y*Level.TILESIZE;}
	public void setDirection (int d) { this.direction = d; }
	public void setMoving() { this.moving = true; }
	public void unsetMoving() { this.moving = false; }
	
	public int getX() { return pos_x; }
	public int getY() {	return pos_y; }
	public int getTileX() {	return pos_x / Level.TILESIZE; }
	public int getTileY() {	return pos_y / Level.TILESIZE + 1; }
	public int getWidth() {	return width; }
	public int getHeight() { return height; }
	public int getMovecounter() { return this.movecounter; }
	public SpriteSet getSpriteSet() { return spriteset;	}
	public BufferedImage getImage() {
		//Gibt die Spritegrafik entsprechend der Blickrichtung und
		//Animationsstufe zur�ck
		return spriteset.getSprite(direction, animation);
	}
	
	public BufferedImage getLowerHalf() {
		return getImage().getSubimage(0, 32, 32, 32);
	}
	
	public BufferedImage getUpperHalf() {
		return getImage().getSubimage(0, 0, 32, 32);
	}
	
	public void makeStep() {
		if (moving) return;
		movecounter = 0;
		moving = true;
	}
	
	public void update() {
		if (moving) {
			
			switch (direction) {
			case DIRECTION_UP:
				pos_y -= move_distance;
				break;
			case DIRECTION_DOWN:
				pos_y += move_distance;
				break;
			case DIRECTION_LEFT:
				pos_x -= move_distance;
				break;
			case DIRECTION_RIGHT:
				pos_x += move_distance;
				break;
			}
			
			if (movecounter < Level.TILESIZE-10) {
				if (old_animation == ANIMATION_LEFT) animation = ANIMATION_RIGHT;
				else animation = ANIMATION_LEFT;
			}
			else {
				animation = ANIMATION_MIDDLE;
			}
			
			movecounter += move_distance;
			
			if (movecounter >= Level.TILESIZE) {
				if (old_animation == ANIMATION_LEFT) old_animation = ANIMATION_RIGHT;
				else old_animation = ANIMATION_LEFT;
				movecounter = 0                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ;
				moving = false;
			}
		}
	}
}
