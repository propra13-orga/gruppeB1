import java.util.Hashtable;
import java.util.LinkedList;
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
	private int value;
	
	public Component_Item(Entity entity, System_Component system,
			String name, String description,
			int value, Hashtable<String,Integer> effects,
			List<String> restrictions, List<String> properties) {
		super("item",entity,system);
		this.name = name;
		this.description = description;
		this.effects = effects;
		this.restrictions = restrictions;
		this.properties = properties;
		this.value = value;
	}
	
	
	/*
	 * Getters
	 */
	
	public String getName() { return this.name; }
	public String getDescription() { return this.description; }
	public int getValue() { return this.value; }
	public List<String> getEffectNames() { return new LinkedList<String>(this.effects.keySet()); }
	public List<String> getRestrictions() { return this.restrictions; }
	public List<String> getProperties() { return this.properties; }
	public boolean hasProperty(String property) { return this.properties.contains(property); }
	public boolean isEquippable() {
		for (String property : this.properties) {
			if (property.matches("slot_.*")) return true;
		}
		return false;
	}
	
	/*
	 * Gibt den Wert eines Effekts zurueck oder Integer.MAX_VALUE, falls dieser
	 * nicht vorhanden ist. Besser ist es aber, erst die vorhandenen Effekte mit
	 * getEffectNames() abzufragen und dann deren Werte.
	 */
	public Integer getEffectValue(String effect) {
		if (this.effects.containsKey(effect)) return this.effects.get(effect);
		return null;
	}
}