import java.awt.Graphics;


public class Scene_GameBeaten extends Scene {
	
	Window_Selectable menu;
	Graphics g;
	
	public Scene_GameBeaten(Object_Game game) {
		super(game);
		g = game.getScreen().getBuffer().getGraphics();
		menu = new Window_Selectable(0,0,game);
		menu.EXIT_POSSIBLE = false;
		menu.addCommand("Spiel neu starten");
		menu.addCommand("Zurück zum Startmenü");
		menu.addCommand("Spiel beenden");
		menu.center();
	}

	@Override
	public void update() {
		g.clearRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
		g.drawString("GEWONNEN!!!", 300,100);
		if (menu.EXECUTED) menu.update();
		else {
			switch (menu.cursor) {
			case 0: //Neu starten
				game.scene = new Scene_Level(game);
				return;
			case 1: // Hauptmenü
				game.scene = new Scene_StartMenu(game);
				return;
			case 2: //Ende
				game.scene = null;
			}
		}
	}

}
