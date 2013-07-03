import java.util.Arrays;
import java.util.List;


public class Object_ShopInventory {
	
	List<IShopItem> items;

	public Object_ShopInventory(IShopItem ...items) {
		this.items = Arrays.asList(items);
	}
	
	/*
	 * Getters
	 */
	
	public List<IShopItem> getItems() { return this.items; }
	

	/*
	 * Setters
	 */
	
	public void addItem(IShopItem item) { this.items.add(item); }
	public void removeItem(IShopItem item) { this.items.remove(item); }
}
