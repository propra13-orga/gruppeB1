import java.util.LinkedList;
import java.util.Map;
import java.util.List;

public class Scene_BuyMenu extends Abstract_Scene {
	
	Scene_Level parent;
	private Window_Menu main_menu;
	private Window_Menu menu_buy;
	private Window_Menu menu_sell;
	
	private Abstract_Update current_message;
	private Window_Message money_display;
	private Window_Message quantity_display;
	
	private Object_DBReader db;
	private Entity seller;
	private Entity buyer;
	
	private List<String> items_seller;
	private List<Integer> items_seller_quant;
	private List<Integer> items_seller_price;
	
	private List<Entity> items_buyer;
	private List<Integer> items_buyer_quant;
	private List<Integer> items_buyer_price;
	
	private List<Window_ItemProps> messages_buyer;
	private List<Window_ItemProps> messages_seller;
	
	private int[] cart_buy;
	private int[] cart_sell;
	
	private int money;
	

	public Scene_BuyMenu(Object_Game game, Scene_Level parent, Entity seller, Entity buyer) {
		super(game);
		this.parent = parent;
		this.db = parent.getFactory().getDBET();
		this.buyer = buyer;
		this.seller = seller;
		this.money = this.retrieveMoney();
		
		String money_string = String.format("Geld: %d", this.money);
		this.money_display = new Window_Message(money_string,Object_Screen.SCREEN_W-150,430,150,game);
		this.quantity_display = new Window_Message("",Object_Screen.SCREEN_W-300-70,0,70,game);
		
		this.main_menu = new Window_Menu(game,"main",0,0);        // Die verschiedenen Menupunkte
		this.menu_buy = new Window_Menu(game,"buy",0,0);          //
		this.menu_sell = new Window_Menu(game,"sell",0,0);
		
		this.main_menu.addMenuCommand("Kaufen", this.menu_buy, false);      // Die verschiedenen Erst_
		this.main_menu.addMenuCommand("Verkaufen", this.menu_sell, false);  // Menu Funktionen
		this.main_menu.addCancelCommand("Beenden", false);
		
		
		this.initItemsSeller();
		this.initItemsBuyer();		
		
		this.cart_buy = new int[this.items_seller.size()];
		this.cart_sell = new int[this.items_buyer.size()];
		
		this.messages_seller = new LinkedList<Window_ItemProps>();
		this.messages_buyer = new LinkedList<Window_ItemProps>();
		
		this.initMessagesBuy();
		this.initMessagesSell();
		
		this.initMenuBuy();
		this.initMenuSell();
		
		Window_Menu.setMainMenu(this.main_menu);
		this.soundmanager.playMidi("shop");     // Hintergrundmusik beim benutzen des Buy_Menu
		                                                      
	}

	@Override
	public void onStart() {
		//
	}

	@Override
	public void onExit() {
		this.checkOut();
	}

	@Override
	public void updateData() {
		if (main_menu.isExecuted()) {
			this.main_menu.setupMenuPath();
			main_menu.updateData();
			int cursor = -1;
			if (menu_buy.isExecuted()) {
				this.menu_buy.setupMenuPath();
				cursor = this.menu_buy.getCurrentCursor();
				this.current_message = this.messages_seller.get(cursor);
				this.updateQuantityDisplay(0, cursor);
			}
			else if (menu_sell.isExecuted()) {
				this.menu_sell.setupMenuPath();
				cursor = this.menu_sell.getCurrentCursor();
				this.current_message = this.messages_buyer.get(cursor);
				this.updateQuantityDisplay(1, cursor);
			}
			else this.updateQuantityDisplay(3, cursor);
			
		}
		else {
			System.out.println("NICHT MEHR AUSGEFUEHRT");
			this.main_menu.setupMenuPath();
			this.main_menu.nextMenu();
			System.out.println("current name: "+this.main_menu.getCurrentName());
			
			int cursor;
			
			switch(this.main_menu.getCurrentName()) {
			
			case "buy": //Oeffne Kaufmenue
				this.menu_buy.setupMenuPath();
				cursor = this.menu_buy.getCurrentCursor();
				if (cursor < this.items_seller.size() && this.menu_buy.isEnabled(cursor)) {
					this.handleBuy(cursor);
				}
				
				System.out.println(this.main_menu.getCurrentChoice()+" wurde gekauft");
				this.main_menu.getLastMenu().restart();
				break;
				
			case "sell":
				this.menu_sell.setupMenuPath();
				cursor = this.menu_sell.getCurrentCursor();
				if (cursor < this.items_buyer.size()  && this.menu_sell.isEnabled(cursor)) {
					this.handleSell(cursor);
				}
				
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
        this.main_menu.updateScreen();
        if (this.current_message != null) {
			this.current_message.updateScreen();
		}
        this.quantity_display.updateScreen();
        this.money_display.updateScreen();
	}
	
	
	/*
	 * Privates
	 */
	
	private void initItemsSeller() {
		this.items_seller = new LinkedList<String>();
		this.items_seller_quant = new LinkedList<Integer>();
		this.items_seller_price = new LinkedList<Integer>();
		Map<String,String> itemProps;
		Map<String,String> triggerProps = ((Component_Trigger) this.seller.getComponent("trigger_event")).getProperties();
		String entityType;
		int pos;
		
		for (String shop_item : triggerProps.keySet()) {
			if (shop_item.matches("shop_item\\d*")) {
				itemProps = this.db.getProperties(triggerProps.get(shop_item));
				entityType = itemProps.get("entityType");
				System.out.println(entityType);
				if (this.items_seller.contains(entityType) && itemProps.containsKey("type_stackable")) {
					pos = items_seller.indexOf(entityType);
					this.items_seller_quant.set(pos, this.items_seller_quant.get(pos)+1);
				}
				else {
					this.items_seller.add(entityType);
					this.items_seller_quant.add(1);
					this.items_seller_price.add(Integer.parseInt(itemProps.get("item_value")));
				}
			}
		}
	}
	
	private void initMenuBuy() {
		String name;
		for (String entityType : this.items_seller) {
			name = this.db.getProperties(entityType).get("name");
			this.menu_buy.addReturnCommand(name, false);
			
		}
	}
	
	private void initItemsBuyer() {
		this.items_buyer = new LinkedList<Entity>();
		this.items_buyer_quant = new LinkedList<Integer>();
		this.items_buyer_price = new LinkedList<Integer>();
		Component_Inventory compInventory = (Component_Inventory) this.buyer.getComponent("inventory");
		for (Entity item : compInventory.getInventory()) {
			if (item != null) {
				Component_Item compItem = (Component_Item) item.getComponent("item");
				boolean stackable = compItem.getProperties().contains("type_stackable");
				Entity item2 = this.fetchItemBuyer(item);
				if (stackable && item2 != null) {
					int pos = this.items_buyer.indexOf(item2);
					this.items_buyer_quant.set(pos, this.items_buyer_quant.get(pos));
				}
				else {
					this.items_buyer.add(item);
					this.items_buyer_quant.add(1);
					this.items_buyer_price.add(compItem.getValue());
				}
			}
		}
	}
	
	private void initMenuSell() {
		String name;
		for (Entity entity : this.items_buyer) {
			name = entity.getName();
			this.menu_sell.addReturnCommand(name, false);			
		}
	}
	
	private void initMessagesSell() {
		if (this.items_buyer.size() == 0) {
			this.main_menu.disableCommand(1);
			return;
		}
		for (Entity entity : this.items_buyer) {
			System.out.println(entity.getName());
			this.messages_buyer.add(new Window_ItemProps(entity,Object_Screen.SCREEN_W-300,0,this.game,this.parent.getFactory()));
		}
	}
	
	private void initMessagesBuy() {
		if (this.items_seller.size() == 0) {
			this.main_menu.disableCommand(0);
			return;
		}
		for (String entityType : this.items_seller) {
			this.messages_seller.add(new Window_ItemProps(entityType,Object_Screen.SCREEN_W-300,0,this.game,this.parent.getFactory()));
		}
	}
	
	private void handleBuy(int pos) {
		int price = this.items_seller_price.get(pos);
		if (price <= this.money) {
			this.cart_buy[pos] += 1;
			this.money -= price;
			int quant = this.items_seller_quant.get(pos);
			this.items_seller_quant.set(pos, quant-1);
			if (quant-1 == 0) {
				this.menu_buy.disableCommand(pos);
			}
		}
		this.updateMoneyDisplay();
	}
	
	
	private void handleSell(int pos) {
		int quant = this.items_buyer_quant.get(pos);
		this.items_buyer_quant.set(pos, quant-1);
		if (quant-1 == 0) {
			this.menu_sell.disableCommand(pos);
		}
		this.cart_sell[pos] += 1;
		this.money += this.items_buyer_price.get(pos);
		this.updateMoneyDisplay();
	}
	
	private void checkOut() {
		Component_Inventory compInventory = (Component_Inventory) this.buyer.getComponent("inventory");
		Map<String,String> entityData;
		String entityType;
		/*
		 * Kaufen.
		 * Das heißt, hier werden Entitäten in den Anzahlen, die in cart_buy 
		 * angegeben sind, gebaut und dem Inventar hinzugefügt.
		 */
		for (int i=0;i<this.cart_buy.length;i++) {
			if (this.cart_buy[i] > 0) {
				entityType = this.items_seller.get(i);
				for (int j=1;j<=this.cart_buy[i];j++) {
					entityData = this.db.getProperties(entityType);
					compInventory.addItem(this.parent.getFactory().build(entityData));
				}
			}
		}
		/*
		 * Verkaufen.
		 * Die Entitäten werden in den Anzahlen, die in cart_sell angegeben
		 * sind, aus dem Inventar entfernt (und damit dem Garbage-Collector
		 * überlassen).
		 */
		for (int i=0;i<this.cart_sell.length;i++) {
			if (this.cart_sell[i] > 0) {
				entityType = this.items_buyer.get(i).getType();
				for (int j=1;j<=this.cart_sell[i];j++) {
					compInventory.removeItem(entityType);
				}
			}
		}
		compInventory.setMoney(this.money);
	}
	
	private Entity fetchItemBuyer(Entity item) {
		for (int i=0;i<this.items_buyer.size();i++) {
			Entity item2 = this.items_buyer.get(i);
			String entityType = item.getType();
			if (entityType.equals(item2.getType())) {
				this.items_buyer_quant.set(i, this.items_buyer_quant.get(i)+1);
				return item2;
			}
		}
		return null;
	}
	
	private int retrieveMoney() {
		return ((Component_Inventory) this.buyer.getComponent("inventory")).getMoney();
	}
	
	private void updateMoneyDisplay() {
		String money_string = String.format("Geld: %d",this.money);
		this.money_display.changeMessage(money_string);
	}
	
	private void updateQuantityDisplay(int menu, int pos) {
		int quant = 0;
		switch (menu) {
		case 0:
			quant = this.items_seller_quant.get(pos);
			break;
		case 1:
			quant = this.items_buyer_quant.get(pos);
			break;
		default:
			break;
		}
		String quant_string = Integer.toString(quant);
		this.quantity_display.changeMessage(quant_string);
	}
}
