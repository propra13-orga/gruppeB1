import java.awt.Color;

public class SubScene_WindowBase extends Abstract_SubScene{

	static final Color BORDER = new Color(0,21,72);
	static final Color FILL   = new Color(0,77,148);

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	SubScene_WindowBase(int x, int y, int width, int height, Object_Game game) {
		super(game);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	@Override
	public void updateData() {}

	@Override
	public void updateScreen() {
		this.screen.setColor(BORDER);
		this.screen.drawRect(
				x,
				y,
				width,
				height);
		this.screen.drawRect(
				x+1,
				y+1,
				width-2,
				height-2);
		this.screen.setColor(FILL);
		this.screen.fillRect(
				x+2,
				y+2,
				width-3,
				height-3);
	}
	
}
