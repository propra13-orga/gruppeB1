import java.util.Map;



enum EventType {
	NONE,
	COLLISION, ILLEGALCOLLISION, ATTACK, DEATH, PLAYERDMG,
	GAMEBEATEN, ACTION, CONSUME, PICKUP, BATTLE, LEVELUP,
	CMD_ACTION, CMD_UP, CMD_DOWN, CMD_LEFT, CMD_RIGHT,
	CHANGEROOM, CHANGELEVEL,
	OPEN_DIALOG, OPEN_BUYMENU, OPEN_QUESTMENU, OPEN_BATTLE, 
	CLOSE_DIALOG,
	QUEST_ACCOMPLISHED,
	ITEM_BOUGHT, ITEM_SOLD, ITEM_DROP, ITEM_POSSESS, ITEM_USE
	}

/**
 * Die Klasse Event spezifiziert Events. Jedes Event hat einen Typ und enthält 
 * mindestens einen Eintrag "actor". Als Actor wird hier diejenige 
 * Entität bezeichnet, die den Event ausgelöst hat. Ein weiterer optionaler 
 * Eintrag ist "undergoer", was diejenige Entität ist, die von dem Event
 * betroffen ist.
 * 
 * Die Bezeichnungen sind nicht zu wörtlich zu nehmen. Wichtig ist einfach, dass
 * hier die Möglichkeit besteht, einem Event ein oder zwei beteiligte Entitäten 
 * zuzuweisen.
 * 
 * @author Victor Persien
 */
class Event {
	
	protected EventType type;
	protected Entity actor;
	protected Entity undergoer;
	protected Map<String,String> properties;
	
	public Event(EventType type, Entity actor, Entity undergoer) {
		this(type,actor,undergoer,null);
	}
	
	public Event(EventType type, Entity actor, Entity undergoer, Map<String, String> properties) {
		this.type = type;
		this.actor = actor;
		this.undergoer = undergoer;
		this.properties = properties;
	}
	
	public EventType getType() { return this.type; }
	public Entity getActor() { return this.actor; }
	public Entity getUndergoer() { return this.undergoer; }
	public String getProperty(String property) { return this.properties.get(property); }
	
}