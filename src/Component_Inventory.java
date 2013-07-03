public class Component_Inventory extends Abstract_Component {

	private static final long serialVersionUID = 1L;
	
	private Entity[] inventory;
	private int money;
	
	public Component_Inventory(Entity entity,
			System_Component system, int money) {
		super("inventory", entity, system);
		this.inventory = new Entity[50];
		this.money = money;
	}
	
	public Component_Inventory(Component_Inventory compInventory) {
		super(compInventory);
		this.inventory = compInventory.inventory;
		this.money = compInventory.money;
	}
	
	public Component_Inventory(Abstract_Component comp, Entity entity, System_Component system) {
		super(comp.getType(),entity,system);
		Component_Inventory compInventory = (Component_Inventory) comp;
		this.inventory = compInventory.inventory;
		this.money = compInventory.money;
	}
	
	/*
	 * Getters
	 */
	
	public Entity[] getInventory() { return this.inventory; }
	public int getMoney() { return this.money; }
	
	/*
	 * Setters
	 */
	
	public void setMoney(int money) { this.money = money; }
	public void addToMoney(int money) { this.money += money; }
	
	
	/*
	 * Boolean Setters
	 */
	
	public boolean addItem(Entity item) {
		if (!item.hasComponent("item")) return false;
		for (int i=0;i<inventory.length;i++) {
			if (this.inventory[i] == null) {
				this.inventory[i] = item;
				return true;
			}
		}
		return false;
	}
		
	
	
	/*
	 * Privates
	 */
	
	/*
	 * Erzeugt ein Integer-Array der Länge 50, deren Einträge nur -1 sind. -1
	 * steht für "kein Item".
	 */
	private int[] baseInventory(int n) {
		int[] array = new int[n];
		for (int i=0;i<n;i++) {
			array[i] = -1;
		}
		return array;
	}
}
