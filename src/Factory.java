import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Factory.java
 * 
 * Mit der Factory soll es möglich sein, Spielobjekte und Level zu erzeugen.
 * 
 */


public class Factory {
	Scene_Level scene;
	Object_DBReader db;
	
	public Factory(Scene_Level scene) {
		this.scene = scene;
		this.db = new Object_DBReader();
		}
	
	
	public Object_DBReader getDB() { return this.db; }
	
	
	public Entity build(Map<String,String> entityData) {
		String entityType = entityData.get("entityType");
		String name = entityData.remove("name");
		
		Entity entity = new Entity(name,entityType,this.scene.getEntityManager());
		
		Map<String,String> data = db.getProperties(entityType);
		
		data.put("x",entityData.get("x"));
		data.put("y",entityData.get("y"));
		data.put("name", name);
		
		if (entityData != null) {
			for (String attr : entityData.keySet()) {
				data.put(attr, entityData.get(attr));
			}
		}
		
		/*
		 * Component_Battle
		 */
		Hashtable<String,String> dataCompBattle = filterHashtable(data,"prop_\\w+");
		if (dataCompBattle.size() > 0) {
			String battleSprite = null;
			HashMap<String,Integer> properties = new HashMap<String,Integer>();
			if (data.containsKey("sprite_battle")) {
				battleSprite = data.get("sprite_battle");
			}
			for (String attr : dataCompBattle.keySet()) {
				String prop = attr;
				String prop_current = attr + "_current";
				int value = Integer.parseInt(dataCompBattle.get(attr));
				properties.put(prop, value);
				properties.put(prop_current, value);
			}
			new Component_Battle(entity,scene.getSystemInteraction(),
					properties,battleSprite);
		}
		
		/*
		 * Component_Item
		 */
		Hashtable<String,String> filteredEffects = filterHashtable(data,"dmg|dmg_\\w+|armor|armor_\\w+");
		Hashtable<String,String> filteredRestrictions = filterHashtable(data,"");
		Hashtable<String,String> filteredProperties = filterHashtable(data,"slot_\\w+|type_\\w+|value");
		if (!filteredEffects.isEmpty() || 
				!filteredRestrictions.isEmpty() ||
				!filteredProperties.isEmpty()) {
			String item_name = null;
			String item_description = null;
			if (data.containsKey("item_name")) item_name = data.get("item_name");
			if (data.containsKey("item_description")) item_name = data.get("item_description");
			
			List<String> restrictions = new LinkedList<String>();
			restrictions.addAll(filteredRestrictions.keySet());
			List<String> properties = new LinkedList<String>();
			properties.addAll(filteredProperties.keySet());
			
			Hashtable<String,Integer> effects = new Hashtable<String,Integer>();
			for (String attr : filteredEffects.keySet()) {
				effects.put(attr, Integer.parseInt(filteredEffects.get(attr)));
			}
			
			new Component_Item(entity,this.scene.getSystemInteraction(),
					item_name, item_description,
					effects, restrictions, properties);
		}
		
		/*
		 * Component_Controls
		 */
		if (data.containsKey("controllable")) {
			new Component_Controls(entity,this.scene.getSystemMovement());
		}
		
		/*
		 * Component_Inventory
		 */
		int money = 0;
		
		if (data.containsKey("inventory")) {
			if (data.containsKey("money")) {
				money = Integer.parseInt(data.get("money"));
			}
			new Component_Inventory(entity,this.scene.getSystemInteraction(),money);
		}
		
		/*
		 * Component_Sprite
		 */
		if (data.containsKey("sprite")) {
			new Component_Sprite(entity,this.scene.getSystemRender(),data.get("sprite"));
		}
		
		/*
		 * Component_Movement
		 */
		if (data.containsKey("x") && data.containsKey("y")) {
			int x1 = Integer.parseInt(data.get("x"));
			int y1 = Integer.parseInt(data.get("y"));
			int orientation = 2;
			int delay = 0;
			boolean walkable = false;
			boolean collidable = false;
			
			if (data.containsKey("orientation")) orientation = Integer.parseInt(data.get("orientation"));
			if (data.containsKey("spd")) delay = this.calculateDelay(Integer.parseInt(data.get("spd")));
			if (data.containsKey("walkable")) walkable = true;
			if (data.containsKey("collidable")) collidable = true;
			
			new Component_Movement(entity,scene.getSystemMovement(),
					x1,y1,0,0,
					orientation,
					delay,
					walkable,
					collidable,
					true);			
		}
		
		/*
		 * Component_Questbag
		 */
		
		if (data.containsKey("questbag")) {
			Component_Questbag bag = new Component_Questbag(entity,scene.getSystemQuest());
			Hashtable<String,String> filteredQuests = this.filterHashtable(data, "has_quest\\d*");
			for (String attr : filteredQuests.keySet()) {
				bag.addQuest(new Object_Quest(filteredQuests.get(attr)));
			}
		}
		
		/*
		 * Component_Trigger
		 */
		
		Hashtable<String,String> filteredEvents = this.filterHashtable(data, "on\\w*");
		if (!filteredEvents.isEmpty()) {
			String firstkey = filteredEvents.keys().nextElement();
			String typestr = filteredEvents.get(firstkey);
			EventType reason = EventType.valueOf(firstkey.replaceFirst("on", "").toUpperCase());
			EventType result = EventType.valueOf(typestr.toUpperCase());
			Hashtable<String,String> triggerprops = this.filterHashtable(data, "toX|toY|toLevel|toRoom|dialog|cause_dmg|shop_item\\d+");
			new Component_Trigger(entity,scene.getSystemInteraction(),reason,result,triggerprops);
		}
		
		
		return entity;
	}
	
		
	private Hashtable<String,String> filterHashtable(Map<String,String> hashtable, String regex) {
		Hashtable<String,String> filtered = new Hashtable<String,String>();
		
		for (String key : hashtable.keySet()) {
			if (key.matches(regex)) {
				filtered.put(key, hashtable.get(key));
			}
		}
		
		return filtered;
	}
	

	/*
	 * Privates
	 */
	
	private int calculateDelay(int speed) { return (int) Math.pow(2,6-speed); }
}