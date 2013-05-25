import java.awt.Graphics;


abstract class Abstract_SubScene implements Interface_SubScene {

	protected Object_Game game;
	protected Graphics screen;
	protected Object_KeyHandler keyhandler;
	
	Abstract_SubScene(Object_Game game) {
		this.game = game;
		this.screen = game.getScreen().getBuffer().getGraphics();
		this.keyhandler = game.getKeyHandler();
	}
	
	abstract public void updateData();
	abstract public void updateScreen();
	
}
