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
	NONE,
	COLLISION, ILLEGALCOLLISION, ATTACK, DEATH, PLAYERDMG,
	GAMEBEATEN, ACTION, CONSUME, PICKUP, BATTLE,
	CMD_ACTION, CMD_UP, CMD_DOWN, CMD_LEFT, CMD_RIGHT,
	CHANGELEVEL, OPEN_DIALOG, OPEN_BUYMENU
	}

class Event {
	
	protected EventType type;
	protected Entity actor;
	protected Entity undergoer;
	
	public Event(EventType type, Entity actor, Entity undergoer) {
		this.type = type;
		this.actor = actor;
		this.undergoer = undergoer;
	}
	
	public EventType getType() { return this.type; }
	public Entity getActor() { return this.actor; }
	public Entity getUndergoer() { return this.undergoer; }
}