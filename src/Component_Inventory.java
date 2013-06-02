
public class Component_Inventory extends Abstract_Component {
	Entity[] inventory;
	
	public Component_Inventory(Entity entity,
			System_Component system) {
		super("inventory", entity, system);
		this.inventory = new Entity[100];
	}
	
	/*
	 * Getters
	 */
	
	public Entity[] getInventory() { return this.inventory; }
	
	
	/*
	 * Boolean Setters
	 */
	public boolean addItem(Entity item) {
		for (int i=0;i<inventory.length;i++) {
			if (this.inventory[i] == null) {
				this.inventory[i] = item;
				return true;
			}
		}
		return false;

	}

}
