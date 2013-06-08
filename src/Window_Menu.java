import java.util.ArrayList;

/*
 * Window_Menu.java
 * 
 * Mit dieser Klasse ist es möglich, verschachtelte Menues zu erstellen.
 * Zuerst müssen alle Menues einzeln erstellt werden. Anschliessend kann jedem Menue
 * mit den Befehlen entweder absolute Befehle (addReturnCommand) oder Befehle, die ein
 * weiteres Menue oeffnen (addMenuCommand) hinzufuegen.
 * Fuer ein Verwendungsbeispiel siehe Scene_BattleSystem.java
 * 
 */

public class Window_Menu extends Abstract_Update {
	
	public boolean EXIT_POSSIBLE = true;
	public boolean ALWAYS_VISIBLE = false;
	
	public boolean final_decision = false;
	public String final_name;
	public int final_cursor;

	private String name;
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
		this.menu.EXECUTED = false;
		this.submenues = new ArrayList<Window_Menu>();
	}
	
	/*
	 * updateData() und updateScreen()
	 */
	
	@Override
	public void updateData() {
		if (this.next_menu != null) {
			//Es existiert ein aktives Submenu, also rufe dessen update Methode auf
			this.next_menu.updateData();
		}
		else {
			//Dieses Submenu ist das aktuell aufgefuehrte
			if (this.menu.EXECUTED) {
				//Noch keine Bestaetigung oder Abbruch
				this.menu.updateData();
			}
			//Prüfe, ob das Menu abgebruchen wurde
			if (!this.menu.EXECUTED) {
				//Menu wurde beendet
				if (this.menu.CANCELED) {
					//Escape wurde gedrueckt
					if (!this.EXIT_POSSIBLE) {
						//Das war aber gar nicht erlaubt, also aktiviere das Menu wieder		HIER NOCH AENDERN!
						//Weil das Menu wirklich erst beendet wird und der entsprechende Sound
						//abgespielt wird, das soll aber nicht so sein
						this.menu.EXECUTED = true;
						this.menu.CANCELED = false;
					}
					else {
						//Das Vorgaengermenu wird wieder aktiviert
						this.previous_menu.next_menu = null;
						this.previous_menu.menu.EXECUTED = true;
						//this.reset();
					}
				}
				else {
					//Es wurde Enter gedrueckt...
					if (this.submenues.get(this.menu.cursor) == null) {
						//...und es existiert fuer den gewaehlten Befehl kein Submenu
						//Also beende das ganze Menu und speichere den Namen und Cursorposition
						//des zuletzt genutzten Menues
						System.out.println("Final decision für menu: "+this.main_menu.name);
						System.out.println("Main menu von item: "+this.main_menu.main_menu.name);
						this.main_menu.final_decision = true;
						this.main_menu.final_name = this.name;
						this.main_menu.menu.cursor = this.menu.cursor;
					}
					else {
						//...und es ist ein Sumenu registriert, also wird dieses jetzt
						//aktiviert und ausgeführt
						this.next_menu = this.submenues.get(this.menu.cursor);
						this.next_menu.previous_menu = this;
						this.next_menu.menu.EXECUTED = true;
						this.next_menu.menu.CANCELED = false;
						this.next_menu.menu.cursor = 0;
					}
				}
			}
		}
	}
	
	@Override
	public void updateScreen() {
		//Dieses Menue...
		if (this.ALWAYS_VISIBLE) {
			//...soll immer angezeigt werden
			this.menu.updateScreen();
		}
		else {
			if (this.menu.EXECUTED) {
				//...ist das aktuell ausgefuehrte Menue und muss angezeigt werden
				this.menu.updateScreen();
			}
		}
		//Pruefe fuer alle Menues ob ALWAYS_VISIBLE gesetzt ist
		for (Window_Menu menu : this.submenues) {
			if (menu != null) {
				menu.updateScreen();
			}
		}
	}
	
	/*
	 * Fügt dem Menue einen Befehl hinzu, der kein weiteres Untermenue aufruft
	 */
	
	public void addReturnCommand(String cmd) {
		this.addMenuCommand(cmd, null);
	}
	
	/*
	 * Fügt dem Menue einen Befehl hinzu, der bei Bestaetigung ein weiteres Menu aufruft
	 */
	
	public void addMenuCommand(String cmd, Window_Menu menu) {
		this.menu.addCommand(cmd);
		this.submenues.add(menu);
		if (menu != null) {
			System.out.println("Registriere Menu: "+menu.name);
			System.out.println("Übergebe als Main menu: "+this.main_menu.name);
			menu.main_menu = this.main_menu;
		}
	}
	
	/*
	 * Setzt das Menu und alle registrierten Untermenues wieder auf ihren Anfangszustand.
	 */
	
	public void reset() {
		if (this.main_menu == this) {
			this.final_decision = false;
			this.EXIT_POSSIBLE = false;
			this.menu.EXECUTED = true;
		}
		else {
			this.EXIT_POSSIBLE = true;
			this.menu.EXECUTED = false;
		}
		this.previous_menu = null;
		this.next_menu = null;
		this.menu.cursor = 0;
		for (Window_Menu menu : this.submenues) {
			if (menu != null) {
				menu.reset();
			}
		}
	}
	
	/*
	 * Statische Methode, die alle Einstellungen vornimmt, um ein Menue als Hauptmenue
	 * zu deklarieren
	 */
	
	static void setMainMenu(Window_Menu menu) {
		menu.EXIT_POSSIBLE = false;
		menu.menu.EXECUTED = true;
		menu.previous_menu = null;
		menu.main_menu = menu;
	}
}
