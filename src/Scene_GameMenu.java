
public class Scene_GameMenu extends Abstract_Scene {

	private Scene_Level current_map;
	private SubScene_WindowSelectable menu;

	Scene_GameMenu(Object_Game game, Scene_Level m) {
		super(game);
		current_map = m;
		menu = new SubScene_WindowSelectable(20,20,game);
		menu.addCommand("Spiel speichern");
		menu.addCommand("Spiel beenden");
		menu.addCommand("Inventar");
		menu.addCommand("Ausrüstung");
		menu.addCommand("Optionen");
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
		if (menu.EXECUTED) menu.updateData();
		else {
			if (menu.CANCELED) {
				//Menü wurde beendet
				this.keyhandler.clear();
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(current_map);
				return;
			}
			else {
				//Ein Menüpunkt wurde bstätigt
				switch (menu.cursor){
				case 0: //Spiel speichern
					System.out.println("Speichere Spiel... :D:D Nicht!");
					menu.EXECUTED = true;
					break;
				case 1: //Spiel beenden
					this.keyhandler.freeze(Object_KeyHandler.KEY_ENTER, 40);
					game.quit();
					return;
				default:
					System.out.println("Nur zu Testzwecken!");
					menu.EXECUTED = true;
				}
			}
		}
	}

	@Override
	public void updateScreen() {
		game.getScreen().clear();
		menu.updateScreen();
	}
}
