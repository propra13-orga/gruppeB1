import java.awt.Graphics;

public class Scene_GameOver extends Scene {
	private Scene_Level scene;
	public Scene_GameOver(Game g, Scene_Level scene) {
		super(g);
		this.scene = scene;
	}

	@Override
	public void update() {
		Graphics g = game.getScreen().getBuffer().getGraphics();
		g.drawString("GAME OVER",Screen.SCREEN_H/2,Screen.SCREEN_W/2);

	}

}
