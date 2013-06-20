
public class Scene_BuyMenu extends Abstract_Scene {
	
	Scene_Level parent;
	private Window_Menu main_menu;
	private Window_Menu menu_buy;
	private Window_Menu menu_sell;
	

	public Scene_BuyMenu(Object_Game game, Scene_Level parent) {
		super(game);
		this.parent = parent;
		this.main_menu = new Window_Menu("main",0,0,game);
		this.menu_buy = new Window_Menu("buy",0,0,game);
		this.menu_sell = new Window_Menu("sell",0,0,game);
		
		this.main_menu.addMenuCommand("Kaufen", this.menu_buy);
		this.main_menu.addMenuCommand("Verkaufen", this.menu_sell);
		this.main_menu.addReturnCommand("Beenden");
		
		this.menu_buy.addReturnCommand("Groﬂer Heiltrank");
		this.menu_buy.addReturnCommand("Kleiner Heiltrank");
		this.menu_buy.addReturnCommand("Attacke 1");
		this.menu_buy.addReturnCommand("Attacke 2");
		/*Hier die Frage mit welchen mittel der player die items kauft*/
		
		this.menu_sell.addReturnCommand("Groﬂer Heiltrank");
		this.menu_sell.addReturnCommand("Kleiner Heiltrank");
		this.menu_sell.addReturnCommand("Attacke 1");
		this.menu_sell.addReturnCommand("Attacke 2");
		
		this.soundmanager.playMidi("shop");
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
		

	}

	@Override
	public void updateScreen() {

		this.screen.setColor(Color.BLUE);
		this.screen.clearRect(320,240,Screen.SCREEN_W,Screen.SCREEN_H);


	}

}
