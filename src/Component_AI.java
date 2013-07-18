
/**
 * 
 * Komponente, die Daten beinhaltet, die für die NPC-KI nötig sind.
 * 
 * @author Victor Persien
 *
 */
public class Component_AI extends Abstract_Component {
	
	private static final long serialVersionUID = 2542429345441972195L;
	
	/**
	 * Entität, die gerade im Fokus der Entität ist, der diese Komponente gehört.
	 * In der Regel handelt es sich bei Ersterer um die Spielerentität.
	 */
	private Entity focus;
	
	private int atkRange;
	private int viewDistance;
	
	/**
	 * Typ der KI. Zur Zeit gibt es nur "basic".
	 */
	private String aiType;
	
	/**
	 * Zustand, in dem sich der KI-Automat befindet.
	 */
	private StateType state;
	
	/**
	 * Der Zustand soll sich nur alle TICK_MAX ticks (Game-Loop-Zyklen) ändern.
	 */
	private int tick;
	private static final int TICK_MAX = 5;
	
	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "ai".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 * @param atkRange		Angriffsradius (auf der Map).
	 * @param viewDistance	Sichtradius (auf der Map).
	 */
	public Component_AI(Entity entity, System_Component system, int atkRange, int viewDistance) {
		super("ai", entity, system);
		this.atkRange = atkRange;
		this.viewDistance = viewDistance;
		
		this.aiType = "basic";
		this.state = StateType.IDLE;
		
		this.tick = 0;
	}

	public Component_AI(Abstract_Component comp) {
		super(comp);
	}
	
	public Entity getFocus() { return focus; }
	public int getAtkRange() { return atkRange; }
	public int getViewDistance() { return viewDistance; }
	public int getTick() { return this.tick; }
	public String getAIType() { return this.aiType; }
	public StateType getState() { return this.state; }

	public void setFocus(Entity focus) { this.focus = focus; }
	public void setAtkRange(int atkRange) { this.atkRange = atkRange; }
	public void setViewDistance(int viewDistance) { this.viewDistance = viewDistance; }
	public void setState(StateType state) { this.state = state; }
	public void tick() { 
		if (this.tick == TICK_MAX) this.tick = 0;
		else this.tick += 1;
	}
}
