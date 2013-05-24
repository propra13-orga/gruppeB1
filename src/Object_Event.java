/*
 * Event.java
 * 
 * Die Klasse Event spezifiziert Events. Jedes Event hat einen Typ und enthält 
 * mindestens einen Eintrag "undergoer". Als Undergoer wird hier diejenige 
 * Entität bezeichnet, die von dem Event betroffen ist. Ein weiterer optionaler 
 * Eintrag ist "actor", was diejenige Entität ist, die das Event beim Undergoer 
 * verursacht hat.
 * 
 * Die Bezeichnungen sind nicht zu wörtlichzu nehmen. Wichtig ist einfach, dass
 * hier die Möglichkeit besteht, einem Event ein oder zwei beteiligte Entitäten 
 * zuzuweisen.
 */
enum EventType {
	COLLISION, ILLEGALCOLLISION, ATTACK, DEATH, PLAYERDMG,
	GAMEBEATEN
	}

class Object_Event {
	protected EventType type;
	protected Object_Entity actor;
	protected Object_Entity undergoer;
	
	public Object_Event(EventType type, Object_Entity actor, Object_Entity undergoer) {
		this.type = type;
		this.actor = actor;
		this.undergoer = undergoer;
	}
	
	public EventType getType() { return this.type; }
	public Object_Entity getActor() { return this.actor; }
	public Object_Entity getUndergoer() { return this.undergoer; }
}