import java.util.ArrayList;

public class Window_Selectable extends Abstract_Update {

	boolean EXECUTED = true;
	boolean CANCELED = false;
	boolean EXIT_POSSIBLE = true;
	
	public static int BORDER_X = 20;
	public static int BORDER_Y = 20;
	public static int CURSOR_HEIGHT = 30;
	
	public int min_x = 200;
	public int max_x = Object_Screen.SCREEN_W;
	
	private ArrayList<String> commands;
	int cursor;
	private Window_Base window;

	Window_Selectable(int x, int y, Object_Game game) {
		super(game);
		window  = new Window_Base(x, y, 0, 0, game);
		commands = new ArrayList<String>();
		cursor = 0;
		this.screen.setFont(Object_Game.FONT);
		CURSOR_HEIGHT = this.screen.getFont().getSize() + 6;
		BORDER_X = CURSOR_HEIGHT / 2;
		BORDER_Y = BORDER_X;
	}
	
	public void addCommand(String command) {
		commands.add(command);
		int new_width = 2*BORDER_X + this.screen.getFontMetrics().stringWidth(command);
		if (window.width < new_width) window.width = new_width;
		if (window.width < min_x) window.width = min_x;
		if (window.width > max_x) window.width = max_x;
		window.height = 2*BORDER_Y + commands.size()*CURSOR_HEIGHT;
	}
	
	public void center() {
		window.x = Object_Screen.SCREEN_W/2 - (window.width/2);
		window.y = Object_Screen.SCREEN_H/2 - (window.height/2);
	}
	
	private void drawCommands() {
		int text_x = window.x + BORDER_X;
		int text_y = window.y + BORDER_Y + this.screen.getFont().getSize()+3;// + CURSOR_HEIGHT/4;
		for (String cmd : commands) {
			this.screen.drawString(cmd, text_x, text_y);
			text_y += CURSOR_HEIGHT;
		}
	}
	
	private void drawCursor() {
		this.screen.drawRect(
				window.x + BORDER_X,
				window.y + BORDER_Y + (cursor*CURSOR_HEIGHT),
				window.width - 2 * BORDER_X,
				CURSOR_HEIGHT);
	}

	@Override
	public void updateData() {
		switch (game.getKeyHandler().getLast()) {
		case Object_KeyHandler.KEY_DOWN:
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_DOWN, 5);
			cursor++;
			if (cursor >= commands.size()) cursor = 0;
			break;
		case Object_KeyHandler.KEY_UP:
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(Object_KeyHandler.KEY_UP, 5);
			cursor--;
			if (cursor < 0) cursor = commands.size()-1;
			break;
		case Object_KeyHandler.KEY_ESCAPE:
			if (!EXIT_POSSIBLE) break;
			game.getKeyHandler().clear();
			EXECUTED = false;
			CANCELED = true;
			break;
		case Object_KeyHandler.KEY_ENTER:
			game.getKeyHandler().clear();
			EXECUTED = false;
			break;
		}
	}

	@Override
	public void updateScreen() {
		window.updateScreen();
		drawCommands();
		drawCursor();
	}

}
