/**
 * Das RenderSystem richtet sich beim Zeichnen der Karte nach der Entität mit 
 * der Kamerakomponente. Muss genau einer Entität gegeben werden 
 * (sinnvollsterweise dem Spieler).
 * 
 * @author Victor Persien
 */

class Component_Camera extends Abstract_Component {

	private static final long serialVersionUID = -574999479262605376L;
	
	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "Kamera".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 */
	public Component_Camera(Entity entity, System_Component system) {
		super("camera",entity,system);
	}
	
	public Component_Camera(Component_Camera compCamera) {
		super(compCamera);
	}
	
	public Component_Camera(Abstract_Component comp, Entity entity, System_Component system) {
		super(comp.getType(),entity,system);
	}
}