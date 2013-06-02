public class Component_Item extends Abstract_Component {
	private String itemType;
	private boolean consumable;
	private boolean permanent;
	private boolean stackable;
	private int hp;
	private int mp;
	private int ap;
	private int value;
	

	public Component_Item(Entity entity, System_Component system,
			String itemType, int hp, int mp, int ap, int value,  
			boolean consumable, boolean permanent, boolean stackable) {
		super("item", entity, system);
		this.itemType = itemType;
		this.hp = hp;
		this.mp = mp;
		this.ap = ap;
		this.value = value;
		this.consumable = consumable;
		this.permanent = permanent;
		this.stackable = stackable;
	}
	
	/*
	 * Getters
	 */
	
	public String getItemType() { return this.itemType; }
	public int getHP() { return this.hp; }
	public int getMP() { return this.mp; }
	public int getAP() { return this.ap; }
	public int getValue() { return this.value; }
	public boolean isConsumable() { return this.consumable; }
	public boolean isPermanent() { return this.permanent; }
	public boolean isStackable() { return this.stackable; }
}
