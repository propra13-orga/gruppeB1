import java.awt.Graphics;


public class Scene_GameBeaten extends Scene {
	private Scene_Level scene;
	public Scene_GameBeaten(Game g, Scene_Level scene) {
		super(g);
		this.scene = scene;
	}

	@Override
	public void update() {
		String s = "Herzlichen Glückwunsch! Du hast das Spiel besiegt!";
		Graphics g = game.getScreen().getBuffer().getGraphics();
		g.drawString(s,Screen.SCREEN_H/2,Screen.SCREEN_W/2);
	}

}
