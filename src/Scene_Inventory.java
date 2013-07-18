import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Bietet ein grafisches Menü zur Anzeige des Inventars einer Entität. Hier
 * können Items konsumiert oder zerstört werden.
 * 
 * @author Victor Persien
 *
 */

public class Scene_Inventory extends Abstract_Scene implements IEventListener {
//	private Abstract_Scene parent;
	private Scene_Level current_level;
	
	private Entity adventurer;
	
	private Window_Menu main_menu;
	private Window_Menu menu_items;
	private List<Window_ItemProps> messages;
	private Window_ItemProps current_message;
	private Window_Message money_display;
	
	private List<Entity> items;
	private List<Entity> trash;
	private List<Entity> use;
	
	/**
	 * Konstruktor.
	 * 
	 * @param game				Aktuelles Spielobjekt.
	 * @param parent			Scene, von der aus diese Scene aufgerufen wurde.
	 * @param current_level		Aktuelle Level-Scene.
	 * @param entity			Entität, deren Ausrüstung dargestellt und manipuliert
	 * 							werden soll.
	 */
	public Scene_Inventory(Object_Game game, Abstract_Scene parent, 
			Scene_Level current_level, Entity entity) {
		super(game);
//		this.parent = parent;
		this.current_level = current_level;
		this.adventurer = entity;

		String moneystring = String.format("Geld: %d", this.retrieveMoney());
		this.money_display = new Window_Message(moneystring,Object_Screen.SCREEN_W-150,430,150,game);
		
		this.main_menu = new Window_Menu(game,"main",0,0);
		this.menu_items = new Window_Menu(game,"items",0,0);
		
		this.trash = new LinkedList<Entity>();
		this.use = new LinkedList<Entity>();
		
		
		this.items = new LinkedList<Entity>();
		this.initItems();
		
		this.messages = new LinkedList<Window_ItemProps>();
		this.initMessages();
		
		this.prepareMainMenu();
		this.prepareItemMenu();
		
		this.updateItemMenu(0);
		this.updateItemMenu(1);
		
		Window_Menu.setMainMenu(this.main_menu);
	}

	@Override
	public void onStart() {
		//
	}

	@Override
	public void onExit() {
		Component_Inventory compInventory = (Component_Inventory) this.adventurer.getComponent("inventory");
		for (Entity item : this.use) {
			this.addEvent(new Event(EventType.ITEM_USE,this.adventurer,item));
			compInventory.removeItem(item);
		}
		for (Entity item : this.trash) {
			compInventory.removeItem(item);
		}
	}

	@Override
	public void updateData() {
		this.main_menu.setupMenuPath();
		int cursor_main = this.main_menu.getCursor();
		if (this.main_menu.isExecuted()) {
			this.main_menu.updateData();
			if (this.menu_items.isExecuted()) {
				this.menu_items.setupMenuPath();
				this.updateItemMenu(cursor_main);
				int cursor_items = this.menu_items.getCurrentCursor();
				if (cursor_items < this.items.size()) {
					this.current_message = this.messages.get(cursor_items);
				}
			}
		}
		else {
			this.main_menu.nextMenu();
			switch(this.main_menu.getCurrentName()) {
			case "null":
			case "Beenden":
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(this.current_level);
				break;
			default:
				int cursor = this.menu_items.getCurrentCursor();
				if (cursor < this.items.size() && this.menu_items.isEnabled(cursor)) {
					this.handleTrashOrUse(cursor_main, cursor);
				}
				else {
					//
				}
				this.main_menu.getLastMenu().restart();
				break;
			}
		}

	}

	@Override
	public void updateScreen() {
		this.main_menu.updateScreen();
		if (this.current_message != null) {
			this.current_message.updateScreen();
		}
		this.money_display.updateScreen();
	}
	
	/**
	 * Sendet Event event an den Eventmanager des Levels. Dient in erster Linie
	 * zum Verschicken von Events des Typs ITEM_USE, um den entsprechenden
	 * Komponentensystemen mitzuteilen, dass ein Item benutzt wird.
	 */
	@Override
	public void addEvent(Event event) {
		this.current_level.addEvent(event);
		
	}

	@Override
	public void broadcastEvent(Event event) {
		//		
	}

	@Override
	public List<Event> getEvents(EventType... eventTypes) {
		return null;
	}

	@Override
	public void listenTo(EventType... eventTypes) {
		//	
	}
	
	/**
	 * Markiere das Item mit Index pos als entweder zu benutzen oder wegzuwerfen,
	 * je nach ausgewähltem menu.
	 * @param menu
	 * @param pos
	 */
	private void handleTrashOrUse(int menu, int pos) {
		Entity item = this.items.get(pos);
		if (menu == 0) {
			this.use.add(item);
		}
		else if (menu == 1) {
			this.trash.add(item);
		}
	}
	
	/**
	 * Ist das Item item schon als zu benutzen markiert?
	 * @param item
	 * @return			true/false
	 */
	private boolean itemInUse(Entity item) {
		for (Entity item2 : this.use) {
			if (item == item2) return true;
		}
		return false;
	}
	
	/**
	 * Ist das Item item schon als wegzuwerfen markiert?
	 * @param item
	 * @return		true/false
	 */
	private boolean itemTrashed(Entity item) {
		for (Entity item2 : this.trash) {
			if (item == item2) return true;
		}
		return false;
	}
	
	/**
	 * Aktiviert bzw. deaktiviert die Auswahl von Items in Abhaengigkeit davon,
	 * welches Menue (Benutzen oder Wegwerfen) gerade ausgewaehlt ist.
	 */
	private void updateItemMenu(int pos) {
		Entity item;
		Component_Item compItem;
		for (int i=0;i<this.items.size();i++) {
			item = this.items.get(i);
			compItem = (Component_Item) item.getComponent("item");
			/*
			 * Aktiviere den Eintrag eines Items, falls wenn pos == 0, das Item 
			 * konsumierbar ist, und das Item weder in Benutzung noch angelegt
			 * ist, wobei eine pos von 0 bedeutet, dass das Menü "Benutzen"
			 * ausgewählt ist.
			 */
			if ((pos != 0 || compItem.isConsumable()) &&
					!this.itemInUse(item) &&
					!this.itemTrashed(item)) {
				this.menu_items.enableCommand(i);
			}
			else this.menu_items.disableCommand(i);
		}
	}
	
	private void prepareMainMenu() {
		boolean emptyInventory = this.items.size() == 0;
		this.main_menu.addMenuCommand("Benutzen", this.menu_items,emptyInventory);
		this.main_menu.addMenuCommand("Wegwerfen", this.menu_items,emptyInventory);
		this.main_menu.addCancelCommand("Beenden");
	}
	
	private void prepareItemMenu() {
		String name;
		for (Entity item : this.items) {
			name = item.getName();
			this.menu_items.addReturnCommand(name, true);
		}
	}
	
	/**
	 * Fügt die anlegbaren Items der Item-Liste hinzu.
	 */
	private void initItems() {
		Component_Inventory compInventory = (Component_Inventory) this.adventurer.getComponent("inventory");
		for (Entity item : compInventory.getInventory()) {
			if (item != null) this.items.add(item);
		}
	}
	
	/**
	 * Erstellt Fenster mit den Daten jedes Items.
	 */
	private void initMessages() {
		for (Entity item : this.items) {
			this.messages.add(new Window_ItemProps(item,Object_Screen.SCREEN_W-300,0,this.game,this.current_level.getFactory()));
		}
	}

	/**
	 * Liest das Geld der Käuferentität aus.
	 * @return		Geld.
	 */
	private int retrieveMoney() {
		Component_Inventory compInventory = (Component_Inventory) this.adventurer.getComponent("inventory");
		return compInventory.getMoney();
	}

}
