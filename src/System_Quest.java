import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * Hier werden Quest-bezogene Komponenten manipuliert. Im Wesentlichen beschränkt
 * sich das darauf, dass in jedem Zyklus jegliche Events abgerufen werden und 
 * daraufhin ermittelt wird, ob diese Teil einer Quest sind.
 * 
 * @author Victor Persien
 *
 */

public class System_Quest extends System_Component {

	public System_Quest(Abstract_Scene scene, String... types) {
		super(scene, "questbag","questdealer");
		this.listenTo(EventType.values());
	}

	@Override
	public void update() {
		for (Event event : this.getEvents(EventType.values())) {
			Entity actor = event.getActor();
			Entity undergoer = event.getUndergoer();
			EventType eventType = event.getType();
			if (actor.hasComponent("questbag")) {
				Component_Questbag bag = (Component_Questbag) actor.getComponent("questbag");
				for (Object_Quest quest : bag.getOpenQuests()) {
					if (quest.hasOpenEventOfType(eventType)) {
						String entityType;
						List<String> events = quest.getOpenEventsOfType(eventType);
						for (int i=0;i<events.size();i++) {
							entityType = events.get(i);
							if (entityType == null || entityType.equals(undergoer.getType())) {
								quest.moveToClosedEvents(eventType, i);
								if (quest.noOpenEventsLeft()) {
									this.handleAccomplishedQuest(bag,quest);
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	private void handleAccomplishedQuest(Component_Questbag bag, Object_Quest quest) {
		bag.setAccomplished(quest);
		Map<String,String> questData = new HashMap<String,String>();
		questData.put("quest_fname", quest.getFName());
		questData.put("quest_name", quest.getName());
		questData.put("quest_description", quest.getDescription());
		questData.put("quest_xp", Integer.toString(quest.getXP()));
		Event event = new Event(EventType.QUEST_ACCOMPLISHED,bag.getEntity(),null,questData);
		this.addEvent(event);
		
	}

}
