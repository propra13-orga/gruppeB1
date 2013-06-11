/* Habe Den Code von Scene_GameMenu kopiert und mit dem Code Window_menu dann hier dran rumgeschrauben.
 So weit ich den Code Window_Menu müsste verstanden habe müsste es so stimmen 
 aber verstehe net die vielen Fehler die er anzeigt.
 */

/* Bin in 3 etapen vorgegangen habe: 1. Die notwendigen Elemente importiert
                                     2. Die Elemente deklariert
                                     3. Versuch wie bei Window_Menu die Funktion Menu/Submenue
                                       einzuarbeiten
 */
import java.util.ArrayList;



public class Scene_ShopMenu extract Abstract_Scene {
	
	public boolean ALWAYS_POSSIBLE = false ;       
	public boolean EXIT_POSSIBLE = true;
	
	
    private Scene_BuyMenu shop_map;
	private Window_Selectable menu1;
	private Window_menu main_shopmenu;
	private Window_menu next_shopmenu;
	private Window_menu previous_shopmenu;
	private ArrayList<Window_Menu> submenu1;
	private ArrayList<Window_Menu> submenu2;

	

	Scene_ShopMenu(Object_Game game,int x, int y,Scene_BuyMenu m1) {
		super(game);
		shop_map = m1;
		main_shopmenu = new Window_Menu(10,10,game);
		next_shopmenu = new Window_Menu(10,10,game);
		previous_shopmenu = new Window_Menu(10,10,game);
		
		menu1 = new Window_Selectable(20,20,game);
		submenu1 = new ArrayList<Window_Menu>();
		submenu2 = new ArrayList<Window_Menu>();
		
		menu1.addCommand("Kaufen");
		menu1.addCommand("Verkaufen");
		menu1.addCommand("Spiel beenden");
		
	}
	


	
	public void onStart1() { }

	
	public void onExit1() { }

	
	public void updateData() {
		if(next_shopmenu != null) next_shopmenu.updateData();
		
		if (menu1.EXECUTED) menu1.updateData();
		else {
			if (menu1.CANCELED) {
				//MenÃ¼ wurde beendet
				this.keyhandler.clear();
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(shop_map);
				return;
			}
			else {
				//Ein MenÃ¼punkt wurde bstÃ¤tigt
				switch (menu1.cursor){
				case 0: //Kaufen
					this.shop_map.serialize();
					System.out.println("Kann was gekauft werden...und öffnet submenu1");
					if(submenu1.get(menu1.cursor) == null) {
						return 0;
					}
					else {
						this.next_shopmenu = this.submenues.get(this.menu.cursor);
						this.next_shopmenu.previous_shopmenu = this;
						this.next_shopmenu.menu1.EXECUTED = true;
						this.next_shopmenu.menu1.CANCELED = false;
						this.next_shopmenu.menu1.cursor = 0;
					}
					menu1.EXECUTED = true;
					break;
					
				case 1: //Verkaufen
					this.shop_map.serialize();
					System.out.println("Kann was verkauft werden..und öffnet submenu2");
					if(submenu2.get(menu1.cursor) == null) {
						return 0;
					}
					else {
						this.next_shopmenu = this.submenues.get(this.menu.cursor);
						this.next_shopmenu.previous_shopmenu = this;
						this.next_shopmenu.menu1.EXECUTED = true;
						this.next_shopmenu.menu1.CANCELED = false;
						this.next_shopmenu.menu1.cursor = 0;
					}
					
					menu1.EXECUTED = true;
					break
					
				case 2: //Spiel beenden
					this.keyhandler.freeze(Object_KeyHandler.KEY_ENTER, 40);
					game.quit();
					return;
					
				default:
					System.out.println("Nur zu Testzwecken!");
					menu1.EXECUTED = true;
				}
			}
		}
	}

	
	public void updateScreen() {
				if (ALWAYS_VISIBLE) {
					menu1.updateScreen();
				}
				else {
					if (menu1.EXECUTED) {
						
						menu1.updateScreen();
					}
				}
				//Pruefe fuer alle Menues ob ALWAYS_VISIBLE gesetzt ist
				for (Window_Menu menu1 : submenu1) {
					if (menu1 != null) {
						menu1.updateScreen();
						submenu1.updateScreen();
					}
				for(Window_Menu menu1 : submenue2)	 {
					if (menu1 != null) {
						menu1.updateScreen();
						submenu2.updateScreen();
					}
				}
				}
	}


public void addMenuCommand(String cmd, Window_Menu menu1) {
	menu1.addCommand(cmd);
	submenu1.add(menu1);
	submenu2.add(menu1);
	if (menu1 != null) {
	menu1.main_shopmenu = this.main_shopmenu;
	}
}

static void setMainMenu(Window_Menu menu1) {
	menu1.EXIT_POSSIBLE = false;
	menu1.menu1.EXECUTED = true;
	menu1.previous_shopmenu = null;
	menu1.main_shopmenu = this.menu1;
}
}


