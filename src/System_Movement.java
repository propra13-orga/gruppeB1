import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

class System_Movement extends System_Component {
	protected Object_KeyHandler keyHandler;
	private HashMap<String,List<Entity>> entityPositions;
	
	public System_Movement(Abstract_Scene scene, Object_KeyHandler keyHandler) {
		super(scene,"controls","movement");
		this.keyHandler = keyHandler;
		this.entityPositions = new HashMap<String,List<Entity>>();
		this.listenTo(EventType.CMD_UP, EventType.CMD_DOWN, EventType.CMD_LEFT, EventType.CMD_RIGHT,EventType.CHANGEROOM);
	}
	
	@Override
	public void update() {		
		// Alle Bewegungsvektoren zurücksetzen.
		for (Entity entity : this.getEntitiesByType("movement")) {
			Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");
			compMovement.setdX(0);
			compMovement.setdY(0);
			this.handleMoveability(compMovement);
		}
		
		// Tastatureingabe verarbeiten.
		this.handlePlayerInput();
		
		// Bewegungsevents verarbeiten.
		this.handleEvents();
		
		// Nun die Entitäten bewegen.
		for (Entity entity : this.getEntitiesByType("movement")) {
			this.moveEntity(entity);
		}
		
		// Alle Kollisionen sammeln und Events verschicken.
		List<Event> collisions = this.getCollisions();
		this.addEvents(collisions);
		
		// Jetzt solange illegale Kollisionen behandeln, bis alle behoben sind.
		// Schleife läuft höchstens N-mal durch, wobei N = Anzahl Entitäten.
		// Die Laufzeitkomplexität liegt aber (leider) bei N^3 (aber höchstens)!
		List<Event> illegalCollisions;
		while(true) {
			collisions = this.getCollisions();
			illegalCollisions = this.getIllegalCollisions(collisions);
			if (illegalCollisions.isEmpty()) break;
			this.resolveIllegalCollisions(illegalCollisions);
		}
		
		this.retrieveEntityPositions();
	}
	
	/*
	 * Gibt alle Entitäten zurück, die sich an Position xy befinden.
	 */
	public List<Entity> getEntitiesAt(int x, int y) {
		if (this.entityPositions.containsKey(String.format("%dx%d",x,y))) {
			return this.entityPositions.get(String.format("%dx%d",x,y));
		}
		return new LinkedList<Entity>();
	}	
	
	
	
	/*
	 * Gibt ein Array zurück, die die Positionsdaten aller Entitäten
	 * enthält.
	 */
	public int[][] getEntityPositions() {
		int w = this.getScene().getCurrentRoom().getWidth();
		int h = this.getScene().getCurrentRoom().getHeight();
		int[][] positions = new int[h][w];
		for (String xy : this.entityPositions.keySet()) {
			String[] xxx = xy.split("x");
			int x = Integer.parseInt(xxx[0]);
			int y = Integer.parseInt(xxx[1]);
			if (x > -1 && y > -1 && x < w && y < h) positions[y][x] = 1;
		}
		return positions;
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
			Component_Movement compMovement1 = (Component_Movement) this.getEntitiesByType("movement").get(i).getComponent("movement");
			if (compMovement1.isCollidable()) {
				for (int j = 0; j < i; j++) {
					Component_Movement compMovement2 = (Component_Movement) this.getEntitiesByType("movement").get(j).getComponent("movement");
					// Haben beide Entitäten dieselbe Position?
//					if (Component_Movement1.x == Component_Movement2.x 
//							&& Component_Movement1.y == Component_Movement2.y
//							&& Component_Movement2.isCollidable()) {
					if (compMovement2.isCollidable()
							&& ((compMovement1.getX() == compMovement2.getX()
									&& compMovement1.getY() == compMovement2.getY())
								|| this.changedPlaces(compMovement1, compMovement2))) {
						collisions.add(new Event(EventType.COLLISION,compMovement1.getEntity(),compMovement2.getEntity(), null));
						collisions.add(new Event(EventType.COLLISION,compMovement2.getEntity(),compMovement1.getEntity(), null));
					}
				}
			}
		}
		return collisions;
	}
	
	/*
	 * Gibt die Movement-Komponente einer Entität zurück.
	 */
	private Component_Movement getMovement(Entity entity) {
		return (Component_Movement) entity.getComponent("movement");
	}
	
	/*
	 * Haben zwei Entitäten einfach die Plätze getauscht? Diese Bedingung ist
	 * auch eine Kollision.
	 */
	private boolean changedPlaces(Component_Movement compMovement1, Component_Movement compMovement2) {
		return compMovement1.getX() == compMovement2.getOldX()
				&& compMovement1.getY() == compMovement2.getOldY()
				&& compMovement2.getX() == compMovement1.getOldX()
				&& compMovement2.getY() == compMovement1.getOldY();
	}
	
	/*
	 * Gibt eine Liste zurück, die Events enthält, welche die Teilnehmer einer
	 * "illegalen" Kollision beinhalten.
	 */
	private List<Event> getIllegalCollisions(List<Event> collisions) {
		List<Event> illegalCollisions = new LinkedList<Event>();
		
		for (Event event : collisions) {
			Component_Movement compMovement1 = (Component_Movement) event.getActor().getComponent("movement");
			Component_Movement compMovement2 = (Component_Movement) event.getUndergoer().getComponent("movement");
			if (this.isIllegalCollision(compMovement1, compMovement2)) {
				illegalCollisions.add(new Event(EventType.ILLEGALCOLLISION,event.getActor(),event.getUndergoer(), null));
			}
		}
		return illegalCollisions;
	}
	
	
	/*
	 * Setzt bestimmte Events in Bewegungen um.
	 */
	private void handleEvents() {
		for (Event event : this.getEvents(EventType.CMD_UP,EventType.CMD_DOWN,EventType.CMD_LEFT,EventType.CMD_RIGHT)) {
			Entity entity = event.getActor();
			Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");

			switch(event.getType()) {
			case CMD_UP:
				if (compMovement.getOrientation() != 1)	compMovement.setOrientation(1);
				else {
					compMovement.setdY(-1);
				}
				break;
			case CMD_DOWN:
				if (compMovement.getOrientation() != 2)	compMovement.setOrientation(2);
				else {
					compMovement.setdY(1);
				}
				break;
			case CMD_LEFT:
				if (compMovement.getOrientation() != 3)	compMovement.setOrientation(3);
				else {
					compMovement.setdX(-1);
				}
				break;
			case CMD_RIGHT:
				if (compMovement.getOrientation() != 4)	compMovement.setOrientation(4);
				else {
					compMovement.setdX(1);
				}
				break;
			default:
				break;
			}
			this.handleOutOfLevel(compMovement);
		}
	}
	
	/*
	 * Überprüft, ob eine Entität bewegt werden darf.
	 */
	private void handleMoveability(Component_Movement compMovement) {
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
	 * Setzt den Bewegungsvektor wieder zurück, falls die neue Position auf
	 * einer nicht begehbaren Kachel oder außerhalb des Levels wäre.
	 */
	private void handleOutOfLevel(Component_Movement compMovement) {
		int newX = compMovement.getX()+compMovement.getdX();
		int newY = compMovement.getY()+compMovement.getdY();
		if (!this.walkable(newX, newY)) {
			compMovement.setdX(0);
			compMovement.setdY(0);
		}
		
	}
	
	/*
	 * Setzt Tasteneingaben vom Keyhandler in Events um.
	 */
	private void handlePlayerInput() {
		Entity player = this.getScene().getPlayer();
		if (player.hasComponent("movement") && player.hasComponent("controls")) {
			Component_Movement compMovement = (Component_Movement) player.getComponent("movement");
			if (compMovement.isMoveable()) {
				switch(this.keyHandler.getLast()) {
				case Object_KeyHandler.KEY_UP:
					this.addEvent(new Event(EventType.CMD_UP,player,null, null));
					break;
				case Object_KeyHandler.KEY_DOWN:
					this.addEvent(new Event(EventType.CMD_DOWN,player,null, null));
					break;
				case Object_KeyHandler.KEY_LEFT:
					this.addEvent(new Event(EventType.CMD_LEFT,player,null, null));
					break;
				case Object_KeyHandler.KEY_RIGHT:
					this.addEvent(new Event(EventType.CMD_RIGHT,player,null, null));
					break;
				case Object_KeyHandler.KEY_ENTER:
					this.addEvent(new Event(EventType.CMD_ACTION,player,null, null));
					break;
				default:
					break;
				}
			}
		}		
	}
	
	/*
	 * Definiert, welche Kollisionen als illegal gelten.
	 */
	private boolean isIllegalCollision(Component_Movement compMovement1, Component_Movement compMovement2) {
		if (!compMovement1.isVisible() || !compMovement2.isVisible()) return false;
		if ((!compMovement1.walkable && !compMovement2.walkable)) return true;
		return false;
	}
	
	/*
	 * Bewegt eine Entität gemäß ihrer Richtungsdaten (dx und dy).
	 */
	private void moveEntity(Entity entity) {
		Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");
		if (compMovement.isMoveable() && compMovement.isVisible()) {
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
				compMovement.nullifyTick();
			}
		}
	}
	
	/*
	 * Setzt die aktuelle Position auf den Stand vor der letzten Bewegung zurück.
	 */
	private void resetPosition(Component_Movement compMovement) {
		compMovement.setX(compMovement.getOldX());
		compMovement.setY(compMovement.getOldY());
		compMovement.setdX(0);
		compMovement.setdY(0);
		compMovement.nullifyTick();
		compMovement.unsetMoving();
		compMovement.setMoveable();
	}
	
	/*
	 * Aktualisiert die Hashtabelle entityPositions, die alle Positionsdaten
	 * enthält.
	 */
	private void retrieveEntityPositions() {
		this.entityPositions.clear();
		for (Entity entity : this.getEntitiesByType("movement")) {
			Component_Movement compMovement = this.getMovement(entity);
			int[] xy = {compMovement.getX(),compMovement.getY()};
			if (!entityPositions.containsKey(xy)) {
				List<Entity> tmplist = new LinkedList<Entity>();
				tmplist.add(entity);
				this.entityPositions.put(String.format("%dx%d",xy[0],xy[1]), tmplist);
			}
			else this.entityPositions.get(String.format("%dx%d",xy[0],xy[1])).add(entity);
		}
	}
	
	/*
	 * Bestimmt, wie eine illegale Kollision aufgelöst werden soll.
	 */
	private void resolveIllegalCollisions(List<Event> illegalCollisions) {
		for (Event event : illegalCollisions) {
			Component_Movement compMovement1 = (Component_Movement) event.getActor().getComponent("movement");
			Component_Movement compMovement2 = (Component_Movement) event.getUndergoer().getComponent("movement");
			
			this.resetPosition(compMovement1);
			this.resetPosition(compMovement2);
		}
	}
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return ((Scene_Level) this.scene).getCurrentRoom().isPassable(x, y);
	}
}