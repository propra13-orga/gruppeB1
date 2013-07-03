/*
 * InteractionSystem.java
 * 
 * Das InteractionSystem behandelt "alles", was mit der Interaktion zwischen
 * einzelnen Entitäten bzw. ihrer Komponenten zu tun hat. Dazu gehören insbeson-
 * dere Trigger und Kampfhandlungen.
 * 
 */


class System_Interaction extends System_Component {

	public System_Interaction(Abstract_Scene scene) {
		super(scene,
				"trigger_event",
				"battle",
				"item",
				"inventory");
	}

	@Override
	public void update() {
		
		this.handleActionCommands();
		
		for (Event event : this.getEvents(EventType.ACTION,EventType.COLLISION)) {
			Entity actor = event.getActor();
			Entity undergoer = event.getUndergoer();
			if (undergoer.hasComponent("trigger_event")) {
				Component_Trigger trigger = (Component_Trigger) undergoer.getComponent("trigger_event");
				if (trigger.getEventType().equals(event.getType())) {
					EventType triggeredEvent = trigger.getTriggeredEvent();
					this.addEvent(new Event(triggeredEvent,actor,undergoer));					
				}
			}
		}
		
		
		for (Event event : this.getEvents(EventType.ATTACK, EventType.OPEN_BUYMENU, EventType.OPEN_DIALOG,
				EventType.PICKUP, EventType.CHANGELEVEL, EventType.GAMEBEATEN)) {
			EventType type = event.getType();
			Entity actor = event.getActor();
			Entity undergoer = event.getUndergoer();
			switch (type) {
			case ATTACK:
				int ap = Integer.parseInt(((Component_Trigger) undergoer.getComponent("trigger_event")).getProperty("cause_dmg"));
				((Component_Battle) actor.getComponent("battle")).addToProperty("prop_hp_current", -ap);
				this.addEvent(new Event(EventType.PLAYERDMG,actor,undergoer));
				System.out.println("ANGRIFF");
				break;
			case CHANGELEVEL:
				if (this.getScene().getPlayer().equals(actor)) {
					Component_Trigger trigger = (Component_Trigger) undergoer.getComponent("trigger_event");
					int ID = Integer.parseInt(trigger.getProperty("toLevel"));
					int x = Integer.parseInt(trigger.getProperty("toX"));
					int y = Integer.parseInt(trigger.getProperty("toY"));
					this.getScene().demandLevelChange(ID,x,y);				
				}
				break;
			case GAMEBEATEN:
				if (this.getScene().getPlayer().equals(actor)) {
					this.getScene().beatGame();
				}
				break;
			case OPEN_BUYMENU:
				this.getScene().demandSceneChange(
						new Scene_BuyMenu(
								this.getScene().game,
								this.getScene()
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
				break;
			case PICKUP:
				if (actor.hasComponent("inventory")) {
					Component_Inventory compInventory = (Component_Inventory) actor.getComponent("inventory");
					if (compInventory.addItem(undergoer)) {
						((Component_Movement) undergoer.getComponent("movement")).drawFromMap();
						this.getScene().getCurrentLevel().removeEntity(undergoer);
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
				if (hp <= 0) this.addEvent(new Event(EventType.DEATH,null,entity));				
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
				if (item != null) System.out.println(item.getName());
			}
		}
	}
	
	/*
	 * 
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
				this.addEvent(new Event(EventType.ACTION,entity,undergoer));
				System.out.println("ACTION!");
			}
		}
	}
	
	
//	private void handleBattleEvents() {
//		if (!this.getEvents(EventType.BATTLE).isEmpty()) {
//			Entity player = this.getScene().getPlayer();
//			List<Object_BattleActor> players = new ArrayList<Object_BattleActor>();
//			players.add(new Object_BattleActor(player));
//			for (Event event : this.getEvents(EventType.BATTLE)) {
//				Entity actor = event.getActor();
//				Entity undergoer = event.getUndergoer();
//				Object_BattleActor ba_actor = new Object_BattleActor(actor);
//				Object_BattleActor ba_undergoer = new Object_BattleActor(undergoer);
//				players.add(ba_undergoer);
//				players.add(ba_actor);
//				if (actor.equals(player)) {
//					ba_actor.side = BattleSide.PLAYER;
//					ba_undergoer.side = BattleSide.ENEMY;
//				}
//				else if (undergoer.equals(player)) {
//					ba_undergoer.side = BattleSide.PLAYER;
//					ba_actor.side = BattleSide.ENEMY;
//				}
//			}
//			
//			this.getScene().demandSceneChange(
//					new Scene_BattleSystem(
//							new Object_BattleContext(players),
//							this.getScene(),
//							this.getScene().game
//					)
//			);
//		}
//	}
	
}