import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Scene_Equipment extends Abstract_Scene {
	private Abstract_Scene parent;
	private Scene_Level current_level;
	
	private Entity adventurer;
	
	private Map<String,String> menuNames;
	
	private Window_Menu main_menu;
	private Window_Menu menu_items;
	private Map<String,Window_Menu> menus;
	private List<Window_ItemProps> messages;
	private Window_ItemProps current_message;
	
	private List<Entity> items;
	private Map<String,Entity> equipped;

	public Scene_Equipment(Object_Game game, Abstract_Scene parent, 
			Scene_Level current_level, Entity entity) {
		super(game);
		this.parent = parent;
		this.current_level = current_level;
		this.adventurer = entity;
		
		this.main_menu = new Window_Menu(game,"main",0,0);
		this.menus = new HashMap<String,Window_Menu>();
		this.setupMenus();
		
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit() {
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

	}
	
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
	
	/*
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
	
	private void initMessages() {
		for (Entity item : this.items) {
			this.messages.add(new Window_ItemProps(item,Object_Screen.SCREEN_W-300,0,this.game,this.current_level.getFactory()));
		}
	}
	
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
	
	private void setupMenus() {
		for (String slot : Component_Equipment.SLOTS) {
			this.menus.put(slot, new Window_Menu(this.game,slot,0,0));
		}
	}
	
	private boolean isEquipped(Entity item) {
		if (item == null) return false;
		for (Entity item2 : this.equipped.values()) {
			if (item == item2) return true;
		}
		return false;
	}
	
	private void handleEquip(int cursor_main, int pos) {
		this.equipped.put(Component_Equipment.SLOTS[cursor_main],this.items.get(pos));
	}
	
	private void handleUnequip(int cursor_main) {
		this.equipped.remove(Component_Equipment.SLOTS[cursor_main]);
	}

}
