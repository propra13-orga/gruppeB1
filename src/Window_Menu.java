import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * Window_Menu.java
 * 
 * Mit dieser Klasse ist es m�glich, verschachtelte Menues zu erstellen.
 * Zuerst m�ssen alle Menues einzeln erstellt werden. Anschliessend kann jedem Menue
 * mit den Befehlen entweder absolute Befehle (addReturnCommand) oder Befehle, die ein
 * weiteres Menue oeffnen (addMenuCommand) hinzufuegen.
 * Fuer ein Verwendungsbeispiel siehe Scene_BattleSystem.java
 * 
 */

public class Window_Menu extends Window_Base {
	
	public Color					COLOR_FONT_STANDARD			= new Color(240, 240, 240);
	public Color					COLOR_FONT_DISABLED			= new Color(100, 100, 100);
	public Color					COLOR_CURSOR_BORDER			= new Color(255, 255, 255);
	public Color					COLOR_CURSOR_FILL			= new Color(255, 255, 255);
	public int						MIN_X						= Object_Map.TILESIZE;
	public int						MAX_X						= Object_Screen.SCREEN_W;
	public int						BORDER_BOX					= Object_Map.TILESIZE/2;
	public int						BORDER_CURSOR				= 5;
	public int						CURSOR_SPACE				= 5; //Der vertikale Platz zwischen zwei Befehlen
	
	public boolean					exit_possible				= true;
	
	private boolean					executed;					//Wird das Menue ausgefuehrt
	private boolean					canceled;					//Wurde das Menue abgebrochen
	private boolean					visible;					//Ist das Menu sichtbar
	private int						cursor;						//Index des aktuell ausgewaehlten Befehls
	private int						cursor_alpha = 50;			//Zur Animation des Cursors
	private int						d_alpha = 3;				// " "
	private int						cursorheight;				//Hoehe des Menuecursors
	private String					name;						//Jedes Menue besitzt einen eigenen Namen
	private Window_Menu				previous_menu;				//Das Vorgaengermenue
	private Window_Menu				next_menu;					//Das an das Menue gekoppelte Untermenue
	private Window_Menu				main_menu;					//Jedes Menue kann auf sein Hauptmenue zugreifen
	private Window_Menu				current_menu;				//Wird nur benutzt, wen das Menue das Hauptmenue ist
	private ArrayList<String>		commands;					//Alle im Menue vorhandenen Befehle
	private ArrayList<Window_Menu>	submenues;					//Alle im Menue vorhandenen Untermenues (evtl. null)
	private ArrayList<Integer>		disabled;					//Liste der Indices von ausgeschalteten Befehlen
	private FontMetrics				metrics;					//Zur Berechnung von Hoehe und Breite von Strings
	private BufferedImage			background;
	
	/*
	 * contructors
	 */
	
	Window_Menu(Object_Game game, String name, int x, int y, int width, int height) {
		super(game, x, y, width, height);
		init(name);
	}
	
	Window_Menu(Object_Game game, String name, int x, int y) {
		super(game, x, y, -1, -1);
		init(name);
	}
	
	Window_Menu(Object_Game game, String name) {
		super(game, 0, 0, -1, -1);
		init(name);
	}
	
	private void init(String name) {
		if (name == "null") {
			this.game.exitOnError("Menuename darf nicht 'null' sein");
		}
		this.screen.setFont(Object_Game.FONT);
		this.name			=	name;
		this.cursor			=	0;
		this.visible		=	false;
		this.previous_menu	=	null;
		this.next_menu		=	null;
		this.main_menu		=	this;
		this.executed		=	false;
		this.submenues		=	new ArrayList<Window_Menu>();
		this.commands		=	new ArrayList<String>();
		this.disabled		=	new ArrayList<Integer>();
		this.metrics		=	this.screen.getFontMetrics();
		this.cursorheight	=	Math.round(this.metrics.getLineMetrics("ApgL", this.screen).getHeight());
		this.background		=	new BufferedImage(Object_Screen.SCREEN_W, Object_Screen.SCREEN_H, BufferedImage.TYPE_INT_ARGB);
		//this.background.getGraphics().drawImage(this.game.getScreen().getBuffer(),0,0,null);
	}
	
	/*
	 * update methods
	 */
	
	@Override
	public void updateData() {
		if (this.next_menu != null) {
			//Es existiert ein aktives Submenu, also rufe dessen update Methode auf
			this.next_menu.updateData();
		}
		else {
			//Dieses Submenu ist das aktuell aufgefuehrte
			if (this.executed) {
				//Noch keine Bestaetigung oder Abbruch
				switch (this.keyhandler.getLast()) {
				case Object_KeyHandler.KEY_DOWN:
					this.soundmanager.playSound("cursor");
					this.cursor++;
					if (this.cursor >= this.commands.size()) this.cursor = 0;
					break;
				case Object_KeyHandler.KEY_UP:
					this.soundmanager.playSound("cursor");
					this.cursor--;
					if (this.cursor < 0) this.cursor = this.commands.size()-1;
					break;
				case Object_KeyHandler.KEY_ESCAPE:
					if (!this.exit_possible) {
						break;
					}
					this.soundmanager.playSound("cancel");
					this.executed = false;
					this.canceled = true;
					break;
				case Object_KeyHandler.KEY_ENTER:
					if (this.disabled.contains(this.cursor)) {
						this.soundmanager.playSound("invalid");
						break;
					}
					this.soundmanager.playSound("decision");
					this.keyhandler.clear();
					this.executed = false;
					this.canceled = false;
					break;
				}
				handleKeyInput(this.keyhandler.getLast());
				//Handle alpha value
				this.cursor_alpha += this.d_alpha;
				if (this.cursor_alpha <= 40 || this.cursor_alpha >= 120) {
					this.d_alpha *= -1;
				}
			}
			//Pruefe, ob das Menu abgebruchen wurde
			if (!this.executed) {
				//Menu wurde beendet
				if (this.canceled) {
					//Escape wurde gedrueckt
					if (!this.exit_possible) {
						//Das war aber gar nicht erlaubt, also aktiviere das Menue wieder
						this.executed = true;
						this.canceled = false;
					}
					else {
						//Das Vorgaengermenu wird wieder aktiviert
						if (this.previous_menu == null) {
							//Hauptmenue wurde durch Escape beendet
							this.reset();
							this.executed = false;
						}
						else {
							this.cursor = 0;
							this.previous_menu.next_menu = null;
							this.previous_menu.executed = true;
						}
					}
				}
				else {
					//Es wurde Enter gedrueckt...
					if (this.submenues.get(this.cursor) != null) {
						//...und es ist ein Sumenu registriert, also wird dieses jetzt
						//aktiviert und ausgefuehrt
						this.next_menu = this.submenues.get(this.cursor);
						this.next_menu.previous_menu = this;
						this.next_menu.executed = true;
						this.next_menu.canceled = false;
						this.next_menu.cursor = 0;
					}
				}
			}
		}
	}
	
	@Override
	public void updateScreen() {
		//Dieses Menue...
		if (this.next_menu != null) {
			this.next_menu.updateScreen();
		}
		else {
			//...ist das aktuell ausgefuehrte Menue und muss angezeigt werden
			if (this.visible || this.executed) {
				this.screen.drawImage(this.background,0,0,null);
				super.updateScreen();
				draw_cursor();
				drawCommands();
			}
		}
	}
	
	/*
	 * Fuegt dem Menue einen Befehl hinzu, der kein weiteres Untermenue aufruft
	 */
	
	public void addReturnCommand(String cmd, boolean disabled) {
		this.addMenuCommand(cmd, null, disabled);
	}
	
	public void addReturnCommand(String cmd) {
		this.addMenuCommand(cmd, null, false);
	}
	
	/*
	 * Fuegt dem Menue einen Befehl hinzu, der entweder in das vorige Menue zurueck
	 * springt oder das Menue komplett beendet
	 */
	
	public void addCancelCommand(String cmd, boolean disabled) {
		this.addMenuCommand(cmd, this.previous_menu, disabled);
	}
	
	public void addCancelCommand(String cmd) {
		this.addMenuCommand(cmd, this.previous_menu, false);
	}
	
	/*
	 * F�gt dem Menue einen Befehl hinzu, der bei Bestaetigung ein weiteres Menu aufruft
	 */
	
	public void addMenuCommand(String cmd, Window_Menu menu, boolean disabled) {
		this.addCommand(cmd);
		this.submenues.add(menu);
		if (menu != null) {
			menu.main_menu = this.main_menu;
		}
		if (disabled) {
			//Speichere den index des neuen commands in der disabled liste
			this.disabled.add(this.commands.size()-1);
		}
		this.refresh_box();
	}
	
	public void addMenuCommand(String cmd, Window_Menu menu) {
		this.addMenuCommand(cmd, menu, false);
	}
	
	public void removeCommand(int idx) {
		this.commands.remove(idx);
		if (this.disabled.contains(idx)) {
			this.disabled.remove(this.disabled.indexOf(idx));
		}
	}
	
	/*
	 * Setzt den current_menu Zeiger auf das naechste aufgerufene Menue
	 */
	
	public void setupMenuPath() {
		this.current_menu = this;
	}
	
	public void nextMenu() {
		this.current_menu = this.current_menu.submenues.get(this.current_menu.cursor);
	}
	
	public String getCurrentName() {
		if (this.current_menu == null) {
			return "null";
		}
		return this.current_menu.name;
	}
	
	public String getCurrentChoice() {
		return this.current_menu.commands.get(this.current_menu.cursor);
	}
	
	public int getCurrentCursor() {
		return this.current_menu.cursor;
	}
	
	public Window_Menu getLastMenu() {
		if (this.next_menu == null) {
			return this;
		}
		else {
			return this.next_menu.getLastMenu();
		}
	}
	
	public int getLastCursor() {
		if (this.next_menu == null) {
			return this.cursor;
		}
		else {
			return this.next_menu.getLastCursor();
		}
	}
	
	/*
	 * Mache einen bereits vorhandenen Befehl aufrufbar
	 */
	
	public void enableCommand(int idx) {
		if (this.disabled.contains(idx)) {
			this.disabled.remove(this.disabled.indexOf(idx));
		}
	}
	
	public void enableCommand(String cmd) {
		if (this.commands.contains(cmd)) {
			int idx = this.commands.indexOf(cmd);
			this.enableCommand(idx);
		}
	}
	
	/*
	 * Mache einen bereits vorhandenen Befehl nicht mehr aufrufbar
	 */
	
	public void disableCommand(int idx) {
		if (!this.disabled.contains(idx)) {
			this.disabled.add(idx);
		}
	}
	
	/*
	 * Loescht alle Untermenues
	 */
	
	public void clear() {
		this.submenues = new ArrayList<Window_Menu>();
		this.commands.clear();
	}
	
	/*
	 * Das Menue kann mit Escape beendet werden
	 */
	
	public void setExitPossible(boolean value) {
		this.exit_possible = value;
	}
	
	/*
	 * Setzt this.executed (wieder) auf true
	 */
	
	public void restart() {
		this.executed = true;
	}
	
	public void stop() {
		this.executed = false;
	}
	
	/*
	 * Das Menue wird immer angezeigt
	 */
	
	public void show() {
		this.visible = true;
	}
	
	/*
	 * Das Menue wird nur angezeigt, wenn es ausgefuehrt wird
	 */
	
	public void hide() {
		this.visible = false;
	}
	
	/*
	 * Setzt das Menu und alle registrierten Untermenues wieder auf ihren Anfangszustand.
	 */
	
	public void reset() {
		if (this.main_menu == this) {
			this.exit_possible = false;
			this.executed = true;
		}
		else {
			this.exit_possible = true;
			this.executed = false;
		}
		this.previous_menu = null;
		this.next_menu = null;
		this.cursor = 0;
		for (Window_Menu menu : this.submenues) {
			if (menu != null && menu != this.previous_menu) {
				menu.reset();
			}
		}
	}
	
	/*
	 * Methoden zum Positionieren des Menues
	 */
	
	public void center() {
		this.x = Object_Screen.SCREEN_W/2 - (this.width/2);
		this.y = Object_Screen.SCREEN_H/2 - (this.height/2);
	}
	
	public void topLeft() {
		this.x = 0;
		this.y = 0;
	}
	
	public void topRight() {
		this.x = Object_Screen.SCREEN_W-this.width;
		this.y = 0;
	}
	
	public void bottomLeft() {
		this.x = 0;
		this.y = Object_Screen.SCREEN_H-this.height;
	}
	
	public void bottomRight() {
		this.x = Object_Screen.SCREEN_W-this.width;
		this.y = Object_Screen.SCREEN_H-this.height;
	}
	
	/*
	 * getMenuPath muss noch gearbeitet werden, spaeter kann man dadurch den Verlauf
	 * des Cursors durch das Menue verfolgen, also beispielsweise:
	 * 
	 * main-item-heiltrank-spieler1
	 */
	
	public ArrayList<String> getMenuPath() {
		ArrayList<String> path = new ArrayList<String>();
		Window_Menu current = this;
		path.add(current.name);
		while (current.next_menu != null) {
			current = current.next_menu;
			path.add(current.name);
		}
		return path;
	}

	/*
	 * Getters
	 */
	
	public boolean isExecuted() {
		if (this.next_menu == null) {
			return this.executed;
		}
		else {
			return this.next_menu.isExecuted();
		}
	}
	
	public boolean isCanceled() {
		if (this.next_menu == null) {
			return this.canceled;
		}
		else {
			return this.next_menu.isCanceled();
		}
	}
	
	public boolean isEnabled(int idx) {
		return !isDisabled(idx);
	}
	
	public boolean isDisabled(int idx) {
		return this.disabled.contains(idx);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getCursor() {
		return this.cursor;
	}
	
	//Private Methoden
	
	private void calculateMenuSize() {
		this.width = MIN_X;
		this.height = 0;
		for (String command : this.commands) {
			int new_width = 2*BORDER_BOX + 2*BORDER_CURSOR + this.metrics.stringWidth(command);
			if (this.width < new_width) this.width = new_width;
			if (this.width < MIN_X) this.width = MIN_X;
			if (this.width > MAX_X) this.width = MAX_X;
			this.height = 2*BORDER_BOX + commands.size()*(2*BORDER_CURSOR+this.cursorheight);
			if (this.commands.size() > 1) {
				this.height += (this.commands.size()-1)*CURSOR_SPACE;
			}
		}
	}
	
	private void addCommand(String command) {
		commands.add(command);
		calculateMenuSize();
	}
	
	private void drawCommands() {
		int text_x = this.x + BORDER_BOX + BORDER_CURSOR;
		int text_y = this.y + BORDER_BOX + BORDER_CURSOR + this.cursorheight - this.metrics.getDescent();
		int index = 0;
		for (String cmd : commands) {
			if (this.disabled.contains(index)) {
				this.screen.setColor(COLOR_FONT_DISABLED);
				this.screen.drawString(cmd, text_x, text_y);
				this.screen.setColor(COLOR_FONT_STANDARD);
			}
			else {
				this.screen.drawString(cmd, text_x, text_y);
			}
			text_y += this.cursorheight + 2*BORDER_CURSOR + CURSOR_SPACE;
			index++;
		}
	}
	
	private void draw_cursor() {
		this.screen.setColor(new Color(	COLOR_CURSOR_FILL.getRed(),
										COLOR_CURSOR_FILL.getGreen(),
										COLOR_CURSOR_FILL.getBlue(),
										this.cursor_alpha));
		int x = this.x + BORDER_BOX;
		int y = this.y + BORDER_BOX + this.cursor*this.cursorheight;
		if (this.cursor >= 1) {
			y += (this.cursor)*2*BORDER_CURSOR;
			y += (this.cursor)*CURSOR_SPACE;
		}
		int width = this.width - 2*BORDER_BOX;
		int height = this.cursorheight + 2*BORDER_CURSOR;
		this.screen.fillRect(
				x,
				y,
				width,
				height);
		this.screen.setColor(COLOR_CURSOR_BORDER);
		this.screen.drawRect(
				x,
				y,
				width,
				height);
	}
	
	private void handleKeyInput(int key) {
		this.keyhandler.clear();
		this.keyhandler.freeze(key, 5);
	}
	
	/*
	 * Statische Methode, die alle Einstellungen vornimmt, um ein Menue als Hauptmenue
	 * zu deklarieren
	 */
	
	static void setMainMenu(Window_Menu menu) {
		menu.exit_possible = false;
		menu.executed = true;
		menu.previous_menu = null;
		menu.main_menu = menu;
	}
}
