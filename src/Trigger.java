/*
 * Trigger sind Komponenten, die auf bestimmte Ereignisse, wie Kollisionen, 
 * reagieren. Das InteractionSystem behandelt die verschiedenen Trigger und
 * was nach ihrer Auslösung passiert. 
 */

abstract class Trigger extends Component {
	
	private EventType eventType;
	private boolean ready;
	
	public Trigger(String type, Entity entity, System_Component system, 
			EventType eventType) {
		super(type,entity,system);
		this.eventType = eventType;
		this.ready = true;
	}
	
	public EventType getEventType() { return this.eventType; }
	public boolean isReady() { return this.ready; }
	
	public void setReady() { this.ready = true; }
	public void unsetReady() { this.ready = false; }
}