/*
 * Das RenderSystem richtet sich beim Zeichnen der Karte nach der Entität mit 
 * der Kamerakomponente. Muss genau einer Entität gegeben werden (sinnvollster-
 * weise dem Spieler).
 */

class Component_Camera extends Component {
	public Component_Camera(Object_Entity entity, System_Component system) {
		super("camera",entity,system);
	}
}