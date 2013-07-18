import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * SpriteSet.java
 * �hnlich wie TileSet.java
 * F�r genauere Erkl�rung siehe dort...
 */

public class Object_SpriteSet {
	
	private BufferedImage	set;
	private int				width;
	private int				height;
	
	Object_SpriteSet(String filename) {
		//Lade Tiles als Images in ein Array und speichere sie dort.
		String path = "res/charset/"+filename+".png";
		set = null;
		BufferedImage temp;
		try {
			temp = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		if (temp == null) return;
		set = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_ARGB);
		set.getGraphics().drawImage(temp, 0, 0, null);
		System.out.println("Transparent machen");
		Object_Screen.makeTransparent(set);
		width = set.getWidth() / 144;
		height = set.getHeight() / 256;
		if (width == 0 || height == 0) {
			return;
		}
	}
	
	public BufferedImage getSprite(int direction, int animation) {
		return set.getSubimage(
				animation*48,
				(direction-1)*64,
				48,
				64);
	}
	
	public BufferedImage getSprite(int idx, int direction, int animation) {
		int start_x = (idx % width) * 144;
		int start_y = (idx / width) * 256;
		return set.getSubimage(
				start_x + animation*48,
				start_y + (direction-1)*64,
				48,
				64);
	}
}
