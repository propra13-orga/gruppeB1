import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * TileSet.java
 * Läd das Tileset, sowie die Begehbarkeitstabelle und bietet
 * entsprechende Gettermethoden (siehe Map.java und Scene_Map.java)
 */

public class TileSet {
	
	static final int LAYER_LOW  = 0;
	static final int LAYER_HIGH = 1;
	
	static final int PASSABLE   = 0;
	static final int UNPASSABLE = 1;
	
	private BufferedImage set;
	int[][] passable;
	
	TileSet(String setname) throws IOException {
		//Jedes Tileset ist in einem Ordner gespeichert, der dessen Namen trägt
		//Darin muss sich eine 320x320 Pixel große Datei 'set.png' befinden, sowie
		//eine Textdatei 'passable.txt', die aus 10 Zeilen besteht, die jeweils
		//10 Einsen und Nullen beinhalten und angeben, ob über die im Tileset
		//entpsrechende Kachel gelaufen werden darf.
		set = new BufferedImage(320,320,BufferedImage.TYPE_INT_ARGB);
		String path = "res/tileset/"+setname+"/";
		try {
			set.getGraphics().drawImage(ImageIO.read( new File(path+"set.png")),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Hintergrundfarbe entfernen
		Screen.makeTransparent(set);
		
		//Lade 'passable.txt'
		FileReader fr = new FileReader(path+"passable.txt");
		BufferedReader br = new BufferedReader(fr);
		passable = new int[10][10];
		String[] line;
		for (int y=0; y<10; y++) {
			line = br.readLine().split(" ");
			for (int x=0; x<10; x++) {
				passable[y][x] = Integer.parseInt(line[x]);
			}
		}
		br.close();
		fr.close();
	}
	
	public Image getMapTile(int layer, int id) {
		//Liefert das Tile aus dem Low- bzw. Highlayer mit der ID 'id'
		int x = layer*160 + (id%5);
		int y = id / 5;
		return set.getSubimage(x*32, y*32, 32, 32);
	}
	
	public boolean isPassable(int layer, int id) {
		//Prüft, ob das Tile mit der ID 'id' im Layer 'layer' begehbar ist
		//oder nicht
		if (passable[id/5][id%5] == PASSABLE) {
			return true;
		}
		else {
			return false;
		}
	}
}
