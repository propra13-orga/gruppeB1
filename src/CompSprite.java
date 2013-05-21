import java.awt.image.BufferedImage;

/*
 * Sprite.java
 * Diese Klasse speicher alle wichtigen Daten, um einen (animierten) Sprite
 * darzustellen.
 */

class CompSprite extends Component {
	
	public static final int ANIMATION_LEFT = 0;
	public static final int ANIMATION_MIDDLE = 1;
	public static final int ANIMATION_RIGHT = 2;
	
	static final int DIRECTION_UP = 1;
	static final int DIRECTION_DOWN = 2;
	static final int DIRECTION_LEFT = 3;
	static final int DIRECTION_RIGHT = 4;
	
	int direction;
	int animation;
	int old_animation;
	boolean visible;
	
	private int width;
	private int height;
	private SpriteSet spriteset;
	private int pos_x;
	private int pos_y;
	
	
	public CompSprite(Entity entity, ComponentSystem system,
			String filename, int x, int y) {
		super("sprite",entity,system);
		spriteset = new SpriteSet(filename);
		pos_x = x*Map.TILESIZE;
		pos_y = y*Map.TILESIZE;
		direction = KeyHandler.KEY_DOWN;
		animation = ANIMATION_MIDDLE;
		old_animation = ANIMATION_LEFT;
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
	
	public void setX(int x) { this.pos_x = x*Level.TILESIZE;}
	public void setY(int y) { this.pos_y = y*Level.TILESIZE;}
	public void addToX(int offset) { this.pos_x += offset; }
	public void addToY(int offset) { this.pos_y += offset; }
	public void setDirection (int d) { this.direction = d; }

	
	public int getX() { return pos_x; }
	public int getY() {	return pos_y; }
	public int getTileX() {	return pos_x / Level.TILESIZE; }
	public int getTileY() {	return pos_y / Level.TILESIZE + 1; }
	public int getWidth() {	return width; }
	public int getHeight() { return height; }

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
	
	public void setAniMiddle() { this.animation = ANIMATION_MIDDLE; }
	public void setAniLeft() { this.animation = ANIMATION_LEFT; }
	public void setAniRight() { this.animation = ANIMATION_RIGHT; }
	
	public void setOldAnimation(int animation) { this.old_animation = animation; }
	
	public int getOldAnimation() { return this.old_animation; }
}
