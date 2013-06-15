
/*
 * Stellt Methoden zu verschiedenen Animationen des BattleSprites bereit
 */

class Object_BattleSprite extends Abstract_Update {
	
	public static final int PLAYER = 0;
	public static final int ENEMY = 1;
	
	//Attribute die spaeter auch gelesen werden koenen
	private int x;
	private int y;
	private int animation_type;
	private int position;
	private int battletype;
	private Object_BattleSpriteSet spriteset;
	private Object_BattleActor actor;
	private boolean moving = false;
	
	//Private Attribute, die nur fuer die Animation gebraucht werden
	private int move_delay = 8;
	private int move_tick;
	private int move_dx;
	private int move_dy;
	private int move_dx_delay;
	private int move_dx_tick;
	private int move_dy_delay;
	private int move_dy_tick;
	
	private int animation;
	private int animation_delta;
	private int animation_delay;
	private int animation_tick;
	
	private int dest_x;
	private int dest_y;
	
	Object_BattleSprite (String filename, int position, int animation_delay,
			int battletype, Object_Game game, Object_BattleActor actor) {
		super(game);
		this.spriteset = new Object_BattleSpriteSet(filename);
		this.position = position;
		this.animation_delay = animation_delay;
		this.battletype = battletype;
		this.actor = actor;

		if (this.battletype == PLAYER) {
			this.x = Scene_BattleSystem.PLAYER_POSITIONS[position-1][0];
			this.y = Scene_BattleSystem.PLAYER_POSITIONS[position-1][1];
		}
		else {
			this.x = Scene_BattleSystem.ENEMY_POSITIONS[position-1][0];
			this.y = Scene_BattleSystem.ENEMY_POSITIONS[position-1][1];
		}
		
		this.animation_type = Object_BattleSpriteSet.ANIMATION_STAND;
		this.animation = 0;
		this.animation_delta = 1;
		this.animation_tick = 0;
		this.move_tick = 0;
	}
	
	Object_BattleSprite(String filename, Object_Game game) {
		super(game);
		this.spriteset = new Object_BattleSpriteSet(filename);
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public void updateData() {
		switch (this.animation_type) {
		case Object_BattleSpriteSet.ANIMATION_FRONT_ATTACK:
			//Normaler Angriff, Sprite bewegt sich zum Targetsprite
			if (this.x < this.dest_x) {
				this.x++;
			}
			else if (this.x > this.dest_x) {
				this.x--;
			}
			else {
				this.animation_type = Object_BattleSpriteSet.ANIMATION_HIT;
			}
			break;
		case Object_BattleSpriteSet.ANIMATION_BACK_ATTACK:
			if (this.x < this.dest_x) {
				this.x++;
			}
			else if (this.x > this.dest_x) {
				this.x--;
			}
			else {
				this.animation_type = Object_BattleSpriteSet.ANIMATION_STAND;
			}
			this.actor.battle_system.EXIT_UPDATE = true;
			break;
		case Object_BattleSpriteSet.ANIMATION_BACK:
			break;
		case Object_BattleSpriteSet.ANIMATION_DAMAGE:
			break;
		case Object_BattleSpriteSet.ANIMATION_DEAD:
			break;
		case Object_BattleSpriteSet.ANIMATION_HIT:
			this.moveToPosition(this.battletype, this.position);
			this.animation_type = Object_BattleSpriteSet.ANIMATION_BACK_ATTACK;
			break;
		case Object_BattleSpriteSet.ANIMATION_STAND:
			break;
		case Object_BattleSpriteSet.ANIMATION_USE:
			break;
		}
		this.animation_tick++;
		if (this.animation_tick == this.animation_delay) {
			this.animation_tick = 0;
			this.animation += this.animation_delta;
			if (this.animation != 1) {
				this.animation_delta *= -1;
			}
		}
	}
	
	public void updateScreen() {
		this.screen.drawImage(this.spriteset.getSprite(this.animation_type, this.animation), this.x, this.y, null);
	}
	
	public void setAnimationDelay(int delay) {
		this.animation_delay = delay;
	}
	
	//Dieser Code wird spaeter in updateData ausgelagert, fuer verschiedene Animationen wird dann nur
	//attack(), defend(), useSkill(), ... aufgerufen
	public void moveToPosition(int battletype, int position) {
		if (battletype == PLAYER) {
			this.dest_x = Scene_BattleSystem.PLAYER_POSITIONS[position-1][0];
			this.dest_y = Scene_BattleSystem.PLAYER_POSITIONS[position-1][1];
		}
		else {
			this.dest_x = Scene_BattleSystem.ENEMY_POSITIONS[position-1][0];
			this.dest_y = Scene_BattleSystem.ENEMY_POSITIONS[position-1][1];
		}
		this.moving = true;
	}
	
	public void attack(Object_BattleSprite target) {
		this.animation_type = Object_BattleSpriteSet.ANIMATION_FRONT_ATTACK;
		this.moveToPosition(target.battletype, target.position);
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
