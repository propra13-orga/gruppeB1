
public class Event {
	
	static int TELEOPRT = 1;
	static int ENEMY = 2;
	
	private int pos_x;
	private int pos_y;
	private int type;
	private SpriteSet spriteset;
	
	Event(int x, int y, int t) {
		pos_x = x;
		pos_y = y;
		type = t;
		//SpriteSet("None") bedeutet einfach keine Grafik
		spriteset = new SpriteSet("None");
	}
	
	Event(int x, int y, int t, SpriteSet s) {
		pos_x = x;
		pos_y = y;
		type = t;
		spriteset = s;
	}
	
}
