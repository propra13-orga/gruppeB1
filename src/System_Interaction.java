/*
 * InteractionSystem.java
 * 
 * Das InteractionSystem behandelt "alles", was mit der Interaktion zwischen
 * einzelnen Entitäten bzw. ihrer Komponenten zu tun hat. Dazu gehören insbeson-
 * dere Trigger und Kampfhandlungen.
 * 
 */


class System_Interaction extends System_Component {

	public System_Interaction(Scene scene) {
		super(scene, "trigger_levelchange","trigger_attack","trigger_endgame","health");
	}

	@Override
	public void update() {
		
		this.handleActionCommands();
		
		this.handleCollisionEvents();
		this.handleActionEvents();
		
		this.checkForDeath();
		
		
		
	}
	
	
	/*
	 * Privates
	 */
	
	private void checkForDeath() {
		for (Entity entity : this.getEntitiesByType("health")) {
			int hp = ((Component_Health) entity.getComponent("health")).getHP();
			if (hp <= 0) this.addEvent(new Event(EventType.DEATH,null,entity));
		}
	}
	
	/*
	 * Gibt die Health-Komponente einer Entität zurück.
	 */
	private Component_Health getHealth(Entity entity) {
		return (Component_Health) entity.getComponent("health");
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
				if (actor.hasComponent("health")) {
					Component_Health Component_Health = this.getHealth(actor);
					int ap = trigger.getAP();
					Component_Health.discountHP(ap);
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
				if (actor.hasComponent("health")) {
					Component_Health Component_Health = this.getHealth(actor);
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