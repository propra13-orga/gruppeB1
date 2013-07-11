import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;


public class Window_ItemProps extends Abstract_Update {
	
	private Window_Message window;
	private Window_Message head;
	private Window_Message foot;

	private Factory factory;
	private Object_DBReader db_et;
	private Object_DBReader db_dat;
	
	private String title;
	private String[] properties;
	private String[] effects;
	private int[] values;
	private String value;
	
	private static final String regex_props = "req_.*|slot_.*";
	private static final String regex_effects = "dmg.*|armor.*|prop_.*";

	public Window_ItemProps(Entity item, int x, int y, Object_Game game, Factory factory) {
		super(game);
		this.screen.setFont(Object_Game.FONT);
		this.factory = factory;
		this.db_et = factory.getDBET();
		this.db_dat = factory.getDBDAT();
		
				
		this.prepareData(item);
		String message = this.prepareMessage();
		
		this.head = new Window_Message(this.title,x,y,game);
		this.window = new Window_Message(message,x,y+this.head.height,game);
		this.foot = new Window_Message(this.value,x,y+this.head.height+this.window.height,game);
	}
	
	public Window_ItemProps(String entityType, int x, int y, Object_Game game, Factory factory) {
		super(game);
		this.screen.setFont(Object_Game.FONT);
		this.factory = factory;
		this.db_et = factory.getDBET();
		this.db_dat = factory.getDBDAT();
		
		this.prepareData(entityType);
		String message = this.prepareMessage();
		
		this.head = new Window_Message(this.title,x,y,game);
		this.window = new Window_Message(message,x,y+this.head.height,game);
		this.foot = new Window_Message(this.value,x,y+this.head.height+this.window.height,game);
		
	}

	@Override
	public void updateData() {
		
	}

	@Override
	public void updateScreen() {
		this.head.updateScreen();
		this.window.updateScreen();
		this.foot.updateScreen();
	}
	
	
	private String prepareMessage() {
		String props = "";
		String effect;
		for (int i=0;i<this.effects.length;i++) {
			effect = this.effects[i];
			if (effect != null) {
				if (this.db_dat.getProperties("names").containsKey(effect)) {
					effect = this.db_dat.getProperties("names").get(effect);
				}
				props += String.format("%s: %d\n",effect,this.values[i]);			
			}
		}
		for (String property : this.properties) {
			if (property != null) {
				if (this.db_dat.getProperties("names").containsKey(property)) {
					property = this.db_dat.getProperties("names").get(property);
				}
				props += property + "\n";				
			}
		}
		
		return props;
	}
	
	private void prepareData(String entityType) {
		Map<String,String> entityData = this.db_et.getProperties(entityType);
		this.title = entityData.get("name");
		this.value = "Wert: "+entityData.get("item_value");
		
		Map<String,String> filteredEffects = this.factory.filterHashtable(entityData, regex_effects);
		Map<String,String> filteredProperties = this.factory.filterHashtable(entityData, regex_props);
		
		int size = filteredEffects.size();
		this.effects = new String[size];
		this.values = new int[size];
		String effect;
		Iterator<String> iterator = filteredEffects.keySet().iterator();
		for (int i=0;i<size;i++) {
			effect = iterator.next();
			if (effect.matches(regex_effects)) {
				this.effects[i] = effect;
			}
		}
		Arrays.sort(this.effects);
		for (int i=0;i<size;i++) {
			if (this.effects[i] == null) break;
			this.values[i] = Integer.parseInt(filteredEffects.get(effects[i]));
		}
		
		size = filteredProperties.size();
		iterator = filteredProperties.keySet().iterator();
		this.properties = new String[size];
		String property;
		for (int i=0;i<size;i++) {
			property = iterator.next();
			if (property.matches(regex_props)) {
				this.properties[i] = property;
			}
		}
	}
	
	private void prepareData(Entity item) {
		if (!item.hasComponent("item")) return;
		this.title = item.getName();
		Component_Item compItem = (Component_Item) item.getComponent("item");
		this.value = String.format("Wert: %d",compItem.getValue());
		int size = compItem.getEffectNames().size();
		this.effects = new String[size];
		this.values = new int[size];
		String effect;
		for (int i=0;i<size;i++) {
			effect = compItem.getEffectNames().get(i);
			if (effect.matches(regex_effects)) {
				this.effects[i] = effect;		
			}
		}
		Arrays.sort(this.effects);
		for (int i=0;i<size;i++) {
			if (this.effects[i] == null) break;
			this.values[i] = compItem.getEffectValue(this.effects[i]);
		}
		size = compItem.getProperties().size();
		this.properties = new String[size];
		String property;
		for (int i=0;i<size;i++) {
			property = compItem.getProperties().get(i);
			if (property.matches(regex_props)) {
				this.properties[i] = property;				
			}
			
		}
	}
	

}





















