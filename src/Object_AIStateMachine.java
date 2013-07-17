import java.util.HashMap;

class Object_AIStateMachine {
	HashMap<StateType,IAIState> stateTable;
	StateType initial;
	
	public Object_AIStateMachine(StateType initial) {
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
	
	public StateType getNext(StateType stateType, boolean bool) {
		return ((AIStateProposition)  this.stateTable.get(stateType)).getNext(bool);
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