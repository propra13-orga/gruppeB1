import java.io.*;
import java.awt.image.BufferedImage;

/*
 * Map.java
 * Die Map ist ein Datencontainer, der von der Klasse Scene_Map benutzt wird, um das
 * Spielfeld zu verwalten und anzuzeigen.
 * Eine Map setzt sich aus einem Tileset (siehe TileSet.java) und ihrer Struktur
 * (lowmap und highmap) zusammen. Da das Tileset für jedes Tile speichert, ob das Tile begehbar
 * ist oder nicht, bietet die Map eine vereinfachte Schnittstelle, die prüft, ob das abgefragte
 * Feld auf der Map begehbar ist (wird in Scene_Map beim Steuern der Spielfigur genutzt).
 * 
 * Die Methode getLowMapImage() zeichnet ein BufferedImage, welches den Teil der Karte enthält,
 * welcher unterhalb der Sprites angezeigt wird.
 */

public class Map {
	
	static int TILESIZE = 32;
	
	//Wichtig zur korrekten Anzeige des Spielers auf der Karte (muss beim Scrolling
	//immer in der Mitte des Bildschirms bleiben, da sich dann nur die Karte bewegt)
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
	
	public boolean isPassable(int x, int y) {
		//Später werden hier auch noch Kollisionen mit beweglichen Objekten
		//berücksichtigt
		//Weil die einzelnen Abfragen so lang sind, werden sie als booleans zwischen-
		//gespeichert und am Ende verglichen
		boolean a = tileset.isPassable(TileSet.LAYER_LOW, getTileID(TileSet.LAYER_LOW,x,y));
		boolean b = tileset.isPassable(TileSet.LAYER_HIGH, getTileID(TileSet.LAYER_HIGH,x,y));
		return a && b;
	}
	
	public BufferedImage getLowMapImage() {
		//Erstellt die gesamte Lowmap, also den Teil der Karte, die unter den Sprites
		//angezeigt wird
		
		//Neues BufferedImage erstellen
		BufferedImage b = new BufferedImage(TILESIZE*width,
				TILESIZE*height,
				BufferedImage.TYPE_INT_ARGB);
		//Die Karte mit Tiles füllen
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				b.getGraphics().drawImage(tileset.getMapTile(0,lowmap[y][x]),
						x*TILESIZE,
						y*TILESIZE,
						scene.game.getScreen());
			}
		}
		//Karte ist fertig und kann angezeigt werden!
		return b;
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
	
	private int getTileID(int layer, int x, int y) {
		//Gibt die ID eines Tiles im jeweiligen Layer des Tilesets zurück
		//Wird von isPassable genutzt
		if (layer == TileSet.LAYER_LOW){
			return lowmap[y][x];
		}
		else {
			return 0;
		}
	}
	
}
