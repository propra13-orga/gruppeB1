/**
 * Diese Szene soll der Darstellung von Dialogen im Spiel dienen. Der Szene wird
 * ein String übergeben, welcher auf dem Bildschirm ausgegegeben wird. Passt
 * der String nicht auf den Bildschirm, so wird durch Drücken von ENTER "eine
 * Seite umgeschlagen".
 * 
 * @author Victor Persien
 */


public class Scene_Dialog extends Abstract_Scene {
	private String dialog;
	private Window_DialogBox message;
	private Scene_Level parent;
	
	/**
	 * Konstruktor
	 * 
	 * @param game		Das aktuelle Spielobjekt.
	 * @param parent	Die Scene, von der aus diese Scene aufgerufen wurde.
	 * @param dialog	Der String, der angezeigt werden soll.
	 */
	public Scene_Dialog(Object_Game game, Scene_Level parent, String dialog) {
		super(game);
		this.parent = parent;
		this.dialog = dialog;
		this.message = new Window_DialogBox(dialog, this.game);
	}

	@Override
	public void onStart() { }

	@Override
	public void onExit() { }

	@Override
	public void updateData() {
		switch(this.game.getKeyHandler().getLast()) {
		case Object_KeyHandler.KEY_ENTER:
			/*
			 * Setze darzustellenden Text auf den Rest, der noch nicht angezeigt
			 * wurde.
			 */
			this.dialog = message.getRemainder();
			/*
			 * Wenn der Text "verbraucht" ist, beende die Szene, ansonsten
			 * blättere weiter.
			 */
			if (this.dialog.equals("")) {
				this.keyhandler.clear();
				this.game.switchScene(this.parent);
			}
			else {
				this.message = new Window_DialogBox(this.dialog, this.game);
			}
			this.keyhandler.clear();
			this.keyhandler.freeze(Object_KeyHandler.KEY_ENTER, 40);
			break;
		default:
			break;
		}
	}

	@Override
	public void updateScreen() {
		this.message.updateScreen();
	}

}
