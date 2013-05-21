import java.util.LinkedList;
import java.util.List;

class MovementSystem extends ComponentSystem {
	protected KeyHandler keyHandler;
	
	public MovementSystem(Scene scene, KeyHandler keyHandler) {
		super(scene,"controls","movement");
		this.keyHandler = keyHandler;
	}
	
	@Override
	public void update() {
		// Erst die Eingaben behandeln.
		for (Entity entity : this.getEntitiesByType("controls")) {
			if (entity.hasComponent("movement")) {
				CompMovement compMovement = (CompMovement) entity.getComponent("movement");
				this.handleMoveability(compMovement);
				if (entity.isPlayer()) {
					this.handlePlayerInput(compMovement);
				}
				else {
					/*
					 * Hier muss dann die Gegnerbewegung behandelt werden. Meine
					 * Idee ist, dies über einen "Pseudo-KeyHandler" zu machen,
					 * dem dieselben Werte zuweisbar sind, wie dem echten.
					 * Am besten ginge das über ein Interface mit Funktionen wie
					 * "boolean getUp()", welches dann vom echten und vom
					 * unechten KeyHandler implementiert wird.
					 */
				}
			}
		}
		
		// Nun die Entitäten bewegen.
		for (Entity entity : this.getEntitiesByType("movement")) {
			this.moveEntity(entity);
		}
		
		// Alle Kollisionen sammeln und Events verschicken.
		List<Event> collisions = this.getCollisions();
		this.addEvents(collisions);
		
		// Jetzt solange illegale Kollisionen behandeln, bis alle behoben sind.
		// Schleife läuft höchstens N-mal durch, wobei N = Anzahl Entitäten.
		// Die Laufzeitkomplexität liegt aber (leider) bei höchstens N^3!
		List<Event> illegalCollisions;
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
	
	private List<Event> getCollisions() {
		List<Event> collisions = new LinkedList<Event>();
		/*
		 * Die beiden geschachtelten For-Schleifen überprüfen für jedes Paar an
		 * Entitäten (die auch eine Movement-Komponente besitzen), ob diese
		 * kollidiert sind. Notwendig für eine Kollision ist ein gesetztes Flag
		 * "collidable".
		 */
		for (int i = 1; i < this.getEntitiesByType("movement").size(); i++) {
			CompMovement compMovement1 = (CompMovement) this.getEntitiesByType("movement").get(i).getComponent("movement");
			if (compMovement1.isCollidable()) {
				for (int j = 0; j < i; j++) {
					CompMovement compMovement2 = (CompMovement) this.getEntitiesByType("movement").get(j).getComponent("movement");
					// Haben beide Entitäten dieselbe Position?
					if (compMovement1.x == compMovement2.x 
							&& compMovement1.y == compMovement2.y
							&& compMovement2.isCollidable()) {
						collisions.add(new Event(EventType.COLLISION,compMovement1.getEntity(),compMovement2.getEntity()));
						collisions.add(new Event(EventType.COLLISION,compMovement2.getEntity(),compMovement1.getEntity()));
					}
				}
			}
		}
		return collisions;
	}
	
	
	/*
	 * Gibt eine Liste zurück, die Events enthält, welche die Teilnehmer einer
	 * "illegalen" Kollision beinhalten.
	 */
	private List<Event> getIllegalCollisions(List<Event> collisions) {
		List<Event> illegalCollisions = new LinkedList<Event>();
		
		for (Event event : collisions) {
			CompMovement compMovement1 = (CompMovement) event.getActor().getComponent("movement");
			CompMovement compMovement2 = (CompMovement) event.getUndergoer().getComponent("movement");
			if (this.isIllegalCollision(compMovement1, compMovement2)) {
				illegalCollisions.add(new Event(EventType.ILLEGALCOLLISION,event.getActor(),event.getUndergoer()));
			}
		}
		return illegalCollisions;
	}
	
	/*
	 * Überprüft, ob eine Entität bewegt werden darf.
	 */
	private void handleMoveability(CompMovement compMovement) {
		if (compMovement.getTick() == 0) {
			compMovement.setMoveable();
			compMovement.unsetMoving();
		}
		else {
			compMovement.unsetMoveable();
			compMovement.setMoving();
			compMovement.tick();
		}
	}
	
	/*
	 * Setzt Tasteneingaben vom Keyhandler in Bewegungen um.
	 */
	private void handlePlayerInput(CompMovement compMovement) {
		if (compMovement.isMoveable()) {
			int dx = 0;
			int dy = 0;
			switch(this.keyHandler.getLast()) {
			case 1: // UP
				compMovement.setOrientation(1);
				dx = 0;
				dy = -1;
				break;
			case 2: // DOWN
				compMovement.setOrientation(2);
				dx = 0;
				dy = 1;
				break;
			case 3: // LEFT
				compMovement.setOrientation(3);
				dx = -1;
				dy = 0;
				break;
			case 4: // RIGHT
				compMovement.setOrientation(4);
				dx = 1;
				dy = 0;
				break;
			default:
				//compMovement.setOrientation(0);
				dx = 0;
				dy = 0;
			}
			compMovement.setdX(dx);
			compMovement.setdY(dy);
			/*
			 * Hier bereits Kollisionsabfrage mit der Map und im Falle einer
			 * Kollision Richtungsvektor auf 0 setzen. Unschön, aber andere
			 * Ansätze sind fehlerbehafteter.
			 */
			int newX = compMovement.getX()+dx;
			int newY = compMovement.getY()+dy;
			if (this.walkable(newX, newY)) {
				compMovement.setdX(dx);
				compMovement.setdY(dy);
			}
			else {
				compMovement.setdX(0);
				compMovement.setdY(0);
			}
		}		
	}
	
	/*
	 * Definiert, welche Kollisionen als illegal gelten.
	 */
	private boolean isIllegalCollision(CompMovement compMovement1, CompMovement compMovement2) {
		if (!compMovement1.walkable && !compMovement2.walkable) return true;
		return false;
	}
	
	/*
	 * Bewegt eine Entität gemäß ihrer Richtungsdaten (dx und dy).
	 */
	private void moveEntity(Entity entity) {
		CompMovement compMovement = (CompMovement) entity.getComponent("movement");
		if (compMovement.isMoveable()) {
			int dx = compMovement.getdX();
			int dy = compMovement.getdY();
			
			compMovement.addToX(dx);
			compMovement.addToY(dy);
			if (dx != 0 || dy != 0) {
				compMovement.setMoving();
				compMovement.unsetMoveable();
				compMovement.resetTick();
			}
			else {
				compMovement.unsetMoving();
				compMovement.setMoveable();
			}
		}
	}
	
	/*
	 * Setzt die aktuelle Position auf den Stand vor der letzten Bewegung zurück.
	 */
	private void resetPosition(CompMovement compMovement) {
		compMovement.setX(compMovement.getOldX());
		compMovement.setY(compMovement.getOldY());
		compMovement.setdX(0);
		compMovement.setdY(0);
		compMovement.nullifyTick();
		compMovement.unsetMoving();
		compMovement.setMoveable();
	}
	
	/*
	 * Bestimmt, wie eine illegale Kollision aufgelöst werden soll.
	 */
	private void resolveIllegalCollisions(List<Event> illegalCollisions) {
		for (Event event : illegalCollisions) {
			CompMovement compMovement1 = (CompMovement) event.getActor().getComponent("movement");
			CompMovement compMovement2 = (CompMovement) event.getUndergoer().getComponent("movement");
			
			this.resetPosition(compMovement1);
			this.resetPosition(compMovement2);
		}
	}
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return ((Scene_Level) this.scene).getCurrentLevel().isPassable(x, y);
	}
}