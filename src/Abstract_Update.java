import java.awt.Graphics;


abstract class Abstract_Update implements IUpdate {

	protected Object_Game game;
	protected Graphics screen;
	protected Object_KeyHandler keyhandler;
	
	Abstract_Update(Object_Game game) {
		this.game = game;
		this.screen = game.getScreen().getBuffer().getGraphics();
		this.keyhandler = game.getKeyHandler();
	}
	
	abstract public void updateData();
	abstract public void updateScreen();
	
}
