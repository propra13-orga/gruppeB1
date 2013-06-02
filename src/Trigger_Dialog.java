
public class Trigger_Dialog extends Trigger {
	String dialog;
	public Trigger_Dialog(Entity entity, System_Component system,
			EventType eventType, String dialog) {
		super("trigger_dialog", entity, system, eventType);
		this.dialog = dialog;
	}
	
	public String getDialog() { return this.dialog; }
}
