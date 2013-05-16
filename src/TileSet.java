import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/*
 * TileSet.java
 * Hier werden alle Tiles als Images gespeichert, die zu einem "Set" zusammen gehören.
 * das kann eine Höhle sein, ein Haus oder ein Wald, je nachdem, wie die Map aussehen soll,
 * die dieses TileSet nutzt.
 * Momentan hat jedes TileSet noch eine maximale Zahl von MAX_TILES Tiles, das ist aber nur
 * so, weil es ausreichend ist und kann beliebig geändert werden.
 */

public class TileSet {
	
	static int MAX_TILES = 40;
	
	private String dirname;
	private Image[] tiles;
	
	TileSet(String dir) {
		//Im angegbenen Tileset Verzeichnis wird erwartet, dass alle Tiles den Namen
		//'0.png', '1.png', ..., '21.png', ... haben
		//WICHTIG! Es müssen sich nicht tatsächlich MAX_TILES Bilder im verzeichnis befinden.
		//Wenn ein Bild nicht gefunden wurde (weil es z.B. nicht vorhanden ist), dann wird es einfach
		//mit null gewertet und beim Anzeigen übersprungen
		dirname = dir;
		tiles = new Image[MAX_TILES]; //maximal 10 Tiles!!
		String path;
		for (int x=0; x<MAX_TILES; x++) {
			path = dir + "/" + x + ".png";
			tiles[x] = Toolkit.getDefaultToolkit().createImage(path);
		}
	}
	
	public Image getTile(int idx) {
		return tiles[idx];
	}
}
