import java.awt.Graphics;


abstract class Abstract_Update implements IUpdate {

	protected Object_Game				game;
	protected Graphics					screen;
	protected Object_KeyHandler			keyhandler;
	protected Object_SoundManager		soundmanager;
	protected Object_AnimationManager	animationmanager;
	
	Abstract_Update(Object_Game game) {
		this.game				= game;
		this.screen				= game.getScreen().getBuffer().getGraphics();
		this.keyhandler			= game.getKeyHandler();
		this.soundmanager		= game.getSoundManager();
		this.animationmanager	= game.getAnimationManager();
	}
	
	abstract public void updateData();
	abstract public void updateScreen();
	
}
