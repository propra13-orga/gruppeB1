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
		super(scene, "trigger_levelchange");
	}

	@Override
	public void update() {
		/*
		 * Durchsuche alle Kollisionsevents nach Entitäten (Underger) mit
		 * Triggerkomponenten.
		 */
		for (Event event : this.getEvents(EventType.COLLISION)) {
			Entity entity = event.getUndergoer();
			if (entity.hasComponent("trigger_levelchange")) {
				System.out.println("aaa");
				CompTriggerLevelChange trigger = (CompTriggerLevelChange) entity.getComponent("trigger_levelchange");
				int ID = trigger.getLevelID();
				int x = trigger.getX();
				int y = trigger.getY();
				this.getScene().demandLevelChange(ID,x,y);
			}
		}

	}

}
