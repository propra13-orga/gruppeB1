import java.awt.Color;
import java.awt.Graphics;

public class Window_Base {

	static final Color BORDER = new Color(0,21,72);
	static final Color FILL   = new Color(0,77,148);
	
	protected Game game;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
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
		g.drawRect(
				x+1,
				y+1,
				width-2,
				height-2);
		g.setColor(FILL);
		g.fillRect(
				x+2,
				y+2,
				width-3,
				height-3);
	}
	
}
