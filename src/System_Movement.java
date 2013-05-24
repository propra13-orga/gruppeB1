import java.util.LinkedList;
import java.util.List;

class System_Movement extends System_Component {
	protected Object_KeyHandler keyHandler;
	
	public System_Movement(Scene scene, Object_KeyHandler keyHandler) {
		super(scene,"controls","movement");
		this.keyHandler = keyHandler;
	}
	
	@Override
	public void update() {
		// Erst die Eingaben behandeln.
		for (Entity entity : this.getEntitiesByType("controls")) {
			if (entity.hasComponent("movement")) {
				Component_Movement Component_Movement = (Component_Movement) entity.getComponent("movement");
				this.handleMoveability(Component_Movement);
				if (entity.isPlayer()) {
					this.handlePlayerInput(Component_Movement);
				}
				else if (entity.hasComponent("ai")) {
					this.handleAI(Component_Movement);
					/*
					 * Hier muss dann die Gegnerbewegung behandelt werden. Meine
					 * Idee ist, dies über einen "Pseudo-KeyHandler" zu machen,
					 * dem dieselben Werte zuweisbar sind, wie dem echten.
					 * Am besten ginge das über ein Interface mit Funktionen wie
					 * "boolean getUp()", welches dann vom echten und vom
					 * unechten KeyHandler implementiert wird.
					 */
				}
				this.handleOutOfLevel(Component_Movement);
			}
		}
		
		// Nun die Entitäten bewegen.
		for (Entity entity : this.getEntitiesByType("movement")) {
			this.moveEntity(entity);
		}
		
		// Alle Kollisionen sammeln und Events verschicken.
		List<Object_Event> collisions = this.getCollisions();
		this.addEvents(collisions);
		
		// Jetzt solange illegale Kollisionen behandeln, bis alle behoben sind.
		// Schleife läuft höchstens N-mal durch, wobei N = Anzahl Entitäten.
		// Die Laufzeitkomplexität liegt aber (leider) bei höchstens N^3!
		List<Object_Event> illegalCollisions;
		while(true) {
			collisions = this.getCollisions();
			illegalCollisions = this.getIllegalCollisions(collisions);
			if (illegalCollisions.isEmpty()) break;
			this.resolveIllegalCollisions(illegalCollisions);
		}
	}

	
	
	/*
	 * Privates.
	 */
	
	private List<Object_Event> getCollisions() {
		List<Object_Event> collisions = new LinkedList<Object_Event>();
		/*
		 * Die beiden geschachtelten For-Schleifen überprüfen für jedes Paar an
		 * Entitäten (die auch eine Movement-Komponente besitzen), ob diese
		 * kollidiert sind. Notwendig für eine Kollision ist ein gesetztes Flag
		 * "collidable".
		 */
		for (int i = 1; i < this.getEntitiesByType("movement").size(); i++) {
			Component_Movement Component_Movement1 = (Component_Movement) this.getEntitiesByType("movement").get(i).getComponent("movement");
			if (Component_Movement1.isCollidable()) {
				for (int j = 0; j < i; j++) {
					Component_Movement Component_Movement2 = (Component_Movement) this.getEntitiesByType("movement").get(j).getComponent("movement");
					// Haben beide Entitäten dieselbe Position?
//					if (Component_Movement1.x == Component_Movement2.x 
//							&& Component_Movement1.y == Component_Movement2.y
//							&& Component_Movement2.isCollidable()) {
					if (Component_Movement2.isCollidable()
							&& ((Component_Movement1.getX() == Component_Movement2.getX()
									&& Component_Movement1.getY() == Component_Movement2.getY())
								|| this.changedPlaces(Component_Movement1, Component_Movement2))) {
						collisions.add(new Object_Event(EventType.COLLISION,Component_Movement1.getEntity(),Component_Movement2.getEntity()));
						collisions.add(new Object_Event(EventType.COLLISION,Component_Movement2.getEntity(),Component_Movement1.getEntity()));
					}
				}
			}
		}
		return collisions;
	}
	
	/*
	 * Haben zwei Entitäten einfach die Plätze getauscht? Diese Bedingung ist
	 * auch eine Kollision.
	 */
	private boolean changedPlaces(Component_Movement Component_Movement1, Component_Movement Component_Movement2) {
		return Component_Movement1.getX() == Component_Movement2.getOldX()
				&& Component_Movement1.getY() == Component_Movement2.getOldY()
				&& Component_Movement2.getX() == Component_Movement1.getOldX()
				&& Component_Movement2.getY() == Component_Movement1.getOldY();
	}
	
	/*
	 * Gibt eine Liste zurück, die Events enthält, welche die Teilnehmer einer
	 * "illegalen" Kollision beinhalten.
	 */
	private List<Object_Event> getIllegalCollisions(List<Object_Event> collisions) {
		List<Object_Event> illegalCollisions = new LinkedList<Object_Event>();
		
		for (Object_Event event : collisions) {
			Component_Movement Component_Movement1 = (Component_Movement) event.getActor().getComponent("movement");
			Component_Movement Component_Movement2 = (Component_Movement) event.getUndergoer().getComponent("movement");
			if (this.isIllegalCollision(Component_Movement1, Component_Movement2)) {
				illegalCollisions.add(new Object_Event(EventType.ILLEGALCOLLISION,event.getActor(),event.getUndergoer()));
			}
		}
		return illegalCollisions;
	}
	
	private void handleAI(Component_Movement Component_Movement) {
		CompAI compAI = (CompAI) Component_Movement.getEntity().getComponent("ai");
		if (Component_Movement.isMoveable()) {
			int key = compAI.getKey();
			this.handleInput(Component_Movement, key);
		}
	}
	
	
	
	/*
	 * Setzt Tasteneingaben in Bewegungen um. Wird auch für die Gegnerbewegung
	 * verwendet.
	 */
	private void handleInput(Component_Movement Component_Movement, int key) {
		int dx = 0;
		int dy = 0;
		switch(key) {
		case 1: // UP
			if (Component_Movement.getOrientation() != 1)	Component_Movement.setOrientation(1);
			else {
				dx = 0;
				dy = -1;
			}
			break;
		case 2: // DOWN
			if (Component_Movement.getOrientation() != 2) Component_Movement.setOrientation(2);
			else {
				dx = 0;
				dy = 1;				
			}
			break;
		case 3: // LEFT
			if (Component_Movement.getOrientation() != 3) Component_Movement.setOrientation(3);
			else {
				dx = -1;
				dy = 0;					
			}
			break;
		case 4: // RIGHT
			if (Component_Movement.getOrientation() != 4) Component_Movement.setOrientation(4);
			else {
				dx = 1;
				dy = 0;					
			}
			break;
		default:
			dx = 0;
			dy = 0;
		}
		Component_Movement.setdX(dx);
		Component_Movement.setdY(dy);
	}
	
	/*
	 * Überprüft, ob eine Entität bewegt werden darf.
	 */
	private void handleMoveability(Component_Movement Component_Movement) {
		if (Component_Movement.getTick() == 0) {
			Component_Movement.setMoveable();
			Component_Movement.unsetMoving();
		}
		else {
			Component_Movement.unsetMoveable();
			Component_Movement.setMoving();
			Component_Movement.tick();
		}
	}
	
	/*
	 * Setzt den Bewegungsvektor wieder zurück, falls die neue Position auf
	 * einer nicht begehbaren Kachel oder außerhalb des Levels wäre.
	 */
	private void handleOutOfLevel(Component_Movement Component_Movement) {
		int newX = Component_Movement.getX()+Component_Movement.getdX();
		int newY = Component_Movement.getY()+Component_Movement.getdY();
		if (!this.walkable(newX, newY)) {
			Component_Movement.setdX(0);
			Component_Movement.setdY(0);
		}
		
	}
	
	/*
	 * Setzt Tasteneingaben vom Keyhandler in Bewegungen um.
	 */
	private void handlePlayerInput(Component_Movement Component_Movement) {
		if (Component_Movement.isMoveable()) {
			int key = this.keyHandler.getLast();
			this.handleInput(Component_Movement,key);
		}		
	}
	
	/*
	 * Definiert, welche Kollisionen als illegal gelten.
	 */
	private boolean isIllegalCollision(Component_Movement Component_Movement1, Component_Movement Component_Movement2) {
		if (!Component_Movement1.walkable && !Component_Movement2.walkable) return true;
		return false;
	}
	
	/*
	 * Bewegt eine Entität gemäß ihrer Richtungsdaten (dx und dy).
	 */
	private void moveEntity(Entity entity) {
		Component_Movement Component_Movement = (Component_Movement) entity.getComponent("movement");
		if (Component_Movement.isMoveable()) {
			int dx = Component_Movement.getdX();
			int dy = Component_Movement.getdY();
			
			Component_Movement.addToX(dx);
			Component_Movement.addToY(dy);
			if (dx != 0 || dy != 0) {
				Component_Movement.setMoving();
				Component_Movement.unsetMoveable();
				Component_Movement.resetTick();
			}
			else {
				Component_Movement.unsetMoving();
				Component_Movement.setMoveable();
				Component_Movement.nullifyTick();
			}
		}
	}
	
	/*
	 * Setzt die aktuelle Position auf den Stand vor der letzten Bewegung zurück.
	 */
	private void resetPosition(Component_Movement Component_Movement) {
		Component_Movement.setX(Component_Movement.getOldX());
		Component_Movement.setY(Component_Movement.getOldY());
		Component_Movement.setdX(0);
		Component_Movement.setdY(0);
		Component_Movement.nullifyTick();
		Component_Movement.unsetMoving();
		Component_Movement.setMoveable();
	}
	
	/*
	 * Bestimmt, wie eine illegale Kollision aufgelöst werden soll.
	 */
	private void resolveIllegalCollisions(List<Object_Event> illegalCollisions) {
		for (Object_Event event : illegalCollisions) {
			Component_Movement Component_Movement1 = (Component_Movement) event.getActor().getComponent("movement");
			Component_Movement Component_Movement2 = (Component_Movement) event.getUndergoer().getComponent("movement");
			
			this.resetPosition(Component_Movement1);
			this.resetPosition(Component_Movement2);
		}
	}
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return ((Scene_Level) this.scene).getCurrentLevel().isPassable(x, y);
	}
}