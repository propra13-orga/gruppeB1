
import java.awt.*;

public class Scene_BuyMenu extends Abstract_Scene {
	
	Scene_Level parent;
	private Window_Menu main_menu;
	private Window_Menu menu_buy;
	private Window_Menu menu_sell;
	

	public Scene_BuyMenu(Object_Game game, Scene_Level parent) {
		super(game);
		this.parent = parent;
		this.main_menu = new Window_Menu("main",0,0,game);        // Die verschiedenen Menupunkte
		this.menu_buy = new Window_Menu("buy",0,0,game);          //
		this.menu_sell = new Window_Menu("sell",0,0,game);
		
		Window_Menu.setMainMenu(this.main_menu);
		
		this.main_menu.addMenuCommand("Kaufen", this.menu_buy);      // Die verschiedenen Erst_
		this.main_menu.addMenuCommand("Verkaufen", this.menu_sell);  // Menu Funktionen
		this.main_menu.addReturnCommand("Beenden");
		
		this.menu_buy.addReturnCommand("Großer Heiltrank");         // Die verschiedenen Zweit-
		this.menu_buy.addReturnCommand("Kleiner Heiltrank");        // Menu Funktionen beim
		this.menu_buy.addReturnCommand("Attacke 1");                //öffnen von "Kaufen"
		this.menu_buy.addReturnCommand("Attacke 2");
		/*Hier die Frage mit welchen mittel der player die items kauft*/
		
		this.menu_sell.addReturnCommand("Großer Heiltrank");     // Die verschiedenen Zweit-Menu
		this.menu_sell.addReturnCommand("Kleiner Heiltrank");    // Funktionen beim öffnen von
		this.menu_sell.addReturnCommand("Attacke 1");            // "Verkaufen
		this.menu_sell.addReturnCommand("Attacke 2");
		
		this.soundmanager.playMidi("shop");     // Hintergrundmusik beim benutzen des Buy_Menu
		                                                      
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

	    this.screen.clearRect(320,240,Object_Screen.SCREEN_W,Object_Screen.SCREEN_H);
	    this.screen.setColor(Color.BLUE);
        main_menu.updateScreen();
        menu_buy.updateScreen();
        menu_sell.updateScreen();
        

	}

}
