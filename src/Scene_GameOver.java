
public class Scene_GameOver extends Abstract_Scene {

	Window_Selectable menu;
	
	public Scene_GameOver(Object_Game game) {
		super(game);
		menu = new Window_Selectable(0,0,game);
		menu.addCommand("Spiel neu starten");
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
			if (menu.CANCELED) game.quit();
			else {
				switch (menu.cursor) {
				case 0:
					game.switchScene(new Scene_Level(game));
					return;
				case 1:
					game.quit();
				}
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
