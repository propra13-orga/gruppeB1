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
	public int height;
	private List<String> lines;
	
	public int WIDTH = 300;
	private static final int PADDING = 12;
	
	private int x;
	private int y;
	
	Window_Message(String msg, int x, int y, Object_Game game) {
		this(msg,x,y,300,game);
//		super(game);
//		this.lines = new ArrayList<String>();
//		this.height = PADDING*2;
//		this.x = x;
//		this.y = y;
//		this.screen.setFont(Object_Game.FONT);
//		this.metrics = this.screen.getFontMetrics();
//		this.prepareMessage(msg);
//		//this.adjustXY();
//		window = new Window_Base(game,this.x,this.y,WIDTH,this.height);
	}
	
	Window_Message(String msg, int x, int y, int width, Object_Game game) {
		super(game);
		this.lines = new ArrayList<String>();
		this.height = PADDING*2;
		this.x = x;
		this.y = y;
		this.WIDTH = width;
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
	
	public void changeMessage(String message) {
		this.lines.clear();
		this.height = PADDING*2;
		this.prepareMessage(message);
		this.window = new Window_Base(game,this.x,this.y,WIDTH,this.height);
	}
	
	/*
	 * Teile den Eingabestring message in Zeilen auf, welche die festgelegte
	 * Breite nicht überschreiten. Passe außerdem die Höhe entsprechend an.
	 */
	private void prepareMessage(String message) {
		String[] lines = message.split("\\n");
		for (int j=0;j<lines.length;j++) {
			String realLine = lines[j];
			String[] msg = realLine.split("\\s");
			String line = "";
			for (String s : msg) {
				if (this.metrics.stringWidth(line+s+" ") < WIDTH-PADDING*6) {
					line += s+" ";
				}
				else {
					line += s;
					this.lines.add(line);
					line = "";
				}
			}
			if (!(j == lines.length-1 && line.equals(""))) this.lines.add(line);
		}
		this.height = (this.lines.size())*(this.metrics.getHeight())+PADDING*2;
	}
	
	private void drawMessage() {
		int text_x = window.x+PADDING;
		int text_y = window.y+metrics.getAscent()+PADDING;
		
		for (int i=0;i<this.lines.size();i++) {
			String s = this.lines.get(i);
			this.screen.drawString(s, text_x, text_y);
			text_y += metrics.getHeight();			
		}
	}
	
//	private void adjustXY() {
//		int screenHeight = Object_Screen.SCREEN_H;
//		if (this.y+this.height > screenHeight) {
//			this.y = screenHeight-this.height;
//		}
//		
//		int screenWidth = Object_Screen.SCREEN_W;
//		if (this.x+WIDTH > screenWidth) {
//			this.x = screenWidth-WIDTH;
//		}
//	}
}
