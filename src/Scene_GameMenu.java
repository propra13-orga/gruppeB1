public class Scene_GameMenu extends Abstract_Scene {

	private Scene_Level current_map;
	private Window_Menu menu;

	Scene_GameMenu(Object_Game game, Scene_Level m) {
		super(game);
		current_map = m;
		menu = new Window_Menu(game, "main");
		menu.addReturnCommand("Spiel speichern");
		menu.addReturnCommand("Spiel beenden");
		menu.addReturnCommand("Faehigkeiten");
		menu.addReturnCommand("Inventar");
		menu.addReturnCommand("Ausruestung");
		menu.addReturnCommand("Optionen");
		menu.topLeft();
		Window_Menu.setMainMenu(menu);
		menu.setExitPossible(true);
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
		if (menu.isExecuted()) menu.updateData();
		else {
			if (menu.isCanceled()) {
				//Menue wurde beendet
				this.keyhandler.clear();
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(current_map, true);
				return;
			}
			else {
				menu.setupMenuPath();
				switch (menu.getCurrentCursor()){
				case 0: //Spiel speichern
					this.current_map.serialize();
					System.out.println("Speichere Spiel... :D:D Nicht!");
					menu.restart();
					break;
				case 1: //Spiel beenden
					this.keyhandler.freeze(Object_KeyHandler.KEY_ENTER, 40);
					game.quit();
					return;
				case 2: //Faehigkeiten
					this.keyhandler.freeze(Object_KeyHandler.KEY_ENTER, 40);
					this.game.switchScene(new Scene_SkillMenu(this.game,this,this.current_map.getPlayer(),new Factory(this.current_map)));
					return;
				default:
					System.out.println("Nur zu Testzwecken!");
					menu.restart();
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
