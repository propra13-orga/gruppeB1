import java.awt.Graphics;
import java.util.ArrayList;

public class Window_Selectable extends Window_Base {

	boolean EXECUTED = true;
	boolean CANCELED = false;
	
	public static int BORDER_X = 20;
	public static int BORDER_Y = 20;
	public static int CURSOR_HEIGHT = 30;
	
	private ArrayList<String> commands;
	int cursor;
	private Graphics g;

	Window_Selectable(int x, int y, Game game) {
		super(x, y, 0, 0, game);
		g = game.getScreen().getBuffer().getGraphics();
		commands = new ArrayList<String>();
		cursor = 0;
		CURSOR_HEIGHT = g.getFont().getSize() + 6;
		BORDER_X = CURSOR_HEIGHT / 2;
		BORDER_Y = BORDER_X;
	}
	
	public void addCommand(String command) {
		commands.add(command);
		int new_width = 2*BORDER_X + command.length()*10;
		if (width < new_width) width = new_width;
		height = 2*BORDER_Y + commands.size()*CURSOR_HEIGHT;
	}
	
	public void update() {
		updateLogic();
		//Fesnter zeichnen (superklasse)
		draw();
		drawCommands();
		drawCursor();
	}
	
	public void center() {
		x = Screen.SCREEN_W/2 - (width/2);
		y = Screen.SCREEN_H/2 - (height/2);
	}
	
	private void updateLogic() {
		switch (game.getKeyHandler().getLast()) {
		case KeyHandler.KEY_DOWN:
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_DOWN, 5);
			cursor++;
			if (cursor >= commands.size()) cursor = 0;
			break;
		case KeyHandler.KEY_UP:
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_UP, 5);
			cursor--;
			if (cursor < 0) cursor = commands.size()-1;
			break;
		case KeyHandler.KEY_ESCAPE:
			EXECUTED = false;
			CANCELED = true;
			break;
		case KeyHandler.KEY_ENTER:
			EXECUTED = false;
			break;
		}
	}
	
	private void drawCommands() {
		int text_x = x + BORDER_X;
		int text_y = y + BORDER_Y + g.getFont().getSize() + CURSOR_HEIGHT/4;
		for (String cmd : commands) {
			g.drawString(cmd, text_x, text_y);
			text_y += CURSOR_HEIGHT;
		}
	}
	
	private void drawCursor() {
		g.drawRect(
				x+BORDER_X,
				y+BORDER_Y+(cursor*CURSOR_HEIGHT),
				width - 2*BORDER_X,
				CURSOR_HEIGHT);
	}

}
