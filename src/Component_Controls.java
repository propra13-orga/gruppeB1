/*
 * Entitäten mit dieser Komponente können gesteuert werden. Der Spieler über die
 * Tastatur, NPCs über die KI (kommt noch).
 */
class Component_Controls extends Component {
	public Component_Controls(Object_Entity entity, System_Component system) {
		super("controls",entity,system);
	}
}