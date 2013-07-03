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
	HashMap<String,AIStateMachine> stateMachines;

	public System_AI(Abstract_Scene scene) {
		super(scene, "ai");
		this.stateMachines = new HashMap<String,AIStateMachine>();
		this.initSMBasicEnemy();
		
	}
	

	@Override
	public void update() {
		this.w = this.getScene().getCurrentLevel().getWidth();
		this.h = this.getScene().getCurrentLevel().getHeight();
		this.retrieveEntityPositions();
		this.retrieveWalkability();
		
		for (Entity entity : this.getEntitiesByType("ai")) {
			Component_AI compAI = (Component_AI) entity.getComponent("ai");
			//
		}
		
//		System.out.println("---");
//		Static_AITools.printArray(Static_AITools.getFOV(this.walkability, 4, 6, 4, 10));
		
	}
	
	/*
	 * Privates
	 */
	
	/*
	 * Ist Kachel (x,y) begehbar?
	 */
	private boolean walkable(int x, int y) {
		return this.getScene().getCurrentLevel().isPassable(x, y);
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
	
	private void initSMBasicEnemy() {
		AIStateMachine sm = new AIStateMachine(StateType.IDLE);
		sm.addState(StateType.IDLE, StateType.INSIGHT_ENEMY);
		sm.addState(StateType.INSIGHT_ENEMY, StateType.TOFOCUS, StateType.IDLE);
		sm.addState(StateType.TOFOCUS,StateType.WEAKENOUGH);
		sm.addState(StateType.WEAKENOUGH,StateType.WITHINATKRANGE,StateType.FLEE);
		sm.addState(StateType.WITHINATKRANGE,StateType.ATTACK,StateType.APPROACH);
		sm.addState(StateType.APPROACH, StateType.WITHINRADIUS);
		sm.addState(StateType.FLEE, StateType.OUTOFRANGE);
		sm.addState(StateType.OUTOFRANGE, StateType.IDLE, StateType.FLEE);
		
		this.stateMachines.put("basicenemy", sm);
	}
	
	private void handleCurrentState(Entity entity) {
		Component_AI compAI = (Component_AI) entity.getComponent("ai");
		AIStateMachine stateMachine = this.stateMachines.get(compAI.getAIType());
		
	}
	
	private StateType handleState(Entity entity, AIStateMachine stateMachine, StateType state) {
		Component_AI compAI = (Component_AI) entity.getComponent("ai");
		StateType newState = null;
		switch (state) {
		case IDLE:
			// Nichts tun.
			newState = stateMachine.getNext(StateType.IDLE);
			break;
		case TOFOCUS:
			// Gegner fokussieren.
			newState = stateMachine.getNext(StateType.TOFOCUS);
			break;
		case OFFFOCUS:
			// Fokus leeren.
			newState = stateMachine.getNext(StateType.OFFFOCUS);
			break;
		case ATTACK:
			// Angreifen.
			this.getScene().addEvent(new Event(EventType.ATTACK,entity,compAI.getFocus()));
			newState = stateMachine.getNext(StateType.ATTACK);
			break;
		case APPROACH:
			// Nähern.
			newState = stateMachine.getNext(StateType.APPROACH);
			break;
		case FLEE:
			// Flüchten.
			newState = stateMachine.getNext(StateType.FLEE);
			break;
		case INSIGHT_ENEMY:
			// Gegner in Sichtweite?
			break;
		case WEAKENOUGH:
			// Gegner (im Fokus) schwach genug?
			break;
		case WITHINATKRANGE:
			// Gegner (im Fokus) in Angriffsreichweite?
			break;
		case WITHINRADIUS:
			// Gegner (im Fokus) noch im Radius?
			break;
		case OUTOFRANGE:
			// Gegner (im Fokus) außer Reichweite?
			break;
		}
		return StateType.IDLE;
	}

}




class Component_AI extends Abstract_Component {
	boolean busy;
	StateType state;
	String aiType;
	
	int[][] range;
	int[][] fov;
	
	Entity focus;
	int atkRange;
	int viewDistance;
	int radius;
	double risk;
	
	public Component_AI(Entity entity, System_Component system,
			String aiType, int atkRange, int viewDistance, int range,
			double risk) {
		super("ai",entity,system);
		this.aiType = aiType;
		this.atkRange = atkRange;
		this.viewDistance = viewDistance;
		this.radius = range;
		this.risk = risk;
		
		
		this.busy = false;
		this.focus = null;
	}
	
	public boolean isBusy() { return this.busy; }
	public String getAIType() { return this.aiType; }
	public Entity getFocus() { return this.focus; }
	public int getATKRange() { return this.atkRange; }
	public int getViewDistance() { return this.viewDistance; }
	public int getRadius() { return this.radius; }
	public int[][] getRange() { return this.range; }
	public int[][] getFOV() { return this.fov; }
	
	public void setBusy(boolean bool) { this.busy = bool; }
	public void setFocus(Entity entity) { this.focus = entity; }
	public void setState(StateType state) { this.state = state; }
	public void setRange(int[][] range) { this.range = range; }
	public void setPOV(int[][] fov) { this.fov = fov; }
}


class AIStateMachine {
	HashMap<StateType,IAIState> stateTable;
	StateType initial;
	
	public AIStateMachine(StateType initial) {
		this.stateTable = new HashMap<StateType,IAIState>();
		this.initial = initial;
	}
	
	public void addState(StateType stateType, StateType onTrue, StateType onFalse) {
		this.stateTable.put(stateType, new AIStateProposition(stateType,onTrue,onFalse));
	}
	
	public void addState(StateType stateType, StateType next) {
		this.stateTable.put(stateType, new AIStateProgram(stateType,next));
	}
	
	public StateType getNext(StateType stateType) {
		return ((AIStateProgram) this.stateTable.get(stateType)).getNext();
	}
	
	public void getNext(StateType stateType, boolean bool) {
		((AIStateProposition)  this.stateTable.get(stateType)).getNext(bool);
	}
}




enum StateType { 
	IDLE, 
	TOFOCUS, OFFFOCUS, ATTACK, APPROACH, FLEE,
	INSIGHT_ENEMY, WEAKENOUGH, WITHINATKRANGE, WITHINRADIUS,
	OUTOFRANGE
}

interface IAIState {
	public StateType getType();
}

class AIStateProgram implements IAIState {
	private StateType type;
	private StateType next;
	public AIStateProgram(StateType type, StateType next) {
		this.type = type;
		this.next = next;
	}
	
	@Override
	public StateType getType() { return this.type; }
	
	public StateType getNext() { return this.next; }
}

class AIStateProposition implements IAIState {
	private StateType type;
	private StateType onTrue;
	private StateType onFalse;
	public AIStateProposition(StateType type, StateType onTrue, StateType onFalse) {
		this.type = type;
		this.onTrue = onTrue;
		this.onFalse = onFalse;
	}
	
	@Override
	public StateType getType() { return this.type; }
	public StateType getNext(boolean bool) {
		if (bool) return this.onTrue();
		return this.onFalse();
	}
	
	public StateType onTrue() { return this.onTrue; }
	public StateType onFalse() { return this.onFalse; }
}