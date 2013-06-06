

import java.awt.Graphics;
import java.awt.Rectangle;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/*
 * hier habe ich Slick2D und lwjgl verwendete
 * und schon ins lib folder speichert
 * */
public class Level {
	//1.[] ist layer
	//2.[] ist height
	//3.[] ist width
	//wert von num[][][] ist gid
	public int width = 25,height = 20;
	public int[][][] num = new int[3][20][25];
	public Background[][] bg = new Background[width][height];
	
	//Den Name von Map nenne ich als Level_1,dann Level_2 oder irgendwas
	public final String Dpath = "res/world/Level_";
	public String path = Dpath;
	
	public TiledMap map = null;
	
	public Level(int id){
		
		path = Dpath + Integer.toString(id) + ".tmx";

		System.out.println(path);
		

		try{
			map = new TiledMap(path, false);
		}catch(SlickException e){
			System.out.println("Error Loading");
		}
		
		for(int x = 0; x < bg.length; x++){
			for(int y = 0;y < bg[0].length;y++){
				bg[x][y] = new Background(new Rectangle(x *Tile.size, y*Tile.size,Tile.size,Tile.size),Tile.blank);
			}
		}
		
		loadWorld();
	}
	
	public void loadWorld(){
		int layer1 = map.getLayerIndex("layer1");
		int layer2 = map.getLayerIndex("layer2");
		int layer3 = map.getLayerIndex("layer3");		
		
		/*hier ist Probe Code von Vorbearbeitung von Object Layer
	    noch nicht fertig, aber fast
	    
		String name = map.getObjectName(0, 1);
		String type = map.getObjectType(0, 1);
		int px = map.getObjectX(0, 1);
		int py = map.getObjectY(0, 1);
		int objectcount = map.getObjectCount(0);
		String property = map.getObjectProperty(0, 1, "map", "N/A");
			
		System.out.println(name);	
		System.out.println(type);
		System.out.println(property);
		System.out.println(objectcount);
		System.out.println(px);
		System.out.println(py);
		*/
		
		
		for(int x = 0; x < bg.length; x++){
			for(int y = 0;y < bg[0].length;y++){
				
				num[0][y][x] = (map.getTileId(x, y, layer1));
				System.out.println(num[0][y][x]);
				
				num[1][y][x] = (map.getTileId(x, y, layer2));
				System.out.println(num[1][y][x]);
							
				num[2][y][x] = (map.getTileId(x, y, layer3));
				System.out.println(num[2][y][x]);
				
				
				
				//die 50, 51 und 227 ist GID von "bg"
				//alle GID davon wird mit file "GID" dargestellt
				if (map.getTileId(x, y, layer1) == 50){
					bg[x][y].id = Tile.terrain_1;
					
				}
				if (map.getTileId(x, y, layer1) == 51){
					bg[x][y].id = Tile.terrain_2;

				}
				if (map.getTileId(x, y, layer1) == 227){
					bg[x][y].id = Tile.terrain_3;

				}				
			}
	     }
      }
	
	// die folgende Code sind nicht wichtig, diese dient zu Probierung
	public void tick(){
		
	}
	
	// die folgende Code sind nicht wichtig, diese dient zu Probierung

	public void render(Graphics g, int camX, int camY, int renX, int renY){
		for(int x = (camX/Tile.size); x < (camX/Tile.size)+renX; x++){
			for(int y = (camY / Tile.size); y < (camY / Tile.size) + renY; y++){
				if(x >= 0 && y >=0 && x < width && y < height){
					bg[x][y].render(g);
				}
			}
		}
	}
}
	
	
