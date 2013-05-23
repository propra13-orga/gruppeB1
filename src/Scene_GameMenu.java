
public class Scene_GameMenu extends Scene {

	Scene_Level current_map;
	Window_Selectable menu;

	Scene_GameMenu(Game g, Scene_Level m) {
		super(g);
		current_map = m;
		menu = new Window_Selectable(20,20,g);
		menu.addCommand("Spiel speichern");
		menu.addCommand("Spiel beenden");
		menu.addCommand("Inventar");
		menu.addCommand("Ausrüstung");
		menu.addCommand("Optionen");
	}
	
	public void update() {
		if (menu.EXECUTED) menu.update();
		else {
			if (menu.CANCELED) {
				//Menü wurde beendet
				game.getKeyHandler().clear();
				game.getKeyHandler().freeze(KeyHandler.KEY_ESCAPE, 40);
				game.scene = current_map;
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
					game.getKeyHandler().freeze(KeyHandler.KEY_ENTER, 40);
					game.scene = new Scene_StartMenu(game);
					return;
				default:
					System.out.println("Nur zu Testzwecken!");
					menu.EXECUTED = true;
				}
			}
		}
	}
}
