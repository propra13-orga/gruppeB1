
public class Scene_GameOver extends Abstract_Scene {

	Window_Menu menu;
	
	public Scene_GameOver(Object_Game game) {
		super(game);
		menu = new Window_Menu(game, "main");
		menu.addReturnCommand("Spiel neu starten", false);
		menu.addReturnCommand("Spiel beenden", false);
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
			this.menu.setupMenuPath();
			switch (this.menu.getCurrentCursor()) {
			case 0:
				game.switchScene(new Scene_Level(game));
				return;
			case 1:
				game.quit();
			}
		}
	}

	@Override
	public void updateScreen() {
		this.game.getScreen().clear();
		this.screen.drawString("GAME OVER",Object_Screen.SCREEN_W/2,20);
		menu.updateScreen();
	}
}
