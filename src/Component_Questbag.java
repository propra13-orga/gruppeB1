import java.util.LinkedList;
import java.util.List;

public class Component_Questbag extends Abstract_Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7205233874573512699L;
	
	private List<Object_Quest> openQuests;
	private List<Object_Quest> closedQuests;

	public Component_Questbag(Entity entity,
			System_Component system) {
		super("questbag", entity, system);
		this.openQuests = new LinkedList<Object_Quest>();
		this.closedQuests = new LinkedList<Object_Quest>();
	}

	public Component_Questbag(Abstract_Component comp) {
		super(comp);
	}
	
	public List<Object_Quest> getOpenQuests() { return this.openQuests; }
	public List<Object_Quest> getClosedQuests() { return this.closedQuests; }
	public boolean hasQuest(Object_Quest quest) {
		return this.openQuests.contains(quest) || this.closedQuests.contains(quest);
	}
	
	
	public void addQuest(Object_Quest quest) { this.openQuests.add(quest); }
	public void setAccomplished(Object_Quest quest) {
		quest.setAccomplished();
		this.openQuests.remove(quest);
		this.closedQuests.add(quest);
	}

}
