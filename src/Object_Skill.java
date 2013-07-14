import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Object_Skill implements Serializable, Comparable<Object_Skill> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1596660653106070453L;
	
	private String name;
	private String description;
	private boolean heal;
	private Map<String,Integer> properties;
	
	public Object_Skill(Map<String,String> properties) {
		this.name = properties.get("name");
		this.description = properties.get("description");
		if (properties.containsKey("heal")) this.heal = true;
		this.properties = this.initProperties(properties);
	}
	
	
	/*
	 * Getter
	 */
	
	public String getName() { return this.name; }
	public String description() { return this.description; }
	public boolean isHeal() { return this.heal; }
	public boolean hasProperty(String property) { return this.properties.containsKey(property); }
	public Map<String,Integer> getProperties() { return this.properties; }
	public Integer getProperty(String property) {
		if (this.properties.containsKey(property)) {
			return this.properties.get(property);
		}
		return null;
	}
	
	/*
	 * Setter
	 */
	
	/*
	 * Privates
	 */
	
	
	private Map<String,Integer> initProperties(Map<String,String> properties) {
		Map<String,Integer> converted = new HashMap<String,Integer>();
		int value;
		for (String key : properties.keySet()) {
			if (key.matches("dmg.*|heal_.*")) {
				value = Integer.parseInt(properties.get(key));
				converted.put(key, value);
			}
		}
		return converted;
	}


	@Override
	public int compareTo(Object_Skill s) {
		if (
				this.name.equals(s.name) &&
				this.heal == s.heal &&
				this.properties.equals(s.properties)) {
			return 0;
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.compareTo((Object_Skill) o) == 0) return true;
		return false;
	}
}
