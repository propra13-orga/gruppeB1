import java.awt.Color;
import java.awt.Graphics;

public class Window_Base {

	static final Color BORDER = new Color(0,0,0);
	static final Color FILL   = new Color(100,100,100);
	
	private Game game;
	private int x;
	private int y;
	private int width;
	private int height;
	
	Window_Base(int x, int y, int width, int height, Game game) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.game = game;
	}
	
	public void update() {
		draw();
	}
	
	protected void draw() {
		Graphics g = game.getScreen().getBuffer().getGraphics();
		g.setColor(BORDER);
		g.drawRect(
				x,
				y,
				width,
				height);
		g.setColor(FILL);
		g.fillRect(
				x+1,
				y+1,
				width-1,
				height-1);
	}
	
}
