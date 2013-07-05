import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Object_Map extends Abstract_Update {
	
	public static final int	TILESIZE = 32;
	
	protected Hashtable<String, String>		properties;
	
	private int								width;
	private int								height;
	private int[][][]						layer;
	private Object_TileSet					tileset;

	private BufferedImage					background;
	private BufferedImage					below;
	private BufferedImage					samelevel;
	private BufferedImage					above;
	

	Object_Map(Object_Game game, String filename) {
		super(game);
		Document doc;
		try {
			doc = loadMap(filename);
		} catch (Exception e) {
			//this.game.exitOnError("XML Datei konnte nicht gelesen werden");
			return;
		}
		
		this.properties = new Hashtable<String, String>();
		retrieveProperties(doc);
		this.width		= getMapWidth(doc);
		this.height		= getMapHeight(doc);
		this.tileset	= loadTileSet(doc);
		this.layer		= setupLayer(doc);
		background		= loadBackground();
		
		below			= new BufferedImage(width*TILESIZE, height*TILESIZE, BufferedImage.TYPE_INT_ARGB);
		samelevel		= new BufferedImage(width*TILESIZE, height*TILESIZE, BufferedImage.TYPE_INT_ARGB);
		above			= new BufferedImage(width*TILESIZE, height*TILESIZE, BufferedImage.TYPE_INT_ARGB);
		
		refreshMap();
	}
	
	public Map<String,String> getProperties() { return this.properties; }
	
	public boolean hasProperty(String property) {
		if (this.properties == null) return false;
		if (this.properties.containsKey(property)) return true;
		return false;
	}
	
	public BufferedImage getLowTiles() {
		return below;
	}
	
	public BufferedImage getSameLevelTiles() {
		return samelevel;
	}
	
	public BufferedImage getAboveTiles() {
		return above;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public BufferedImage getBackground() {
		return this.background;
	}
	
	public int getLayerNumber() {
		return this.layer.length;
	}
	
	public boolean isPassable(int x, int y) {
		if (x<0 || y<0 || x>=this.width || y>=this.height) {
			return false;
		}
		for (int l=0; l<getLayerNumber(); l++) {
			if (this.tileset.isPassable(this.getTileID(l, x, y)) == false) {
				return false;
			}
		}
		return true;
	}
	
	public void refreshMap() {
		drawTiles(below, 0);
		drawTiles(samelevel, 1);
		drawTiles(above, 2);
	}
	
	/*
	 * drawTiles ist ne miese Methode :D
	 * Die Map teilt sich in drei Schichten ein:
	 * 
	 * 	1 -	Die Schicht, die unter den Sprites angezeigt wird (BELOW)
	 * 
	 * 	2 - Die Schicht die unter den Sprites, aber �ber BELOW angezeigt wird (SAME_LEVEL)
	 * 		Hier werden Mapelemente angesiedelt, welche den Sprites 'im Weg stehen' koennen
	 * 
	 *  3 -	Die Schicht, die ueber den Sprites angezeigt wird (ABOVE)
	 *  
	 *  Jedes Tile ist eindeutig einer dieser Schichten zugetiled (...:D)
	 *  
	 *  drawTiles uebernimmt nun als Parameter ein BufferedImage und die ID der darauf
	 *  zu zeichnenden Schicht.
	 */
	
	private void drawTiles(BufferedImage b, int level) {
		
		BufferedImage	current_tile;
		int				tile_id;
		int				tile_passability;
		int				x;
		int				y;
		int				l;
		int				max_l;
		int				min_l;
		int				min_passability;
		boolean			draw;
		
		for (y=0; y<height; y++) {
			for (x=0; x<width; x++) {
				
				draw				= true;
				max_l				= layer.length-1;
				min_l				= 0;
				min_passability		= level;
				
				//Bestimmte min_l, max_l, min_passability und draw
				for (l=layer.length-1; l>=0; l--) {
					tile_id = getTileID(l, x, y);
					if (tile_id == -1) {
						continue;
					}
					tile_passability = tileset.getPassability(tile_id);
					if (tile_passability < level) {
						draw = false;
						break;
					}
					if (tile_passability == level) {
						min_passability = tile_passability;
						max_l = l;
						//Bestimmte min_l
						for (min_l=max_l; min_l>=0; min_l--) {
							if (getTileID(min_l,x,y) == -1) {
								continue;
							}
							if (tileset.getPassability(getTileID(min_l,x,y))<tile_passability) {
								break;
							}
						}
						if (min_l == -1) {
							min_l=0;
						}
						break;
					}
				}
				if (l == -1) {
					draw = false;
				}
				
				//Zeichne alle relevanten Tiles
				if (draw) {
					for (l=min_l; l<=max_l; l++) {
						tile_id = getTileID(l, x, y);
						if (tile_id == -1) {
							continue;
						}
						if (tileset.getPassability(tile_id) >= min_passability) {
							current_tile = tileset.getMapTile(this.layer[l][y][x]);
							b.getGraphics().drawImage(current_tile,x*Object_Map.TILESIZE,y*Object_Map.TILESIZE,null);
						}
					}
				}
			}
		}
	}
	
	private int getTileID(int l, int x, int y) {
		//Gibt die ID des Tiles an der Position (l, x, y) auf der Map an. l bezeichnet dabei den
		//entsprechenden Layer
		return this.layer[l][y][x];
	}
	
	protected Document loadMap(String filename) throws Exception {
		System.out.println("Lade: "+"res/maps/"+filename+".tmx");
		File map = new File("res/maps/"+filename+".tmx");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(map);
	}
	
	private int getMapWidth(Document doc) {
		return Integer.parseInt(((Element) (doc.getDocumentElement())).getAttribute("width"));
	}
	
	private int getMapHeight(Document doc) {
		return Integer.parseInt(((Element) (doc.getDocumentElement())).getAttribute("height"));
	}
	
	private Object_TileSet loadTileSet(Document doc) {
		NodeList properties = doc.getElementsByTagName("tileset");
		for (int k=0; k<properties.getLength(); k++) {
			Element e = (Element) properties.item(k);
			System.out.println("Name: "+e.getAttribute("name"));
		}
		String filename = ((Element) properties.item(0)).getAttribute("name");
		return new Object_TileSet(this.game, filename);
	}
	
	private int[][][] setupLayer(Document doc) {
		NodeList layers = doc.getElementsByTagName("layer");
		int[][][] tmp_layer = new int[layers.getLength()][this.height][this.width];
		
		NodeList tiles;
		Node current;
		for (int i=0; i<layers.getLength(); i++) {
			current = layers.item(i);
			tiles = ( (Element) current).getElementsByTagName("tile");
			System.out.println("Insgesamt "+tiles.getLength()+" Tiles");
			for (int j=0; j<tiles.getLength(); j++) {
				String gid = ((Element)tiles.item(j)).getAttribute("gid");
				tmp_layer[i][j/this.width][j%this.width] = Integer.parseInt(gid)-1;
			}
		}
		return tmp_layer;
	}
	
	private void retrieveProperties(Document doc) {
		String name;
		String value;
		if (doc.getElementsByTagName("properties").getLength() < 1) return;
		NodeList properties = ((Element) doc.getElementsByTagName("properties").item(0)).getElementsByTagName("property");
		System.out.println("LAAAANGE: "+properties.getLength());
		for (int i=0; i<properties.getLength(); i++) {
			Element property = (Element) properties.item(i);
			name	= property.getAttribute("name");
			value	= property.getAttribute("value");
			this.properties.put(name, value);
		}
	}
	
	private BufferedImage loadBackground() {
		if (!this.properties.containsKey("background")) {
			return null;
		}
		BufferedImage background = new BufferedImage(width*TILESIZE, height*TILESIZE, BufferedImage.TYPE_INT_ARGB);
		String path = "res/background/"+this.properties.get("background")+".png";
		try {
			BufferedImage img = ImageIO.read( new File(path));
			background = new BufferedImage(
					img.getWidth(),
					img.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			background.getGraphics().drawImage(img, 0, 0, null);
		} catch (IOException e) {
			this.game.exitOnError("Tileset '"+properties.get("background")+"' konnte nicht geladen werden");
			return null;
		}
		return background;
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
