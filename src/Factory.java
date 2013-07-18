import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Mit der Factory ist es möglich, Entitäten und andere Spielobjekte zu erzeugen.
 * Weiter dient sie dem Zugriff auf die Datenbanken für Entitäten, Quests, Skills
 * und allgemeineren Daten.
 * 
 * @author Victor Persien
 * 
 */


public class Factory {
	Scene_Level scene;
	/**
	 * Liest Daten aus .et-Dateien (für Entitäten).
	 */
	Object_DBReader db_et;
	/**
	 * Liest Daten aus .q-Dateien (für Quests).
	 */
	Object_DBReader db_q;
	/**
	 * Liest Daten aus .skl-Dateien (für Skills).
	 */
	Object_DBReader db_skl;
	/**
	 * Liest Daten aus .dat-Dateien (für sonstige Daten).
	 */
	Object_DBReader db_dat;
	
	public Factory(Scene_Level scene) {
		this.scene = scene;
		this.db_et = new Object_DBReader("entity");
		this.db_q = new Object_DBReader("quest");
		this.db_skl = new Object_DBReader("skill");
		this.db_dat = new Object_DBReader("data");
	}
	
	
	public Object_DBReader getDBET() { return this.db_et; }
	public Object_DBReader getDBQ() { return this.db_q; }
	public Object_DBReader getDBSKL() { return this.db_skl; }
	public Object_DBReader getDBDAT() { return this.db_dat; }
	
	
	/**
	 * Baut eine Entität gemäß der in "entityData" spezifizierten Daten.
	 * 
	 * @param entityData		Tabelle mit Daten, die eine Entität ausmachen.
	 * 							Diese befinden sich in der Regel in einer .et-Datei
	 * 							und können mittels db_et ausgelesen werden.
	 */
	public Entity build(Map<String,String> entityData) {
		String entityType = entityData.get("entityType");
		String name = entityData.remove("name");
		
		Map<String,String> data = db_et.getProperties(entityType);
		
		data.put("entityType", entityType);
		
		if (entityData.containsKey("x") && entityData.containsKey("y")) {
			data.put("x",entityData.get("x"));
			data.put("y",entityData.get("y"));
		}
		else {
			data.put("x", "-1");
			data.put("-1", "-1");
		}
		if (!data.containsKey("name") || data.get("name").equals("item")) {
			data.put("name", name);			
		}
		else name = data.get("name");
		
		if (entityData != null) {
			for (String attr : entityData.keySet()) {
				data.put(attr, entityData.get(attr));
			}
		}
		
		Entity entity = new Entity(name,entityType,this.scene.getEntityManager());
		
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
			if (!dataCompBattle.containsKey("prop_lvl")) {
				properties.put("prop_lvl", 1);
			}
			new Component_Battle(entity,scene.getSystemInteraction(),
					properties,battleSprite);
		}
		
		/*
		 * Component_Item
		 */
		Hashtable<String,String> filteredEffects = filterHashtable(data,"dmg|dmg_\\w+|armor|armor_\\w+|prop_\\w+");
		Hashtable<String,String> filteredRestrictions = filterHashtable(data,"");
		Hashtable<String,String> filteredProperties = filterHashtable(data,"slot_\\w+|type_\\w+");
		if (!filteredEffects.isEmpty() || 
				!filteredRestrictions.isEmpty() ||
				!filteredProperties.isEmpty()) {
			String item_name = null;
			String item_description = null;
			int item_value = 0;
			if (data.containsKey("item_name")) item_name = data.get("item_name");
			if (data.containsKey("item_description")) item_description = data.get("item_description");
			if (data.containsKey("item_value")) item_value = Integer.parseInt(data.get("item_value"));
			
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
					item_value, effects, restrictions, properties);
		}
		
		/*
		 * Component_Controls
		 */
		if (data.containsKey("controllable")) {
			new Component_Controls(entity,this.scene.getSystemMovement());
		}
		
		/*
		 * Component_Camera
		 */
		
		if (data.containsKey("camera")) {
			new Component_Camera(entity,this.scene.getSystemRender());
		}
		
		/*
		 * Component_Equipment
		 */
		
		if (data.containsKey("equipment")) {
			new Component_Equipment(entity,this.scene.getSystemInteraction());
		}
		
		/*
		 * Component_Inventory
		 */
		int money = 0;
		
		if (data.containsKey("inventory")) {
			if (data.containsKey("money")) {
				money = Integer.parseInt(data.get("money"));
			}
			Component_Inventory compInventory = new Component_Inventory(entity,this.scene.getSystemInteraction(),money);
			Hashtable<String,String> filteredItems = this.filterHashtable(data, "has_item\\d*");
			for (String et : filteredItems.values()) {
				compInventory.addItem(this.build(this.db_et.getProperties(et)));
			}
		}
		
		/*
		 * Component_Skillbag
		 */
		if (data.containsKey("skillbag")) {
			Component_Skillbag compSkillbag = new Component_Skillbag(entity,this.scene.getSystemInteraction());
			Hashtable<String,String> filteredSkills = this.filterHashtable(data, "has_skill\\d*");
			for (String attr : filteredSkills.keySet()) {
				compSkillbag.addSkill(new Object_Skill(this.db_skl.getProperties(filteredSkills.get(attr))));
			}
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
		 * Component_Questdealer
		 */
		
		if (data.containsKey("questdealer")) {
			Hashtable<String,String> filteredQuests = this.filterHashtable(data, "deal_quest\\d*");
			Component_Questdealer qdealer = new Component_Questdealer(entity,scene.getSystemQuest(),null);
			for (String attr : filteredQuests.keySet()) {
				qdealer.addQuest(filteredQuests.get(attr));
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
		
		
		/*
		 * Component_AI
		 */
		
		if (data.containsKey("ai")) {
			new Component_AI(entity,scene.getSystemAI(),2,7);
		}
				
		
		return entity;
	}
	
	/**
	 * Filtert eine Hashtable so, dass sie nur noch Schlüssel enthält, die
	 * den regulären Ausdruck "regex" matchen.
	 * 
	 * @param hashtable		Die zu filternde Tabelle.
	 * @param regex			Der Regex, nach dem gefiltert werden soll.
	 */
	public Hashtable<String,String> filterHashtable(Map<String,String> hashtable, String regex) {
		Hashtable<String,String> filtered = new Hashtable<String,String>();
		
		for (String key : hashtable.keySet()) {
			if (key.matches(regex)) {
				filtered.put(key, hashtable.get(key));
			}
		}
		
		return filtered;
	}
	
	/**
	 * Aktualisiert die Verweise auf die Komponentensysteme nach Levelwechsel.
	 */
	public void updateSystems(Entity entity) {
		String type;
		for (Abstract_Component comp : entity.getComponents()) {
			type = comp.getType();
			comp.setSystem(this.scene.getSystemByType(type));
		}
	}
	

	/*
	 * Privates
	 */
	
	/**
	 * Berechnet die Verzögerung beim Bewegen auf der Karte anhand des Wertes
	 * speed.
	 * 
	 * @param speed		Die Bewegungsgeschwindigkeit auf der Karte.
	 * @return			Die Entsprechende Verzögerung.
	 */
	private int calculateDelay(int speed) { return (int) Math.pow(2,6-speed); }
}