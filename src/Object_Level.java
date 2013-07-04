import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Object_Level extends Object_Map implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8032513502291500180L;
	List<Entity> entities;
	List<Map<String,String>> entityData;
	int ID;

	public Object_Level(Object_Game game, String mapname, int ID) {
		super(game, mapname);
		this.entities = new LinkedList<Entity>();
		this.entityData = new LinkedList<Map<String,String>>();
		this.ID = ID;
		Document doc;
		try {
			doc = this.loadMap(mapname);
		}
		catch (Exception e) {
			return;
		}
		this.readEntities(doc);
	}
	
	// Getters
	
	public List<Entity> getEntities() { return this.entities; }
	public List<Map<String,String>> getEntityData() { return this.entityData; }
	public int getID() { return this.ID; }
	
	// Setters
	
	public void addEntity(Entity entity) { this.entities.add(entity); }
	public void addEntities(List<Entity> entities) { this.entities.addAll(entities); }
	public void removeEntity(Entity entity) {
		if (this.entities.contains(entity)) this.entities.remove(entity);
	}
	
	public void init() {
		for (Entity entity : this.entities) {
			entity.init();
		}
		if (this.properties != null && this.properties.containsKey("music")) {
			this.soundmanager.playMidi(this.properties.get("music"));
		}
	}
	
	public void deinit() {
		for (Entity entity : this.entities) {
			entity.getManager().deregister(entity);
		}
	}
	
	/*
	 * Privates
	 */
	
	private void readEntities(Document doc) {
		String name, type, x, y, propname, propvalue;
		NodeList objectgroup = doc.getElementsByTagName("objectgroup");
		System.out.println("LAENGE: "+objectgroup.getLength());
		if (objectgroup.getLength() > 0) {
			NodeList objects = doc.getElementsByTagName("object");
			for (int i=0;i<objects.getLength();i++) {
				Element object = (Element) objects.item(i);
				name = object.getAttribute("name");
				type = object.getAttribute("type");
				x = Integer.toString(Integer.parseInt(object.getAttribute("x"))/TILESIZE);
				y = Integer.toString(Integer.parseInt(object.getAttribute("y"))/TILESIZE);
				Map<String,String> objectData = new HashMap<String,String>();
				objectData.put("x", x);
				objectData.put("y", y);
				objectData.put("name", name);
				objectData.put("entityType", type);
				
				NodeList properties = object.getElementsByTagName("property");
				for (int j=0;j<properties.getLength();j++) {
					Element property = (Element) properties.item(j);
					propname = property.getAttribute("name");
					propvalue = property.getAttribute("value");
					objectData.put(propname, propvalue);
					System.out.printf("%s : %s\n",propname,propvalue);						
				}
				this.entityData.add(objectData);
			}
			
		}
	}
}
