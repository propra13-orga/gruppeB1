import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Window_Base extends Abstract_Update{

	static final Color BORDER = new Color(0,21,72);
	static final Color FILL   = new Color(0,77,148);

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected BufferedImage box;
	protected BufferedImage set;
	
	Window_Base(int x, int y, int width, int height, Object_Game game) {
		super(game);
		if (width==0) width=1;
		if (height==0) height=1;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		//Windowset laden
		String path = "res/windowset/window-1.png";
		this.set = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);
		try {
			this.set.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object_Screen.makeTransparent(this.set);
		
		this.drawBox();
	}

	@Override
	public void updateData() {}

	@Override
	public void updateScreen() {
		this.screen.drawImage(this.box, this.x, this.y, null);
	}
	
	public void drawBox() {
		int i;
		int j;
		this.box = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		//BG zeichnen
		for (j=32; j<=this.height-32; j+=Object_Map.TILESIZE) {
			for (i=32; i<=this.width-32; i+=Object_Map.TILESIZE) {
				System.out.println("draw");
				this.box.getGraphics().drawImage(
						this.set.getSubimage(32, 32, 32, 32),
						i,
						j,
						null
						);
			}
		}
		for (i=32; i<=this.width-32; i+=32) {
			this.box.getGraphics().drawImage(this.set.getSubimage(32, 0, 32, 32),i,0,null); //Top Leiste
			this.box.getGraphics().drawImage(this.set.getSubimage(32, 64, 32, 32),i,this.height-32,null); //Top Leiste
		}
		for (i=32; i<=this.height-32; i+=32) {
			this.box.getGraphics().drawImage(this.set.getSubimage(0, 32, 32, 32),0,i,null); //Top Leiste
			this.box.getGraphics().drawImage(this.set.getSubimage(64, 32, 32, 32),this.width-32,i,null); //Top Leiste
		}
		//Rahmen zeichnen
				this.box.getGraphics().drawImage(this.set.getSubimage(0, 0, 32, 32),0,0,null); //TopLeft Rahmen
				this.box.getGraphics().drawImage(this.set.getSubimage(64, 0, 32, 32),this.width-32,0,null); //TopRight Rahmen
				this.box.getGraphics().drawImage(this.set.getSubimage(0, 64, 32, 32),0,this.height-32,null); //BottomLeft Rahmen
				this.box.getGraphics().drawImage(this.set.getSubimage(64, 64, 32, 32),this.width-32,this.height-32,null); //BottomRight Rahmen
	}
	
	public void __updateScreen() {
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
