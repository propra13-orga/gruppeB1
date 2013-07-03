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

public class Object_TileSet extends Abstract_Update {
	
	static final int			BELOW_SPRITE			= 0;
	static final int			SAME_LEVEL_AS_SPRITE	= 1;
	static final int			ABOVE_SPRITE			= 2;
	
	private int					tile_width;
	private int					tile_height;
	private int[][]				passable;
	private BufferedImage		set;
	
	Object_TileSet(Object_Game game, String filename) {
		super(game);
		
		//Lade BufferedImage
		String path = "res/tileset/"+filename+".png";
		try {
			BufferedImage img = ImageIO.read( new File(path));
			set = new BufferedImage(
					img.getWidth(),
					img.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			set.getGraphics().drawImage(img, 0, 0, null);
		} catch (IOException e) {
			this.game.exitOnError("Tileset '"+filename+"' konnte nicht geladen werden");
			return;
		}

		//Hintergrundfarbe entfernen
		Object_Screen.makeTransparent(set);
		
		//Lade 'passable.txt'
		this.tile_width		= set.getWidth()  / Object_Map.TILESIZE;
		this.tile_height	= set.getHeight() / Object_Map.TILESIZE;

		try {
			FileReader fr = new FileReader("res/tileset/passable/"+filename+".txt");
			BufferedReader br = new BufferedReader(fr);
			passable = new int[tile_height][tile_width];
			String[] line;
			for (int y=0; y<tile_height; y++) {
				line = br.readLine().split(" ");
				for (int x=0; x<tile_width; x++) {
					passable[y][x] = Integer.parseInt(line[x]);
				}
			}
			br.close();
			fr.close();
		}
		catch (IOException e) {
			//this.game.exitOnError("Datei 'passable/"+filename+".txt' konnte nicht geladen werden");
			return;
		}
	}
	
	public BufferedImage getMapTile(int id) {
		int x = id % tile_width;
		int y = id / tile_width;
		return set.getSubimage(
				x*Object_Map.TILESIZE,
				y*Object_Map.TILESIZE,
				Object_Map.TILESIZE,
				Object_Map.TILESIZE);
	}
	
	public boolean isPassable(int id) {
		if (id == -1) return true;
		if (passable[id/this.tile_width][id%this.tile_width] == SAME_LEVEL_AS_SPRITE) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public int getPassability(int id) {
		return passable[id/this.tile_width][id%this.tile_width];
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		
	}
}
