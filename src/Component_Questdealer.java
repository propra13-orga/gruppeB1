import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Der Questdealer ist eine Komponente, die es NPCs ermöglicht, Quests zu
 * verteilen.
 * 
 * @author Victor Persien
 *
 */
public class Component_Questdealer extends Abstract_Component {

	private static final long serialVersionUID = -583939779703444390L;
	List<Object_Quest> quests;
	
	/**
	 * Konstruktor. Alle diese Komponenten sind vom Typ "questdealer".
	 * 
	 * @param entity		Entität, der diese Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 * @param quests		Liste aller verteilbaren Quests.
	 */
	public Component_Questdealer(Entity entity,
			System_Component system, List<Object_Quest> quests) {
		super("questdealer", entity, system);
		if (quests != null)	this.quests = quests;
		else this.quests = new LinkedList<Object_Quest>();
	}

	public Component_Questdealer(Abstract_Component comp) {
		super(comp);
	}
	
	public List<Object_Quest> getQuests() { return this.quests; }
	public boolean hasQuest(Object_Quest quest) {
		return this.quests.contains(quest);
	}
	
	public void addQuest(String fname) {
		this.quests.add(new Object_Quest(fname));
	}
	
	public void addQuest(Object_Quest quest) { this.quests.add(quest); }

}
