import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Diese Klasse baut auf Window_Base auf und bietet die Moeglichkeit, Menues ins Spiel zu integrieren.
 * Dabei koennen mehrere Menues verschachtelt und dynamisch verwaltet werden.
 * Dabei kann jedes Menue als Hauptmenue fungieren und gleichzeitig auch wieder als Untermenue in ein bereits
 * bestehendes eingebaut werden.
 * 
 * @author Alexander
 *
 *@param COLOR_FONT_STANDARD		Die Standardfarbe fuer Schrift
 *@param COLOR_FONT_DISABLED		Die Standardfarbe fuer Schrift von nicht auswaehlbaren Menueelementen
 *@param COLOR_CURSOR_BORDER		Die Standardfarbe fuer den Rand des Menuecursors
 *@param COLOR_CURSOR_FILL			Die Standardfarbe fuer das Innere des Menuecursors
 *@param MIN_X						Minimale Groesse eines Menues
 *@param MAX_X						Maximale Groesse eines Menues
 *@param BORDER_BOX					Abstand zwischen Fensterrand und Cursor
 *@param BORDER_CURSOR				Abstand zwischen Cursor und Text, den er umschliesst
 *@param CURSOR_SPACE				Vertikaler Abstand zwischen zwei untereinanderliegenden Cursorpositionen
 *
 *@param exit_posiible				true, wenn das Menue per Escape beendet werden kann
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
	
	/**
	 * Erstellt ein neues Menueobjekt
	 * 
	 * @param game		Das Gameobjekt
	 * @param name		Ein Name fuer das Menue, mit dem es sich eindeutig identifizieren laesst. Darf nill "null" sein
	 * @param x
	 * @param y
	 * @param width
	 * @param height
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
	
	/**
	 * Fuegt dem Menue einen return command hinzu
	 * 
	 * @param cmd			Name des commands
	 * @param disabled		Kann der command ausgewaehlt werden
	 */
	
	public void addReturnCommand(String cmd, boolean disabled) {
		this.addMenuCommand(cmd, null, disabled);
	}
	
	/**
	 * Fuegt dem Menue einen return command hinzu
	 * 
	 * @param cmd			Name des commands
	 */
	public void addReturnCommand(String cmd) {
		this.addMenuCommand(cmd, null, false);
	}
	
	/**
	 * Guegt dem Menue einen cancel command hinzu
	 * 
	 * @param cmd			Name des commands
	 * @param disabled		Kann der command ausgewaehlt werden
	 */
	
	public void addCancelCommand(String cmd, boolean disabled) {
		this.addMenuCommand(cmd, this.previous_menu, disabled);
	}
	
	/**
	 * Guegt dem Menue einen cancel command hinzu
	 * 
	 * @param cmd			Name des commands
	 */
	
	public void addCancelCommand(String cmd) {
		this.addMenuCommand(cmd, this.previous_menu, false);
	}
	
	/**
	 * Fuegt dem Menue einen menu command hinzu
	 * 
	 * @param cmd			Name des commands
	 * @param menu			das bei Bestaetigung auszufuehrende Submenu
	 * @param disabled		Kann der command ausgewaehlt werden
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
	
	/**
	 * Fuegt dem Menue einen menu command hinzu
	 * 
	 * @param cmd			Name des commands
	 * @param menu			das bei Bestaetigung auszufuehrende Submenu
	 */
	
	public void addMenuCommand(String cmd, Window_Menu menu) {
		this.addMenuCommand(cmd, menu, false);
	}
	
	/**
	 * Entfernt einen command vom Menue
	 * 
	 * @param idx			Index des zu loeschenden commands
	 */
	
	public void removeCommand(int idx) {
		this.commands.remove(idx);
		if (this.disabled.contains(idx)) {
			this.disabled.remove(this.disabled.indexOf(idx));
		}
	}
	
	/**
	 * Setzt seinen Menuezeiger auf sich selbst, um so von sich aus durch seine Untermenues navigieren
	 * zu koennen. Sollte nur von als MainMenu deklarierten Menues aufgerufen werden.
	 */
	
	public void setupMenuPath() {
		this.current_menu = this;
	}
	
	/**
	 * Setzt den Menuezeiger auf das im aktuellen Menue aufgerufene Untermenue (kann auch null sein)
	 */
	
	public void nextMenu() {
		this.current_menu = this.current_menu.submenues.get(this.current_menu.cursor);
	}
	
	/**
	 * 
	 * @return			Der Name des Menues auf das der Menuezeiger zeigt
	 */
	
	public String getCurrentName() {
		if (this.current_menu == null) {
			return "null";
		}
		return this.current_menu.name;
	}
	
	/**
	 * 
	 * @return			Der Name des im aktuellen Menue aufgerufenen commands
	 */
	
	public String getCurrentChoice() {
		return this.current_menu.commands.get(this.current_menu.cursor);
	}
	
	/**
	 * 
	 * @return			Die im aktuellen Menue stehende Cursorposition
	 */
	
	public int getCurrentCursor() {
		return this.current_menu.cursor;
	}
	
	/**
	 * 
	 * @return			Das absolut zuletzt aufgerufene Menue
	 */
	
	public Window_Menu getLastMenu() {
		if (this.next_menu == null) {
			return this;
		}
		else {
			return this.next_menu.getLastMenu();
		}
	}
	
	/**
	 * 
	 * @return			Die absolut zuletzt gesetzte Cursorposition
	 */
	
	public int getLastCursor() {
		if (this.next_menu == null) {
			return this.cursor;
		}
		else {
			return this.next_menu.getLastCursor();
		}
	}
	
	/**
	 * Macht einen command auswaehlbar
	 * @param idx			Der Index des commands in der Commandliste
	 */
	
	public void enableCommand(int idx) {
		if (this.disabled.contains(idx)) {
			this.disabled.remove(this.disabled.indexOf(idx));
		}
	}
	
	/**
	 * Macht einen command auswaehlbar
	 * @param cmd			Der Name des Commands (falls mehrere commands den selben Namen
	 * 						besitzen, wird der zuerst hinzugefuegte bearbeitet)
	 */
	
	public void enableCommand(String cmd) {
		if (this.commands.contains(cmd)) {
			int idx = this.commands.indexOf(cmd);
			this.enableCommand(idx);
		}
	}
	
	/**
	 * Macht einen command nicht auswaehlbar
	 * @param idx			Der Index des commands in der Commandliste
	 */
	
	public void disableCommand(int idx) {
		if (!this.disabled.contains(idx)) {
			this.disabled.add(idx);
		}
	}
	
	/**
	 * Loescht alle Untermenues und die Commandliste
	 */
	
	public void clear() {
		this.submenues = new ArrayList<Window_Menu>();
		this.commands.clear();
	}
	
	/**
	 * 
	 * @param value true, wenn man das Menue mit Escape beenden koennen soll, sonst false
	 */
	
	public void setExitPossible(boolean value) {
		this.exit_possible = value;
	}
	
	/**
	 * isExecuted() gibt (wieder) true zurueck
	 */
	
	public void restart() {
		this.executed = true;
	}
	
	/**
	 * isExecuted() gibt nicht (mehr) true zurueck
	 */
	
	public void stop() {
		this.executed = false;
	}
	
	/**
	 * Menue wird auch angezeigt, wenn isExecuted() nicht true zurueckgibt
	 */
	
	public void show() {
		this.visible = true;
	}
	
	/**
	 * Menue wird nur angezeigt, wenn isExecuted() true zurueck gibt
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
