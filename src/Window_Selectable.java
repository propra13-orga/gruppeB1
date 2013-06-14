import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;

public class Window_Selectable extends Abstract_Update {
	
	public boolean EXECUTED = true;
	public boolean CANCELED = false;
	public boolean EXIT_POSSIBLE = true;
	public int min_x = 250;
	public int max_x = Object_Screen.SCREEN_W;
	public int border_x = 20;
	public int border_y = 20;
	public int cursor;
	public int cursorheight;
	
	private ArrayList<String> commands;
	private Window_Base window;
	private FontMetrics metrics;
	private int alpha = 50;
	private int d_alpha = 3;

	Window_Selectable(int x, int y, Object_Game game) {
		super(game);
		window  = new Window_Base(x, y, 0, 0, game);
		commands = new ArrayList<String>();
		cursor = 0;
		this.screen.setFont(Object_Game.FONT);
		this.metrics = this.screen.getFontMetrics();
		this.border_x = this.screen.getFont().getSize()/2;
		this.border_y = this.border_x;
		this.cursorheight = 30;
	}
	
	public void addCommand(String command) {
		commands.add(command);
		int new_width = 2*this.border_x + this.metrics.stringWidth(command);
		System.out.println("Neue Breite: "+new_width);
		if (window.width < new_width) window.width = new_width;
		System.out.println("Breite: "+window.width);
		if (window.width < min_x) window.width = min_x;
		if (window.width > max_x) window.width = max_x;
		window.height = 2*this.border_y + commands.size()*this.cursorheight;
		this.window.drawBox();
	}
	
	public void clear() {
		this.commands = new ArrayList<String>();
	}
	
	public void center() {
		window.x = Object_Screen.SCREEN_W/2 - (window.width/2);
		window.y = Object_Screen.SCREEN_H/2 - (window.height/2);
	}
	
	public void topLeft() {
		window.x = 0;
		window.y = 0;
	}
	
	public void topRight() {
		window.x = Object_Screen.SCREEN_W-window.width;
		window.y = 0;
	}
	
	public void bottomLeft() {
		window.x = 0;
		window.y = Object_Screen.SCREEN_H-window.height;
	}
	
	public void bottomRight() {
		window.x = Object_Screen.SCREEN_W-window.width;
		window.y = Object_Screen.SCREEN_H-window.height;
	}
	
	private void drawCommands() {
		int text_x = window.x + this.border_x + 10;
		int text_y = window.y + this.border_y + this.screen.getFont().getSize()+3;
		for (String cmd : commands) {
			this.screen.drawString(cmd, text_x, text_y);
			text_y += this.cursorheight;
		}
	}
	
	private void drawCursor() {
		this.alpha += this.d_alpha;
		this.screen.setColor(new Color(255,255,255,this.alpha));
		this.screen.fillRect(
				window.x + this.border_x,
				window.y + this.border_y + (cursor*this.cursorheight),
				window.width - 2 * this.border_x,
				this.cursorheight);
		if (this.alpha <= 40 || this.alpha >= 120) this.d_alpha *= -1;
		this.screen.setColor(new Color(255,255,255));
		this.screen.drawRect(
				window.x + this.border_x,
				window.y + this.border_y + (cursor*this.cursorheight),
				window.width - 2 * this.border_x,
				this.cursorheight);
	}

	@Override
	public void updateData() {
		switch (game.getKeyHandler().getLast()) {
		case Object_KeyHandler.KEY_DOWN:
			this.soundmanager.playSound2("cursor");
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_DOWN, 5);
			cursor++;
			if (cursor >= commands.size()) cursor = 0;
			break;
		case Object_KeyHandler.KEY_UP:
			this.soundmanager.playSound2("cursor");
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_UP, 5);
			cursor--;
			if (cursor < 0) cursor = commands.size()-1;
			break;
		case Object_KeyHandler.KEY_ESCAPE:
			this.soundmanager.playSound2("cancel");
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_ESCAPE, 5);
			if (!EXIT_POSSIBLE) break;
			EXECUTED = false;
			CANCELED = true;
			break;
		case Object_KeyHandler.KEY_ENTER:
			this.soundmanager.playSound2("decision");
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_ENTER, 5);
			game.getKeyHandler().clear();
			EXECUTED = false;
			break;
		}
	}

	@Override
	public void updateScreen() {
		window.updateScreen();
		drawCursor();
		drawCommands();
	}
	
	public int getX() {
		return this.window.x;
	}
	public int getY() {
		return this.window.y;
	}
	public void setX(int x) {
		this.window.x = x;
	}
	public void setY(int y) {
		this.window.y = y;
	}

}
