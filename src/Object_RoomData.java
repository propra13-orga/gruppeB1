import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Object_RoomData {

	int[][][] room;
	List<LinkedHashMap> entityData;
	
	public Object_RoomData() {
		this.entityData = new LinkedList<LinkedHashMap>();
	}
	
	public void setRoom(int[][][] room) { this.room = room; }
	public void addEntityData(LinkedHashMap data) { this.entityData.add(data); }
	
	public int[][][] getRoom() { return this.room; }
	public List<LinkedHashMap> getEntityData() { return this.entityData; }
}
