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
		/*
		 * Durchsuche alle Kollisionsevents nach Entitäten (Underger) mit
		 * Triggerkomponenten.
		 * Wird noch ordentlich in Funktionen ausgelagert, sobald sich ein
		 * Muster ergeben hat.
		 */
		for (Object_Event event : this.getEvents(EventType.COLLISION)) {
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
				Trigger_LevelChange trigger = (Trigger_LevelChange) entity.getComponent("trigger_levelchange");
				int ID = trigger.getLevelID();
				int x = trigger.getX();
				int y = trigger.getY();
				this.getScene().demandLevelChange(ID,x,y);
			}
			if (entity.hasComponent("trigger_attack")) {
				Trigger_Attack compTriggerAttack = ((Trigger_Attack) entity.getComponent("trigger_attack"));
				if (compTriggerAttack.isReady()) {
					if (actor.hasComponent("health")) {
						Component_Health compHealth = this.getHealth(actor);
						int ap = compTriggerAttack.getAP();
						compHealth.discountHP(ap);
						if (this.getScene().getPlayer().equals(actor)) {
							this.addEvent(new Object_Event(EventType.PLAYERDMG,entity,actor));
						}
						compTriggerAttack.unsetReady();
					}
				}
			}
			if (entity.hasComponent("trigger_endgame")
					&& this.getScene().getPlayer().equals(actor)) {
				this.getScene().beatGame();
			}
		}
		
		for (Entity entity : this.getEntitiesByType("health")) {
			int hp = ((Component_Health) entity.getComponent("health")).getHP();
			if (hp <= 0) this.addEvent(new Object_Event(EventType.DEATH,null,entity));
		}
	}
	
	private Component_Health getHealth(Entity entity) {
		return (Component_Health) entity.getComponent("health");
	}

}
