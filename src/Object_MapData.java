


import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;


/*hier habe ich Slick2D und lwjgl verwendete
und schon ins lib folder speichert
*/

public class Object_MapData {
	//1.[] ist layer
	//2.[] ist height
	//3.[] ist width
	//wert von num[][][] ist gid
	public int width = 25,height = 20;
	public int[][][] num = new int[3][20][25];
	public int[][] bg = new int[width][height];
	
	//Den Name von Map nenne ich als Level_1,dann Level_2 oder irgendwas
	//public String Dpath = "res/tiled_map";
	
	public String path;
	public TiledMap map = null;
	
	public Object_MapData(String path){
		
		this.path = path;//Address von Map.tmx auszulessen.
		
		System.out.println(path);
		
		try{
			map = new TiledMap(path, false);//Durch die Path von Map.tmx wird instanz von tiledmap erzeugt.
		}catch(SlickException e){
			System.out.println("Error Loading");
		}
		
		loadWorld();
				
	}
	
	public void loadWorld(){
		int layer1 = map.getLayerIndex("layer1");
		int layer2 = map.getLayerIndex("layer2");
		int layer3 = map.getLayerIndex("layer3");		
		
		/*hier ist Probe Code von Vorbearbeitung von Object Layer
        aber jetzt benuzen wir *.csv file, damit object zu erkennen.
	    
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
				
				num[0][y][x] = (map.getTileId(x, y, layer1));//das Satz gibt GID von jedem Stuck aus.
				//System.out.println(num[0][y][x]);
				
				num[1][y][x] = (map.getTileId(x, y, layer2));//das Satz gibt GID von jedem Stuck aus.
				//System.out.println(num[1][y][x]);
							
				num[2][y][x] = (map.getTileId(x, y, layer3));//das Satz gibt GID von jedem Stuck aus.
				//System.out.println(num[2][y][x]);
 		
			}
	     }
      }
}
	
	
