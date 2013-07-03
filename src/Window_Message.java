import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

/*
 * Window_Message.java
 * 
 * Zeigt einen Text in einer Box auf dem Bildschirm an. Die Breite ist WIDTH und
 * es wird automatisch umgebrochen. Die Höhe ist variabel und richtet sich nach
 * dem darzustellenden Text.
 */


public class Window_Message extends Abstract_Update {

	private Window_Base window;
	private FontMetrics metrics;
	private int height;
	private List<String> lines;
	
	private static final int WIDTH = 300;
	private static final int PADDING = 8;
	
	Window_Message(String msg, int x, int y, Object_Game game) {
		super(game);
		this.lines = new ArrayList<String>();
		this.height = PADDING*2;
		this.screen.setFont(Object_Game.FONT);
		this.metrics = this.screen.getFontMetrics();
		this.prepareMessage(msg, x, y);
		window = new Window_Base(game,x,y,WIDTH,this.height);
	}

	@Override
	public void updateData() {
		
	}

	@Override
	public void updateScreen() {
		this.window.updateScreen();
		this.drawMessage();
	}
	
	/*
	 * Teile den Eingabestring message in Zeilen auf, welche die festgelegte
	 * Breite nicht überschreiten. Passe außerdem die Höhe entsprechend an.
	 */
	private void prepareMessage(String message, int x, int y) {
		String[] msg = message.split(" ");

		int text_y = y+metrics.getAscent()+PADDING;
		String line = "";
		
		for (String s : msg) {
			if (metrics.stringWidth(line+s) < WIDTH-PADDING*2) {
				line += s+" ";
			}
			else {
				text_y += metrics.getHeight();
				this.height += text_y;
				lines.add(line);
				line = "";
			}
		}
		lines.add(line);
	}
	
	private void drawMessage() {
		int text_x = window.x+PADDING;
		int text_y = window.y+metrics.getAscent()+PADDING;
		
		for (String s : this.lines) {
			this.screen.drawString(s, text_x, text_y);
			text_y += metrics.getHeight();
		}

	}
}
