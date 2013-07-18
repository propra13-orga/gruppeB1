import java.util.Collection;
import java.util.Map;


/**
 * 
 * Komponente, die alle Daten beinhaltet, die für das Kämpfen wichtig sind. Die
 * meisten davon sind in der Tabelle "properties" abgelegt. Darunter etwa
 * "prop_lvl" für das aktuelle Level.
 * 
 * @author Victor Persien
 *
 */
class Component_Battle extends Abstract_Component {
	
	private static final long serialVersionUID = 4080073737857884118L;
	private Map<String,Integer> properties;
	private String sprite;
	
	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "battle".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 * @param properties	Weitere Eigenschaften.
	 * @param sprite		Dateiname (ohne Endung) des Sprites, welches im Kampf
	 * 						angezeigt werden soll.
	 */
	public Component_Battle(Entity entity, System_Component system,
			Map<String,Integer> properties,
			String sprite) {
		super("battle",entity,system);
		this.properties = properties;
		this.sprite = sprite;
	}
	
	public Collection<String> getPropertyNames() { return this.properties.keySet(); }
	public Integer getPropertyValue(String property) {
		if (this.properties.containsKey(property)) return this.properties.get(property);
		return null;
	}
	public String getSprite() { return this.sprite; }
	public boolean hasProperty(String property) { return this.properties.containsKey(property); }
	
	public void setProperty(String property, int value) { this.properties.put(property, value); }
	public void addToProperty(String property, int value) {
		int currentValue = this.properties.get(property);
		this.properties.put(property, currentValue+value);
		}
	
}