import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;


/*
 * AISystem.java
 * 
 * Kümmert sich um die KI. Bitte nicht großartig beachten, da kaum 
 * funktionstüchtig!
 */

class System_AI extends System_Component {
	int w;
	int h;
	int[][] walkability;
	int[][] entityPositions;
	HashMap<String,Object_AIStateMachine> stateMachines;

	public System_AI(Abstract_Scene scene) {
		super(scene, "ai");
		this.stateMachines = new HashMap<String,Object_AIStateMachine>();
		this.stateMachines.put("basic", this.simpleSMEnemy());
		
	}
	

	@Override
	public void update() {
		this.w = this.getScene().getCurrentRoom().getWidth();
		this.h = this.getScene().getCurrentRoom().getHeight();
		this.retrieveEntityPositions();
		this.retrieveWalkability();
		
		for (Entity entity : this.getEntitiesByType("ai")) {
			Component_AI compAI = (Component_AI) entity.getComponent("ai");
			if (compAI.getTick() == 0) {
				this.handleCurrentState(entity);
			}
			compAI.tick();
		}
		
	}
	
	/*
	 * Privates
	 */
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return this.getScene().getCurrentRoom().isPassable(x, y);
	}
	
	
	
	/*
	 * Pfadsuche.
	 */
	
	private void findPath(Entity entity, int toX, int toY) {
		Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");
		AStar astar = new AStar(compMovement.getX(),compMovement.getY(),toX,toY,this.walkability);
		astar.findPath();
		astar.reconstructPath();
	}
	
	private void retrieveEntityPositions() {
		this.entityPositions = this.getScene().getEntityPositions();
	}
	
	private void retrieveWalkability() {
		int[][] walkability = new int[this.h][this.w];
		for (int i=0;i<walkability[0].length;i++) {
			for (int j=0;j<walkability.length;j++) {
				if (!this.walkable(i, j)) walkability[j][i] = 1;
			}
		}
		this.walkability = walkability;
	}
	
//	private void initSMBasicEnemy() {
//		Object_AIStateMachine sm = new Object_AIStateMachine(StateType.IDLE);
//		sm.addState(StateType.IDLE, StateType.INSIGHT_ENEMY);
//		sm.addState(StateType.INSIGHT_ENEMY, StateType.TOFOCUS, StateType.IDLE);
//		sm.addState(StateType.TOFOCUS,StateType.WEAKENOUGH);
//		sm.addState(StateType.WEAKENOUGH,StateType.WITHINATKRANGE,StateType.FLEE);
//		sm.addState(StateType.WITHINATKRANGE,StateType.ATTACK,StateType.APPROACH);
//		sm.addState(StateType.APPROACH, StateType.WITHINRADIUS);
//		sm.addState(StateType.FLEE, StateType.OUTOFRANGE);
//		sm.addState(StateType.OUTOFRANGE, StateType.IDLE, StateType.FLEE);
//		
//		this.stateMachines.put("basicenemy", sm);
//	}
	
	public Object_AIStateMachine simpleSMEnemy() {
		Object_AIStateMachine sm = new Object_AIStateMachine(StateType.IDLE);
		sm.addState(StateType.IDLE, StateType.INSIGHT_ENEMY);
		sm.addState(StateType.INSIGHT_ENEMY, StateType.WITHINATKRANGE, StateType.IDLE);
		sm.addState(StateType.WITHINATKRANGE, StateType.ATTACK, StateType.APPROACH);
		sm.addState(StateType.APPROACH, StateType.INSIGHT_ENEMY);
		sm.addState(StateType.ATTACK, StateType.IDLE);
		
		return sm;
	}
	
	private void handleCurrentState(Entity entity) {
		Component_AI compAI = (Component_AI) entity.getComponent("ai");
		Object_AIStateMachine stateMachine = this.stateMachines.get(compAI.getAIType());
		StateType state = compAI.getState();
		compAI.setState(this.handleState(entity, stateMachine, state));
	}
	
	private StateType handleState(Entity entity, Object_AIStateMachine stateMachine, StateType state) {
		Component_AI compAI = (Component_AI) entity.getComponent("ai");
		StateType newState = null;
		switch (state) {
		case IDLE:
			// Nichts tun.
			System.out.println("IDLE");
			if (compAI.getTick() == 0) newState = stateMachine.getNext(StateType.IDLE);
			else newState = state;
			break;
		case OFFFOCUS:
			// Fokus leeren.
			compAI.setFocus(null);
			newState = stateMachine.getNext(StateType.OFFFOCUS);
			break;
		case ATTACK:
			// Angreifen.
			this.getScene().addEvent(new Event(EventType.OPEN_BATTLE,entity,compAI.getFocus(), null));
			newState = stateMachine.getNext(StateType.ATTACK);
			break;
		case APPROACH:
			// Nähern.
			this.approach(entity, compAI.getFocus());
			newState = stateMachine.getNext(StateType.APPROACH);
			System.out.println("NÄHERE MICH...");
			break;
		case FLEE:
			// Flüchten.
			newState = stateMachine.getNext(StateType.FLEE);
			break;
		case INSIGHT_ENEMY:
			// Gegner in Sichtweite?
			System.out.println("GEGNER IN SICHT?");
			Entity player = this.getScene().getPlayer();
			boolean insight = this.getDistance(entity, player) <= compAI.getViewDistance();
			newState = stateMachine.getNext(StateType.INSIGHT_ENEMY,insight);
			if (insight) {
				System.out.println("GEGNER IN SICHT!");
				compAI.setFocus(player);
			}
			break;
		case WEAKENOUGH:
			// Gegner (im Fokus) schwach genug?
			break;
		case WITHINATKRANGE:
			// Gegner (im Fokus) in Angriffsreichweite?
			System.out.println("GEGNER ANGREIFBAR?");
			boolean withinrange = this.getDistance(entity, compAI.getFocus()) <= compAI.getAtkRange();
			newState = stateMachine.getNext(state, withinrange);
			if (withinrange) {
				System.out.println("GEGNER ANGREIFBAR!");
			}
			break;
		case WITHINRADIUS:
			// Gegner (im Fokus) noch im Radius?
			break;
		case OUTOFRANGE:
			// Gegner (im Fokus) außer Reichweite?
			break;
		default:
			newState = state;
			break;
		}
		return newState;
	}
	
	/*
	 * Berechnet die (euklidische) Distanz zwischen zwei Entitäten.
	 */
	private int getDistance(Entity entity1, Entity entity2) {
		if (entity1.equals(entity2)) return 0;
		Component_Movement compMovement1 = (Component_Movement) entity1.getComponent("movement");
		Component_Movement compMovement2 = (Component_Movement) entity2.getComponent("movement");
		
		int xx = compMovement1.getX()-compMovement2.getX();
		int yy = compMovement1.getY()-compMovement2.getY();
		
		int d = (int) Math.sqrt((double) xx*xx+yy*yy);
		
		return d;
	}
	
	/*
	 * Bewegt eine Entität auf eine andere zu. Ganz billig, keine Pfadsuche nötig.
	 */
	private void approach(Entity entity1, Entity entity2) {
		Component_Movement compMovement1 = (Component_Movement) entity1.getComponent("movement");
		Component_Movement compMovement2 = (Component_Movement) entity2.getComponent("movement");
		
		if (!compMovement1.isMoveable()) return;
		
		int dx = compMovement2.getX()-compMovement1.getX();
		int dy = compMovement2.getY()-compMovement1.getY();
		
		int orientation = Static_AITools.vectorToOrientation(dx, dy);
		switch(orientation) {
		case 0:
			this.addEvent(new Event(EventType.CMD_UP,entity1,null));
			break;
		case 1:
			this.addEvent(new Event(EventType.CMD_DOWN,entity1,null));
			break;
		case 2:
			this.addEvent(new Event(EventType.CMD_LEFT,entity1,null));
			break;
		case 3:
			this.addEvent(new Event(EventType.CMD_RIGHT,entity1,null));
			break;
		}
	}
	
}





//class Component_AI extends Abstract_Component {
//	boolean busy;
//	StateType state;
//	String aiType;
//	
//	int[][] range;
//	int[][] fov;
//	
//	Entity focus;
//	int atkRange;
//	int viewDistance;
//	int radius;
//	double risk;
//	
//	public Component_AI(Entity entity, System_Component system,
//			String aiType, int atkRange, int viewDistance, int range,
//			double risk) {
//		super("ai",entity,system);
//		this.aiType = aiType;
//		this.atkRange = atkRange;
//		this.viewDistance = viewDistance;
//		this.radius = range;
//		this.risk = risk;
//		
//		
//		this.busy = false;
//		this.focus = null;
//	}
//	
//	public boolean isBusy() { return this.busy; }
//	public String getAIType() { return this.aiType; }
//	public Entity getFocus() { return this.focus; }
//	public int getATKRange() { return this.atkRange; }
//	public int getViewDistance() { return this.viewDistance; }
//	public int getRadius() { return this.radius; }
//	public int[][] getRange() { return this.range; }
//	public int[][] getFOV() { return this.fov; }
//	
//	public void setBusy(boolean bool) { this.busy = bool; }
//	public void setFocus(Entity entity) { this.focus = entity; }
//	public void setState(StateType state) { this.state = state; }
//	public void setRange(int[][] range) { this.range = range; }
//	public void setPOV(int[][] fov) { this.fov = fov; }
//}


