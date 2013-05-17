//Testscene
//garnicht beachten

public class Scene_StartMenu extends Scene {
	
	Scene_StartMenu(Game g) {
		super(g);
	}
	
	public void update() {
		game.getScreen().getGraphics().clearRect(0, 0, 1000, 1000);
	}
}
