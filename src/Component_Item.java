import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class Component_Item extends Abstract_Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6019813138461977130L;
	private Hashtable<String,Integer> effects;
	private List<String> restrictions;
	private List<String> properties;
	private String name;
	private String description;
	
	public Component_Item(Entity entity, System_Component system,
			String name, String description,
			Hashtable<String,Integer> effects, List<String> restrictions,
			List<String> properties) {
		super("item",entity,system);
		this.name = name;
		this.description = description;
		this.effects = effects;
		this.restrictions = restrictions;
		this.properties = properties;
	}
	
	
	/*
	 * Getters
	 */
	
	public String getName() { return this.name; }
	public String getDescription() { return this.description; }
	public Collection<String> getEffectNames() { return this.effects.keySet(); }
	public List<String> getRestrictions() { return this.restrictions; }
	public List<String> getProperties() { return this.properties; }
	
	/*
	 * Gibt den Wert eines Effekts zurueck oder Integer.MAX_VALUE, falls dieser
	 * nicht vorhanden ist. Besser ist es aber, erst die vorhandenen Effekte mit
	 * getEffectNames() abzufragen und dann deren Werte.
	 */
	public int getEffectValue(String effect) {
		if (this.effects.containsKey(effect)) return this.effects.get(effect);
		return Integer.MAX_VALUE;
	}
}






//
//public class Component_Item extends Abstract_Component implements IShopItem {
//
//	private String itemType;
//	private boolean consumable;
//	private boolean permanent;
//	private boolean stackable;
//	private int hp;
//	private int mp;
//	private int ap;
//	private int value;
//	private String description;
//	
//
//	public Component_Item(Entity entity, System_Component system,
//			String itemType, int hp, int mp, int ap, int value,  
//			boolean consumable, boolean permanent, boolean stackable,
//			String description) {
//		super("item", entity, system);
//		this.itemType = itemType;
//		this.hp = hp;
//		this.mp = mp;
//		this.ap = ap;
//		this.value = value;
//		this.consumable = consumable;
//		this.permanent = permanent;
//		this.stackable = stackable;
//		this.description = description;
//	}
//	
//	public Component_Item(Component_Item compItem) {
//		super(compItem);
//		this.itemType = compItem.itemType;
//		this.hp = compItem.hp;
//		this.mp = compItem.mp;
//		this.ap = compItem.ap;
//		this.value = compItem.value;
//		this.consumable = compItem.consumable;
//		this.permanent = compItem.permanent;
//		this.stackable = compItem.stackable;
//		this.description = compItem.description;
//	}
//	
//	public Component_Item(Abstract_Component comp, Entity entity, System_Component system) {
//		super(comp.getType(),entity,system);
//		Component_Item compItem = (Component_Item) comp;
//		this.itemType = compItem.itemType;
//		this.hp = compItem.hp;
//		this.mp = compItem.mp;
//		this.ap = compItem.ap;
//		this.value = compItem.value;
//		this.consumable = compItem.consumable;
//		this.permanent = compItem.permanent;
//		this.stackable = compItem.stackable;
//		this.description = compItem.description;
//	}
//	
//	/*
//	 * Getters
//	 */
//	
//	public String getItemType() { return this.itemType; }
//	public int getHP() { return this.hp; }
//	public int getMP() { return this.mp; }
//	public int getAP() { return this.ap; }
//	public int getValue() { return this.value; }
//	public String getDescription() { return this.description; }
//	public boolean isConsumable() { return this.consumable; }
//	public boolean isPermanent() { return this.permanent; }
//	public boolean isStackable() { return this.stackable; }
//}
