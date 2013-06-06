import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Tile {
	
	public static int[] blank = {-1,-1};
	
	// Zu Funktion zu Probieren, mussen wir durch num[][][] mit schleife Tile zu machen.
	public static int[] terrain_1 = {15,0};	
	public static int[] terrain_2 = {16,0};
	public static int[] terrain_3 = {2,0};
	
	public static int size = 32;
	public static BufferedImage terrain, layer,items;
	
	public Tile(){
		try{ 
			Tile.layer = ImageIO.read(new File("res/bg.png"));
			
			//nur layer kann fonktionieren, terrain und item dient zu Erweiterung in der Zukunft
			
			Tile.terrain = ImageIO.read(new File("res/bg.png"));
			Tile.items = ImageIO.read(new File("res/bg.png"));
		}catch(Exception e){
			System.out.println("Error Loading >>> Tile");
		}
		
	}

}
