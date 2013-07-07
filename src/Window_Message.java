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
	
	public static final int WIDTH = 300;
	private static final int PADDING = 12;
	
	private int x;
	private int y;
	
	Window_Message(String msg, int x, int y, Object_Game game) {
		super(game);
		this.lines = new ArrayList<String>();
		this.height = PADDING*2;
		this.x = x;
		this.y = y;
		this.screen.setFont(Object_Game.FONT);
		this.metrics = this.screen.getFontMetrics();
		this.prepareMessage(msg);
		//this.adjustXY();
		window = new Window_Base(game,this.x,this.y,WIDTH,this.height);
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
	private void prepareMessage(String message) {
		String[] lines = message.split("\\n");
		int text_y = this.y+metrics.getAscent()+PADDING;
		for (String realLine : lines) {
			String[] msg = realLine.split("\\s");
			String line = "";
			text_y = this.metrics.getHeight();
			this.height += text_y;
			for (String s : msg) {
				if (this.metrics.stringWidth(line+s+" ") < WIDTH-PADDING*6) {
					line += s+" ";
				}
				else {
					text_y += this.metrics.getHeight();
					this.height += text_y;
					this.lines.add(line+s);
					line = "";
				}
			}
			this.lines.add(line);
		}
	}
	
	private void drawMessage() {
		int text_x = window.x+PADDING;
		int text_y = window.y+metrics.getAscent()+PADDING;
		
		for (String s : this.lines) {
			this.screen.drawString(s, text_x, text_y);
			text_y += metrics.getHeight();
		}
	}
	
	private void adjustXY() {
		int screenHeight = Object_Screen.SCREEN_H;
		if (this.y+this.height > screenHeight) {
			this.y = screenHeight-this.height;
		}
		
		int screenWidth = Object_Screen.SCREEN_W;
		if (this.x+WIDTH > screenWidth) {
			this.x = screenWidth-WIDTH;
		}
	}
}
