import java.util.Hashtable;


public class Object_BattleEvent {

	private boolean						done;
	private Hashtable<String, Object>	properties;
	
	Object_BattleEvent(String type) {
		this.properties = new Hashtable<String, Object>();
		this.properties.put("type", type);
		this.properties.put("parallel", true);
	}
	
	public Object getAttribute(String key) {
		if (this.properties.containsKey(key)) {
			return this.properties.get(key);
		}
		return null;
	}
	
	public void setAttribute(String key, Object value) {
		this.properties.put(key, value);
	}
	
	public boolean isDone() {
		return this.done;
	}
	
	public void finish() {
		this.done = true;
	}
	
}