public interface IShopItem {
	public String getItemType();
	public int getHP();
	public int getMP();
	public int getAP();
	public int getValue();
	public String getDescription();
	public boolean isConsumable();
	public boolean isPermanent();
	public boolean isStackable();
}