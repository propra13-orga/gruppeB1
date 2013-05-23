import java.awt.Color;
import java.awt.Graphics;

public class Scene_GameOver extends Scene {
	
	private Scene_Level scene;
	Window_Selectable menu;
	Graphics g;
	
	public Scene_GameOver(Game game, Scene_Level scene) {
		super(game);
		this.scene = scene;
		g = game.getScreen().getBuffer().getGraphics();
		menu = new Window_Selectable(0,0,game);
		menu.addCommand("Spiel neu starten");
		menu.addCommand("Spiel beenden");
		menu.center();
	}

	@Override
	public void update() {
		g.setColor(new Color(0,0,0));
		g.clearRect(0, 0, Screen.SCREEN_W, Screen.SCREEN_H);
		g.drawString("GAME OVER",Screen.SCREEN_W/2,20);
		if (menu.EXECUTED) menu.update();
		else {
			if (menu.CANCELED) game.scene = null;
			else {
				switch (menu.cursor) {
				case 0:
					game.scene = new Scene_Level(game);
					return;
				case 1:
					game.scene = null;
				}
			}
		}
	}

	
}
