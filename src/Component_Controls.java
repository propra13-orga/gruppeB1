/**
 * Entitäten mit dieser Komponente können gesteuert werden. Der Spieler über die
 * Tastatur, NPCs über die KI.
 * 
 * @author Victor Persien
 */
class Component_Controls extends Abstract_Component {

	private static final long serialVersionUID = -4498451329055383926L;

	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "controls".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 */
	public Component_Controls(Entity entity, System_Component system) {
		super("controls",entity,system);
	}
	
	public Component_Controls(Component_Controls compControls) {
		super(compControls);
	}
	
	public Component_Controls(Abstract_Component comp, Entity entity, System_Component system) {
		super(comp.getType(),entity,system);
	}
}