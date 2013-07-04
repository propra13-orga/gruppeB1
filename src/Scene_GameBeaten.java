
public class Scene_GameBeaten extends Abstract_Scene {
	
	Window_Menu menu;
	
	public Scene_GameBeaten(Object_Game game) {
		super(game);
		menu = new Window_Menu(game, "main");
		menu.addReturnCommand("Spiel neu starten");
		menu.addReturnCommand("Zur�ck zum Startmen�");
		menu.addReturnCommand("Spiel beenden");
		menu.center();
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
			menu.setupMenuPath();
			switch (menu.getCurrentCursor()) {
			case 0: //Neu starten
				game.switchScene(new Scene_Level(game,"level01"));
				return;
			case 1: // Hauptmen�
				game.switchScene(new Scene_StartMenu(game));
				return;
			case 2: //Ende
				game.quit();
			}
		}
	}

	@Override
	public void updateScreen() {
		this.game.getScreen().clear();
		menu.updateScreen();
		this.screen.drawString("GEWONNEN!!!", 300,100);
	}

}
