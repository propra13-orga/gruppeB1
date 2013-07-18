import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Diese Scene bietet ein Menü, welches Ausrüstung an- und ablegen lässt. Dabei
 * gibt es für jeden Slot (Linker Arm, rechter Arm, Beine, Kopf, etc.) ein 
 * Untermenü, in dem diejenigen Items aus dem Inventar aus- und zugewählt werden
 * können, die in den Slot passen.
 * 
 * @author Victor Persien
 *
 */

public class Scene_Equipment extends Abstract_Scene {
//	private Abstract_Scene parent;
	private Scene_Level current_level;
	
	private Entity adventurer;
	
	private Map<String,String> menuNames;
	
	private Window_Menu main_menu;
	private Window_Menu menu_items;
	private List<Window_ItemProps> messages;
	private Window_ItemProps current_message;
	private Window_Message current_item;
	
	private List<Entity> items;
	private Map<String,Entity> equipped;

	/**
	 * Konstruktor.
	 * 
	 * @param game				Aktuelles Spielobjekt.
	 * @param parent			Scene, von der aus diese Scene aufgerufen wurde.
	 * @param current_level		Aktuelle Level-Scene.
	 * @param entity			Entität, deren Ausrüstung dargestellt und manipuliert
	 * 							werden soll.
	 */
	public Scene_Equipment(Object_Game game, Abstract_Scene parent, 
			Scene_Level current_level, Entity entity) {
		super(game);
//		this.parent = parent;
		this.current_level = current_level;
		this.adventurer = entity;
		
		this.current_item = new Window_Message("Angelegt: ",Object_Screen.SCREEN_W-300,430,300,game);
		
		this.main_menu = new Window_Menu(game,"main",0,0);
		
		this.items = new LinkedList<Entity>();
		this.equipped = new HashMap<String,Entity>();
		this.initItems();
		
		this.messages = new LinkedList<Window_ItemProps>();
		this.initMessages();
		
		this.menu_items = new Window_Menu(game,"items",0,0);
		
		this.prepareMainMenu();
		this.prepareMenus();
		
		Window_Menu.setMainMenu(this.main_menu);
		
	}

	@Override
	public void onStart() {
		//
	}

	@Override
	public void onExit() {
		/*
		 * Items zurueckuebertragen.
		 */
		Component_Equipment compEquipment = (Component_Equipment) this.adventurer.getComponent("equipment");
		Component_Inventory compInventory = (Component_Inventory) this.adventurer.getComponent("inventory");
		for (String slot : this.equipped.keySet()) {
			Entity item = this.equipped.get(slot);
			if (item != null) {
				compEquipment.setEquipment(slot, this.equipped.get(slot));				
			}
		}
		for (Entity item : this.items) {
			if (!this.isEquipped(item) && !compInventory.containsItem(item)) {
				compInventory.addItem(item);
			}
			else if (this.isEquipped(item)) {
				compInventory.removeItem(item);
			}
		}

	}

	@Override
	public void updateData() {
		this.main_menu.setupMenuPath();
		if (this.main_menu.isExecuted()) {
			this.main_menu.updateData();
			int cursor_main = this.main_menu.getCursor();
			if (this.menu_items.isExecuted()) {
				this.menu_items.setupMenuPath();
				this.updateItemMenu(cursor_main);
				int cursor_items = this.menu_items.getCurrentCursor();
				if (cursor_items < this.items.size()) {
					this.current_message = this.messages.get(cursor_items);
				}
				if (cursor_main < Component_Equipment.SLOTS.length) {
					this.updateMessageCurrentItem(cursor_main);
				}
			}
		}
		else {
			this.main_menu.nextMenu();
			int cursor_main = this.main_menu.getCursor();
			String menuName = this.main_menu.getCurrentName();
			switch (menuName) {
			case "null":
			case "Beenden":
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(this.current_level);
				break;
			default:
				int cursor = this.menu_items.getCurrentCursor();
				if (cursor < this.items.size() && this.menu_items.isEnabled(cursor)) {
					this.handleEquip(cursor_main, cursor);
				}
				else {
					this.handleUnequip(cursor_main);
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
		this.current_item.updateScreen();

	}
	
	/**
	 * Bereitet das Hauptmenü vor, d.h. für jeden Slot gibt es einen Auswahlpunkt
	 * zum Itemmenü. Dort sind die Items gelistet, die sich anlegen lassen.
	 * 
	 * Die Reihenfolge der Menüs richtet sich nach der, die in Component_Equipment.SLOTS
	 * angegeben ist.
	 */
	private void prepareMainMenu() {
		this.menuNames = new HashMap<String,String>();
		this.menuNames.put("slot_head","Kopf");
		this.menuNames.put("slot_arm_left","Linker Arm");
		this.menuNames.put("slot_arm_right","Rechter Arm");
		this.menuNames.put("slot_breast","Brust");
		this.menuNames.put("slot_legs","Beine");
		this.menuNames.put("slot_ring","Ring");
		this.menuNames.put("slot_necklace","Anhaenger");
		
		for (String slot : Component_Equipment.SLOTS) {
			this.main_menu.addMenuCommand(this.menuNames.get(slot), this.menu_items, true);
		}
		this.main_menu.addCancelCommand("Beenden", false);
		
		List<String> properties;
		
		for (Entity item : this.items) {
			Component_Item compItem = (Component_Item) item.getComponent("item");
			properties = compItem.getProperties();
			for (String property : properties) {
				if (property.matches("slot_.*")) {
					this.main_menu.enableCommand(this.menuNames.get(property));
					
				}
			}
		}
	}
	
	
	private void prepareMenus() {
		String name;
		for (Entity item : this.items) {
			name = item.getName();
			this.menu_items.addReturnCommand(name, true);
		}
		this.menu_items.addCancelCommand("Ablegen");
	}
	
	/**
	 * Aktiviert bzw. deaktiviert die Auswahl von Items in Abhaengigkeit davon,
	 * welches Menue (Linker Arm, Kopf, etc.) gerade ausgewaehlt ist.
	 */
	private void updateItemMenu(int pos) {
		String slot = Component_Equipment.SLOTS[pos];
		Entity item;
		Component_Item compItem;
		for (int i=0;i<this.items.size();i++) {
			item = this.items.get(i);
			compItem = (Component_Item) item.getComponent("item");
			if (compItem.hasProperty(slot) && !this.isEquipped(item)) {
				this.menu_items.enableCommand(i);
			}
			else this.menu_items.disableCommand(i);
		}
	}
	
	/**
	 * Aktualisiert die Anzeige des gerade am entsprechenden Slot angelegten
	 * Items, angegeben durch pos.
	 * @param pos		Nummer des Items in der Liste.
	 */
	private void updateMessageCurrentItem(int pos) {
		String slot = Component_Equipment.SLOTS[pos];
		String name = "";
		if (this.equipped.containsKey(slot)) {
			name = this.equipped.get(slot).getName();
		}
		this.current_item.changeMessage("Angelegt: "+name);
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
	 * Fügt die anlegbaren Items der Item-Liste hinzu.
	 */
	private void initItems() {
		Component_Equipment compEquipment = (Component_Equipment) this.adventurer.getComponent("equipment");
		Component_Inventory compInventory = (Component_Inventory) this.adventurer.getComponent("inventory");
		
		if (compEquipment.getEquipment() != null) this.equipped = compEquipment.getEquipment();
		
		if (this.equipped != null) {
			this.items.addAll(this.equipped.values());
		}
		for (Entity item : compInventory.getInventory()) {
			if (item != null) {
				Component_Item compItem = (Component_Item) item.getComponent("item");
				if (compItem.isEquippable()) this.items.add(item);
			}
		}
		
	}
	
	/**
	 * Überprüft, ob ein Item bereits angelegt ist.
	 * @param item		Das zu überprüfende Item.
	 * @return			true/false
	 */
	private boolean isEquipped(Entity item) {
		if (item == null) return false;
		for (Entity item2 : this.equipped.values()) {
			if (item == item2) return true;
		}
		return false;
	}
	
	/**
	 * Fügt ein Item zu den angelegten Items hinzu.
	 * @param cursor_main	Index des ausgewählten Menüs.
	 * @param pos			Position in der Liste
	 */
	private void handleEquip(int cursor_main, int pos) {
		this.equipped.put(Component_Equipment.SLOTS[cursor_main],this.items.get(pos));
	}
	
	/**
	 * 
	 * Legt ein vormals angelegtes Item ab.
	 * @param cursor_main	Index des ausgewählten Menüs.
	 */
	private void handleUnequip(int cursor_main) {
		this.equipped.remove(Component_Equipment.SLOTS[cursor_main]);
	}

}
