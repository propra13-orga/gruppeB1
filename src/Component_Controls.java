/*
 * Entitäten mit dieser Komponente können gesteuert werden. Der Spieler über die
 * Tastatur, NPCs über die KI (kommt noch).
 */
class Component_Controls extends Abstract_Component {
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