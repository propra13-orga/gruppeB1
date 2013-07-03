
import java.awt.*;

public class Scene_BuyMenu extends Abstract_Scene {
	
	Scene_Level parent;
	private Window_Menu main_menu;
	private Window_Menu menu_buy;
	private Window_Menu menu_sell;
	

	public Scene_BuyMenu(Object_Game game, Scene_Level parent) {
		super(game);
		this.parent = parent;
		this.main_menu = new Window_Menu(game,"main",0,0);        // Die verschiedenen Menupunkte
		this.menu_buy = new Window_Menu(game,"buy",0,0);          //
		this.menu_sell = new Window_Menu(game,"sell",0,0);
		
		this.main_menu.addMenuCommand("Kaufen", this.menu_buy, false);      // Die verschiedenen Erst_
		this.main_menu.addMenuCommand("Verkaufen", this.menu_sell, false);  // Menu Funktionen
		this.main_menu.addCancelCommand("Beenden", false);
		
		this.menu_buy.addReturnCommand("Großer Heiltrank", false);         // Die verschiedenen Zweit-
		this.menu_buy.addReturnCommand("Kleiner Heiltrank", false);        // Menu Funktionen beim
		this.menu_buy.addReturnCommand("Attacke 1", false);                //öffnen von "Kaufen"
		this.menu_buy.addReturnCommand("Attacke 2", true);
		/*Hier die Frage mit welchen mittel der player die items kauft*/
		
		this.menu_sell.addReturnCommand("Großer Heiltrank", false);     // Die verschiedenen Zweit-Menu
		this.menu_sell.addReturnCommand("Kleiner Heiltrank", false);    // Funktionen beim öffnen von
		this.menu_sell.addReturnCommand("Attacke 1", true);            // "Verkaufen
		this.menu_sell.addReturnCommand("Attacke 2", true);
		
		Window_Menu.setMainMenu(this.main_menu);
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
		if (main_menu.isExecuted()) {
			main_menu.updateData();
		}
		else {
			System.out.println("NICHT MEHR AUSGEFUEHRT");
			this.main_menu.setupMenuPath();
			this.main_menu.nextMenu();
			System.out.println("current name: "+this.main_menu.getCurrentName());
			switch(this.main_menu.getCurrentName()) {
			
			case "buy": //Oeffne Kaufmenue
				System.out.println(this.main_menu.getCurrentChoice()+" wurde gekauft");
				this.main_menu.getLastMenu().restart();
				break;
				
			case "sell":
				System.out.println(this.main_menu.getCurrentChoice()+" wurde verkauft");
				this.main_menu.getLastMenu().restart();
				break;
				
			case "null":
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(parent);
				return;
			}
		}
	}
	
	@Override
	public void updateScreen() {
        main_menu.updateScreen();
	}

}
