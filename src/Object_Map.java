import java.io.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

/*
 * Map.java
 * Die Map ist ein Datencontainer, der von der Klasse Scene_Map benutzt wird, um das
 * Spielfeld zu verwalten und anzuzeigen.
 * Eine Map setzt sich aus einem Tileset (siehe TileSet.java) und ihrer Struktur
 * (lowmap und highmap) zusammen. Da das Tileset f�r jedes Tile speichert, ob das Tile begehbar
 * ist oder nicht, bietet die Map eine vereinfachte Schnittstelle, die pr�ft, ob das abgefragte
 * Feld auf der Map begehbar ist (wird in Scene_Map beim Steuern der Spielfigur genutzt).
 * 
 * Die Methode getLowMapImage() zeichnet ein BufferedImage, welches den Teil der Karte enth�lt,
 * welcher unterhalb der Sprites angezeigt wird.
 */

public class Object_Map {
	
	static final int TILESIZE = 32;
	
	//Wichtig zur korrekten Anzeige des Spielers auf der Karte (muss beim Scrolling
	//immer in der Mitte des Bildschirms bleiben, da sich dann nur die Karte bewegt)
	boolean scrolling;
	int layer;
	
	private Object_TileSet			tileset;
	private int 			width;
	private int 			height;
	private int[][][] 		maplayer;
	
	Object_Map(String mapname) {
		String filename = "res/maps/"+mapname+".txt";
		try {
			readData(filename);
		} catch (IOException e) {
			// Datei besch�digt!
			e.printStackTrace();
		}
	}
	
	public boolean isPassable(int x, int y) {
		//Es wird in jedem Maplayer nachgesehen, ob auf der angegebenen
		//Position ein Tile liegt, welches begehbar ist, oder nicht
		//Sprites werden hier nicht beachtet!
		if (x < 0 || y < 0 || x >= width || y >= height) return false;
		for (int l=0; l<layer; l++) {
			if (!tileset.isPassable(getTileID(l, x,y))) return false;
		}
		return true;
	}
	
	public void drawTiles(BufferedImage screen, int level) {

		//Zeichnet auf das �bergebene BufferedImage alle Tiles mit dem angegebenen
		//Level (also BELOW, SAME_LEVEL, oder ABOVE)
		Image current_tile;
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				for (int l=0; l<layer; l++) {
					int tile_id = getTileID(l, x, y);
					if (tile_id == 99) continue;
					if (tileset.getPassability(tile_id) == level) {
						current_tile = tileset.getMapTile(maplayer[l][y][x]);
						screen.getGraphics().drawImage(current_tile,
								x*TILESIZE,
								y*TILESIZE,
								null);
					}
				}
			}
		}
	}
	
	public Object_TileSet getTileset() {
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
		tileset = new Object_TileSet(br.readLine());
		width = Integer.parseInt(br.readLine());
		height = Integer.parseInt(br.readLine());
		layer = Integer.parseInt(br.readLine());
		maplayer = new int[layer][height][width];
		String[] line;
		int[] i_line = new int[width];
		for (int l=0; l<layer; l++) {
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
					maplayer[l][y][x] = i_line[x];
				}
			}
			br.readLine();
		}
		fr.close();
		br.close();
	}
	
	private int getTileID(int layer, int x, int y) {
		//Gibt die ID eines Tiles im jeweiligen Layer des Tilesets zur�ck
		//Wird von isPassable genutzt
		return maplayer[layer][y][x];
	}
	
}
