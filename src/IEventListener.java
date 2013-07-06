import java.util.List;
import java.util.Map;

public interface IEventListener {
	public void addEvent(Event event);
	public void broadcastEvent(Event event);
	public List<Event> getEvents(EventType...eventTypes);
	public void listenTo(EventType...eventTypes);
}
