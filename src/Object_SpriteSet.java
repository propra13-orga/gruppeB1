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
	
	private BufferedImage set;
	
	Object_SpriteSet(String filename) {
		//Lade Tiles als Images in ein Array und speichere sie dort.
		String path = "res/charset/"+filename+".png";
		set = new BufferedImage(96, 256, BufferedImage.TYPE_INT_ARGB);
		try {
			set.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object_Screen.makeTransparent(set);
	}
	
	public BufferedImage getSprite(int direction, int animation) {
		return set.getSubimage(animation*32, (direction-1)*64, 32, 64);
	}
}
