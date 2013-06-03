
public class Object_BattleSprite extends Abstract_Update {
	
	public static final int[][] PLAYER_POSITIONS = {
		{480,210},
		{530,300},
		{515,380}
	};
	
	public static final int[][] ENEMY_POSITIONS = {
		{50,200},
		{20, 290},
		{60, 380}
	};
	
	public int x;
	public int y;
	public int action;
	public boolean busy;
	public BattleSide side;
	
	private int animation;
	private int ani_delta;
	private int move_delay;
	private int move_tick;
	private int ani_delay = 16;
	private int ani_tick;
	private int dest_x;
	private int dest_y;
	private Object_BattleSpriteSet spriteset;
	
	Object_BattleSprite (String filename, int position, int delay, BattleSide side, Object_Game game) {
		super(game);
		this.spriteset = new Object_BattleSpriteSet(filename);
		this.side = side;
		if (this.side == BattleSide.PLAYER) {
			this.x = PLAYER_POSITIONS[position-1][0];
			this.y = PLAYER_POSITIONS[position-1][1];
		}
		else {
			this.x = ENEMY_POSITIONS[position-1][0];
			this.y = ENEMY_POSITIONS[position-1][1];
		}
		this.action = Object_BattleSpriteSet.ANIMATION_STAND;
		this.busy = false;
		this.animation = 1;
		this.ani_delta = 1;
		this.move_delay = delay;
		this.move_tick = 0;
	}
	
	public void updateData() {
		switch (this.action) {
			//Hier noch einfuegen!!!
		}
		this.ani_tick++;
		if (this.ani_tick == this.ani_delay) {
			this.ani_tick = 0;
			this.animation += this.ani_delta;
			if (this.animation != 2) {
				this.ani_delta *= -1;
			}
		}
	}
	
	public void updateScreen() {
		this.screen.drawImage(this.spriteset.getSprite(this.action, this.animation), this.x, this.y, null);
	}
	
	public void setAnimationDelay(int delay) {
		this.ani_delay = delay;
	}
	
	public void moveToPosition(BattleSide side, int position) {
		if (side == BattleSide.PLAYER) {
			this.dest_x = PLAYER_POSITIONS[position][0];
			this.dest_y = PLAYER_POSITIONS[position][1];
		}
		else {
			this.dest_x = ENEMY_POSITIONS[position][0];
			this.dest_y = ENEMY_POSITIONS[position][1];
		}
		//moving = TRUE!
	}
}
