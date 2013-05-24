class Trigger_Attack extends Trigger {
	private int ap;		// Angriffspunkte
	
	public Trigger_Attack(Object_Entity entity, System_Component system,
			EventType eventType, int ap) {
		super("trigger_attack",entity,system,eventType);
		this.ap = ap;
	}
	
	public int getAP() { return this.ap; }
}