import java.io.*;
import java.awt.image.BufferedImage;

/*
 * Map.java
 * Diese Klasse ist eigentlich nur ein Container, der ein TileSet und ein
 * Datenfeld enthält, welches die Map repräsentiert.
 * Das Datenfeld ist einfach ein zweidimensionales int Array. Die Zahlen darin
 * entsprechen den IDs des Tiles an der jeweiligen Position im Screen. Siehe dazu
 * im Konstruktor von Game nach. Dort wird das Datenfeld einer Testmap initialisiert.
 * Wichtig ist die Funktion Map.getLowMapImage()
 * Hier wird die komplette Map als Bild gerendert und dann zurückgegeben (siehe Screen.update)
 */

public class Map {
	
	static int TILESIZE = 32;
	
	boolean scrolling;
	
	private Scene scene;
	private TileSet			tileset;
	private int 			width;
	private int 			height;
	private int[][] 		lowmap;
	//private int[][] 		highmap;
	
	Map(String mapname, Scene s) {
		scene = s;
		String filename = "res/maps/"+mapname+".txt";
		try {
			readData(filename);
		} catch (IOException e) {
			// Datei beschädigt!
			e.printStackTrace();
		}
	}
	
	public TileSet getTileset() {
		return tileset;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	public boolean isPassable(int x, int y) {
		if (y >= height) return false;
		if (lowmap[y][x] == 0  ||  lowmap[y][x] == 4) {
			return true;
		}
		return false;
	}
	
	public BufferedImage getLowMapImage() {
		//'gen_counter' zählt runter, wie oft das Mapbild generiert wurde. Aus irgendeinem
		//Grund muss dies nämlich mindestens zwei mal geschehen, damit das Bild vollständig
		//ist und als fertiges Bild im Speicher gehalten werden kann
		BufferedImage b = new BufferedImage(TILESIZE*width,
				TILESIZE*height,
				BufferedImage.TYPE_INT_ARGB);
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				//später getFloor(), etc. durch getTile(ID) ersetzen um beliebige Tiles zu
				//bekommen! Spätestens dann auch subImages oder so benutzen
				b.getGraphics().drawImage(tileset.getTile(0,lowmap[y][x]),
						x*TILESIZE,
						y*TILESIZE,
						scene.game.getScreen());
			}
		}
		return b;
	}
	
	private void readData(String filename) throws IOException {
		//Erstellt Mapobjekt aus Datei
		//!!! NOCH IN ARBEIT! BIS JETZT MIESE UMSETZUNG
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		tileset = new TileSet(br.readLine());
		width = Integer.parseInt(br.readLine());
		height = Integer.parseInt(br.readLine());
		//hier wird erstmal geskippt, muss später noch geändert werden
		br.readLine();
		br.readLine();
		lowmap = new int[height][width];
		String[] line;
		int[] i_line = new int[width];
		for (int y=0; y<height; y++) {
			line = br.readLine().split(" ");
			int i=0;
			for (String s : line) {
				if (s.length()==0) continue;
				if (i == width) break;
				i_line[i] = Integer.parseInt(s);
				i++;
			}
			for (int x=0; x<width; x++) {
				lowmap[y][x] = i_line[x];
			}
		}
		br.close();
	}
}
