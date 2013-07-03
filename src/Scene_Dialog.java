/*
 * Scene_Dialog.java
 * 
 * Diese Szene soll der Darstellung von Dialogen im Spiel dienen. Der Szene wird
 * ein String (später ein komplexeres Objekt) übergeben, welcher auf dem
 * Bildschirm ausgegegeben wird.
 * 
 */


public class Scene_Dialog extends Abstract_Scene {
	private String dialog;
	private Window_DialogBox message;
	private Scene_Level parent;

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
