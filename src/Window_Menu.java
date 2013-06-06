import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Window_Menu extends Abstract_Update {
	
	public boolean EXIT_POSSIBLE = false;
	
	public Window_Menu final_menu;
	public boolean final_decision = false;

	private String name;
	private boolean executed = false;
	private Window_Menu previous_menu;
	private Window_Menu next_menu;
	private Window_Selectable menu;
	private ArrayList<Window_Menu> submenues;
	
	Window_Menu(String name, int x, int y, Object_Game game) {
		super(game);
		
		//this.EXIT_POSSIBLE = true;
		
		this.name = name;
		this.previous_menu = null;
		this.next_menu = null;
		this.menu = new Window_Selectable(0, 0, game);
		this.submenues = new ArrayList<Window_Menu>();
	}
	
	@Override
	public void updateData() {
		if (this.next_menu != null) {
			this.next_menu.updateData();
		}
		if (this.menu.EXECUTED) {
			this.menu.updateData();
		}
		else {
			if (this.submenues.get(this.menu.cursor) == null) {
				this.final_decision = true;
			}
			else {
				
			}
		}
	}
	@Override
	public void updateScreen() {
		if (this.next_menu != null) {
			this.next_menu.updateData();
			
		}
		if (this.menu.EXECUTED) {
			this.menu.updateData();
		}
		else {
			if (this.submenues.get(this.menu.cursor) == null) {
				this.final_decision = true;
			}
		}	
	}
	
	public void addReturnCommand(String cmd) {
		this.addMenuCommand(cmd, null);
	}
	
	public void addMenuCommand(String cmd, Window_Menu menu) {
		this.menu.addCommand(cmd);
		this.submenues.add(menu);
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isExecuted() {
		return this.executed;
	}
}
