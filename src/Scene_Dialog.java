/*
 * Scene_Dialog.java
 * 
 * Diese Szene soll der Darstellung von Dialogen im Spiel dienen. Der Szene wird
 * ein String (später ein komplexeres Objekt) übergeben, welcher auf dem
 * Bildschirm ausgegegeben wird.
 * 
 * Leider funktioniert das momentan noch nicht, da WindowMessage noch nicht
 * funktionstüchtig ist.
 */


public class Scene_Dialog extends Abstract_Scene {
	private String dialog;
	private SubScene_WindowMessage message;
	private Scene_Level parent;

	public Scene_Dialog(Object_Game game, Scene_Level parent, String dialog) {
		super(game);
		this.parent = parent;
		this.dialog = dialog;
		this.message = new SubScene_WindowMessage(dialog,25,25,this.game);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateData() {
		switch(this.game.getKeyHandler().getLast()) {
		case Object_KeyHandler.KEY_ENTER:
			/*
			 *  Scene wird beim Drücken von Enter beendet.
			 *  Später soll stattdessen umgeblättert werden, wenn der String zu
			 *  lang zur Anzeige ist. 
			 */
			this.keyhandler.clear();
			this.game.switchScene(this.parent);
			break;
		default:
			this.message.updateData();
			break;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void updateScreen() {
		this.message.updateScreen();
		// TODO Auto-generated method stub

	}

}
