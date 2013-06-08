
public class Scene_AnimationManagerTest extends Abstract_Scene {

	Window_Selectable menu;
	Object_AnimationManager manager;
	
	Scene_AnimationManagerTest(Object_Game game) {
		super(game);
		this.manager = new Object_AnimationManager(this.game);
		this.keyhandler.clear();
		menu = new Window_Selectable(0,0,game);
		menu.EXIT_POSSIBLE = false;
		menu.addCommand("Animation abspielen");
		menu.addCommand("Animation langsam abspielen");
		menu.addCommand("Beenden");
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
		if (menu.EXECUTED) {
			menu.updateData();
		}
		else {
			switch (menu.cursor){
			case 0: //Spiel starten
				this.manager.playAnimation("test_animation", 1);
				this.menu.EXECUTED = true;
				break;
			case 1:
				this.manager.playAnimation("test_animation", 5);
				this.menu.EXECUTED = true;
				break;
			case 2: //Spiel beenden
				game.quit();
			}
		}
	}

	@Override
	public void updateScreen() {
		this.game.getScreen().clear();
		menu.updateScreen();
	}
	
}
