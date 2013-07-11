import java.util.LinkedList;
import java.util.List;


public class Scene_Inventory extends Abstract_Scene implements IEventListener {
	
	private Scene_Level parent;
	
	private Window_Menu main_menu;
	private Window_Menu menu_choice;
	
	private Window_ItemProps current_message;
	private Window_Message quantity_display;
	private Window_Message money_display;
	private Window_ItemProps[] messages;
	
	private List<List<Entity>> items;
	private List<Integer> items_value;
	private List<Entity> dropList;
	
	private Factory factory;
	private Entity entity;
	
	private int money;

	public Scene_Inventory(Object_Game game, Scene_Level parent, Entity entity) {
		super(game);
		this.parent = parent;
		this.entity = entity;
		this.factory = parent.getFactory();
		
		this.money = this.retrieveMoney();
		
		String money_string = String.format("Geld: %d", this.money);
		this.money_display = new Window_Message(money_string,Object_Screen.SCREEN_W-150,430,150,game);
		this.quantity_display = new Window_Message("",Object_Screen.SCREEN_W-300-70,0,70,game);
		
		this.main_menu = new Window_Menu(game,"main",0,0);
		this.menu_choice = new Window_Menu(game,"choice",0,0);
		
		this.initItems();
		
		this.dropList = new LinkedList<Entity>();
		
		this.initMessages();
		
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
		// TODO Auto-generated method stub

	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub

	}
	
	private int retrieveMoney() {
		return ((Component_Inventory) this.entity.getComponent("inventory")).getMoney();
	}

	@Override
	public void addEvent(Event event) {
		this.parent.addEvent(event);		
	}

	@Override
	public void broadcastEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Event> getEvents(EventType... eventTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void listenTo(EventType... eventTypes) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Privates
	 */
	
	private void initItems() {
		this.items = new LinkedList<List<Entity>>();
		Component_Inventory compInventory = (Component_Inventory) this.entity.getComponent("inventory");
		for (Entity item : compInventory.getInventory()) {
			if (item != null) {
				Component_Item compItem = (Component_Item) item.getComponent("item");
				boolean stackable = compItem.getProperties().contains("type_stackable");
				int pos = this.getListIndex(item);
				if (stackable && pos > -1) {
					this.items.get(pos).add(item);
				}
				else {
					List<Entity> list = new LinkedList<Entity>();
					list.add(item);
					this.items_value.add(compItem.getValue());
				}
			}
		}
	}
	
	private void initMessages() {
		this.messages = new Window_ItemProps[this.items.size()];
		if (this.items.size() == 0) return;
		Entity item;
		for (int i=0;i<this.items.size();i++) {
			item = this.items.get(i).get(0);
			this.messages[i] = new Window_ItemProps(item,Object_Screen.SCREEN_W-300,0,this.game,this.factory);
		}
	}
	
	private Entity fetchItem(Entity item) {
		int pos = this.getListIndex(item);
		if (pos > -1) return this.items.get(pos).get(0);
		return null;
	}
	
	private int getListIndex(Entity item) {
		for (int i=0;i<this.items.size();i++) {
			Entity item2 = this.items.get(i).get(0);
			if (item2.equals(item)) return i;
		}
		return -1;
	}

}
