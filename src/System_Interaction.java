/*
 * InteractionSystem.java
 * 
 * Das InteractionSystem behandelt "alles", was mit der Interaktion zwischen
 * einzelnen Entitäten bzw. ihrer Komponenten zu tun hat. Dazu gehören insbeson-
 * dere Trigger und Kampfhandlungen.
 * 
 */


class System_Interaction extends System_Component {
	
	private int tick;
	private static final int TICK_MAX = 9;
	private int[] levelBorders = {0,10,25,63,158,395};

	public System_Interaction(Abstract_Scene scene) {
		super(scene,
				"trigger_event",
				"battle",
				"item",
				"inventory",
				"skillbag",
				"equipment");
		this.listenTo(EventType.ACTION, EventType.COLLISION, EventType.ATTACK, EventType.OPEN_BUYMENU,
				EventType.OPEN_DIALOG, EventType.CLOSE_DIALOG, EventType.OPEN_QUESTMENU, EventType.OPEN_BATTLE,
				EventType.PICKUP, EventType.CHANGEROOM, EventType.CHANGELEVEL, EventType.GAMEBEATEN, EventType.CMD_ACTION,
				EventType.QUEST_ACCOMPLISHED, EventType.ITEM_USE, EventType.LEVELUP);
		
		this.tick = 0;
	}

	@Override
	public void update() {
		
		this.updateTick();
		
		this.handleActionCommands();
		this.handlePossession(5);
		
		for (Event event : this.getEvents(EventType.ACTION,EventType.COLLISION)) {
			Entity actor = event.getActor();
			Entity undergoer = event.getUndergoer();
			if (undergoer.hasComponent("trigger_event")) {
				Component_Trigger trigger = (Component_Trigger) undergoer.getComponent("trigger_event");
				if (trigger.getEventType().equals(event.getType())) {
					EventType triggeredEvent = trigger.getTriggeredEvent();
					this.addEvent(new Event(triggeredEvent,actor,undergoer, null));					
				}
			}
		}
		
		
		for (Event event : this.getEvents(EventType.ATTACK, EventType.OPEN_BUYMENU, EventType.OPEN_DIALOG, EventType.OPEN_QUESTMENU,
				EventType.OPEN_BATTLE,
				EventType.PICKUP, EventType.CHANGEROOM, EventType.CHANGELEVEL, EventType.GAMEBEATEN, EventType.QUEST_ACCOMPLISHED,
				EventType.ITEM_USE,
				EventType.LEVELUP)) {
			EventType type = event.getType();
			Entity actor = event.getActor();
			Entity undergoer = event.getUndergoer();
			switch (type) {
			case ATTACK:
				int ap = Integer.parseInt(((Component_Trigger) undergoer.getComponent("trigger_event")).getProperty("cause_dmg"));
				((Component_Battle) actor.getComponent("battle")).addToProperty("prop_hp_current", -ap);
				if (this.getScene().getPlayer().equals(actor)) {
					this.addEvent(new Event(EventType.PLAYERDMG,actor,undergoer, null));					
				}
				break;
			case CHANGEROOM:
				if (this.getScene().getPlayer().equals(actor)) {
					Component_Trigger trigger = (Component_Trigger) undergoer.getComponent("trigger_event");
					int ID = Integer.parseInt(trigger.getProperty("toRoom"));
					int x = Integer.parseInt(trigger.getProperty("toX"));
					int y = Integer.parseInt(trigger.getProperty("toY"));
					this.getScene().demandRoomChange(ID,x,y);				
				}
				break;
			case CHANGELEVEL:
				if (this.getScene().getPlayer().equals(actor)) {
					Component_Trigger trigger = (Component_Trigger) undergoer.getComponent("trigger_event");
					String levelname = trigger.getProperty("toLevel");
					this.getScene().demandLevelChange(levelname);
				}
				break;
			case GAMEBEATEN:
				if (this.getScene().getPlayer().equals(actor)) {
					this.getScene().beatGame();
				}
				break;
			case ITEM_USE:
				this.handleItemUse(actor, undergoer);
				break;
			case LEVELUP:
				this.handleLevelUp(actor);
				break;
			case OPEN_BATTLE:
				Object_BattleContext bc = new Object_BattleContext();
				Scene_BattleSystem bs = new Scene_BattleSystem(bc,this.getScene(),this.getScene().game);
				Object_BattleActor us = new Object_BattleActor(undergoer,bs);
				Object_BattleActor them = new Object_BattleActor(actor,bs);
				bc.getActors().add(us);
				bc.getActors().add(them);
				bs.setCtx(bc);
				this.getScene().demandSceneChange(bs);
				break;
			case OPEN_BUYMENU:
				this.getScene().demandSceneChange(
						new Scene_BuyMenu(
								this.getScene().game,
								this.getScene(),
								undergoer,
								actor
								)
						);
				break;
			case OPEN_DIALOG:
				this.getScene().demandSceneChange(
						new Scene_Dialog(
								this.getScene().game,
								this.getScene(),
								((Component_Trigger) undergoer.getComponent("trigger_event")).getProperty("dialog")
								)
				);
				this.addEvent(new Event(EventType.CLOSE_DIALOG,actor,undergoer, null));
				break;
			case OPEN_QUESTMENU:
				if (undergoer.hasComponent("questdealer") && actor.hasComponent("questbag")) {
					Component_Questdealer compQuestdealer = (Component_Questdealer) undergoer.getComponent("questdealer");
					Component_Questbag compQuestbag = (Component_Questbag) actor.getComponent("questbag");
					for (Object_Quest quest : compQuestdealer.getQuests()) {
						if (!compQuestbag.hasQuest(quest)) System.out.printf("Verfuegbare Quest: %s\n", quest.getName());
						else System.out.printf("Aktive oder abgeschlossene Quest: %s\n",quest.getName());
					}
					this.getScene().demandSceneChange(
							new Scene_QuestMenu(
									this.getScene().game,
									this.getScene(),
									undergoer,
									actor)
					);
				}
				break;
			case PICKUP:
				if (actor.hasComponent("inventory")) {
					Component_Inventory compInventory = (Component_Inventory) actor.getComponent("inventory");
					if (compInventory.addItem(undergoer)) {
						((Component_Movement) undergoer.getComponent("movement")).drawFromMap();
						this.getScene().getCurrentRoom().removeEntity(undergoer);
					}				
				}
				break;
			case QUEST_ACCOMPLISHED:
				System.out.println("AAA");
				
				if (actor.hasComponent("battle")) {
					Component_Battle compBattle = (Component_Battle) actor.getComponent("battle");
					if (compBattle.hasProperty("prop_xp")) {
						int xp = Integer.parseInt(event.getProperty("quest_xp"));
						this.handleXP(compBattle, xp);
					}
				}
				break;
			default:
				break;
			}
		}
		
		
		this.checkForDeath();
	}
	
	
	/*
	 * Privates
	 */
	
	private void checkForDeath() {
		for (Entity entity : this.getEntitiesByType("battle")) {
			Component_Battle compBattle = this.getBattle(entity);
			if (compBattle.hasProperty("prop_hp_current")) {
				int hp = compBattle.getPropertyValue("prop_hp_current");
				if (hp <= 0) this.addEvent(new Event(EventType.DEATH,null,entity, null));				
			}
		}
	}
	
	/*
	 * Gibt die Battle-Komponente einer Entität zurück.
	 */
	private Component_Battle getBattle(Entity entity) {
		return (Component_Battle) entity.getComponent("battle");
	}
	
	private void printInventory(Entity entity) {
		if (entity.hasComponent("inventory")) {
			Component_Inventory compInventory = (Component_Inventory) entity.getComponent("inventory");
			for (Entity item : compInventory.getInventory()) {
				if (item != null) System.out.println(((Component_Item) item.getComponent("item")).getName());
			}
		}
	}
	
	/*
	 * Wird ein Event CMD_ACTION verschickt (z.B. nach Tastendruck), so prüft
	 * die Methode, ob sich vor der entsprechenden Entität eine weitere Entität
	 * befindet. Falls ja, wird das Event ACTION verschickt.
	 */
	private void handleActionCommands() {
		for (Event event : this.getEvents(EventType.CMD_ACTION)) {
			Entity entity = event.getActor();
			this.printInventory(entity);
			Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");
			int[] xy = compMovement.orientationToVector(compMovement.orientation);
			int x = compMovement.getX();
			int y = compMovement.getY();
			xy[0] += x;
			xy[1] += y;
			if (!this.getScene().getEntitiesAt(xy[0],xy[1]).isEmpty()) {
				Entity undergoer = this.getScene().getEntitiesAt(xy[0],xy[1]).get(0);
				this.addEvent(new Event(EventType.ACTION,entity,undergoer, null));
				System.out.println("ACTION!");
			}
		}
	}
	
	/*
	 * 
	 */
	private void handleLevelUp(Entity entity) {
		if (entity.hasComponent("skillbag")) {
			Component_Skillbag compSkillbag = (Component_Skillbag) entity.getComponent("skillbag");
			compSkillbag.addToSkillPoints(2);
		}
	}
	
	/*
	 * Verschickt jeden time-ten Updatezyklus ein Event ITEM_POSSESS für jedes
	 * Item, was sich in irgendeinem Inventar befindet.
	 */
	private void handlePossession(int time) {
		if (this.tick != time) return;
		
		for (Entity entity : this.getEntitiesByType("inventory")) {
			Component_Inventory compInventory = (Component_Inventory) entity.getComponent("inventory");
			for (Entity item : compInventory.getInventory()) {
				if (item != null) {
					this.addEvent(new Event(EventType.ITEM_POSSESS,entity,item));				
				}
			}
		}
	}
	
	private void handleXP(Component_Battle compBattle, int xp) {
		compBattle.addToProperty("prop_xp", xp);
		int newXP = compBattle.getPropertyValue("prop_xp");
		int lvl = compBattle.getPropertyValue("prop_lvl");
		if (newXP >= this.levelBorders[lvl]) {
			compBattle.addToProperty("prop_lvl", 1);
			System.out.println("Level up!");
			this.addEvent(new Event(EventType.LEVELUP,compBattle.getEntity(),null));
		}
		System.out.printf("+%d XP macht %d XP!\n",xp,compBattle.getPropertyValue("prop_xp"));
	}
	
	/*
	 * Erhöht den Tick um 1, wenn er kleiner als TICK_MAX ist, ansonsten wird er
	 * auf 0 gesetzt.
	 */
	private void updateTick() {
		if (this.tick < TICK_MAX) this.tick += 1;
		else this.tick = 0;
	}
	
	private void handleItemUse(Entity entity, Entity item) {
		Component_Item compItem = (Component_Item) item.getComponent("item");
		Component_Battle compBattle = (Component_Battle) entity.getComponent("battle");
		for (String property : compItem.getEffectNames()) {
			if (property.matches("prop_.*")) {
				int current = compBattle.getPropertyValue(property+"_current");
				int oldmax = compBattle.getPropertyValue(property);
				int add = compItem.getEffectValue(property);
				int newv = Math.min(current+add, oldmax);
				compBattle.addToProperty(property+"_current", newv);
			}
		}
	}
	
}
