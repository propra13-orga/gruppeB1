import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Object_BattleSpriteSet {
	
	public final static int ANIMATION_STAND			= 0;
	public final static int ANIMATION_DEAD			= 1;
	public final static int ANIMATION_FRONT_ATTACK	= 2;
	public final static int ANIMATION_BACK_ATTACK	= 3;
	public final static int ANIMATION_HIT			= 4;
	public final static int ANIMATION_BACK			= 5;
	public final static int ANIMATION_DAMAGE		= 6;
	public final static int ANIMATION_USE			= 7;
	
	private BufferedImage set;
	
	Object_BattleSpriteSet(String filename) {
		//Lade Tiles als Images in ein Array und speichere sie dort.
		String path = "res/battlecharset/"+filename+".png";
		set = new BufferedImage(288, 672, BufferedImage.TYPE_INT_ARGB);
		try {
			set.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object_Screen.makeTransparent(set);
	}
	
	public BufferedImage getSprite(int animation, int animation_counter) {
		return set.getSubimage((animation_counter)*96, animation*96, 96, 96);
	}
}
