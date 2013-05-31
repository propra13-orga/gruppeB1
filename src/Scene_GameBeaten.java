
public class Scene_GameBeaten extends Abstract_Scene {
	
	SubScene_WindowSelectable menu;
	
	public Scene_GameBeaten(Object_Game game) {
		super(game);
		menu = new SubScene_WindowSelectable(0,0,game);
		menu.EXIT_POSSIBLE = false;
		menu.addCommand("Spiel neu starten");
		menu.addCommand("Zurück zum Startmenü");
		menu.addCommand("Spiel beenden");
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
		if (menu.EXECUTED) menu.updateData();
		else {
			switch (menu.cursor) {
			case 0: //Neu starten
				game.switchScene(new Scene_Level(game));
				return;
			case 1: // Hauptmenü
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
