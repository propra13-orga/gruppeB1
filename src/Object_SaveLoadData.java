import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

//public final class Static_SaveLoad {
//	public static final String PATH = "res/save/";
//
//	public static void serializeLevel(Scene_Level scene, Object_Level level) {
//		try {
//			String fpath = String.format("%slevel_%d.ser",PATH,level.getID());
//			FileOutputStream fileOut = new FileOutputStream(fpath);
//			ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			out.writeObject(level);
//			out.close();
//			fileOut.close();	
//		}
//		catch (IOException e) { e.printStackTrace(); }
//	}
//	
//	public static List<Object_Level> deserializeLevels() {
//		List<Object_Level> levels = new LinkedList<Object_Level>();
//		File folder = new File(PATH);
//		File[] files = folder.listFiles();
//		for (File file : files) {
//			String fname = file.toString();
//			if (fname.contains("level")) {
//				try {
//					FileInputStream fileIn = new FileInputStream(fname);
//					ObjectInputStream in = new ObjectInputStream(fileIn);
//					levels.add((Object_Level) in.readObject());
//					in.close();
//					fileIn.close();
//				}
//				catch (Exception e) { e.printStackTrace(); }
//			}
//		}
//		return levels;
//	}
//	
//	public static void serializePlayerData(Scene_Level scene, Entity player) {
//		try {
//			String fpath = String.format("%splayer.ser", PATH);
//			FileOutputStream fileOut = new FileOutputStream(fpath);
//			ObjectOutputStream out = new ObjectOutputStream(fileOut);
//			out.writeObject(player);
//			out.close();
//			fileOut.close();
//		}
//		catch (IOException e) { e.printStackTrace(); }
//	}
//	
//	public static Entity deserializePlayerData(Scene_Level scene) {
//		//List<Entity> data = new LinkedList<Entity>();
//		Entity player = null;
//		String fname = PATH+"player.ser";
//		try {
//			FileInputStream fileIn = new FileInputStream(fname);
//			ObjectInputStream in = new ObjectInputStream(fileIn);
//			//data.add((Entity) in.readObject());
//			player = (Entity) in.readObject();
//			in.close();
//			fileIn.close();
//		}
//		catch (Exception e) { e.printStackTrace(); }
//		return player;
//	}
//}

class Object_SaveLoadData implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5376363191835331279L;

	public static final String PATH = "res/save/";
	
	private Hashtable<Integer,Object_Room> rooms;
	private int currentRoom;
	private Entity player;
	
	public Object_SaveLoadData(Hashtable<Integer,Object_Room> rooms, int currentRoom, Entity player) {
		this.rooms = rooms;
		this.currentRoom = currentRoom;
		this.player = player;
	}
	
	public Hashtable<Integer,Object_Room> getRooms() { return this.rooms; }
	public int getCurrentRoom() { return this.currentRoom; }
	public Entity getPlayer() { return this.player; }
	
	public static void serialize(Object_SaveLoadData sld) {
		try {
			String fname = PATH+"savedata.res";
			FileOutputStream fileOut = new FileOutputStream(fname);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(sld);
			out.close();
			fileOut.close();
		}
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public static Object_SaveLoadData deserialize(Scene_Level scene) {
		Object_SaveLoadData sld = null;
		try {
			String fname = PATH+"savedata.res";
			FileInputStream fileIn = new FileInputStream(fname);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			sld = (Object_SaveLoadData) in.readObject();
			in.close();
			fileIn.close();
		}
		catch (Exception e) { e.printStackTrace(); }
		
		for (Object_Room level : sld.getRooms().values()) {
			for (Entity entity : level.getEntities()) {
				entity.setManager(scene.getEntityManager());
				for (Abstract_Component comp : entity.components.values()) {
					comp.setSystem(scene.getSystemByType(comp.getType()));
				}		
			}
		}
		
		sld.getPlayer().setManager(scene.getEntityManager());
		for (Abstract_Component comp : sld.getPlayer().components.values()) {
			comp.setSystem(scene.getSystemByType(comp.getType()));
		}
		
		return sld;
	}
}