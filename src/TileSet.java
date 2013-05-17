import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * TileSet.java
 * Hier werden alle Tiles als Images gespeichert, die zu einem "Set" zusammen gehören.
 * das kann eine Höhle sein, ein Haus oder ein Wald, je nachdem, wie die Map aussehen soll,
 * die dieses TileSet nutzt.
 * Momentan hat jedes TileSet noch eine maximale Zahl von MAX_TILES Tiles, das ist aber nur
 * so, weil es ausreichend ist und kann beliebig geändert werden.
 */

public class TileSet {
	
	static final int LAYER_LOW  = 0;
	static final int LAYER_HIGH = 1;
	
	private BufferedImage set;
	
	TileSet(String setname) {
		//Im angegbenen Tileset Verzeichnis wird erwartet, dass alle Tiles den Namen
		//'0.png', '1.png', ..., '21.png', ... haben
		//WICHTIG! Es müssen sich nicht tatsächlich MAX_TILES Bilder im verzeichnis befinden.
		//Wenn ein Bild nicht gefunden wurde (weil es z.B. nicht vorhanden ist), dann wird es einfach
		//mit null gewertet und beim Anzeigen übersprungen
		set = new BufferedImage(320,320,BufferedImage.TYPE_INT_ARGB);
		String path = "res/tileset/"+setname+".png";
		try {
			set.getGraphics().drawImage(ImageIO.read( new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Screen.makeTransparent(set);
	}
	
	public Image getTile(int layer, int idx) {
		int x = layer*160 + (idx%5);
		int y = idx / 5;
		return set.getSubimage(x*32, y*32, 32, 32);
	}
}
