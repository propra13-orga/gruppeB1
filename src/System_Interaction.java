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
				"trigger_levelchange",
				"trigger_attack",
				"trigger_endgame",
				"trigger_pickup",
				"trigger_dialog",
				"battle",
				"item",
				"inventory");
	}

	@Override
	public void update() {
		
		this.handleActionCommands();
		
		this.handleCollisionEvents();
		this.handleActionEvents();
		
		this.handlePickUpEvents();
		
		this.checkForDeath();
		
		
		
	}
	
	
	/*
	 * Privates
	 */
	
	private void checkForDeath() {
		for (Entity entity : this.getEntitiesByType("battle")) {
			int hp = ((Component_Battle) entity.getComponent("battle")).getHP();
			if (hp <= 0) this.addEvent(new Event(EventType.DEATH,null,entity));
		}
	}
	
	/*
	 * Gibt die Health-Komponente einer Entität zurück.
	 */
	private Component_Battle getBattle(Entity entity) {
		return (Component_Battle) entity.getComponent("battle");
	}
	
	/*
	 * 
	 */
	private void handleActionCommands() {
		for (Event event : this.getEvents(EventType.CMD_ACTION)) {
			Entity entity = event.getActor();
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
	
	/*
	 * Durchsuche alle Kollisionsevents nach Entitäten (Undergoer) mit
	 * Triggerkomponenten.
	 */
	private void handleCollisionEvents() {
		for (Event event : this.getEvents(EventType.COLLISION)) {
			this.handleTriggers(event);
		}
	}
	
	private void handleActionEvents() {
		for (Event event : this.getEvents(EventType.ACTION)) {
			this.handleTriggers(event);
		}
	}
	
	private void handlePickUpEvents() {
		for (Event event : this.getEvents(EventType.PICKUP)) {
			Entity actor = event.getActor();
			Entity undergoer = event.getUndergoer();
			Component_Inventory compInventory = (Component_Inventory) actor.getComponent("inventory");
			if (compInventory.addItem(undergoer)) {
				((Component_Movement) undergoer.getComponent("movement")).deinit();
			}
		}
	}
	
	private void handleTriggers(Event event) {
		Entity entity = event.getUndergoer();

		if (entity.hasComponent("trigger_levelchange")) {
			this.handleTrigger_LevelChange(event);
		}
		else if (entity.hasComponent("trigger_endgame")) {
			this.handleTrigger_EndGame(event);
		}
		else if (entity.hasComponent("trigger_attack")) {
			this.handleTrigger_Attack(event);
		}
		else if (entity.hasComponent("trigger_dialog")) {
			this.handleTrigger_Dialog(event);
		}
		if (entity.hasComponent("trigger_pickup")) {
			this.handleTrigger_PickUp(event);
		}
	}
	
	private void handleTrigger_Dialog(Event event) {
		Entity undergoer = event.getUndergoer();
		Trigger_Dialog trigger = (Trigger_Dialog) undergoer.getComponent("trigger_dialog");
		this.getScene().demandSceneChange(
				new Scene_Dialog(
						this.getScene().game,
						this.getScene(),
						trigger.getDialog()
				)
		);
	}
	
	private void handleTrigger_PickUp(Event event) {
		System.out.println("aaa");
		Entity undergoer = event.getUndergoer();
		Entity actor = event.getActor();
		EventType type = event.getType();
		Trigger_PickUp trigger = (Trigger_PickUp) undergoer.getComponent("trigger_pickup");
		if (trigger.getEventType() == type
				&& actor.hasComponent("inventory")) {
			this.addEvent(new Event(EventType.PICKUP,actor,undergoer));
		}
	}
	
	private void handleTrigger_LevelChange(Event event) {
		Entity entity = event.getUndergoer();
		Entity actor = event.getActor();
		EventType type = event.getType();
		Trigger_LevelChange trigger = (Trigger_LevelChange) entity.getComponent("trigger_levelchange");
		if (trigger.getEventType() == type
				&& this.getScene().getPlayer().equals(actor)) {
			int ID = trigger.getLevelID();
			int x = trigger.getX();
			int y = trigger.getY();
			this.getScene().demandLevelChange(ID,x,y);				
		}
	}
	
	private void handleTrigger_EndGame(Event event) {
		Entity entity = event.getUndergoer();
		Entity actor = event.getActor();
		EventType type = event.getType();
		Trigger_EndGame trigger = (Trigger_EndGame) entity.getComponent("trigger_endgame");
		if (trigger.getEventType() == type
				&& this.getScene().getPlayer().equals(actor)) {
			this.getScene().beatGame();
		}
	}
	
	private void handleTrigger_Attack(Event event) {
		Entity entity = event.getUndergoer();
		Entity actor = event.getActor();
		EventType type = event.getType();
		Trigger_Attack trigger = (Trigger_Attack) entity.getComponent("trigger_attack");
		if (trigger.getEventType() == type) {
			if (trigger.isReady()) {
				if (actor.hasComponent("battle")) {
					Component_Battle compBattle = this.getBattle(actor);
					int ap = trigger.getAP();
					compBattle.discountHP(ap);
					if (this.getScene().getPlayer().equals(actor)) {
						this.addEvent(new Event(EventType.PLAYERDMG,entity,actor));
					}
					trigger.unsetReady();
				}
			}
		}
	}
	
	/*
	 * 
	 */
	private void handleTriggersCollision(Event event) {
		/*
		 * Im Folgenden:
		 * "entity" ist jeweils die Entität mit der Triggerkomponente.
		 * "actor" ist jeweils die Entität, die den Trigger ausgelöst hat.
		 */
		Entity entity = event.getUndergoer();
		Entity actor = event.getActor();
		if (this.getScene().getPlayer().equals(actor)) {
			if (entity.hasComponent("trigger_levelchange")) {
				/*
				 * Die Komponente trigger_levelchange enthält alle wichtigen 
				 * Daten für den Levelwechsel.
				 */
				Trigger_LevelChange trigger = (Trigger_LevelChange) entity.getComponent("trigger_levelchange");
				int ID = trigger.getLevelID();
				int x = trigger.getX();
				int y = trigger.getY();
				this.getScene().demandLevelChange(ID,x,y);
			}
			if (entity.hasComponent("trigger_endgame")) {
				this.getScene().beatGame();
			}				
		}
		if (entity.hasComponent("trigger_attack")) {
			Trigger_Attack Trigger_Attack = ((Trigger_Attack) entity.getComponent("trigger_attack"));
			if (Trigger_Attack.isReady()) {
				if (actor.hasComponent("battle")) {
					Component_Battle Component_Health = this.getBattle(actor);
					int ap = Trigger_Attack.getAP();
					Component_Health.discountHP(ap);
					if (this.getScene().getPlayer().equals(actor)) {
						this.addEvent(new Event(EventType.PLAYERDMG,entity,actor));
					}
					Trigger_Attack.unsetReady();
				}
			}
		}
	}
}