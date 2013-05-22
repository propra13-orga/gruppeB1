/*
 * InteractionSystem.java
 * 
 * Das InteractionSystem behandelt "alles", was mit der Interaktion zwischen
 * einzelnen Entitäten bzw. ihrer Komponenten zu tun hat. Dazu gehören insbeson-
 * dere Trigger und Kampfhandlungen.
 * 
 */


class InteractionSystem extends ComponentSystem {

	public InteractionSystem(Scene scene) {
		super(scene, "trigger_levelchange","trigger_attack","health");
	}

	@Override
	public void update() {
		/*
		 * Durchsuche alle Kollisionsevents nach Entitäten (Underger) mit
		 * Triggerkomponenten.
		 * Wird noch ordentlich in Funktionen ausgelagert, sobald sich ein
		 * Muster ergeben hat.
		 */
		for (Event event : this.getEvents(EventType.COLLISION)) {
			/*
			 * Im Folgenden:
			 * "entity" ist jeweils die Entität mit der Triggerkomponente.
			 * "actor" ist jeweils die Entität, die den Trigger ausgelöst hat.
			 */
			Entity entity = event.getUndergoer();
			Entity actor = event.getActor();
			if (entity.hasComponent("trigger_levelchange")
					&& this.getScene().getPlayer().equals(actor)) {
				/*
				 * Die Komponente trigger_levelchange enthält alle wichtigen 
				 * Daten für den Levelwechsel.
				 */
				CompTriggerLevelChange trigger = (CompTriggerLevelChange) entity.getComponent("trigger_levelchange");
				int ID = trigger.getLevelID();
				int x = trigger.getX();
				int y = trigger.getY();
				this.getScene().demandLevelChange(ID,x,y);
			}
			if (entity.hasComponent("trigger_attack")) {
				CompTriggerAttack compTriggerAttack = ((CompTriggerAttack) entity.getComponent("trigger_attack"));
				if (compTriggerAttack.isReady()) {
					if (actor.hasComponent("health")) {
						CompHealth compHealth = this.getHealth(actor);
						int ap = compTriggerAttack.getAP();
						compHealth.discountHP(ap);
						if (this.getScene().getPlayer().equals(actor)) {
							this.addEvent(new Event(EventType.PLAYERDMG,entity,actor));
						}
					}
					compTriggerAttack.unsetReady();
				}
			}
		}
		
		for (Entity entity : this.getEntitiesByType("health")) {
			int hp = ((CompHealth) entity.getComponent("health")).getHP();
			if (hp <= 0) this.addEvent(new Event(EventType.DEATH,null,entity));
		}
	}
	
	private CompHealth getHealth(Entity entity) {
		return (CompHealth) entity.getComponent("health");
	}

}
