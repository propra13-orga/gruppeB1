public class Component_Inventory extends Abstract_Component {
	private int[] inventory;
	private int money;
	
	public Component_Inventory(Entity entity,
			System_Component system, int money) {
		super("inventory", entity, system);
		this.inventory = this.baseInventory(50);	// = {-1,-1,...,-1}
		this.money = money;
	}
	
	/*
	 * Getters
	 */
	
	public int getIDAt(int i) { return this.inventory[i]; }
	public int[] getInventory() { return this.inventory; }
	public int getMoney() { return this.money; }
	public boolean contains(Entity entity) {
		int entityID = entity.getID();
		for (int id : this.inventory) {
			if (id == entityID) return true;
		}
		return false;
	}
	
	/*
	 * Setters
	 */
	
	public void setMoney(int money) { this.money = money; }
	public void addToMoney(int money) { this.money += money; }
	
	
	/*
	 * Boolean Setters
	 */
	
	public boolean addItem(int entityID) {
		for (int i=0;i<inventory.length;i++) {
			if (this.inventory[i] == -1) {
				this.inventory[i] = entityID;
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
