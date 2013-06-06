import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Window_Menu extends Abstract_Update {
	
	public boolean EXIT_POSSIBLE = true;
	
	public String final_name;
	public boolean final_decision = false;
	public int final_cursor;

	private String name;
	private boolean executed = true;
	private Window_Menu previous_menu;
	private Window_Menu next_menu;
	private Window_Menu main_menu;
	private Window_Selectable menu;
	private ArrayList<Window_Menu> submenues;
	
	Window_Menu(String name, int x, int y, Object_Game game) {
		super(game);
		this.name = name;
		this.previous_menu = null;
		this.next_menu = null;
		this.main_menu = this;
		this.menu = new Window_Selectable(0, 0, game);
		this.submenues = new ArrayList<Window_Menu>();
	}
	
	@Override
	public void updateData() {
		if (this.next_menu != null) {
			this.next_menu.updateData();
		}
		else {
			if (this.menu.EXECUTED) {
				this.menu.updateData();
			}
			else {
				if (this.menu.CANCELED) {
					if (!this.EXIT_POSSIBLE) {
						this.menu.EXECUTED = true;
						return;
					}
					this.previous_menu.next_menu = null;
					this.previous_menu.menu.EXECUTED = true;
					return;
				}
				if (this.submenues.get(this.menu.cursor) == null) {
					this.main_menu.final_decision = true;
					this.main_menu.final_name = this.name;
					this.main_menu.menu.cursor = this.menu.cursor;
				}
				else {
					this.next_menu = this.submenues.get(this.menu.cursor);
					this.next_menu.previous_menu = this;
					this.next_menu.menu.EXECUTED = true;
					this.next_menu.menu.cursor = 0;
				}
			}
		}
	}
	@Override
	public void updateScreen() {
		if (this.next_menu != null) {
			this.next_menu.updateScreen();
		}
		else {
			this.menu.updateScreen();
		}
	}
	
	public void addReturnCommand(String cmd) {
		this.addMenuCommand(cmd, null);
	}
	
	public void addMenuCommand(String cmd, Window_Menu menu) {
		this.menu.addCommand(cmd);
		this.submenues.add(menu);
		if (menu != null) {
			menu.main_menu = this.main_menu;
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isExecuted() {
		return this.executed;
	}
	
	public Window_Selectable getMenu() {
		return this.menu;
	}
}
