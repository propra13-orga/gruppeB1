import java.util.List;

public interface IEventListener {
	public void addEvent(Event event);
	public void broadcastEvent(Event event);
	public List<Event> getEvents(EventType...eventTypes);
	public void listenTo(EventType...eventTypes);
}
