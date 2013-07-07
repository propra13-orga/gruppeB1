
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scene_BuyMenu extends Abstract_Scene {
	
	Scene_Level parent;
	private Window_Menu main_menu;
	private Window_Menu menu_buy;
	private Window_Menu menu_sell;
	
	private Object_DBReader db;
	private Entity seller;
	private Entity buyer;
	
	private Map<String,Integer> items_seller;
	private Map<String,Integer> items_buyer;
	

	public Scene_BuyMenu(Object_Game game, Scene_Level parent, Entity seller, Entity buyer) {
		super(game);
		this.parent = parent;
		this.db = new Object_DBReader();
		this.buyer = buyer;
		this.seller = seller;
		
		this.initItemsBuy();
		
		for (String item : this.items_seller.keySet()) {
			System.out.printf("item: %d\n", this.items_seller.get(item));
		}
		
		this.main_menu = new Window_Menu(game,"main",0,0);        // Die verschiedenen Menupunkte
		this.menu_buy = new Window_Menu(game,"buy",0,0);          //
		this.menu_sell = new Window_Menu(game,"sell",0,0);
		
		this.main_menu.addMenuCommand("Kaufen", this.menu_buy, false);      // Die verschiedenen Erst_
		this.main_menu.addMenuCommand("Verkaufen", this.menu_sell, false);  // Menu Funktionen
		this.main_menu.addCancelCommand("Beenden", false);
		
		this.menu_buy.addReturnCommand("Gro�er Heiltrank", false);         // Die verschiedenen Zweit-
		this.menu_buy.addReturnCommand("Kleiner Heiltrank", false);        // Menu Funktionen beim
		this.menu_buy.addReturnCommand("Attacke 1", false);                //�ffnen von "Kaufen"
		this.menu_buy.addReturnCommand("Attacke 2", true);
		/*Hier die Frage mit welchen mittel der player die items kauft*/
		
		this.menu_sell.addReturnCommand("Gro�er Heiltrank", false);     // Die verschiedenen Zweit-Menu
		this.menu_sell.addReturnCommand("Kleiner Heiltrank", false);    // Funktionen beim �ffnen von
		this.menu_sell.addReturnCommand("Attacke 1", true);            // "Verkaufen
		this.menu_sell.addReturnCommand("Attacke 2", true);
		
		Window_Menu.setMainMenu(this.main_menu);
		this.soundmanager.playMidi("shop");     // Hintergrundmusik beim benutzen des Buy_Menu
		                                                      
	}

	@Override
	public void onStart() {
		//
	}

	@Override
	public void onExit() {
		//
	}

	@Override
	public void updateData() {
//		for (String item : this.items_seller.keySet()) {
//			this.menu_buy.addReturnCommand(item,false);
//		}
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
	
	
	/*
	 * Privates
	 */
	
	private void initItemsBuy() {
		this.items_seller = new HashMap<String,Integer>();
		Map<String,String> itemProps;
		Map<String,String> triggerProps = ((Component_Trigger) this.seller.getComponent("trigger_event")).getProperties();

		for (String shop_item : triggerProps.keySet()) {
			if (shop_item.matches("shop_item\\d+")) {
				itemProps = this.db.getProperties(triggerProps.get(shop_item));
				String entityType = this.db.getProperties(itemProps.get(shop_item)).get("entityType");
				System.out.println(entityType);
				if (this.items_seller.containsKey(entityType)) {
					System.out.println("AAA");
					if (itemProps.containsKey("stackable")) {
						this.items_seller.put(entityType,this.items_seller.get(entityType)+1);
					}
				}
				else {
					this.items_seller.put(entityType, 1);
				}
			}
		}
	}
	
	private void initItemsSell() {
		
	}
	
}
