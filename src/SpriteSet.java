import java.awt.Image;
import java.awt.Toolkit;

/*
 * SpriteSet.java
 * Ähnlich wie TileSet.java
 * Für genauere Erklärung siehe dort...
 */

public class SpriteSet {
	
	private Image[] sprites;
	
	SpriteSet(String filename) {
		//Lade Tiles als Images in ein Array und speichere sie dort.
		String path;
		sprites = new Image[12];
		//Einzelne Animationen in korrekter Reihenfolge ins Image Array laden
		for (int x=0; x<12; x++) {
			path = "res/charset/"+filename+"/"+(int)Math.ceil((double)(x+1)/3)+"_"+x%3+".png";
			sprites[x] = Toolkit.getDefaultToolkit().createImage(path);
		}
	}
	
	public Image getSprite(int idx) {
		return sprites[idx];
	}
}
