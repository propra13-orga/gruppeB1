import java.awt.image.BufferedImage;

/**
 * 
 * Diese Komponente speichert alle wichtigen Daten, um einen (animierten) Sprite
 * auf der Karte darzustellen.
 * 
 * @author Alexander Schäfer, Victor Persien
 * 
 */

class Component_Sprite extends Abstract_Component {
	
	private static final long serialVersionUID = 617556010116827829L;
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
	
	private String filename;
	private int width;
	private int height;
	transient private Object_SpriteSet spriteset;
	private int pos_x;
	private int pos_y;
	
	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "sprite".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 * @param filename		Dateiname (ohne Endung) des Sprites.
	 * @param x				X-Koordinate.
	 * @param y				Y-Koordinate.
	 */
	public Component_Sprite(Entity entity, System_Component system,
			String filename, int x, int y) {
		super("sprite",entity,system);
		this.filename = filename;
		spriteset = new Object_SpriteSet(filename);
		pos_x = x*Object_Map.TILESIZE;
		pos_y = y*Object_Map.TILESIZE;
		direction = Object_KeyHandler.KEY_DOWN;
		animation = ANIMATION_MIDDLE;
		old_animation = ANIMATION_LEFT;
		width = 32;
		height = 64;
		this.visible = true;
	}
	
	public Component_Sprite(Entity entity, System_Component system, String filename) {
		this(entity,system,filename,0,0);
		this.visible = false;
	}
	
	public Component_Sprite(Component_Sprite compSprite) {
		super(compSprite);
		this.filename = compSprite.filename;
		this.spriteset = new Object_SpriteSet(compSprite.filename);
		this.pos_x = compSprite.pos_x;
		this.pos_y = compSprite.pos_y;
		this.direction = compSprite.direction;
		this.animation = compSprite.animation;
		this.old_animation = compSprite.old_animation;
		this.width = compSprite.width;
		this.height = compSprite.height;
		this.visible = compSprite.visible;
	}
	
	public Component_Sprite(Abstract_Component comp, Entity entity, System_Component system) {
		super(comp.getType(),entity,system);
		Component_Sprite compSprite = (Component_Sprite) comp;
		this.filename = compSprite.filename;
		this.spriteset = new Object_SpriteSet(compSprite.filename);
		this.pos_x = compSprite.pos_x;
		this.pos_y = compSprite.pos_y;
		this.direction = compSprite.direction;
		this.animation = compSprite.animation;
		this.old_animation = compSprite.old_animation;
		this.width = compSprite.width;
		this.height = compSprite.height;
		this.visible = compSprite.visible;		
	}
	
	public void setX(int x) { this.pos_x = x*Object_Room.TILESIZE;}
	public void setY(int y) { this.pos_y = y*Object_Room.TILESIZE;}
	public void addToX(int offset) { this.pos_x += offset; }
	public void addToY(int offset) { this.pos_y += offset; }
	public void setDirection (int d) { this.direction = d; }

	
	public int getX() { return pos_x; }
	public int getY() {	return pos_y; }
	public int getTileX() {	return pos_x / Object_Room.TILESIZE; }
	public int getTileY() {	return pos_y / Object_Room.TILESIZE + 1; }
	public int getWidth() {	return width; }
	public int getHeight() { return height; }

	public Object_SpriteSet getSpriteSet() { return spriteset;	}
	public BufferedImage getImage() {
		//Gibt die Spritegrafik entsprechend der Blickrichtung und
		//Animationsstufe zur�ck
		if (spriteset == null) this.spriteset = new Object_SpriteSet(this.filename);
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
