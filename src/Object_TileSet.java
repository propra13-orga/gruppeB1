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

public class Object_TileSet {
	
	static final int BELOW_SPRITE = 0;
	static final int SAME_LEVEL_AS_SPRITE = 1;
	static final int ABOVE_SPRITE   = 2;
	
	private BufferedImage set;
	int[][] passable;
	
	Object_TileSet(String setname) throws IOException {
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
		Object_Screen.makeTransparent(set);
		
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
	
	public BufferedImage getMapTile(int id) {
		//Liefert das Tile aus dem Low- bzw. Highlayer mit der ID 'id'
		int x = id % 10;
		int y = id / 10;
		return set.getSubimage(x*32, y*32, 32, 32);
	}
	
	public boolean isPassable(int id) {
		//Prüft, ob das Tile mit der ID 'id' im Layer 'layer' begehbar ist
		//oder nicht
		if (passable[id/10][id%10] == SAME_LEVEL_AS_SPRITE) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public int getPassability(int id) {
		return passable[id/10][id%10];
	}
}
