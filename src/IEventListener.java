import java.util.List;

/**
 * 
 * Dieses Interface können alle Klassen implementieren, die bei einem Eventmanager
 * (Object_EventManager) Events abonnieren oder selber welche verschicken wollen.
 * 
 * @author Victor Persien
 *
 */

public interface IEventListener {
	
	/**
	 * Sende ein Event an den Eventmanager.
	 * @param event
	 */
	public void addEvent(Event event);
	
	/**
	 * Methode, die vom Eventmanager aufgerufen wird, um der Klasse, die dieses
	 * Interface implementiert, Events zuzuweisen.
	 * @param event
	 */
	public void broadcastEvent(Event event);
	
	/**
	 * Ruft alle abonnierten Events der angegebenen Typen ab.
	 * @param eventTypes	Typen der gewünschten Events.
	 * @return				List von Events.
	 */
	public List<Event> getEvents(EventType...eventTypes);
	
	/**
	 * Legt fest, Events welchen Typs abonniert werden sollen.
	 * @param eventTypes
	 */
	public void listenTo(EventType...eventTypes);
}
