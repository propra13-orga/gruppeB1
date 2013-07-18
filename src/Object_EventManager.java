import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Der Eventmanager verwaltet Events, d.h. er empfängt Events von Klassen, die 
 * IEventListener implementiert haben und verteilt sie an die entsprechenden
 * Abonnenten.
 * 
 * @author Victor Persien
 *
 */

public class Object_EventManager {
	
	private Map<EventType,List<IEventListener>> listenersByEventType;
	
	public Object_EventManager() {
		this.listenersByEventType = new HashMap<EventType,List<IEventListener>>();
	}
	
	public void listenTo(IEventListener listener, EventType...eventTypes) {
		for (EventType eventType : eventTypes) {
			if (!this.listenersByEventType.containsKey(eventType)) {
				this.listenersByEventType.put(eventType, new LinkedList<IEventListener>());
			}
			this.listenersByEventType.get(eventType).add(listener);
		}
	}
	
	public void addEvent(Event event) {
		EventType type = event.getType();
		if (!this.listenersByEventType.containsKey(type)) return;
		for (IEventListener listener : this.listenersByEventType.get(type)) {
			listener.broadcastEvent(event);
		}
	}
	
	
}
